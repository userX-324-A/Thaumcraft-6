package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaPumpBlockEntity extends TileEntity implements ITickableTileEntity {
    private final EssentiaTransportCapability.BasicEssentiaTransport tank =
            new EssentiaTransportCapability.BasicEssentiaTransport(16, 40);
    private final LazyOptional<IEssentiaTransport> transport = LazyOptional.of(() -> tank);

    public EssentiaPumpBlockEntity() { super(ModBlockEntities.ESSENTIA_PUMP.get()); }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        // Pull from DOWN, push to UP by default
        Direction input = Direction.DOWN;
        Direction output = Direction.UP;

        TileEntity src = level.getBlockEntity(worldPosition.relative(input));
        if (src != null) {
            IEssentiaTransport other = src.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, input.getOpposite()).orElse(null);
            if (other != null) {
                int mySuction = tank.getSuctionAmount(input);
                int theirSuction = other.getSuctionAmount(input.getOpposite());
                if (mySuction > theirSuction) {
                    Aspect type = other.getEssentiaType(input.getOpposite());
                    if (type != null) {
                        int pull = Math.max(0, thaumcraft.common.config.ModConfig.COMMON.pumpPullRate.get());
                        if (pull > 0) {
                            int moved = other.takeEssentia(type, pull, input.getOpposite());
                            if (moved > 0) tank.addEssentia(type, moved, input);
                        }
                    }
                }
            }
        }

        TileEntity dst = level.getBlockEntity(worldPosition.relative(output));
        if (dst != null) {
            IEssentiaTransport other = dst.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, output.getOpposite()).orElse(null);
            if (other != null) {
                Aspect stored = tank.getEssentiaType(output);
                if (stored != null && tank.getEssentiaAmount(output) > 0) {
                    int push = Math.max(0, thaumcraft.common.config.ModConfig.COMMON.pumpPushRate.get());
                    if (push > 0) {
                        int offered = Math.min(push, tank.getEssentiaAmount(output));
                        int accepted = other.addEssentia(stored, offered, output.getOpposite());
                    if (accepted > 0) tank.takeEssentia(stored, accepted, output);
                    }
                }
            }
        }
        setChanged();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return transport.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() { super.setRemoved(); transport.invalidate(); }

	@Override
	public void load(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
		super.load(state, tag);
		if (tag.contains("StoredType")) {
			thaumcraft.api.aspects.Aspect type = thaumcraft.api.aspects.Aspect.getAspect(tag.getString("StoredType"));
			int amt = tag.getInt("StoredAmount");
			tank.setStored(type, amt);
		}
		if (tag.contains("Suction")) {
			tank.setSuction(tag.getInt("Suction"));
		}
	}

	@Override
	public net.minecraft.nbt.CompoundNBT save(net.minecraft.nbt.CompoundNBT tag) {
		thaumcraft.api.aspects.Aspect type = tank.getStoredType();
		if (type != null) {
			tag.putString("StoredType", type.getTag());
			tag.putInt("StoredAmount", tank.getStoredAmount());
		}
		tag.putInt("Suction", tank.getSuction());
		return super.save(tag);
	}

	@Override
	public net.minecraft.nbt.CompoundNBT getUpdateTag() {
		net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
		thaumcraft.api.aspects.Aspect type = tank.getEssentiaType(null);
		if (type != null) tag.putString("aspect", type.getTag());
		tag.putInt("amount", tank.getEssentiaAmount(null));
		tag.putInt("suction", tank.getSuction());
		return tag;
	}

	@Override
	public void handleUpdateTag(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		thaumcraft.api.aspects.Aspect type = tag.contains("aspect") ? thaumcraft.api.aspects.Aspect.getAspect(tag.getString("aspect")) : null;
		int amount = tag.getInt("amount");
		tank.setStored(type, amount);
		tank.setSuction(tag.getInt("suction"));
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




