package thaumcraft.common.registers;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;

public class RegistryManager {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Thaumcraft.MODID);
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Thaumcraft.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Thaumcraft.MODID);
    public static final DeferredRegister<ContainerType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Thaumcraft.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Thaumcraft.MODID);


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        ENTITIES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        MENUS.register(eventBus);
        SOUNDS.register(eventBus);
        ModFeatures.FEATURES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = thaumcraft.Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusHandlers {
        @SubscribeEvent
        public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(thaumcraft.common.registers.ModEntities.FIRE_BAT.get(),
                    thaumcraft.common.entities.monster.FireBatEntity.createAttributes().build());
            event.put(thaumcraft.common.registers.ModEntities.ELDRITCH_CRAB.get(),
                    thaumcraft.common.entities.monster.EldritchCrabEntity.createAttributes().build());
            event.put(thaumcraft.common.registers.ModEntities.TC_GOLEM.get(),
                    thaumcraft.common.entities.golem.ThaumcraftGolemEntity.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = thaumcraft.Thaumcraft.MODID)
    public static class ForgeBusHandlers {
        @SubscribeEvent
        public static void onBiomeLoading(BiomeLoadingEvent event) {
            // Nether spawns baseline
            if (event.getCategory() == net.minecraft.world.biome.Biome.Category.NETHER) {
                event.getSpawns().getSpawner(net.minecraft.entity.EntityClassification.MONSTER).add(
                        new net.minecraft.world.biome.MobSpawnInfo.Spawners(
                                thaumcraft.common.registers.ModEntities.FIRE_BAT.get(), 15, 1, 3));
            }
            // Seasonal overworld spawns on Oct 31
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int month = calendar.get(java.util.Calendar.MONTH) + 1; // 1..12
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
            if (month == 10 && day == 31) {
                switch (event.getCategory()) {
                    case PLAINS:
                    case FOREST:
                    case SWAMP:
                    case TAIGA:
                    case EXTREME_HILLS:
                    case SAVANNA:
                    case JUNGLE:
                    case DESERT:
                    case MUSHROOM:
                    case MESA:
                    case BEACH:
                    case RIVER:
                        event.getSpawns().getSpawner(net.minecraft.entity.EntityClassification.MONSTER).add(
                                new net.minecraft.world.biome.MobSpawnInfo.Spawners(
                                        thaumcraft.common.registers.ModEntities.FIRE_BAT.get(), 5, 1, 2));
                        break;
                    default:
                        break;
                }
            }

            // Worldgen: crystals in overworld caves, low chance in nether
            net.minecraft.world.gen.GenerationStage.Decoration decoUnderground = net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_DECORATION;
            if (event.getCategory() != net.minecraft.world.biome.Biome.Category.THEEND) {
                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> visCrystals = ModFeatures.VIS_CRYSTAL_CLUSTER.get()
                        .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                        .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                        .count(2);
                event.getGeneration().addFeature(decoUnderground, visCrystals);
            }

            // Worldgen: aura seeding sparse near surface
            net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> auraSeed = ModFeatures.AURA_SEED.get()
                    .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                    .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                    .count(1);
            event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.TOP_LAYER_MODIFICATION, auraSeed);

            // Worldgen: trees
            if (event.getCategory() == net.minecraft.world.biome.Biome.Category.FOREST || event.getCategory() == net.minecraft.world.biome.Biome.Category.PLAINS || event.getCategory() == net.minecraft.world.biome.Biome.Category.TAIGA) {
                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> greatwood = ModFeatures.GREATWOOD_TREE.get()
                        .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                        .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                        .count(1);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION, greatwood);

                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> silverwood = ModFeatures.SILVERWOOD_TREE.get()
                        .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                        .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                        .count(1);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION, silverwood);
            }

            // Worldgen: plants
            if (event.getCategory() == net.minecraft.world.biome.Biome.Category.DESERT || event.getCategory() == net.minecraft.world.biome.Biome.Category.SAVANNA) {
                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> cinder = ModFeatures.CINDERPEARL.get()
                        .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                        .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                        .count(2);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION, cinder);
            } else {
                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> shimmer = ModFeatures.SHIMMERLEAF.get()
                        .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                        .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                        .count(1);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION, shimmer);
            }

            if (event.getCategory() != net.minecraft.world.biome.Biome.Category.THEEND) {
                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> vishroom = ModFeatures.VISHROOM.get()
                        .configured(net.minecraft.world.gen.feature.NoFeatureConfig.INSTANCE)
                        .decorated(net.minecraft.world.gen.feature.Features.Placements.HEIGHTMAP_SQUARE)
                        .count(1);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_DECORATION, vishroom);
            }

            // Worldgen: ores (overworld)
            if (event.getCategory() != net.minecraft.world.biome.Biome.Category.NETHER && event.getCategory() != net.minecraft.world.biome.Biome.Category.THEEND) {
                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> cinnabar = net.minecraft.world.gen.feature.Feature.ORE
                        .configured(new net.minecraft.world.gen.feature.OreFeatureConfig(
                                net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                thaumcraft.common.registers.ModBlocks.ORE_CINNABAR.get().defaultBlockState(),
                                6))
                        .range(32)
                        .squared()
                        .count(8);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_ORES, cinnabar);

                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> quartz = net.minecraft.world.gen.feature.Feature.ORE
                        .configured(new net.minecraft.world.gen.feature.OreFeatureConfig(
                                net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                thaumcraft.common.registers.ModBlocks.ORE_QUARTZ.get().defaultBlockState(),
                                8))
                        .range(48)
                        .squared()
                        .count(8);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_ORES, quartz);

                net.minecraft.world.gen.feature.ConfiguredFeature<?, ?> amber = net.minecraft.world.gen.feature.Feature.ORE
                        .configured(new net.minecraft.world.gen.feature.OreFeatureConfig(
                                net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                thaumcraft.common.registers.ModBlocks.ORE_AMBER.get().defaultBlockState(),
                                6))
                        .range(80)
                        .squared()
                        .count(3);
                event.getGeneration().addFeature(net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_ORES, amber);
            }
        }
    }
}
