package thaumcraft.common.world.features.trees;

import net.minecraft.block.trees.AbstractTreeGrower;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class SilverwoodTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        // TODO: Replace with actual Silverwood tree feature
        return null; // Returning null might cause issues, or default to vanilla oak.
    }
} 