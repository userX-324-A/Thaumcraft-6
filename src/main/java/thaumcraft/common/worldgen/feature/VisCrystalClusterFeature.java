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
        // Try a few candidates near origin to find stone-adjacent pockets
        boolean placedAny = false;
        int attempts = 6 + random.nextInt(5);
        for (int a = 0; a < attempts; a++) {
            BlockPos base = origin.offset(random.nextInt(12) - 6, random.nextInt(20) - 10, random.nextInt(12) - 6);
            int clusterSize = 6 + random.nextInt(8);
            Block crystal = CRYSTAL_BLOCKS.get(random.nextInt(CRYSTAL_BLOCKS.size()));
            placedAny |= placeCluster(level, random, base, clusterSize, crystal);
        }
        return placedAny;
    }

    private boolean placeCluster(ISeedReader level, Random random, BlockPos center, int size, Block crystal) {
        boolean placed = false;
        for (int i = 0; i < size; i++) {
            BlockPos p = center.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
            if (!level.isEmptyBlock(p) && !canReplace(level.getBlockState(p))) continue;
            if (!isTouchingSolid(level, p)) continue;
            BlockState state = crystal.defaultBlockState();
            if (state.getBlock() instanceof FlowingFluidBlock) continue;
            level.setBlock(p, state, 2);
            placed = true;
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
            if (bs.getMaterial().isSolid()) return true;
        }
        return false;
    }
}


