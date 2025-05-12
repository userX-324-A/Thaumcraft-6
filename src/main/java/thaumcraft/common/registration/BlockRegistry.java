package thaumcraft.common.registration;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.basic.BlockStairsTC;
import thaumcraft.common.blocks.basic.BlockStonePorous;
import thaumcraft.common.blocks.basic.BlockStoneTC;
import thaumcraft.common.blocks.world.ore.BlockCrystal;
import thaumcraft.common.blocks.world.ore.BlockOreTC;
import thaumcraft.common.blocks.world.plants.BlockLeavesTC;
import thaumcraft.common.blocks.world.plants.BlockLogsTC;
import thaumcraft.common.blocks.world.plants.BlockPlantCinderpearl;
import thaumcraft.common.blocks.world.plants.BlockPlantShimmerleaf;
import thaumcraft.common.blocks.world.plants.BlockPlantVishroom;
import thaumcraft.common.blocks.world.plants.BlockSaplingTC;
import thaumcraft.common.blocks.world.loot.BlockLootTC;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);

    // Ores
    public static final RegistryObject<Block> ORE_AMBER = BLOCKS.register("ore_amber",
            () -> new BlockOreTC("ore_amber", Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 5.0f)));
    public static final RegistryObject<Block> ORE_CINNABAR = BLOCKS.register("ore_cinnabar",
            () -> new BlockOreTC("ore_cinnabar", Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 5.0f)));
    public static final RegistryObject<Block> ORE_QUARTZ = BLOCKS.register("ore_quartz",
            () -> new BlockOreTC("ore_quartz", Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f, 5.0f)));

    // Crystals
    public static final RegistryObject<Block> CRYSTAL_AIR = BLOCKS.register("crystal_aer",
            () -> new BlockCrystal("crystal_aer", Aspect.AIR,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));
    public static final RegistryObject<Block> CRYSTAL_FIRE = BLOCKS.register("crystal_ignis",
            () -> new BlockCrystal("crystal_ignis", Aspect.FIRE,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));
    public static final RegistryObject<Block> CRYSTAL_WATER = BLOCKS.register("crystal_aqua",
            () -> new BlockCrystal("crystal_aqua", Aspect.WATER,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));
    public static final RegistryObject<Block> CRYSTAL_EARTH = BLOCKS.register("crystal_terra",
            () -> new BlockCrystal("crystal_terra", Aspect.EARTH,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));
    public static final RegistryObject<Block> CRYSTAL_ORDER = BLOCKS.register("crystal_ordo",
            () -> new BlockCrystal("crystal_ordo", Aspect.ORDER,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));
    public static final RegistryObject<Block> CRYSTAL_ENTROPY = BLOCKS.register("crystal_perditio",
            () -> new BlockCrystal("crystal_perditio", Aspect.ENTROPY,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));
    public static final RegistryObject<Block> CRYSTAL_TAINT = BLOCKS.register("crystal_vitium",
            () -> new BlockCrystal("crystal_vitium", Aspect.FLUX,
                    Block.Properties.create(Material.GLASS)
                            .hardnessAndResistance(0.25f, 0.0f)
                            .sound(SoundType.GLASS)
                            .tickRandomly()
                            .setLightLevel((state) -> 1)
            ));

    // Stones
    public static final RegistryObject<Block> STONE_ARCANE = BLOCKS.register("stone_arcane",
            () -> new BlockStoneTC("stone_arcane", true,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ARCANE_BRICK = BLOCKS.register("stone_arcane_brick",
            () -> new BlockStoneTC("stone_arcane_brick", true,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ANCIENT = BLOCKS.register("stone_ancient",
            () -> new BlockStoneTC("stone_ancient", true,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ANCIENT_TILE = BLOCKS.register("stone_ancient_tile",
            () -> new BlockStoneTC("stone_ancient_tile", false,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ANCIENT_ROCK = BLOCKS.register("stone_ancient_rock",
            () -> new BlockStoneTC("stone_ancient_rock", false,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ANCIENT_GLYPHED = BLOCKS.register("stone_ancient_glyphed",
            () -> new BlockStoneTC("stone_ancient_glyphed", false,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ANCIENT_DOORWAY = BLOCKS.register("stone_ancient_doorway",
            () -> new BlockStoneTC("stone_ancient_doorway", false,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0f, 10.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_ELDRITCH_TILE = BLOCKS.register("stone_eldritch_tile",
            () -> new BlockStoneTC("stone_eldritch_tile", true,
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(15.0f, 1000.0f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> STONE_POROUS = BLOCKS.register("stone_porous",
            () -> new BlockStonePorous(
                    Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0f, 5.0f).sound(SoundType.STONE)));

    // Stairs
    public static final RegistryObject<Block> STAIRS_ARCANE = BLOCKS.register("stairs_arcane",
            () -> new BlockStairsTC("stairs_arcane", () -> STONE_ARCANE.get().getDefaultState(),
                    Block.Properties.from(STONE_ARCANE.get()).nonOpaque()));
    public static final RegistryObject<Block> STAIRS_ARCANE_BRICK = BLOCKS.register("stairs_arcane_brick",
            () -> new BlockStairsTC("stairs_arcane_brick", () -> STONE_ARCANE_BRICK.get().getDefaultState(),
                    Block.Properties.from(STONE_ARCANE_BRICK.get()).nonOpaque()));
    public static final RegistryObject<Block> STAIRS_ANCIENT = BLOCKS.register("stairs_ancient",
            () -> new BlockStairsTC("stairs_ancient", () -> STONE_ANCIENT.get().getDefaultState(),
                    Block.Properties.from(STONE_ANCIENT.get()).nonOpaque()));

    // Slabs (Stone-based)
    public static final RegistryObject<Block> SLAB_ARCANE_STONE = BLOCKS.register("slab_arcane_stone",
            () -> new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE).nonOpaque()));
    public static final RegistryObject<Block> SLAB_ARCANE_BRICK = BLOCKS.register("slab_arcane_brick",
            () -> new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE).nonOpaque()));
    public static final RegistryObject<Block> SLAB_ANCIENT = BLOCKS.register("slab_ancient",
            () -> new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE).nonOpaque()));
    public static final RegistryObject<Block> SLAB_ELDRITCH = BLOCKS.register("slab_eldritch",
            () -> new SlabBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 10.0f).sound(SoundType.STONE).nonOpaque()));

    // Saplings
    public static final RegistryObject<Block> SAPLING_GREATWOOD = BLOCKS.register("sapling_greatwood",
            () -> new BlockSaplingTC("sapling_greatwood",
                    Block.Properties.create(Material.PLANTS, MaterialColor.WOOD)
                            .doesNotBlockMovement()
                            .tickRandomly()
                            .hardnessAndResistance(0.0f, 0.0f)
                            .sound(SoundType.PLANT)
                            .setFireInfo(30, 60)
            ));
    public static final RegistryObject<Block> SAPLING_SILVERWOOD = BLOCKS.register("sapling_silverwood",
            () -> new BlockSaplingTC("sapling_silverwood",
                    Block.Properties.create(Material.PLANTS, MaterialColor.SNOW)
                            .doesNotBlockMovement()
                            .tickRandomly()
                            .hardnessAndResistance(0.0f, 0.0f)
                            .sound(SoundType.PLANT)
                            .setFireInfo(30, 60)
            ));

    // Logs
    public static final RegistryObject<Block> LOG_GREATWOOD = BLOCKS.register("log_greatwood",
            () -> new BlockLogsTC("log_greatwood",
                    Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
                            .hardnessAndResistance(2.0f, 5.0f)
                            .sound(SoundType.WOOD)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .setFireInfo(5, 5)
            ));
    public static final RegistryObject<Block> LOG_SILVERWOOD = BLOCKS.register("log_silverwood",
            () -> new BlockLogsTC("log_silverwood",
                    Block.Properties.create(Material.WOOD, MaterialColor.SNOW)
                            .hardnessAndResistance(2.0f, 5.0f)
                            .sound(SoundType.WOOD)
                            .harvestTool(ToolType.AXE).harvestLevel(0)
                            .setLightLevel((state) -> 5)
                            .setFireInfo(5, 5)
            ));

    // Leaves
    public static final RegistryObject<Block> LEAF_GREATWOOD = BLOCKS.register("leaf_greatwood",
            () -> new BlockLeavesTC("leaf_greatwood",
                    Block.Properties.create(Material.LEAVES, MaterialColor.FOLIAGE)
                            .hardnessAndResistance(0.2F, 0.2F)
                            .tickRandomly()
                            .nonOpaque()
                            .sound(SoundType.PLANT)
                            .setFireInfo(30, 60)
            ));
    public static final RegistryObject<Block> LEAF_SILVERWOOD = BLOCKS.register("leaf_silverwood",
            () -> new BlockLeavesTC("leaf_silverwood",
                    Block.Properties.create(Material.LEAVES, MaterialColor.LIGHT_BLUE)
                            .hardnessAndResistance(0.2F, 0.2F)
                            .tickRandomly()
                            .nonOpaque()
                            .sound(SoundType.PLANT)
                            .setFireInfo(30, 60)
            ));

    // Wood Products
    public static final RegistryObject<Block> GREATWOOD_PLANKS = BLOCKS.register("greatwood_planks",
            () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
                    .hardnessAndResistance(2.0f, 3.0f)
                    .sound(SoundType.WOOD)
                    .harvestTool(ToolType.AXE).harvestLevel(0)
                    .setFireInfo(5, 20) // Typical wood plank flammability
            ));
    public static final RegistryObject<Block> SILVERWOOD_PLANKS = BLOCKS.register("silverwood_planks",
            () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.SAND) // SAND for light color
                    .hardnessAndResistance(2.0f, 3.0f)
                    .sound(SoundType.WOOD)
                    .harvestTool(ToolType.AXE).harvestLevel(0)
                    .setFireInfo(5, 20)
            ));
    public static final RegistryObject<Block> GREATWOOD_SLAB = BLOCKS.register("greatwood_slab",
            () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
                    .hardnessAndResistance(1.2f, 2.0f) // From old ConfigBlocks
                    .sound(SoundType.WOOD)
                    .harvestTool(ToolType.AXE).harvestLevel(0)
                    .setFireInfo(5, 20)
            ));
    public static final RegistryObject<Block> SILVERWOOD_SLAB = BLOCKS.register("silverwood_slab",
            () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.SAND)
                    .hardnessAndResistance(1.0f, 2.0f) // From old ConfigBlocks
                    .sound(SoundType.WOOD)
                    .harvestTool(ToolType.AXE).harvestLevel(0)
                    .setFireInfo(5, 20)
            ));

    public static final RegistryObject<Block> STAIRS_GREATWOOD = BLOCKS.register("stairs_greatwood",
            () -> new StairsBlock(() -> GREATWOOD_PLANKS.get().getDefaultState(), Block.Properties.from(GREATWOOD_PLANKS.get()).nonOpaque()));
    public static final RegistryObject<Block> STAIRS_SILVERWOOD = BLOCKS.register("stairs_silverwood",
            () -> new StairsBlock(() -> SILVERWOOD_PLANKS.get().getDefaultState(), Block.Properties.from(SILVERWOOD_PLANKS.get()).nonOpaque()));

    // Other Plants
    public static final RegistryObject<Block> SHIMMERLEAF = BLOCKS.register("shimmerleaf",
            () -> new BlockPlantShimmerleaf(
                    Block.Properties.create(Material.PLANTS)
                            .doesNotBlockMovement()
                            .hardnessAndResistance(0.0f, 0.0f)
                            .sound(SoundType.PLANT)
                            .setLightLevel((state) -> 6)
                            .setOffsetType(Block.OffsetType.XZ)
            ));
    public static final RegistryObject<Block> CINDERPEARL = BLOCKS.register("cinderpearl",
            () -> new BlockPlantCinderpearl(
                    Block.Properties.create(Material.PLANTS)
                            .doesNotBlockMovement()
                            .hardnessAndResistance(0.0f, 0.0f)
                            .sound(SoundType.PLANT)
                            .setLightLevel((state) -> 7)
                            .setOffsetType(Block.OffsetType.XZ)
            ));
    public static final RegistryObject<Block> VISHROOM = BLOCKS.register("vishroom",
            () -> new BlockPlantVishroom(
                    Block.Properties.create(Material.PLANTS)
                            .doesNotBlockMovement()
                            .hardnessAndResistance(0.0f, 0.0f)
                            .sound(SoundType.PLANT)
                            .setLightLevel((state) -> 6)
            ));

    // Decorative Blocks
    public static final RegistryObject<Block> AMBER_BLOCK = BLOCKS.register("amber_block",
            () -> new Block(Block.Properties.create(Material.GLASS, MaterialColor.GOLD)
                    .hardnessAndResistance(0.5f, 0.5f)
                    .sound(SoundType.GLASS)
                    .nonOpaque()
            ));
    public static final RegistryObject<Block> AMBER_BRICK = BLOCKS.register("amber_brick",
            () -> new Block(Block.Properties.create(Material.GLASS, MaterialColor.GOLD)
                    .hardnessAndResistance(0.5f, 0.5f)
                    .sound(SoundType.GLASS)
                    .nonOpaque()
            ));
    public static final RegistryObject<Block> FLESH_BLOCK = BLOCKS.register("flesh_block",
            () -> new Block(Block.Properties.create(Material.SPONGE, MaterialColor.TERRACOTTA_RED)
                    .hardnessAndResistance(0.25f, 2.0f)
                    .sound(SoundType.SLIME_BLOCK) // Placeholder for SoundsTC.GORE
            ));

    // Loot Containers
    public static final RegistryObject<Block> LOOT_CRATE_COMMON = BLOCKS.register("loot_crate_common",
            () -> new BlockLootTC(Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
                    .sound(SoundType.WOOD).nonOpaque(), // Hardness/Resistance set in BlockLootTC constructor
                    BlockLootTC.CRATE_SHAPE, BlockLootTC.LootType.COMMON_CRATE));
    public static final RegistryObject<Block> LOOT_CRATE_UNCOMMON = BLOCKS.register("loot_crate_uncommon",
            () -> new BlockLootTC(Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
                    .sound(SoundType.WOOD).nonOpaque(),
                    BlockLootTC.CRATE_SHAPE, BlockLootTC.LootType.UNCOMMON_CRATE));
    public static final RegistryObject<Block> LOOT_CRATE_RARE = BLOCKS.register("loot_crate_rare",
            () -> new BlockLootTC(Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
                    .sound(SoundType.WOOD).nonOpaque(),
                    BlockLootTC.CRATE_SHAPE, BlockLootTC.LootType.RARE_CRATE));

    public static final RegistryObject<Block> LOOT_URN_COMMON = BLOCKS.register("loot_urn_common",
            () -> new BlockLootTC(Block.Properties.create(Material.ROCK, MaterialColor.TERRACOTTA_GRAY) // Gray terracotta for a ceramic feel
                    .sound(SoundType.STONE).nonOpaque(), // Placeholder for SoundsTC.URN
                    BlockLootTC.URN_SHAPE, BlockLootTC.LootType.COMMON_URN));
    public static final RegistryObject<Block> LOOT_URN_UNCOMMON = BLOCKS.register("loot_urn_uncommon",
            () -> new BlockLootTC(Block.Properties.create(Material.ROCK, MaterialColor.TERRACOTTA_GRAY)
                    .sound(SoundType.STONE).nonOpaque(),
                    BlockLootTC.URN_SHAPE, BlockLootTC.LootType.UNCOMMON_URN));
    public static final RegistryObject<Block> LOOT_URN_RARE = BLOCKS.register("loot_urn_rare",
            () -> new BlockLootTC(Block.Properties.create(Material.ROCK, MaterialColor.TERRACOTTA_GRAY)
                    .sound(SoundType.STONE).nonOpaque(),
                    BlockLootTC.URN_SHAPE, BlockLootTC.LootType.RARE_URN));

    // Example of how a block would be registered:
    // public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(Block.Properties.create(Material.ROCK)));

    public static void register() {
        // This method is intentionally empty for now.
        // We will call BLOCKS.register(eventBus) in the main mod file.
    }
} 