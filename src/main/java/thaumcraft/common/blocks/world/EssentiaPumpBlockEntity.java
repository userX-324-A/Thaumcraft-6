package thaumcraft.common.blocks.world;

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

public class EssentiaPumpBlockEntity extends TileEntity implements ITickableTileEntity {
    private final EssentiaTransportCapability.BasicEssentiaTransport tank =
            new EssentiaTransportCapability.BasicEssentiaTransport(16, 40);
    private final LazyOptional<IEssentiaTransport> transport = LazyOptional.of(() -> tank);

    public EssentiaPumpBlockEntity() { super(ModBlockEntities.ESSENTIA_PUMP.get()); }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        // Pull from DOWN, push to UP by default
        Direction input = Direction.DOWN;
        Direction output = Direction.UP;

        TileEntity src = level.getBlockEntity(worldPosition.relative(input));
        if (src != null) {
            IEssentiaTransport other = src.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, input.getOpposite()).orElse(null);
            if (other != null) {
                int mySuction = tank.getSuctionAmount(input);
                int theirSuction = other.getSuctionAmount(input.getOpposite());
                if (mySuction > theirSuction) {
                    Aspect type = other.getEssentiaType(input.getOpposite());
                    if (type != null) {
                        int moved = other.takeEssentia(type, 2, input.getOpposite());
                        if (moved > 0) tank.addEssentia(type, moved, input);
                    }
                }
            }
        }

        TileEntity dst = level.getBlockEntity(worldPosition.relative(output));
        if (dst != null) {
            IEssentiaTransport other = dst.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, output.getOpposite()).orElse(null);
            if (other != null) {
                Aspect stored = tank.getEssentiaType(output);
                if (stored != null && tank.getEssentiaAmount(output) > 0) {
                    int offered = Math.min(4, tank.getEssentiaAmount(output));
                    int accepted = other.addEssentia(stored, offered, output.getOpposite());
                    if (accepted > 0) tank.takeEssentia(stored, accepted, output);
                }
            }
        }
        setChanged();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return transport.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() { super.setRemoved(); transport.invalidate(); }
}



