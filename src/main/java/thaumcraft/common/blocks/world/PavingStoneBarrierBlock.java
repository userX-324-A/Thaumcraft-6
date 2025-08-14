package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Paving stone that periodically spawns an invisible barrier column above when unpowered,
 * and gently pushes non-player mobs away like 1.12's TileBarrierStone.
 */
public class PavingStoneBarrierBlock extends Block {
    private static final VoxelShape SLAB_SHAPE = Block.box(0, 0, 0, 16, 15, 16);

    public PavingStoneBarrierBlock(Properties properties) {
        super(properties.sound(SoundType.STONE).strength(2.5f));
        registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PavingStoneBarrierBlockEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SLAB_SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean isMoving) {
        boolean powered = level.hasNeighborSignal(pos);
        if (state.getValue(BlockStateProperties.POWERED) != powered) {
            level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, powered), 3);
        }
    }
}



