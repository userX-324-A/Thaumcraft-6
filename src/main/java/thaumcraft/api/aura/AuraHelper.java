package thaumcraft.api.aura;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.common.world.aura.AuraWorldData;

public class AuraHelper {
    

    public static float getAura(World world, BlockPos pos) {
        if (world instanceof ServerWorld) {
            return AuraWorldData.get((ServerWorld) world).getVis(pos);
        }
        return 0f;
    }

    public static int getVis(World world, BlockPos pos) {
        return (int) getAura(world, pos);
    }

    public static float getFlux(World world, BlockPos pos) {
        if (world instanceof ServerWorld) {
            return AuraWorldData.get((ServerWorld) world).getFlux(pos);
        }
        return 0f;
    }

    public static void addVis(World world, BlockPos pos, float amount) {
        if (world instanceof ServerWorld) {
            AuraWorldData.get((ServerWorld) world).addVis(pos, amount);
        }
    }

    public static void addFlux(World world, BlockPos pos, float amount) {
        if (world instanceof ServerWorld) {
            AuraWorldData.get((ServerWorld) world).addFlux(pos, amount);
        }
    }

    public static boolean drainVis(World world, BlockPos pos, float amount, boolean simulate) {
        if (world instanceof ServerWorld) {
            double mul = thaumcraft.common.config.ModConfig.COMMON.auraDrainMultiplier.get();
            float tuned = (float) (amount * mul);
            return AuraWorldData.get((ServerWorld) world).drainVis(pos, tuned, simulate);
        }
        return false;
    }

    public static float drainVis(World world, BlockPos pos, float amount) {
        if (world instanceof ServerWorld) {
            double mul = thaumcraft.common.config.ModConfig.COMMON.auraDrainMultiplier.get();
            float tuned = (float) (amount * mul);
            return AuraWorldData.get((ServerWorld) world).drainVis(pos, tuned);
        }
        return 0f;
    }

    public static boolean drainFlux(World world, BlockPos pos, float amount, boolean simulate) {
        if (world instanceof ServerWorld) {
            return AuraWorldData.get((ServerWorld) world).drainFlux(pos, amount, simulate);
        }
        return false;
    }

    public static float drainFlux(World world, BlockPos pos, float amount) {
        if (world instanceof ServerWorld) {
            return AuraWorldData.get((ServerWorld) world).drainFlux(pos, amount);
        }
        return 0f;
    }

    public static void pollute(World world, BlockPos pos, int amount, boolean showEffect) {
        addFlux(world, pos, amount);
    }

    public static boolean shouldPreserveAura(World world, PlayerEntity player, BlockPos pos) {
        return false;
    }
}


