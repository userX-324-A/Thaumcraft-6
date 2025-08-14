package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaValveBlockEntity extends TileEntity {
    private boolean open = true;
    private final EssentiaTransportCapability.BasicEssentiaTransport tank = new EssentiaTransportCapability.BasicEssentiaTransport(8, 0);
    private final LazyOptional<IEssentiaTransport> cap = LazyOptional.of(() -> tank);

    public EssentiaValveBlockEntity() { super(ModBlockEntities.ESSENTIA_VALVE.get()); }

    public void setOpen(boolean open) { this.open = open; setChanged(); }
    public boolean isOpen() { return open; }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable net.minecraft.util.Direction side) {
        if (capability == EssentiaTransportCapability.ESSENTIA_TRANSPORT) {
            if (open) return cap.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void setRemoved() { super.setRemoved(); cap.invalidate(); }

    @Override
    public net.minecraft.nbt.CompoundNBT getUpdateTag() {
        net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
        tag.putBoolean("open", open);
        return tag;
    }

    @Override
    public void handleUpdateTag(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        if (tag.contains("open")) this.open = tag.getBoolean("open");
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
}




