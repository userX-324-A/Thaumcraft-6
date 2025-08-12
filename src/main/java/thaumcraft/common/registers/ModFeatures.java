package thaumcraft.common.registers;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.Thaumcraft;
import thaumcraft.common.worldgen.feature.AuraSeedFeature;
import thaumcraft.common.worldgen.feature.VisCrystalClusterFeature;
import thaumcraft.common.worldgen.feature.GreatwoodTreeFeature;
import thaumcraft.common.worldgen.feature.SilverwoodTreeFeature;
import thaumcraft.common.worldgen.feature.CinderpearlFeature;
import thaumcraft.common.worldgen.feature.ShimmerleafFeature;
import thaumcraft.common.worldgen.feature.VishroomFeature;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Thaumcraft.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> AURA_SEED = FEATURES.register(
            "aura_seed",
            () -> new AuraSeedFeature(NoFeatureConfig.CODEC)
    );

    public static final RegistryObject<Feature<NoFeatureConfig>> VIS_CRYSTAL_CLUSTER = FEATURES.register(
            "vis_crystal_cluster",
            () -> new VisCrystalClusterFeature(NoFeatureConfig.CODEC)
    );

    public static final RegistryObject<Feature<NoFeatureConfig>> GREATWOOD_TREE = FEATURES.register(
            "greatwood_tree",
            () -> new GreatwoodTreeFeature(NoFeatureConfig.CODEC)
    );

    public static final RegistryObject<Feature<NoFeatureConfig>> SILVERWOOD_TREE = FEATURES.register(
            "silverwood_tree",
            () -> new SilverwoodTreeFeature(NoFeatureConfig.CODEC)
    );

    public static final RegistryObject<Feature<NoFeatureConfig>> CINDERPEARL = FEATURES.register(
            "cinderpearl",
            () -> new CinderpearlFeature(NoFeatureConfig.CODEC)
    );

    public static final RegistryObject<Feature<NoFeatureConfig>> SHIMMERLEAF = FEATURES.register(
            "shimmerleaf",
            () -> new ShimmerleafFeature(NoFeatureConfig.CODEC)
    );

    public static final RegistryObject<Feature<NoFeatureConfig>> VISHROOM = FEATURES.register(
            "vishroom",
            () -> new VishroomFeature(NoFeatureConfig.CODEC)
    );
}


