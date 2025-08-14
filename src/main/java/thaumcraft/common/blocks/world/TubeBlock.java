package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class TubeBlock extends Block implements IForgeBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public TubeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    private static final VoxelShape CORE = Block.box(6, 6, 6, 10, 10, 10);
    private static final VoxelShape ARM_UP = Block.box(6, 10, 6, 10, 16, 10);
    private static final VoxelShape ARM_DOWN = Block.box(6, 0, 6, 10, 6, 10);
    private static final VoxelShape ARM_NORTH = Block.box(6, 6, 0, 10, 10, 6);
    private static final VoxelShape ARM_SOUTH = Block.box(6, 6, 10, 10, 10, 16);
    private static final VoxelShape ARM_WEST = Block.box(0, 6, 6, 6, 10, 10);
    private static final VoxelShape ARM_EAST = Block.box(10, 6, 6, 16, 10, 10);

    private static VoxelShape buildShape(BlockState state) {
        VoxelShape shape = CORE;
        if (state.getValue(UP)) shape = VoxelShapes.or(shape, ARM_UP);
        if (state.getValue(DOWN)) shape = VoxelShapes.or(shape, ARM_DOWN);
        if (state.getValue(NORTH)) shape = VoxelShapes.or(shape, ARM_NORTH);
        if (state.getValue(SOUTH)) shape = VoxelShapes.or(shape, ARM_SOUTH);
        if (state.getValue(WEST)) shape = VoxelShapes.or(shape, ARM_WEST);
        if (state.getValue(EAST)) shape = VoxelShapes.or(shape, ARM_EAST);
        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return buildShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return buildShape(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.TUBE.get().create();
    }
}


