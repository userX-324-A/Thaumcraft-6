package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.TUBE.get().create();
    }
}

