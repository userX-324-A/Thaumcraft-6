package thaumcraft.common.blocks.world.plants;

import java.util.Random;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
// import thaumcraft.client.fx.FXDispatcher; // Placeholder for FXDispatcher

public class BlockPlantShimmerleaf extends BushBlock {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public BlockPlantShimmerleaf(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(3) == 0) {
            // Replicate FXDispatcher.INSTANCE.drawWispyMotes behavior
            // For now, let's add a generic particle as a placeholder.
            // We'll need to implement a proper particle system or find an equivalent.
            double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.4D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            // Example: worldIn.addParticle(ParticleTypes.EFFECT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            // TODO: Replace with actual Thaumcraft particle effect (e.g., wispy motes)
            // FXDispatcher.INSTANCE.drawWispyMotes(xr, yr, zr, rand.nextGaussian() * 0.01, rand.nextGaussian() * 0.01, rand.nextGaussian() * 0.01, 10, 0.3f + world.rand.nextFloat() * 0.3f, 0.7f + world.rand.nextFloat() * 0.3f, 0.7f + world.rand.nextFloat() * 0.3f, 0.0f);
        }
    }

    // The getOffsetType is handled by the properties passed in the constructor if needed
    // setOffsetType(Block.OffsetType.XZ)
    
    // EnumPlantType getPlantType - not directly needed in 1.16.5 when extending BushBlock
    // as placement is handled by isValidGround and general BushBlock logic.
}
