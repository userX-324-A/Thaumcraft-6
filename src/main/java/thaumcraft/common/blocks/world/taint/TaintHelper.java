package thaumcraft.common.blocks.world.taint;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.aura.AuraHandler;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class TaintHelper {
    private static ConcurrentHashMap<Integer, ArrayList<BlockPos>> taintSeeds = new ConcurrentHashMap<>();

    public static void addTaintSeed(World world, BlockPos pos) {
        ArrayList<BlockPos> locs = taintSeeds.get(world.dimension().getId());
        if (locs == null) {
            locs = new ArrayList<>();
        }
        locs.add(pos);
        taintSeeds.put(world.dimension().getId(), locs);
    }

    public static void removeTaintSeed(World world, BlockPos pos) {
        ArrayList<BlockPos> locs = taintSeeds.get(world.dimension().getId());
        if (locs != null && !locs.isEmpty()) {
            locs.remove(pos);
        }
    }

    public static boolean isNearTaintSeed(World world, BlockPos pos) {
        double area = ModConfig.CONFIG_WORLD.taintSpreadArea * ModConfig.CONFIG_WORLD.taintSpreadArea;
        ArrayList<BlockPos> locs = taintSeeds.get(world.dimension().getId());
        if (locs != null && !locs.isEmpty()) {
            for (BlockPos p : locs) {
                if (p.distSqr(pos) <= area) {
                    if (EntityUtils.getEntitiesInRange(world, p, null, EntityTaintSeed.class, 1.0).size() <= 0) {
                        removeTaintSeed(world, p);
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAtTaintSeedEdge(World world, BlockPos pos) {
        double area = ModConfig.CONFIG_WORLD.taintSpreadArea * ModConfig.CONFIG_WORLD.taintSpreadArea;
        double fringe = ModConfig.CONFIG_WORLD.taintSpreadArea * 0.8 * (ModConfig.CONFIG_WORLD.taintSpreadArea * 0.8);
        ArrayList<BlockPos> locs = taintSeeds.get(world.dimension().getId());
        if (locs != null && !locs.isEmpty()) {
            for (BlockPos p : locs) {
                double d = p.distSqr(pos);
                if (d < area && d > fringe) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void spreadFibres(World world, BlockPos pos) {
        spreadFibres(world, pos, false);
    }

    public static void spreadFibres(World world, BlockPos pos, boolean force) {
        if (!world.isClientSide && (force || world.random.nextInt(10) == 0)) {
            BlockPos t = pos;
            BlockState bs = world.getBlockState(t);
            Material bm = bs.getMaterial();

            if (bs.getBlock() == BlocksTC.taintLog) {
                world.setBlock(t, BlocksTC.taintLog.defaultBlockState().setValue(BlockTaintLog.AXIS, BlockUtils.getBlockAxis(world, t)), 3);
                return;
            }
            if (bs.getBlock() == Blocks.RED_MUSHROOM_BLOCK || bs.getBlock() == Blocks.BROWN_MUSHROOM_BLOCK || bm == Material.VEGETABLE || bm == Material.CACTUS || bm == Material.CORAL || bm == Material.SPONGE || bm == Material.WOOD) {
                world.setBlock(t, BlocksTC.taintCrust.defaultBlockState(), 3);
                world.blockEvent(t, BlocksTC.taintCrust, 1, 0);
                AuraHelper.drainFlux(world, t, 0.01f, false);
                return;
            }
            if (bm == Material.SAND || bm == Material.DIRT || bm == Material.GRASS || bm == Material.CLAY) {
                world.setBlock(t, BlocksTC.taintSoil.defaultBlockState(), 3);
                world.blockEvent(t, BlocksTC.taintSoil, 1, 0);
                AuraHelper.drainFlux(world, t, 0.01f, false);
                return;
            }
            if (bm == Material.STONE) {
                world.setBlock(t, BlocksTC.taintRock.defaultBlockState(), 3);
                world.blockEvent(t, BlocksTC.taintRock, 1, 0);
                AuraHelper.drainFlux(world, t, 0.01f, false);
                return;
            }
        }
    }
}
