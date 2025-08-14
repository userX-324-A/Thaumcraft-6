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
    public void setRemoved() {
        super.setRemoved();
        transport.invalidate();
    }

	@Override
	public net.minecraft.nbt.CompoundNBT getUpdateTag() {
		net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
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
		this.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT).ifPresent(cap -> {
			thaumcraft.api.aspects.Aspect type = tag.contains("aspect") ? thaumcraft.api.aspects.Aspect.getAspect(tag.getString("aspect")) : null;
			((EssentiaTransportCapability.BasicEssentiaTransport) cap).setStored(type, tag.getInt("amount"));
			cap.setSuction(tag.getInt("suction"));
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
}




