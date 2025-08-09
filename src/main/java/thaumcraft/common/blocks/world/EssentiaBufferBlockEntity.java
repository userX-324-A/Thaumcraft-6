package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaBufferBlockEntity extends TileEntity {
    private final LazyOptional<thaumcraft.api.aspects.IEssentiaTransport> transport = LazyOptional.of(
            () -> new EssentiaTransportCapability.BasicEssentiaTransport(10, 8)
    );

    public EssentiaBufferBlockEntity() {
        super(ModBlockEntities.ESSENTIA_BUFFER.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return transport.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        transport.invalidate();
    }
}



