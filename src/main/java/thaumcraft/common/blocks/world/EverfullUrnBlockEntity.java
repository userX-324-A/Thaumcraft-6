package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import thaumcraft.common.registers.ModBlockEntities;

public class EverfullUrnBlockEntity extends TileEntity implements ITickableTileEntity {
    public EverfullUrnBlockEntity() {
        super(ModBlockEntities.EVERFULL_URN.get());
    }

    @Override
    public void tick() {
        World level = this.level;
        if (level == null || level.isClientSide) return;
        if ((level.getGameTime() % 20L) != 0L) return;
        if (!level.getFluidState(this.worldPosition.below()).isSource()) return;
        for (Direction dir : Direction.values()) {
            TileEntity neighbor = level.getBlockEntity(this.worldPosition.relative(dir));
            if (neighbor == null) continue;
            LazyOptional<IFluidHandler> fh = neighbor.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir.getOpposite());
            fh.ifPresent(handler -> handler.fill(new FluidStack(net.minecraft.fluid.Fluids.WATER, 50), IFluidHandler.FluidAction.EXECUTE));
        }
	}

	@Override
	public net.minecraft.nbt.CompoundNBT getUpdateTag() {
		return super.getUpdateTag();
	}

	@Override
	public void handleUpdateTag(BlockState state, net.minecraft.nbt.CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
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


