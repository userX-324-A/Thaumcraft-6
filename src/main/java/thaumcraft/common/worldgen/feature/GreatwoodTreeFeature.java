package thaumcraft.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import thaumcraft.common.registers.ModBlocks;

import java.util.Random;

/**
 * Minimal placeholder tree: straight trunk with a simple leaf canopy.
 */
public class GreatwoodTreeFeature extends Feature<NoFeatureConfig> {
    public GreatwoodTreeFeature(Codec<NoFeatureConfig> codec) { super(codec); }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig config) {
        BlockPos ground = level.getHeightmapPos(net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, origin);
        int height = 6 + random.nextInt(5);
        BlockState log = ModBlocks.LOG_GREATWOOD.get().defaultBlockState();
        BlockState leaves = ModBlocks.LEAVES_GREATWOOD.get().defaultBlockState();

        // Trunk
        for (int i = 0; i < height; i++) {
            BlockPos p = ground.above(i);
            if (!level.isEmptyBlock(p)) return false;
            level.setBlock(p, log, 2);
        }

        // Simple canopy
        BlockPos top = ground.above(height - 1);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int dist = Math.abs(dx) + Math.abs(dz);
                if (dist > 3) continue;
                for (int dy = 0; dy <= 2; dy++) {
                    BlockPos lp = top.offset(dx, dy, dz);
                    if (level.isEmptyBlock(lp)) level.setBlock(lp, leaves, 2);
                }
            }
        }

        // Few branches
        for (Direction d : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            if (random.nextBoolean()) {
                BlockPos bp = top.relative(d);
                if (level.isEmptyBlock(bp)) level.setBlock(bp, log, 2);
            }
        }

        return true;
    }
}



