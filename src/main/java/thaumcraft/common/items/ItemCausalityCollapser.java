package thaumcraft.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import thaumcraft.api.aura.AuraHelper;

public class ItemCausalityCollapser extends Item {
    public ItemCausalityCollapser(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        World level = ctx.getLevel();
        if (level.isClientSide) return ActionResultType.SUCCESS;
        BlockPos pos = ctx.getClickedPos();
        PlayerEntity player = ctx.getPlayer();
        // Tunables via config
        final int radius = thaumcraft.common.config.ModConfig.COMMON.collapserRadius.get();
        final float fluxDrainTotal = thaumcraft.common.config.ModConfig.COMMON.collapserFluxDrainTotal.get().floatValue();
        final float visDrainTotal = thaumcraft.common.config.ModConfig.COMMON.collapserVisDrainTotal.get().floatValue();
        final int cooldownTicks = 20 * thaumcraft.common.config.ModConfig.COMMON.collapserCooldownSeconds.get();

        // Distribute drain across sample points in a small grid within radius
        int step = 4;
        int samples = 0;
        for (int dx = -radius; dx <= radius; dx += step) {
            for (int dz = -radius; dz <= radius; dz += step) {
                if (dx * dx + dz * dz <= radius * radius) samples++;
            }
        }
        float perFlux = fluxDrainTotal / Math.max(1, samples);
        float perVis = visDrainTotal / Math.max(1, samples);
        for (int dx = -radius; dx <= radius; dx += step) {
            for (int dz = -radius; dz <= radius; dz += step) {
                if (dx * dx + dz * dz > radius * radius) continue;
                BlockPos p = pos.offset(dx, 0, dz);
                AuraHelper.drainFlux(level, p, perFlux);
                AuraHelper.drainVis(level, p, perVis);
            }
        }

        // Custom sound event (replace placeholder asset in sounds.json as needed)
        level.playSound(null, pos, thaumcraft.common.registers.SoundsTC.CAUSALITY_COLLAPSE.get(), SoundCategory.PLAYERS, 1.0f, 0.8f);
        if (player != null) {
            player.getCooldowns().addCooldown(this, cooldownTicks);
        }
        // Visual hint
        if (thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) {
            ((net.minecraft.world.server.ServerWorld) level).sendParticles(net.minecraft.particles.ParticleTypes.END_ROD,
                    pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                    80, 2.0, 0.8, 2.0, 0.05);
            ((net.minecraft.world.server.ServerWorld) level).sendParticles(net.minecraft.particles.ParticleTypes.PORTAL,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    40, 1.2, 0.4, 1.2, 0.05);
        }
        ItemStack stack = ctx.getItemInHand();
        if (!stack.isEmpty() && (player == null || !player.isCreative())) stack.hurtAndBreak(1, player, p -> {});
        return ActionResultType.CONSUME;
    }
}



