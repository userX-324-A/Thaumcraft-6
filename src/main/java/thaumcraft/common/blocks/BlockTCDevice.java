package thaumcraft.common.blocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalDirectionalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTCDevice extends BlockTCTile
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public BlockTCDevice(AbstractBlock.Properties properties, Class<? extends TileEntity> tc) {
        super(properties, tc);
        BlockState defaultState = this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ENABLED, Boolean.valueOf(true));
        this.registerDefaultState(defaultState);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState()
                   .setValue(FACING, facing)
                   .setValue(ENABLED, Boolean.valueOf(true));
    }
    
    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        if (!worldIn.isClientSide) {
            updateState(worldIn, pos, state);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isClientSide) {
            updateState(worldIn, pos, state);
        }
    }

    protected void updateState(World worldIn, BlockPos pos, BlockState state) {
        if (state.hasProperty(ENABLED)) {
            boolean powered = worldIn.hasNeighborSignal(pos);
            boolean currentEnabled = state.getValue(ENABLED);
            boolean shouldBeEnabled = !powered;

            if (shouldBeEnabled != currentEnabled) {
                worldIn.setBlock(pos, state.setValue(ENABLED, Boolean.valueOf(shouldBeEnabled)), 3);
            }
        }
    }

    public void updateFacing(World world, BlockPos pos, Direction face) {
        BlockState currentState = world.getBlockState(pos);
        if (currentState.hasProperty(FACING) && currentState.getValue(FACING) != face) {
            if (FACING.getPossibleValues().contains(face)) {
                 world.setBlock(pos, currentState.setValue(FACING, face), 3);
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (state.hasProperty(FACING)) {
            return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
        }
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (state.hasProperty(FACING)) {
            return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
        }
        return state;
    }
}
