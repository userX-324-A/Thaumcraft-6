package thaumcraft.common.blocks.world.plants;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.trees.AbstractTreeGrower;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockSaplingTC extends SaplingBlock {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D); // Standard sapling shape

    public BlockSaplingTC(AbstractTreeGrower treeGrower, AbstractBlock.Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    // Flammability can be set in properties, but if specific values are needed:
    // @Override
    // public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
    // return 60; // From old code
    // }

    // @Override
    // public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
    // return 30; // From old code
    // }

    // Bonemealability and growth logic are largely handled by SaplingBlock and AbstractTreeGrower
    // We will need to implement the ConfiguredFeatures in the TreeGrowers for actual tree generation.
}
