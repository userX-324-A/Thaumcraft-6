package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaPumpBlockEntity extends TileEntity {
    private final LazyOptional<thaumcraft.api.aspects.IEssentiaTransport> transport = LazyOptional.of(
            () -> new EssentiaTransportCapability.BasicEssentiaTransport(8, 0)
    );

    public EssentiaPumpBlockEntity() { super(ModBlockEntities.ESSENTIA_PUMP.get()); }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return transport.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() { super.remove(); transport.invalidate(); }
}



