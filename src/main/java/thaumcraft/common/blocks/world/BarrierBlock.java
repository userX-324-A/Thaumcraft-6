package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import thaumcraft.common.registers.ModBlocks;

public class BarrierBlock extends Block {
    private static final VoxelShape EMPTY_SHAPE = VoxelShapes.empty();

    public BarrierBlock(Properties properties) {
        super(properties.noOcclusion().noDrops().sound(SoundType.GLASS));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return EMPTY_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return EMPTY_SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        // Must be above paving stone barrier or another barrier column segment
        BlockState below = world.getBlockState(pos.below());
        return below.getBlock() == ModBlocks.BARRIER.get() || (below.getBlock().getRegistryName() != null && "paving_stone_barrier".equals(below.getBlock().getRegistryName().getPath()));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!canSurvive(state, world, pos)) {
            world.removeBlock(pos, false);
        }
    }
}



