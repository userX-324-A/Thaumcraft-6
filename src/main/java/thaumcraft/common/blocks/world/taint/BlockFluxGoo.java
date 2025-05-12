package thaumcraft.common.blocks.world.taint;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.potions.ModPotions;
import thaumcraft.common.registration.ModBlocks;
import thaumcraft.common.registration.ModEntities;
import thaumcraft.common.registration.ModSounds;

public class BlockFluxGoo extends FlowingFluidBlock implements ITaintBlock {

    public BlockFluxGoo(Supplier<? extends FlowingFluid> supplier, AbstractBlock.Properties properties) {
        super(supplier, properties);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        int level = state.getValue(LEVEL);
        float quantaPercentage = getQuantaPercentage(world, pos, state);

        if (entity instanceof EntityThaumicSlime) {
            EntityThaumicSlime slime = (EntityThaumicSlime) entity;
            if (slime.getSlimeSize() < level && world.random.nextBoolean()) {
                slime.setSlimeSize(slime.getSlimeSize() + 1, true);
                if (level > 1) {
                    // world.setBlockState(pos, state.withProperty((IProperty)BlockFluxGoo.LEVEL, (Comparable)(md - 1)), 2);
                    // This is complex with fluid states. Perhaps just damage/remove some fluid source blocks nearby?
                    // For now, let's just say it consumes *some* goo.
                } else {
                    world.removeBlock(pos, false);
                }
            }
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0F - quantaPercentage, 1.0F - quantaPercentage, 1.0F - quantaPercentage));
            
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (ModPotions.VIS_EXHAUST != null) {
                    // livingEntity.addEffect(new PotionEffect(ModPotions.VIS_EXHAUST.get(), 600, Math.min(level / 2, 2)));
                }
                if (world.random.nextInt(50) == 0) {
                    livingEntity.hurt(DamageSourceThaumcraft.FLUX, 1.0F + (level / 3.0F) );
                }
            }
        }
        super.entityInside(state, world, pos, entity);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        super.randomTick(state, world, pos, rand);

        if (world.isClientSide) return;

        int level = state.getValue(LEVEL);
        boolean isSource = state.getValue(LEVEL) == 0 && !state.getValue(FALLING);
        isSource = state.getFluidState().isSource();
        level = state.getFluidState().getAmount();

        if (level >= 2 && level < 6 && world.isEmptyBlock(pos.above()) && rand.nextInt(150) == 0) {
            world.removeBlock(pos, false);
            if (ModEntities.THAUMIC_SLIME != null) {
                EntityThaumicSlime slime = ModEntities.THAUMIC_SLIME.get().create(world);
                if (slime != null) {
                    slime.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    slime.setSlimeSize(1, true);
                    world.addFreshEntity(slime);
                    if (ModSounds.GORE != null) world.playSound(null, pos, ModSounds.GORE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
            }
            return;
        }
        if (level >= 6 && world.isEmptyBlock(pos.above()) && rand.nextInt(100) == 0) {
            world.removeBlock(pos, false);
             if (ModEntities.THAUMIC_SLIME != null) {
                EntityThaumicSlime slime = ModEntities.THAUMIC_SLIME.get().create(world);
                if (slime != null) {
                    slime.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    slime.setSlimeSize(2, true);
                    world.addFreshEntity(slime);
                    if (ModSounds.GORE != null) world.playSound(null, pos, ModSounds.GORE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
            }
            return;
        }

        if (rand.nextInt(10) == 0) {
            if (!isSource && level == 1) {
                if (rand.nextBoolean()) {
                    AuraHelper.polluteAura(world, pos, 0.25f, true);
                    world.removeBlock(pos, false);
                } else {
                    if (ModBlocks.TAINT_FIBRE.isPresent()) {
                        world.setBlock(pos, ModBlocks.TAINT_FIBRE.get().defaultBlockState(), 3);
                    }
                }
            } else if (isSource || level > 1) {
                AuraHelper.polluteAura(world, pos, 0.1f * level, true);
            }
            return;
        }
    }

    private float getQuantaPercentage(IBlockReader world, BlockPos pos, BlockState state) {
        int level = state.getFluidState().getAmount();
        return level / 8.0f;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.animateTick(state, world, pos, rand);
        if (!Minecraft.getInstance().shouldEntityAppearGlowing(null)) return;

        int level = state.getFluidState().getAmount();
        if (level == 0) return;

        if (rand.nextInt(44) <= level * 5) {
            if (rand.nextInt(3) == 0) {
                world.addParticle(net.minecraft.particles.ParticleTypes.PORTAL, 
                                pos.getX() + rand.nextDouble(), 
                                pos.getY() + (level / 8.0f) * 0.9 + 0.1,
                                pos.getZ() + rand.nextDouble(), 
                                (rand.nextDouble() - 0.5) * 0.1, 
                                rand.nextDouble() * 0.1 + 0.05, 
                                (rand.nextDouble() - 0.5) * 0.1);
            }
        }
    }
    
    @Override
    public void die(World world, BlockPos pos, BlockState blockState) {
        AuraHelper.polluteAura(world, pos, 1.0f * blockState.getFluidState().getAmount(), true);
        world.removeBlock(pos, false);
    }
}
