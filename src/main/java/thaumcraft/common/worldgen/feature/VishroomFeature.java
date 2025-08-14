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

/** Places vishroom underground on stone with low light. */
public class VishroomFeature extends Feature<NoFeatureConfig> {
    public VishroomFeature(Codec<NoFeatureConfig> codec) { super(codec); }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig cfg) {
        boolean placed = false;
        int tries = 10;
        for (int i = 0; i < tries; i++) {
            BlockPos p = origin.offset(random.nextInt(12) - 6, random.nextInt(24) - 12, random.nextInt(12) - 6);
            if (!level.isEmptyBlock(p)) continue;
            if (p.getY() > level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, p).getY() - 4) continue; // prefer underground
            BlockState below = level.getBlockState(p.below());
            if (!below.getMaterial().isSolid()) continue;
            level.setBlock(p, ModBlocks.VISHROOM.get().defaultBlockState(), 2);
            placed = true;
        }
        return placed;
    }
}



