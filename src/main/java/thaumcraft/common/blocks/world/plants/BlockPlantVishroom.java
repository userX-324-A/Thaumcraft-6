package thaumcraft.common.blocks.world.plants;

import java.util.Random;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockPlantVishroom extends BushBlock {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D); // Typical mushroom shape

    public BlockPlantVishroom(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        // Mushrooms can typically grow on dirt, grass, podzol, mycelium, stone, etc., especially in dim light.
        // BushBlock also checks light levels for canSurvive, so we mostly need to define the ground.
        // Let's allow it on common natural/underground blocks.
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || 
               block == Blocks.MYCELIUM || block == Blocks.STONE || block == Blocks.ANDESITE || block == Blocks.DIORITE || 
               block == Blocks.GRANITE || block == Blocks.FARMLAND || state.canSustainPlant(worldIn, pos, net.minecraft.util.Direction.UP, this);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof LivingEntity && worldIn.rand.nextInt(5) == 0) {
            ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(3) == 0) {
            // Replicate FXDispatcher.INSTANCE.drawWispyMotes behavior
            // FXDispatcher.INSTANCE.drawWispyMotes(xr, yr, zr, 0.0, 0.0, 0.0, 10, 0.5f, 0.3f, 0.8f, 0.001f);
            double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.8D; // Wider spread for mushroom spores?
            double d1 = (double)pos.getY() + 0.3D;
            double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.8D;
            // TODO: Replace with actual Thaumcraft particle effect (e.g., wispy motes, purple-ish for vishroom)
            // Example: worldIn.addParticle(ParticleTypes.PORTAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    // isFullCube - BushBlock is generally not a full cube, shape is handled by getShape.
    // EnumPlantType getPlantType - Not directly needed, placement governed by isValidGround and BushBlock logic.
    // No offset type specified in registry or old code, defaults to NONE via BushBlock.
}
