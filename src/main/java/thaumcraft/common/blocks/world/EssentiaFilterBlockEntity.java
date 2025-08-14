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
    private final EssentiaTransportCapability.BasicEssentiaTransport tank = new EssentiaTransportCapability.BasicEssentiaTransport(16, 10)
            .allowInput(java.util.EnumSet.allOf(net.minecraft.util.Direction.class))
            .allowOutput(java.util.EnumSet.allOf(net.minecraft.util.Direction.class));
    private LazyOptional<thaumcraft.api.aspects.IEssentiaTransport> essentiaOptional = LazyOptional.of(() -> new FilteredTransport());

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
    public void setRemoved() { super.setRemoved(); essentiaOptional.invalidate(); }

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
    public void handleUpdateTag(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        if (tag.contains("filter")) this.filter = Aspect.getAspect(tag.getString("filter"));
        this.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT).ifPresent(cap -> {
            thaumcraft.api.aspects.Aspect type = tag.contains("aspect") ? Aspect.getAspect(tag.getString("aspect")) : null;
			tank.setStored(type, tag.getInt("amount"));
            tank.setSuction(tag.getInt("suction"));
        });
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

    private class FilteredTransport implements thaumcraft.api.aspects.IEssentiaTransport {
        @Override public boolean isConnectable(net.minecraft.util.Direction face) { return tank.isConnectable(face); }
        @Override public boolean canInputFrom(net.minecraft.util.Direction face) { return tank.canInputFrom(face); }
        @Override public boolean canOutputTo(net.minecraft.util.Direction face) { return tank.canOutputTo(face); }
        @Override public void setSuction(Aspect aspect, int amount) { tank.setSuction(aspect, amount); }
        @Override public Aspect getSuctionType(net.minecraft.util.Direction face) { return tank.getSuctionType(face); }
        @Override public int getSuctionAmount(net.minecraft.util.Direction face) { return tank.getSuctionAmount(face); }
        @Override public int takeEssentia(Aspect aspect, int amount, net.minecraft.util.Direction face) {
            if (filter != null && aspect != filter) return 0;
            return tank.takeEssentia(aspect, amount, face);
        }
        @Override public int addEssentia(Aspect aspect, int amount, net.minecraft.util.Direction face) {
            if (filter != null && aspect != filter) return 0;
            return tank.addEssentia(aspect, amount, face);
        }
        @Override public Aspect getEssentiaType(net.minecraft.util.Direction face) { return tank.getEssentiaType(face); }
        @Override public int getEssentiaAmount(net.minecraft.util.Direction face) { return tank.getEssentiaAmount(face); }
        @Override public int getMinimumSuction() { return tank.getMinimumSuction(); }
        @Override public int getSuction() { return tank.getSuction(); }
        @Override public void setSuction(int amount) { tank.setSuction(amount); }
    }
}



