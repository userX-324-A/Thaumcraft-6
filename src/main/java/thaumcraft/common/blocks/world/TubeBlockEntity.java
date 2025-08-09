package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.api.aspects.IEssentiaTransport;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TubeBlockEntity extends TileEntity {
    // Placeholder suction and storage for essentia transport scaffold
    public int suction = 0;
    private LazyOptional<IEssentiaTransport> essentiaOptional = LazyOptional.of(() -> new EssentiaTransportCapability.BasicEssentiaTransport(16, 5)
            .allowInput(java.util.EnumSet.allOf(net.minecraft.util.Direction.class))
            .allowOutput(java.util.EnumSet.allOf(net.minecraft.util.Direction.class))
    );
    public TubeBlockEntity() {
        super(ModBlockEntities.TUBE.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentiaOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() { super.remove(); essentiaOptional.invalidate(); }

    @Override
    public net.minecraft.nbt.CompoundNBT getUpdateTag() {
        net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
        this.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT).ifPresent(cap -> {
            tag.putInt("suction", cap.getSuction());
            thaumcraft.api.aspects.Aspect type = cap.getEssentiaType(null);
            if (type != null) tag.putString("aspect", type.getTag());
            tag.putInt("amount", cap.getEssentiaAmount(null));
        });
        return tag;
    }

    @Override
    public void handleUpdateTag(net.minecraft.nbt.CompoundNBT tag) {
        super.handleUpdateTag(tag);
        this.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT).ifPresent(cap -> {
            if (cap instanceof EssentiaTransportCapability.BasicEssentiaTransport) {
                EssentiaTransportCapability.BasicEssentiaTransport be = (EssentiaTransportCapability.BasicEssentiaTransport) cap;
                thaumcraft.api.aspects.Aspect type = tag.contains("aspect") ? thaumcraft.api.aspects.Aspect.getAspect(tag.getString("aspect")) : null;
                int amount = tag.getInt("amount");
                be.setStored(type, amount);
                be.setSuction(tag.getInt("suction"));
            }
        });
    }
}

