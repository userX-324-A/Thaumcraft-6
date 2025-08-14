package thaumcraft.common.blocks.world;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.registers.ModBlockEntities;

public class ArcaneEarBlockEntity extends TileEntity implements ITickableTileEntity {
    public enum Mode { NOTE, BLOCK, REDSTONE }

    public Mode mode = Mode.NOTE;
    public int note = 0;

    public ArcaneEarBlockEntity() {
        super(ModBlockEntities.ARCANE_EAR.get());
    }

    @Override
    public void tick() {
        World level = this.level;
        if (level == null || level.isClientSide) return;

        BlockPos currentPos = this.worldPosition;
        if (mode == Mode.REDSTONE) {
            level.updateNeighborsAt(currentPos, getBlockState().getBlock());
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        try {
            mode = Mode.valueOf(tag.getString("Mode"));
        } catch (Exception ignored) {
        }
        note = tag.getInt("Note");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putString("Mode", mode.name());
        tag.putInt("Note", note);
        return super.save(tag);
    }

	@Override
	public net.minecraft.nbt.CompoundNBT getUpdateTag() {
		net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
		tag.putString("Mode", mode.name());
		tag.putInt("Note", note);
		return tag;
	}

	@Override
	public void handleUpdateTag(BlockState state, net.minecraft.nbt.CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		try { mode = Mode.valueOf(tag.getString("Mode")); } catch (Exception ignored) {}
		note = tag.getInt("Note");
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




