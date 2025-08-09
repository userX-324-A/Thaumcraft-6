package thaumcraft.common.blocks.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AlembicBlockEntity extends TileEntity {
    private final LazyOptional<IEssentiaTransport> essentia = LazyOptional.of(() -> new EssentiaTransportCapability.BasicEssentiaTransport(32, 10));

    public AlembicBlockEntity() {
        super(ModBlockEntities.ALEMBIC.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentia.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        essentia.invalidate();
    }
}
