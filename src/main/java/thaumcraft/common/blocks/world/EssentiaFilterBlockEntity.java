package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaFilterBlockEntity extends TileEntity {
    private Aspect filter; // null means pass-through
    private LazyOptional<thaumcraft.api.aspects.IEssentiaTransport> essentiaOptional = LazyOptional.of(
            () -> new EssentiaTransportCapability.BasicEssentiaTransport(16, 10)
                    .allowInput(java.util.EnumSet.allOf(net.minecraft.util.Direction.class))
                    .allowOutput(java.util.EnumSet.allOf(net.minecraft.util.Direction.class))
    );

    public EssentiaFilterBlockEntity() {
        super(ModBlockEntities.ESSENTIA_FILTER.get());
    }

    // Active ticking moved to block side during port; this TE only stores state and exposes capability

    public void setFilter(Aspect aspect) {
        this.filter = aspect;
        setChanged();
    }
    public Aspect getFilter() { return filter; }

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
        if (filter != null) tag.putString("filter", filter.getTag());
        this.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT).ifPresent(cap -> {
            thaumcraft.api.aspects.Aspect type = cap.getEssentiaType(null);
            if (type != null) tag.putString("aspect", type.getTag());
            tag.putInt("amount", cap.getEssentiaAmount(null));
            tag.putInt("suction", cap.getSuction());
        });
        return tag;
    }

    @Override
    public void handleUpdateTag(net.minecraft.nbt.CompoundNBT tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("filter")) this.filter = Aspect.getAspect(tag.getString("filter"));
        this.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT).ifPresent(cap -> {
            if (cap instanceof EssentiaTransportCapability.BasicEssentiaTransport) {
                EssentiaTransportCapability.BasicEssentiaTransport be = (EssentiaTransportCapability.BasicEssentiaTransport) cap;
                thaumcraft.api.aspects.Aspect type = tag.contains("aspect") ? Aspect.getAspect(tag.getString("aspect")) : null;
                be.setStored(type, tag.getInt("amount"));
                be.setSuction(tag.getInt("suction"));
            }
        });
    }
}



