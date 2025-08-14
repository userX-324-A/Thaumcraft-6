package thaumcraft.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Minimal client FX system to replace the legacy FXDispatcher. This is intentionally lightweight
 * so networking can be wired now; effects can be improved incrementally.
 */
public final class FXClient {
    private FXClient() {}

    private static ClientWorld world() {
        return Minecraft.getInstance().level;
    }

    public static void pollute(BlockPos pos, int amount) {
        ClientWorld w = world(); if (w == null) return;
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        for (int i = 0; i < Math.min(amount, 40); i++) {
            double x = pos.getX() + 0.2 + w.random.nextDouble() * 0.6;
            double y = pos.getY() + 0.2 + w.random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.2 + w.random.nextDouble() * 0.6;
            w.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0, 0.02, 0.0);
        }
    }

    public static void blockBamf(double x, double y, double z, int color, boolean sound, boolean flair, Direction face) {
        ClientWorld w = world(); if (w == null) return;
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        if (sound) {
            w.playLocalSound(x, y, z, SoundEvents.FIREWORK_ROCKET_BLAST, SoundCategory.BLOCKS, 0.4f, 1.0f, false);
        }
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        for (int i = 0; i < 12; i++) {
            double vx = (w.random.nextDouble() - 0.5) * 0.1;
            double vy = (w.random.nextDouble() - 0.5) * 0.1;
            double vz = (w.random.nextDouble() - 0.5) * 0.1;
            if (face != null) {
                vx += face.getStepX() * 0.05;
                vy += face.getStepY() * 0.05;
                vz += face.getStepZ() * 0.05;
            }
            w.addParticle(new RedstoneParticleData(r, g, b, 1.0f), x + vx * 2, y + vy * 2, z + vz * 2, 0, 0, 0);
        }
        if (flair) {
            for (int i = 0; i < 6; i++) {
                w.addParticle(ParticleTypes.WITCH, x, y, z, 0, 0.01, 0);
            }
        }
    }

    public static void zap(Vector3d src, Vector3d dst, int color, float width) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        // Draw a simple spark line between points
        int steps = 8;
        double dx = (dst.x - src.x) / steps;
        double dy = (dst.y - src.y) / steps;
        double dz = (dst.z - src.z) / steps;
        for (int i = 0; i <= steps; i++) {
            double x = src.x + dx * i;
            double y = src.y + dy * i;
            double z = src.z + dz * i;
            w.addParticle(ParticleTypes.CRIT, x, y, z, 0, 0, 0);
        }
    }

    public static void wispZap(int sourceId, int targetId) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        net.minecraft.entity.Entity s = w.getEntity(sourceId);
        net.minecraft.entity.Entity t = w.getEntity(targetId);
        if (s == null || t == null) return;
        Vector3d src = s.position().add(0, s.getBbHeight() * 0.5, 0);
        Vector3d dst = t.position().add(0, t.getBbHeight() * 0.5, 0);
        zap(src, dst, 0x66CCFF, 0.04f);
    }

    public static void sonic(int sourceId) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        net.minecraft.entity.Entity s = w.getEntity(sourceId);
        if (s == null) return;
        for (int i = 0; i < 8; i++) {
            w.addParticle(ParticleTypes.END_ROD, s.getX(), s.getY() + s.getBbHeight() * 0.5, s.getZ(), (w.random.nextDouble() - 0.5) * 0.1, 0.02, (w.random.nextDouble() - 0.5) * 0.1);
        }
    }

    public static void slash(int sourceId, int targetId) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        net.minecraft.entity.Entity s = w.getEntity(sourceId);
        net.minecraft.entity.Entity t = w.getEntity(targetId);
        if (s == null || t == null) return;
        Vector3d mid = s.position().add(t.position()).scale(0.5);
        for (int i = 0; i < 10; i++) {
            w.addParticle(ParticleTypes.SWEEP_ATTACK, mid.x, mid.y + 1e-2, mid.z, 0, 0, 0);
        }
    }

    public static void shield(int sourceId, int targetId) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        net.minecraft.entity.Entity s = w.getEntity(sourceId);
        net.minecraft.entity.Entity t = w.getEntity(targetId);
        if (s == null || t == null) return;
        Vector3d p = t.position().add(0, t.getBbHeight() * 0.5, 0);
        for (int i = 0; i < 16; i++) w.addParticle(ParticleTypes.INSTANT_EFFECT, p.x, p.y, p.z, 0, 0, 0);
    }

    public static void blockArc(int bx, int by, int bz, float tx, float ty, float tz, float r, float g, float b) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        Vector3d src = new Vector3d(bx + 0.5, by + 0.5, bz + 0.5);
        Vector3d dst = new Vector3d(tx, ty, tz);
        zap(src, dst, ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255), 0.04f);
    }

    public static void blockMist(BlockPos pos, int color) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        for (int i = 0; i < 8; i++) {
            double x = pos.getX() + w.random.nextDouble();
            double y = pos.getY() + w.random.nextDouble();
            double z = pos.getZ() + w.random.nextDouble();
            w.addParticle(new RedstoneParticleData(r, g, b, 1.0f), x, y, z, 0, 0.01, 0);
        }
    }

    public static void boreDig(int x, int y, int z, int boreEntityId, int delay) {
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableParticles.get()) return;
        ClientWorld w = world(); if (w == null) return;
        for (int i = 0; i < 6; i++) w.addParticle(ParticleTypes.CRIT, x + 0.5, y + 0.5, z + 0.5, 0, 0, 0);
    }

    public static void essentiaSource(int x, int y, int z, byte dx, byte dy, byte dz, int color, int ext) {
        blockMist(new BlockPos(x, y, z), color);
    }

    public static void focusEffect(float x, float y, float z) {
        ClientWorld w = world(); if (w == null) return;
        for (int i = 0; i < 12; i++) w.addParticle(ParticleTypes.PORTAL, x, y, z, 0, 0, 0);
    }

    public static void focusImpact(float x, float y, float z) {
        ClientWorld w = world(); if (w == null) return;
        for (int i = 0; i < 10; i++) w.addParticle(ParticleTypes.POOF, x, y, z, 0, 0.02, 0);
    }

    public static void focusImpactBurst(float x, float y, float z) {
        focusImpact(x, y, z);
    }

    public static void infusionSource(BlockPos p1, BlockPos p2, int color) {
        ClientWorld w = world(); if (w == null) return;
        blockArc(p1.getX(), p1.getY(), p1.getZ(), p2.getX() + 0.5f, p2.getY() + 0.5f, p2.getZ() + 0.5f, 0.6f, 0.2f, 0.8f);
    }

    public static void scanSource(BlockPos pos, int size) {
        ClientWorld w = world(); if (w == null) return;
        for (int i = 0; i < Math.max(6, size); i++) w.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
    }
}



