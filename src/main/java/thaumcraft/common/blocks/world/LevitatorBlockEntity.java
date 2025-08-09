package thaumcraft.common.blocks.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.registers.ModBlockEntities;

import java.util.List;

public class LevitatorBlockEntity extends TileEntity implements ITickableTileEntity {
    private static final int[] RANGES = new int[]{4,8,16,32};
    private int rangeIndex = 1;
    private int rangeActual = 0;
    private int counter = 0;
    private int vis = 0;

    public LevitatorBlockEntity() {
        super(ModBlockEntities.LEVITATOR.get());
    }

    @Override
    public void tick() {
        World level = world;
        if (level == null || level.isRemote) return;
        BlockPos pos = getPos();
        Direction facing = Direction.UP; // placeholder
        int maxRange = RANGES[Math.max(0, Math.min(RANGES.length - 1, rangeIndex))];
        if (rangeActual > maxRange) rangeActual = 0;
        int p = counter % maxRange;
        BlockPos checkPos = pos.offset(facing, 1 + p);
        if (level.getBlockState(checkPos).isSolid()) {
            if (1 + p < rangeActual) rangeActual = 1 + p;
            counter = -1;
        } else if (1 + p > rangeActual) {
            rangeActual = 1 + p;
        }
        counter++;

        if (vis < 10) {
            vis += (int)(AuraHelper.drainVis(level, pos, 1.0f, false) * 1200.0f);
        }
        if (rangeActual > 0 && vis > 0) {
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + rangeActual + 1, pos.getZ() + 1);
            List<Entity> entities = level.getEntitiesWithinAABB(Entity.class, box);
            for (Entity e : entities) {
                e.setOnGround(false);
                e.setMotion(e.getMotion().x, Math.max(0.2, e.getMotion().y + 0.1), e.getMotion().z);
            }
            vis--;
        }
    }

    public void cycleRange() {
        rangeIndex = (rangeIndex + 1) % RANGES.length;
        setChanged();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        rangeIndex = tag.getInt("Range");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("Range", rangeIndex);
        return super.write(tag);
    }
}



