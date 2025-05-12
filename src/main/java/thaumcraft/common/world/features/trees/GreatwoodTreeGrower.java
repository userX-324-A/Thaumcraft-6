package thaumcraft.common.world.features.trees;

import net.minecraft.block.trees.AbstractTreeGrower;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class GreatwoodTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        // TODO: Replace with actual Greatwood tree feature
        return null; // Returning null might cause issues, or default to vanilla oak. We'll need to create and register a feature.
    }

    // For 2x2 Greatwood trees, we might need a mega feature
    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getConfiguredMegaFeature(Random randomIn) {
        // TODO: Replace with actual Greatwood mega tree feature (2x2)
        return null;
    }
} 