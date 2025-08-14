package thaumcraft.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import thaumcraft.common.registers.ModBlocks;

import java.util.Random;

/** Places shimmerleaf in cooler, non-desert biomes on grass. */
public class ShimmerleafFeature extends Feature<NoFeatureConfig> {
    public ShimmerleafFeature(Codec<NoFeatureConfig> codec) { super(codec); }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig cfg) {
        boolean placed = false;
        int tries = 6 + random.nextInt(5);
        for (int i = 0; i < tries; i++) {
            BlockPos p = level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, origin.offset(random.nextInt(8) - 4, 0, random.nextInt(8) - 4));
            BlockState below = level.getBlockState(p.below());
            if (below.is(net.minecraft.block.Blocks.GRASS_BLOCK) && level.isEmptyBlock(p)) {
                if (level.getBiome(p).getBaseTemperature() < 0.95f) {
                    level.setBlock(p, ModBlocks.SHIMMERLEAF.get().defaultBlockState(), 2);
                    placed = true;
                }
            }
        }
        return placed;
    }
}



