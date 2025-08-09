package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.api.aspects.IEssentiaTransport;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaJarBlockEntity extends TileEntity {
    private LazyOptional<IEssentiaTransport> essentiaOptional = LazyOptional.of(() -> new EssentiaTransportCapability.BasicEssentiaTransport(64, 20));

    public EssentiaJarBlockEntity() {
        super(ModBlockEntities.ESSENTIA_JAR.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentiaOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        essentiaOptional.invalidate();
    }
}
