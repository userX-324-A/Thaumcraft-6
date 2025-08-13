package thaumcraft.common.blocks.world;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TubeBlockEntity extends TileEntity implements ITickableTileEntity {
    private final EssentiaTransportCapability.BasicEssentiaTransport tank =
            new EssentiaTransportCapability.BasicEssentiaTransport(16, 5)
                    .allowInput(java.util.EnumSet.allOf(Direction.class))
                    .allowOutput(java.util.EnumSet.allOf(Direction.class));
    private final LazyOptional<IEssentiaTransport> essentiaOptional = LazyOptional.of(() -> tank);

    public TubeBlockEntity() { super(ModBlockEntities.TUBE.get()); }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        // Pull from neighbors with lower suction (towards higher suction)
        for (Direction side : Direction.values()) {
            TileEntity neighbor = level.getBlockEntity(worldPosition.relative(side));
            if (neighbor == null) continue;
            IEssentiaTransport other = neighbor.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, side.getOpposite()).orElse(null);
            if (other == null) continue;
            int mySuction = tank.getSuctionAmount(side);
            int otherSuction = other.getSuctionAmount(side.getOpposite());
            if (mySuction <= otherSuction) continue;
            Aspect type = other.getEssentiaType(side.getOpposite());
            if (type == null) continue;
            int moved = other.takeEssentia(type, 1, side.getOpposite());
            if (moved > 0) {
                tank.addEssentia(type, moved, side);
                setChangedAndUpdate();
            }
        }
        // Try push to neighbors with higher suction than ours
        Aspect stored = tank.getEssentiaType(null);
        if (stored != null && tank.getEssentiaAmount(null) > 0) {
            for (Direction side : Direction.values()) {
                TileEntity neighbor = level.getBlockEntity(worldPosition.relative(side));
                if (neighbor == null) continue;
                IEssentiaTransport other = neighbor.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, side.getOpposite()).orElse(null);
                if (other == null) continue;
                int mySuction = tank.getSuctionAmount(side);
                int otherSuction = other.getSuctionAmount(side.getOpposite());
                if (otherSuction <= mySuction) continue;
                int offered = Math.min(2, tank.getEssentiaAmount(side));
                if (offered <= 0) continue;
                int accepted = other.addEssentia(stored, offered, side.getOpposite());
                if (accepted > 0) {
                    tank.takeEssentia(stored, accepted, side);
                    setChangedAndUpdate();
                    break; // push one neighbor per tick
                }
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentiaOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() { super.setRemoved(); essentiaOptional.invalidate(); }

    @Override
    public net.minecraft.nbt.CompoundNBT getUpdateTag() {
        net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
        tag.putInt("suction", tank.getSuction());
        Aspect type = tank.getEssentiaType(null);
        if (type != null) tag.putString("aspect", type.getTag());
        tag.putInt("amount", tank.getEssentiaAmount(null));
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        Aspect type = tag.contains("aspect") ? Aspect.getAspect(tag.getString("aspect")) : null;
        int amount = tag.getInt("amount");
        tank.setStored(type, amount);
        tank.setSuction(tag.getInt("suction"));
    }

    @Override
    public void load(BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.load(state, tag);
        Aspect type = tag.contains("TubeAspect") ? Aspect.getAspect(tag.getString("TubeAspect")) : null;
        int amount = tag.getInt("TubeAmount");
        tank.setStored(type, amount);
        if (tag.contains("TubeSuction")) tank.setSuction(tag.getInt("TubeSuction"));
    }

    @Override
    public net.minecraft.nbt.CompoundNBT save(net.minecraft.nbt.CompoundNBT tag) {
        Aspect type = tank.getEssentiaType(null);
        if (type != null) tag.putString("TubeAspect", type.getTag());
        tag.putInt("TubeAmount", tank.getEssentiaAmount(null));
        tag.putInt("TubeSuction", tank.getSuction());
        return super.save(tag);
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}

