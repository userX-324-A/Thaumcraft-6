package thaumcraft.common.blocks.basic;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import thaumcraft.common.blocks.BlockTC;

public class BlockStoneTC extends BlockTC
{
    private final boolean spawn;
    
    public BlockStoneTC(boolean spawn, AbstractBlock.Properties properties) {
        super(properties);
        this.spawn = spawn;
    }
    
    public boolean canSpawnEntities() {
        return this.spawn;
    }
    
    @Override
    public boolean isBeaconBase(BlockState state, IBlockReader world, BlockPos pos, BlockPos beaconPos) {
        return true;
    }
    
    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return state.getDestroySpeed(world, pos) >= 0.0f;
    }
}
