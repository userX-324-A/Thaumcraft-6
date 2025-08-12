package thaumcraft.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import thaumcraft.common.registers.ModBlocks;

import java.util.Random;

/** Places cinderpearl on sand in hot/desert-like biomes. */
public class CinderpearlFeature extends Feature<NoFeatureConfig> {
    public CinderpearlFeature(Codec<NoFeatureConfig> codec) { super(codec); }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig cfg) {
        Biome biome = level.getBiome(origin);
        if (biome.getBaseTemperature() <= 1.0f) return false;

        boolean placed = false;
        int tries = 8 + random.nextInt(6);
        for (int i = 0; i < tries; i++) {
            BlockPos p = level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, origin.offset(random.nextInt(8) - 4, 0, random.nextInt(8) - 4));
            BlockState below = level.getBlockState(p.below());
            if ((below.is(Blocks.SAND) || below.is(Blocks.RED_SAND)) && level.isEmptyBlock(p)) {
                // keep a bit away from water edges
                int waterAdj = 0;
                for (Direction d : Direction.Plane.HORIZONTAL) {
                    if (level.getBlockState(p.relative(d)).is(Blocks.WATER)) waterAdj++;
                }
                if (waterAdj > 1) continue;
                level.setBlock(p, ModBlocks.CINDERPEARL.get().defaultBlockState(), 2);
                placed = true;
            }
        }
        return placed;
    }
}


