package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliserExt;

public class StabilizerBlock extends Block implements IInfusionStabiliserExt {
    public StabilizerBlock(Properties props) { super(props); }

    @Override
    public boolean canStabaliseInfusion(World world, BlockPos pos) {
        return true;
    }

    @Override
    public float getStabilizationAmount(World world, BlockPos pos) {
        return 0.25f;
    }

    @Override
    public int getLightValue(BlockState state) {
        return 4;
    }
}



