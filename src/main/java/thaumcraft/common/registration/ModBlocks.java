package thaumcraft.common.registration;

import net.minecraft.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.basic.BlockPlanksTC;
import thaumcraft.common.blocks.basic.BlockStoneTC;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.common.blocks.basic.BlockMetalTC;
import thaumcraft.common.blocks.world.plants.BlockLogsTC;
import thaumcraft.common.blocks.world.plants.BlockLeavesTC;
import thaumcraft.common.blocks.world.plants.BlockSaplingTC;
import thaumcraft.common.world.features.trees.GreatwoodTreeGrower;
import thaumcraft.common.world.features.trees.SilverwoodTreeGrower;
import thaumcraft.common.blocks.world.ore.BlockOreAmber;
import thaumcraft.common.blocks.world.ore.BlockOreCinnabar;
import thaumcraft.common.blocks.world.ore.BlockOreTCQuartz;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;
import thaumcraft.common.blocks.world.taint.BlockFluxGoo;
import net.minecraft.block.FlowingFluidBlock;
import thaumcraft.common.registration.ModFluids;
import thaumcraft.common.blocks.world.taint.BlockTaint;
import thaumcraft.common.blocks.world.taint.BlockTaintFibre;
import thaumcraft.common.blocks.world.taint.BlockTaintFeature;
import thaumcraft.common.blocks.world.loot.BlockLoot;
import thaumcraft.common.blocks.world.crystal.BlockCrystal;
import thaumcraft.common.blocks.world.arcane.BlockArcaneWorkbench;
import thaumcraft.common.blocks.world.arcane.BlockArcaneWorkbenchCharger;
import thaumcraft.common.blocks.world.research.BlockResearchTable;
import thaumcraft.common.blocks.world.crucible.BlockCrucible;
import thaumcraft.common.blocks.world.lamp.BlockLamp;
import thaumcraft.common.blocks.world.lamp.TileLampArcane;
import thaumcraft.common.blocks.world.lamp.TileLampFertility;
import thaumcraft.common.blocks.world.lamp.TileLampGrowth;
import thaumcraft.common.blocks.world.effect.BlockEffect;
import thaumcraft.common.blocks.world.placeholder.BlockPlaceholder;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);

    private static AbstractBlock.Properties getStandardStoneProperties() {
        return AbstractBlock.Properties.of(Material.STONE)
                .strength(2.0f, 10.0f)
                .sound(SoundType.STONE)
                .harvestTool(ToolType.PICKAXE).harvestLevel(1);
    }

    // Planks
    public static final RegistryObject<Block> GREATWOOD_PLANKS = BLOCKS.register("greatwood_planks",
            () -> new BlockPlanksTC());
    public static final RegistryObject<Block> SILVERWOOD_PLANKS = BLOCKS.register("silverwood_planks",
            () -> new BlockPlanksTC());

    // Logs
    public static final RegistryObject<Block> GREATWOOD_LOG = BLOCKS.register("greatwood_log",
            () -> new BlockLogsTC(false, MaterialColor.WOOD, MaterialColor.PODZOL));
    public static final RegistryObject<Block> SILVERWOOD_LOG = BLOCKS.register("silverwood_log",
            () -> new BlockLogsTC(true, MaterialColor.SAND, MaterialColor.WOOD));

    // TAINT LOG
    public static final RegistryObject<Block> TAINT_LOG = BLOCKS.register("taint_log",
            () -> new BlockTaintLog(BlockTaintLog.TaintLogProperties()));

    // Leaves
    public static final RegistryObject<Block> GREATWOOD_LEAVES = BLOCKS.register("greatwood_leaves",
            () -> new BlockLeavesTC(false,
                    AbstractBlock.Properties.of(Material.LEAVES, MaterialColor.PLANT)
                            .strength(0.2F)
                            .randomTicks()
                            .sound(SoundType.GRASS)
                            .noOcclusion()
                            .flammable(30, 60)
            ));
    public static final RegistryObject<Block> SILVERWOOD_LEAVES = BLOCKS.register("silverwood_leaves",
            () -> new BlockLeavesTC(true,
                    AbstractBlock.Properties.of(Material.LEAVES, MaterialColor.DIAMOND) // Using DIAMOND as a placeholder for LIGHT_BLUE, can be refined
                            .strength(0.2F)
                            .randomTicks()
                            .sound(SoundType.GRASS)
                            .noOcclusion()
                            .flammable(30, 60)
                            .lightLevel((state) -> 2) // Silverwood leaves had some light
            ));

    // Saplings
    public static final RegistryObject<Block> GREATWOOD_SAPLING = BLOCKS.register("greatwood_sapling",
            () -> new BlockSaplingTC(new GreatwoodTreeGrower(),
                    AbstractBlock.Properties.of(Material.PLANT, MaterialColor.WOOD)
                            .noCollission()
                            .randomTicks()
                            .strength(0.0F)
                            .sound(SoundType.GRASS)
                            .flammable(60, 30)
            ));
    public static final RegistryObject<Block> SILVERWOOD_SAPLING = BLOCKS.register("silverwood_sapling",
            () -> new BlockSaplingTC(new SilverwoodTreeGrower(),
                    AbstractBlock.Properties.of(Material.PLANT, MaterialColor.SNOW) // SNOW as placeholder for light/silvery color
                            .noCollission()
                            .randomTicks()
                            .strength(0.0F)
                            .sound(SoundType.GRASS)
                            .flammable(60, 30)
            ));

    // Ores (New Section)
    public static final RegistryObject<Block> ORE_AMBER = BLOCKS.register("ore_amber",
            () -> new BlockOreAmber());
    public static final RegistryObject<Block> ORE_CINNABAR = BLOCKS.register("ore_cinnabar",
            () -> new BlockOreCinnabar());
    public static final RegistryObject<Block> ORE_QUARTZ = BLOCKS.register("ore_quartz",
            () -> new BlockOreTCQuartz());

    // Stone Variants
    public static final RegistryObject<Block> STONE_ARCANE = BLOCKS.register("stone_arcane",
            () -> new BlockStoneTC(true, getStandardStoneProperties()));
    public static final RegistryObject<Block> STONE_ARCANE_BRICK = BLOCKS.register("stone_arcane_brick",
            () -> new BlockStoneTC(true, getStandardStoneProperties()));
    public static final RegistryObject<Block> STONE_ANCIENT = BLOCKS.register("stone_ancient",
            () -> new BlockStoneTC(true, getStandardStoneProperties()));
    public static final RegistryObject<Block> STONE_ANCIENT_TILE = BLOCKS.register("stone_ancient_tile",
            () -> new BlockStoneTC(false, getStandardStoneProperties()));
    public static final RegistryObject<Block> STONE_ANCIENT_ROCK = BLOCKS.register("stone_ancient_rock",
            () -> new BlockStoneTC(false, AbstractBlock.Properties.of(Material.STONE)
                .strength(-1.0f, 3600000.0f) // Unbreakable
                .sound(SoundType.STONE)
                .noDrops() // Typically unbreakable blocks don't drop themselves
                ));
    public static final RegistryObject<Block> STONE_ELDRITCH_TILE = BLOCKS.register("stone_eldritch_tile",
            () -> new BlockStoneTC(true, getStandardStoneProperties()));
    public static final RegistryObject<Block> MATRIX_SPEED = BLOCKS.register("matrix_speed",
            () -> new BlockStoneTC(false, getStandardStoneProperties())); // Assuming these are decorative
    public static final RegistryObject<Block> MATRIX_COST = BLOCKS.register("matrix_cost",
            () -> new BlockStoneTC(false, getStandardStoneProperties())); // Assuming these are decorative

    // Metal Blocks
    public static final RegistryObject<Block> METAL_BLOCK_BRASS = BLOCKS.register("metal_block_brass",
            () -> new BlockMetalTC());
    public static final RegistryObject<Block> METAL_BLOCK_THAUMIUM = BLOCKS.register("metal_block_thaumium",
            () -> new BlockMetalTC());
    public static final RegistryObject<Block> METAL_BLOCK_VOID = BLOCKS.register("metal_block_void",
            () -> new BlockMetalTC());
    public static final RegistryObject<Block> METAL_ALCHEMICAL = BLOCKS.register("metal_alchemical",
            () -> new BlockMetalTC());
    public static final RegistryObject<Block> METAL_ALCHEMICAL_ADVANCED = BLOCKS.register("metal_alchemical_advanced",
            () -> new BlockMetalTC());

    // Fluid Blocks
    public static final RegistryObject<FlowingFluidBlock> FLUX_GOO_BLOCK = BLOCKS.register("flux_goo",
            () -> new BlockFluxGoo(() -> ModFluids.STILL_FLUX_GOO.get(),
                    AbstractBlock.Properties.of(Material.WATER) // TODO: Consider a custom Taint Material or Material.LAVA if it behaves more like lava
                                        .noCollission()
                                        .strength(100.0F)
                                        .noDrops()
                                        .randomTicks() // Because BlockFluxGoo implements randomTick
                    ));

    // Taint Blocks
    public static final RegistryObject<Block> TAINT_CRUST = BLOCKS.register("taint_crust",
            () -> new BlockTaint(AbstractBlock.Properties.of(Material.GRASS)
                    .strength(0.5f)
                    .sound(SoundType.SLIME_BLOCK)
                    .randomTicks()));
    public static final RegistryObject<Block> TAINT_SOIL = BLOCKS.register("taint_soil",
            () -> new BlockTaint(AbstractBlock.Properties.of(Material.DIRT)
                    .strength(0.5f)
                    .sound(SoundType.SLIME_BLOCK)
                    .randomTicks()));
    public static final RegistryObject<Block> TAINT_ROCK = BLOCKS.register("taint_rock",
            () -> new BlockTaint(AbstractBlock.Properties.of(Material.STONE)
                    .strength(1.5f)
                    .sound(SoundType.STONE)
                    .randomTicks()));
    public static final RegistryObject<Block> TAINT_GEYSER = BLOCKS.register("taint_geyser",
            () -> new BlockTaint(AbstractBlock.Properties.of(Material.STONE)
                    .strength(1.5f)
                    .sound(SoundType.STONE)
                    .randomTicks()));
    public static final RegistryObject<Block> TAINT_FIBRE = BLOCKS.register("taint_fibre",
            () -> new BlockTaintFibre(AbstractBlock.Properties.of(Material.PLANT)
                    .strength(0.2f)
                    .sound(SoundType.GRASS)
                    .noCollission()
                    .randomTicks()));
    public static final RegistryObject<Block> TAINT_FEATURE = BLOCKS.register("taint_feature",
            () -> new BlockTaintFeature(AbstractBlock.Properties.of(Material.STONE)
                    .strength(1.5f)
                    .sound(SoundType.STONE)
                    .randomTicks()));

    // Loot Blocks
    public static final RegistryObject<Block> LOOT_CRATE_COMMON = BLOCKS.register("loot_crate_common",
            () -> new BlockLoot(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.0f)
                    .sound(SoundType.WOOD),
                    BlockLoot.LootType.COMMON));
    public static final RegistryObject<Block> LOOT_CRATE_UNCOMMON = BLOCKS.register("loot_crate_uncommon",
            () -> new BlockLoot(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.0f)
                    .sound(SoundType.WOOD),
                    BlockLoot.LootType.UNCOMMON));
    public static final RegistryObject<Block> LOOT_CRATE_RARE = BLOCKS.register("loot_crate_rare",
            () -> new BlockLoot(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.0f)
                    .sound(SoundType.WOOD),
                    BlockLoot.LootType.RARE));
    public static final RegistryObject<Block> LOOT_URN_COMMON = BLOCKS.register("loot_urn_common",
            () -> new BlockLoot(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0f)
                    .sound(SoundType.STONE),
                    BlockLoot.LootType.COMMON));
    public static final RegistryObject<Block> LOOT_URN_UNCOMMON = BLOCKS.register("loot_urn_uncommon",
            () -> new BlockLoot(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0f)
                    .sound(SoundType.STONE),
                    BlockLoot.LootType.UNCOMMON));
    public static final RegistryObject<Block> LOOT_URN_RARE = BLOCKS.register("loot_urn_rare",
            () -> new BlockLoot(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0f)
                    .sound(SoundType.STONE),
                    BlockLoot.LootType.RARE));

    // Crystal Blocks
    public static final RegistryObject<Block> CRYSTAL_AIR = BLOCKS.register("crystal_aer",
            () -> new BlockCrystal("crystal_aer", Aspect.AIR,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));
    public static final RegistryObject<Block> CRYSTAL_FIRE = BLOCKS.register("crystal_ignis",
            () -> new BlockCrystal("crystal_ignis", Aspect.FIRE,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));
    public static final RegistryObject<Block> CRYSTAL_WATER = BLOCKS.register("crystal_aqua",
            () -> new BlockCrystal("crystal_aqua", Aspect.WATER,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));
    public static final RegistryObject<Block> CRYSTAL_EARTH = BLOCKS.register("crystal_terra",
            () -> new BlockCrystal("crystal_terra", Aspect.EARTH,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));
    public static final RegistryObject<Block> CRYSTAL_ORDER = BLOCKS.register("crystal_ordo",
            () -> new BlockCrystal("crystal_ordo", Aspect.ORDER,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));
    public static final RegistryObject<Block> CRYSTAL_ENTROPY = BLOCKS.register("crystal_perditio",
            () -> new BlockCrystal("crystal_perditio", Aspect.ENTROPY,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));
    public static final RegistryObject<Block> CRYSTAL_TAINT = BLOCKS.register("crystal_vitium",
            () -> new BlockCrystal("crystal_vitium", Aspect.FLUX,
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.25f)
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .lightLevel((state) -> 1)));

    // Machine Blocks
    public static final RegistryObject<Block> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench",
            () -> new BlockArcaneWorkbench(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)));
    public static final RegistryObject<Block> ARCANE_WORKBENCH_CHARGER = BLOCKS.register("arcane_workbench_charger",
            () -> new BlockArcaneWorkbenchCharger(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)));
    public static final RegistryObject<Block> RESEARCH_TABLE = BLOCKS.register("research_table",
            () -> new BlockResearchTable(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)));
    public static final RegistryObject<Block> CRUCIBLE = BLOCKS.register("crucible",
            () -> new BlockCrucible(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0f)
                    .sound(SoundType.STONE)));
    public static final RegistryObject<Block> LAMP_ARCANE = BLOCKS.register("lamp_arcane",
            () -> new BlockLamp(TileLampArcane.class, "lamp_arcane",
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.3f)
                            .sound(SoundType.GLASS)
                            .lightLevel((state) -> 15)));
    public static final RegistryObject<Block> LAMP_FERTILITY = BLOCKS.register("lamp_fertility",
            () -> new BlockLamp(TileLampFertility.class, "lamp_fertility",
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.3f)
                            .sound(SoundType.GLASS)
                            .lightLevel((state) -> 15)));
    public static final RegistryObject<Block> LAMP_GROWTH = BLOCKS.register("lamp_growth",
            () -> new BlockLamp(TileLampGrowth.class, "lamp_growth",
                    AbstractBlock.Properties.of(Material.GLASS)
                            .strength(0.3f)
                            .sound(SoundType.GLASS)
                            .lightLevel((state) -> 15)));

    // Effect Blocks
    public static final RegistryObject<Block> EFFECT_SHOCK = BLOCKS.register("effect_shock",
            () -> new BlockEffect("effect_shock",
                    AbstractBlock.Properties.of(Material.AIR)
                            .noCollission()
                            .noDrops()
                            .air()));
    public static final RegistryObject<Block> EFFECT_SAP = BLOCKS.register("effect_sap",
            () -> new BlockEffect("effect_sap",
                    AbstractBlock.Properties.of(Material.AIR)
                            .noCollission()
                            .noDrops()
                            .air()));
    public static final RegistryObject<Block> EFFECT_GLIMMER = BLOCKS.register("effect_glimmer",
            () -> new BlockEffect("effect_glimmer",
                    AbstractBlock.Properties.of(Material.AIR)
                            .noCollission()
                            .noDrops()
                            .air()));

    // Placeholder Blocks
    public static final RegistryObject<Block> PLACEHOLDER_NETHERBRICK = BLOCKS.register("placeholder_brick",
            () -> new BlockPlaceholder("placeholder_brick",
                    AbstractBlock.Properties.of(Material.STONE)
                            .strength(2.0f)
                            .sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLACEHOLDER_OBSIDIAN = BLOCKS.register("placeholder_obsidian",
            () -> new BlockPlaceholder("placeholder_obsidian",
                    AbstractBlock.Properties.of(Material.STONE)
                            .strength(50.0f)
                            .sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLACEHOLDER_BARS = BLOCKS.register("placeholder_bars",
            () -> new BlockPlaceholder("placeholder_bars",
                    AbstractBlock.Properties.of(Material.METAL)
                            .strength(5.0f)
                            .sound(SoundType.METAL)));
    public static final RegistryObject<Block> PLACEHOLDER_ANVIL = BLOCKS.register("placeholder_anvil",
            () -> new BlockPlaceholder("placeholder_anvil",
                    AbstractBlock.Properties.of(Material.HEAVY_METAL)
                            .strength(5.0f)
                            .sound(SoundType.ANVIL)));
    public static final RegistryObject<Block> PLACEHOLDER_CAULDRON = BLOCKS.register("placeholder_cauldron",
            () -> new BlockPlaceholder("placeholder_cauldron",
                    AbstractBlock.Properties.of(Material.METAL)
                            .strength(2.0f)
                            .sound(SoundType.METAL)));
    public static final RegistryObject<Block> PLACEHOLDER_TABLE = BLOCKS.register("placeholder_table",
            () -> new BlockPlaceholder("placeholder_table",
                    AbstractBlock.Properties.of(Material.WOOD)
                            .strength(2.0f)
                            .sound(SoundType.WOOD)));

    public static void register() {
        // This method is not strictly necessary if using the event bus registration in the main mod file,
        // but can be useful for organizing registration calls if needed later.
    }
} 