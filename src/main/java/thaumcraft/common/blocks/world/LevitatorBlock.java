package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class LevitatorBlock extends Block implements IForgeBlock {
    public LevitatorBlock(Properties props) { super(props); }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, World world) {
        return thaumcraft.common.registers.ModBlockEntities.LEVITATOR.get().create();
    }
}



