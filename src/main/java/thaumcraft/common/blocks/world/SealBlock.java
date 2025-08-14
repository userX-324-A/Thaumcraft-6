package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class SealBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape THIN = Block.box(0, 0, 0, 16, 2, 16);

    public SealBlock(Properties properties) {
        super(properties.noOcclusion());
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction face = ctx.getClickedFace();
        BlockState state = defaultBlockState().setValue(FACING, face);
        return canSurvive(state, ctx.getLevel(), ctx.getClickedPos()) ? state : null;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        Direction d = state.getValue(FACING);
        switch (d) {
            case DOWN: return Block.box(0, 14, 0, 16, 16, 16);
            case NORTH: return Block.box(0, 0, 14, 16, 16, 16);
            case SOUTH: return Block.box(0, 0, 0, 16, 16, 2);
            case WEST: return Block.box(14, 0, 0, 16, 16, 16);
            case EAST: return Block.box(0, 0, 0, 2, 16, 16);
            case UP:
            default: return THIN;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        Direction face = state.getValue(FACING);
        BlockPos support = pos.relative(face.getOpposite());
        BlockState supportState = world.getBlockState(support);
        return supportState.isFaceSturdy(world, support, face);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, net.minecraft.world.IWorld world, BlockPos currentPos, BlockPos facingPos) {
        return canSurvive(state, (IWorldReader) world, currentPos) ? state : net.minecraft.block.Blocks.AIR.defaultBlockState();
    }
}



