package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class EssentiaJarVoidBlock extends Block {
    public static final BooleanProperty BRACED = BooleanProperty.create("braced");
    public EssentiaJarVoidBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(BRACED, Boolean.FALSE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.ESSENTIA_JAR_VOID.get().create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BRACED);
    }
}



