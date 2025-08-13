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
}



