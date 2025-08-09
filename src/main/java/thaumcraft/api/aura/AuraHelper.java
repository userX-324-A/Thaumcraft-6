package thaumcraft.api.aura;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AuraHelper {
    // Temporary in-memory aura pool per chunk to enable crafting gates during migration
    private static final java.util.Map<Long, Float> CHUNK_VIS = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Map<Long, Float> CHUNK_FLUX = new java.util.concurrent.ConcurrentHashMap<>();

    private static long key(World world, BlockPos pos) {
        // key by dimension (best effort across mappings) + chunk coords
        long dimHash = 0L;
        try {
            // official mappings 1.16+
            Object rk = world.getClass().getMethod("dimension").invoke(world);
            Object rl = rk.getClass().getMethod("location").invoke(rk);
            dimHash = rl.toString().hashCode();
        } catch (Throwable t) {
            try {
                // MCP style: getDimensionKey().getLocation()
                Object rk = world.getClass().getMethod("getDimensionKey").invoke(world);
                Object rl = rk.getClass().getMethod("getLocation").invoke(rk);
                dimHash = rl.toString().hashCode();
            } catch (Throwable t2) {
                dimHash = 0L;
            }
        }
        long cx = (long) (pos.getX() >> 4);
        long cz = (long) (pos.getZ() >> 4);
        return (dimHash << 32) ^ ((cx & 0xFFFF_FFFFL) << 16) ^ (cz & 0xFFFFL);
    }

    public static float getAura(World world, BlockPos pos) {
        return CHUNK_VIS.getOrDefault(key(world, pos), 100f);
    }

    public static int getVis(World world, BlockPos pos) {
        return (int) getAura(world, pos);
    }

    public static float getFlux(World world, BlockPos pos) {
        return CHUNK_FLUX.getOrDefault(key(world, pos), 0f);
    }

    public static void addVis(World world, BlockPos pos, float amount) {
        long k = key(world, pos);
        CHUNK_VIS.put(k, getAura(world, pos) + amount);
    }

    public static void addFlux(World world, BlockPos pos, float amount) {
        long k = key(world, pos);
        CHUNK_FLUX.put(k, getFlux(world, pos) + amount);
    }

    public static boolean drainVis(World world, BlockPos pos, float amount, boolean simulate) {
        float current = getAura(world, pos);
        if (current < amount) return false;
        if (!simulate) {
            addVis(world, pos, -amount);
        }
        return true;
    }

    public static float drainVis(World world, BlockPos pos, float amount) {
        float current = getAura(world, pos);
        float taken = Math.min(current, amount);
        addVis(world, pos, -taken);
        return taken;
    }

    public static boolean drainFlux(World world, BlockPos pos, float amount, boolean simulate) {
        float current = getFlux(world, pos);
        if (current < amount) return false;
        if (!simulate) {
            addFlux(world, pos, -amount);
        }
        return true;
    }

    public static float drainFlux(World world, BlockPos pos, float amount) {
        float current = getFlux(world, pos);
        float taken = Math.min(current, amount);
        addFlux(world, pos, -taken);
        return taken;
    }

    public static void pollute(World world, BlockPos pos, int amount, boolean showEffect) {
        addFlux(world, pos, amount);
    }

    public static boolean shouldPreserveAura(World world, PlayerEntity player, BlockPos pos) {
        return false;
    }
}

