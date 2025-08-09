package thaumcraft.common.blocks.world;

import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.extensions.IForgeBlock;
import javax.annotation.Nullable;

public class VisGeneratorBlock extends Block implements IForgeBlock {
    public VisGeneratorBlock(Properties props) { super(props); }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.VIS_GENERATOR.get().create();
    }
}



