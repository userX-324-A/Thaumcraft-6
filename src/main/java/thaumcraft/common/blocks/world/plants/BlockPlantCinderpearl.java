package thaumcraft.common.blocks.world.plants;

import java.util.Random;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockPlantCinderpearl extends BushBlock {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public BlockPlantCinderpearl(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return block == Blocks.SAND || block == Blocks.RED_SAND || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND || state.isIn(BlockTags.TERRACOTTA);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextBoolean()) {
            float xr = (float)pos.getX() + 0.5F + (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            float yr = (float)pos.getY() + 0.6F + (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            float zr = (float)pos.getZ() + 0.5F + (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            worldIn.addParticle(ParticleTypes.SMOKE, xr, yr, zr, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, xr, yr, zr, 0.0D, 0.0D, 0.0D);
        }
    }

    // The getOffsetType is handled by the properties passed in the constructor if needed
    // setOffsetType(Block.OffsetType.XZ)
}
