package thaumcraft.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import thaumcraft.common.registers.ModBlocks;

import java.util.Random;

/**
 * Minimal placeholder silverwood tree.
 */
public class SilverwoodTreeFeature extends Feature<NoFeatureConfig> {
    public SilverwoodTreeFeature(Codec<NoFeatureConfig> codec) { super(codec); }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig config) {
        BlockPos ground = level.getHeightmapPos(net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, origin);
        int height = 7 + random.nextInt(5);
        BlockState log = ModBlocks.LOG_SILVERWOOD.get().defaultBlockState();
        BlockState leaves = ModBlocks.LEAVES_SILVERWOOD.get().defaultBlockState();

        for (int i = 0; i < height; i++) {
            BlockPos p = ground.above(i);
            if (!level.isEmptyBlock(p)) return false;
            level.setBlock(p, log, 2);
        }

        BlockPos top = ground.above(height - 1);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                for (int dy = 0; dy <= 2; dy++) {
                    if (Math.abs(dx) + Math.abs(dz) + dy > 4) continue;
                    BlockPos lp = top.offset(dx, dy, dz);
                    if (level.isEmptyBlock(lp)) level.setBlock(lp, leaves, 2);
                }
            }
        }
        return true;
    }
}



