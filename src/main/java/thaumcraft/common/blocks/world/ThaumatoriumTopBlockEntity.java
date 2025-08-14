package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Thin shim that forwards essentia I/O to the base Thaumatorium below.
 */
public class ThaumatoriumTopBlockEntity extends TileEntity {
    public ThaumatoriumTopBlockEntity() {
        super(ModBlockEntities.THAUMATORIUM.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) {
            TileEntity below = level != null ? level.getBlockEntity(worldPosition.below()) : null;
            if (below instanceof ThaumatoriumBlockEntity) {
                LazyOptional<IEssentiaTransport> delegate = ((ThaumatoriumBlockEntity) below).getEssentiaInternal();
                return delegate.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }
}



