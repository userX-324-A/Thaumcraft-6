package thaumcraft.common.blocks.world;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
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

/**
 * Minimal 1.16.5 TE for the Essentia Centrifuge.
 * Placeholder behavior: pulls essentia from sides with higher suction and pushes to the opposite side.
 * Splitting logic will be added later.
 */
public class EssentiaCentrifugeBlockEntity extends TileEntity implements ITickableTileEntity {
    private final EssentiaTransportCapability.BasicEssentiaTransport tank =
            new EssentiaTransportCapability.BasicEssentiaTransport(128, 20);

    private final LazyOptional<IEssentiaTransport> essentia = LazyOptional.of(() -> tank);

    public EssentiaCentrifugeBlockEntity() {
        super(ModBlockEntities.ESSENTIA_CENTRIFUGE_BE.get());
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        // Pull from all horizontal sides based on neighbor suction
        for (Direction side : Direction.Plane.HORIZONTAL) {
            TileEntity neighbor = level.getBlockEntity(worldPosition.relative(side));
            if (neighbor == null) continue;
            IEssentiaTransport other = neighbor.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, side.getOpposite()).orElse(null);
            if (other == null) continue;

            int otherSuction = other.getSuctionAmount(side.getOpposite());
            int mySuction = tank.getSuctionAmount(side);
            if (otherSuction <= mySuction) continue;

            Aspect type = other.getEssentiaType(side.getOpposite());
            if (type == null) continue;
            int moved = other.takeEssentia(type, 1, side.getOpposite());
            if (moved > 0) {
                tank.addEssentia(type, moved, side);
            }
        }

        // Try push to UP first, then DOWN as fallback
        tryPush(Direction.UP);
        tryPush(Direction.DOWN);
    }

    private void tryPush(Direction out) {
        if (level == null) return;
        TileEntity neighbor = level.getBlockEntity(worldPosition.relative(out));
        if (neighbor == null) return;
        IEssentiaTransport other = neighbor.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, out.getOpposite()).orElse(null);
        if (other == null) return;
        Aspect stored = tank.getEssentiaType(out);
        if (stored == null) return;
        int amount = Math.min(4, tank.getEssentiaAmount(out));
        if (amount <= 0) return;
        int accepted = other.addEssentia(stored, amount, out.getOpposite());
        if (accepted > 0) {
            tank.takeEssentia(stored, accepted, out);
            setChangedAndUpdate();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentia.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        essentia.invalidate();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        if (tag.contains("StoredType")) {
            Aspect type = Aspect.getAspect(tag.getString("StoredType"));
            int amt = tag.getInt("StoredAmount");
            tank.setStored(type, amt);
        }
        if (tag.contains("Suction")) {
            tank.setSuction(tag.getInt("Suction"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        Aspect type = tank.getStoredType();
        if (type != null) {
            tag.putString("StoredType", type.getTag());
            tag.putInt("StoredAmount", tank.getStoredAmount());
        }
        tag.putInt("Suction", tank.getSuction());
        return super.save(tag);
    }

    @Override
    public net.minecraft.nbt.CompoundNBT getUpdateTag() {
        net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
        Aspect type = tank.getEssentiaType(null);
        if (type != null) tag.putString("aspect", type.getTag());
        tag.putInt("amount", tank.getEssentiaAmount(null));
        tag.putInt("suction", tank.getSuction());
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
    public net.minecraft.network.play.server.SUpdateTileEntityPacket getUpdatePacket() {
        net.minecraft.nbt.CompoundNBT tag = new net.minecraft.nbt.CompoundNBT();
        save(tag);
        return new net.minecraft.network.play.server.SUpdateTileEntityPacket(this.worldPosition, 0, tag);
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
        this.load(getBlockState(), pkt.getTag());
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}


