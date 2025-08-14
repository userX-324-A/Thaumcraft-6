package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class ArcaneLampBlock extends Block {
    public ArcaneLampBlock(Properties properties) { super(properties); }

    private static final VoxelShape POST = Block.box(7, 0, 7, 9, 12, 9);
    private static final VoxelShape HEAD = Block.box(4, 12, 4, 12, 16, 12);
    private static final VoxelShape SHAPE = VoxelShapes.or(POST, HEAD);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}



