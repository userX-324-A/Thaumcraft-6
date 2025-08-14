package thaumcraft.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import thaumcraft.common.registers.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Places small clusters of Thaumcraft crystal blocks embedded in stone-like terrain.
 */
public class VisCrystalClusterFeature extends Feature<NoFeatureConfig> {

    private static final List<Block> CRYSTAL_BLOCKS = new ArrayList<>();
    static {
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_AIR.get());
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_FIRE.get());
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_WATER.get());
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_EARTH.get());
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_ORDER.get());
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_ENTROPY.get());
        // Optional taint
        CRYSTAL_BLOCKS.add(ModBlocks.CRYSTAL_TAINT.get());
    }

    public VisCrystalClusterFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig config) {
        // Parity with 1.12 behavior:
        // - attempts ~ t (8 overworld; 1 nether)
        // - adjacency to stone-like blocks
        // - cap total placed per chunk
        int maxPerChunk = thaumcraft.common.config.ModConfig.COMMON.wgCrystalsMaxPerChunk.get();
        int attempts = thaumcraft.common.config.ModConfig.COMMON.wgCrystalsAttemptsPerChunk.get();
        // If in Nether, reduce attempts by divisor
        net.minecraft.world.biome.Biome.Category cat = level.getBiome(origin).getBiomeCategory();
        if (cat == net.minecraft.world.biome.Biome.Category.NETHER) {
            int div = Math.max(1, thaumcraft.common.config.ModConfig.COMMON.wgCrystalsNetherDivisor.get());
            attempts = Math.max(1, attempts / div);
        }

        boolean placedAny = false;
        int placedTotal = 0;
        for (int a = 0; a < attempts && placedTotal < maxPerChunk; a++) {
            BlockPos base = origin.offset(random.nextInt(12) - 6, random.nextInt(20) - 10, random.nextInt(12) - 6);
            int clusterSize = 6 + random.nextInt(8);
            Block crystal = CRYSTAL_BLOCKS.get(random.nextInt(CRYSTAL_BLOCKS.size()));
            placedTotal += placeCluster(level, random, base, clusterSize, crystal, maxPerChunk - placedTotal);
            if (placedTotal > 0) placedAny = true;
        }
        return placedAny;
    }

    private int placeCluster(ISeedReader level, Random random, BlockPos center, int size, Block crystal, int remainingBudget) {
        int placed = 0;
        for (int i = 0; i < size && placed < remainingBudget; i++) {
            BlockPos p = center.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
            if (!level.isEmptyBlock(p) && !canReplace(level.getBlockState(p))) continue;
            if (!isTouchingSolid(level, p)) continue;
            BlockState state = crystal.defaultBlockState();
            if (state.getBlock() instanceof FlowingFluidBlock) continue;
            level.setBlock(p, state, 2);
            placed++;
        }
        return placed;
    }

    private boolean canReplace(BlockState state) {
        Material m = state.getMaterial();
        return m.isReplaceable() || m == Material.AIR || m == Material.PLANT || m == Material.WATER || m == Material.REPLACEABLE_PLANT;
    }

    private boolean isTouchingSolid(ISeedReader level, BlockPos pos) {
        for (BlockPos off : new BlockPos[]{pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()}) {
            BlockState bs = level.getBlockState(off);
            if (bs.getMaterial() == Material.STONE) return true;
        }
        return false;
    }
}



