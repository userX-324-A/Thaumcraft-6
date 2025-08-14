package thaumcraft.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import java.util.Random;
// import thaumcraft.api.aura.AuraHelper;

/**
 * Lightweight aura seeding per-chunk. On placement, it bumps a chunk's aura slightly
 * to simulate legacy aura node distribution during terrain gen.
 */
public class AuraSeedFeature extends Feature<NoFeatureConfig> {
    public AuraSeedFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        // seed a small random aura baseline in this chunk once in a while
        if (random.nextInt(5) != 0) return false;
        float base = 80f + random.nextFloat() * 60f; // 80-140
        // lean a bit higher near y ~ 64
        float bonus = Math.max(0, 16 - Math.abs(64 - pos.getY())) * 0.5f;
        // Set baseline once per chunk rather than just adding vis
        thaumcraft.common.world.aura.AuraWorldData.get(level.getLevel()).seedBaseline(pos, base + bonus);
        return true;
    }
}



