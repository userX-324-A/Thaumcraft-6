package thaumcraft.common.registers;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.AbstractBlock;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.common.blocks.world.*;

import java.util.function.Supplier;

public class ModBlocks {

    public static void init() {
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = RegistryManager.BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        RegistryManager.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static AbstractBlock.Properties block(Material material, SoundType soundType) {
        return AbstractBlock.Properties.of(material).sound(soundType);
    }

    private static AbstractBlock.Properties woodBlock() {
        return block(Material.WOOD, SoundType.WOOD).strength(2.0F, 5.0F);
    }

    private static AbstractBlock.Properties stoneBlock() {
        return block(Material.STONE, SoundType.STONE).strength(2.0F, 6.0F);
    }

    private static AbstractBlock.Properties plantBlock() {
        return block(Material.PLANT, SoundType.GRASS).instabreak().noOcclusion();
    }

    private static AbstractBlock.Properties magicalDevice() {
        return block(Material.STONE, SoundType.STONE).strength(1.5f, 8.0f);
    }

    public static final RegistryObject<Block> BLOCK_THAUMIUM = registerBlock("block_thaumium", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> BLOCK_VOID = registerBlock("block_void", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> BLOCK_BRASS = registerBlock("block_brass", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> AMBER_BLOCK = registerBlock("amber_block", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> AMBER_BRICK = registerBlock("amber_brick", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> LOG_GREATWOOD = registerBlock("log_greatwood", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> LOG_SILVERWOOD = registerBlock("log_silverwood", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> PLANK_GREATWOOD = registerBlock("plank_greatwood", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> PLANK_SILVERWOOD = registerBlock("plank_silverwood", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> LEAVES_GREATWOOD = registerBlock("leaves_greatwood", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> LEAVES_SILVERWOOD = registerBlock("leaves_silverwood", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> SAPLING_GREATWOOD = registerBlock("sapling_greatwood", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> SAPLING_SILVERWOOD = registerBlock("sapling_silverwood", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> ARCANE_STONE_BLOCK = registerBlock("arcane_stone_block", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> ARCANE_STONE_BRICK = registerBlock("arcane_stone_brick", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> ANCIENT_STONE = registerBlock("ancient_stone", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> ANCIENT_ROCK = registerBlock("ancient_rock", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> CRUSTED_STONE = registerBlock("crusted_stone", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> ELDRITCH_STONE = registerBlock("eldritch_stone", () -> new Block(stoneBlock()));
    // Ores
    public static final RegistryObject<Block> ORE_CINNABAR = registerBlock("ore_cinnabar", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> ORE_AMBER = registerBlock("ore_amber", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> ORE_QUARTZ = registerBlock("ore_quartz", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> CRYSTAL_AIR = registerBlock("crystal_air", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CRYSTAL_FIRE = registerBlock("crystal_fire", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CRYSTAL_WATER = registerBlock("crystal_water", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CRYSTAL_EARTH = registerBlock("crystal_earth", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CRYSTAL_ORDER = registerBlock("crystal_order", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CRYSTAL_ENTROPY = registerBlock("crystal_entropy", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CRYSTAL_TAINT = registerBlock("crystal_taint", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> TAINTED_SOIL = registerBlock("tainted_soil", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> TAINTED_ROCK = registerBlock("tainted_rock", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> TAINTED_LOG = registerBlock("tainted_log", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> TAINTED_FIBRE = registerBlock("tainted_fibre", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> TAINTED_CRUST = registerBlock("tainted_crust", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> SHIMMERLEAF = registerBlock("shimmerleaf", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> CINDERPEARL = registerBlock("cinderpearl", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> VISHROOM = registerBlock("vishroom", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> P_GREATWOOD = registerBlock("p_greatwood", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> P_SILVERWOOD = registerBlock("p_silverwood", () -> new Block(woodBlock()));
    public static final RegistryObject<Block> P_OBSIDIAN = registerBlock("p_obsidian", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> P_ARCANE = registerBlock("p_arcane", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> P_ELDRITCH = registerBlock("p_eldritch", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> P_ANCIENT = registerBlock("p_ancient", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> P_TAINTED = registerBlock("p_tainted", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> FLESH_BLOCK = registerBlock("flesh_block", () -> new Block(stoneBlock()));
    public static final RegistryObject<Block> GRASS_AMBIENT = registerBlock("grass_ambient", () -> new Block(plantBlock()));
    public static final RegistryObject<Block> GRASS_TAINTED = registerBlock("grass_tainted", () -> new Block(plantBlock()));
    public static final RegistryObject<SlabBlock> SLAB_GREATWOOD = registerBlock("slab_greatwood", () -> new SlabBlock(woodBlock()));
    public static final RegistryObject<SlabBlock> SLAB_SILVERWOOD = registerBlock("slab_silverwood", () -> new SlabBlock(woodBlock()));
    public static final RegistryObject<SlabBlock> SLAB_ARCANE_STONE = registerBlock("slab_arcane_stone", () -> new SlabBlock(stoneBlock()));
    public static final RegistryObject<SlabBlock> SLAB_ARCANE_BRICK = registerBlock("slab_arcane_brick", () -> new SlabBlock(stoneBlock()));
    public static final RegistryObject<SlabBlock> SLAB_ANCIENT = registerBlock("slab_ancient", () -> new SlabBlock(stoneBlock()));
    public static final RegistryObject<SlabBlock> SLAB_ELDRITCH = registerBlock("slab_eldritch", () -> new SlabBlock(stoneBlock()));
    public static final RegistryObject<StairsBlock> STAIRS_GREATWOOD = registerBlock("stairs_greatwood", () -> new StairsBlock(() -> ModBlocks.PLANK_GREATWOOD.get().defaultBlockState(), woodBlock()));
    public static final RegistryObject<StairsBlock> STAIRS_SILVERWOOD = registerBlock("stairs_silverwood", () -> new StairsBlock(() -> ModBlocks.PLANK_SILVERWOOD.get().defaultBlockState(), woodBlock()));
    public static final RegistryObject<StairsBlock> STAIRS_ARCANE_STONE = registerBlock("stairs_arcane_stone", () -> new StairsBlock(() -> ModBlocks.ARCANE_STONE_BLOCK.get().defaultBlockState(), stoneBlock()));
    public static final RegistryObject<StairsBlock> STAIRS_ARCANE_BRICK = registerBlock("stairs_arcane_brick", () -> new StairsBlock(() -> ModBlocks.ARCANE_STONE_BRICK.get().defaultBlockState(), stoneBlock()));
    public static final RegistryObject<StairsBlock> STAIRS_ANCIENT = registerBlock("stairs_ancient", () -> new StairsBlock(() -> ModBlocks.ANCIENT_STONE.get().defaultBlockState(), stoneBlock()));
    public static final RegistryObject<StairsBlock> STAIRS_ELDRITCH = registerBlock("stairs_eldritch", () -> new StairsBlock(() -> ModBlocks.ELDRITCH_STONE.get().defaultBlockState(), stoneBlock()));
    public static final RegistryObject<Block> ARCANE_WORKBENCH = registerBlock("arcane_workbench", () -> new ArcaneWorkbenchBlock(magicalDevice().noOcclusion()));
    public static final RegistryObject<Block> ARCANE_WORKBENCH_CHARGER = registerBlock("arcane_workbench_charger", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> CRUCIBLE = registerBlock("crucible", () -> new CrucibleBlock(magicalDevice()));
    public static final RegistryObject<Block> INFUSION_MATRIX = registerBlock("infusion_matrix", () -> new InfusionMatrixBlock(magicalDevice()));
    public static final RegistryObject<Block> PEDESTAL = registerBlock("pedestal", () -> new PedestalBlock(magicalDevice()));
    public static final RegistryObject<Block> RESEARCH_TABLE = registerBlock("research_table", () -> new ResearchTableBlock(magicalDevice()));
    public static final RegistryObject<Block> TUBE = registerBlock("tube", () -> new TubeBlock(magicalDevice()));
    public static final RegistryObject<Block> BELLOWS = registerBlock("bellows", () -> new BellowsBlock(magicalDevice()));
    public static final RegistryObject<Block> ARCANE_LAMP = registerBlock("arcane_lamp", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> ARCANE_FURNACE = registerBlock("arcane_furnace", () -> new ArcaneFurnaceBlock(magicalDevice()));
    public static final RegistryObject<Block> ARCANE_EAR = registerBlock("arcane_ear", () -> new ArcaneEarBlock(magicalDevice()));
    public static final RegistryObject<Block> ARCANE_EAR_TOGGLE = registerBlock("arcane_ear_toggle", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> ALEMBIC = registerBlock("alembic", () -> new AlembicBlock(magicalDevice()));
    public static final RegistryObject<Block> GOLEM_BUILDER = registerBlock("golem_builder", () -> new GolemBuilderBlock(magicalDevice()));
    public static final RegistryObject<Block> HUNGRY_CHEST = registerBlock("hungry_chest", () -> new HungryChestBlock(magicalDevice()));
    public static final RegistryObject<Block> SEAL = registerBlock("seal", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> SEAL_PLACER = registerBlock("seal_placer", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> BARRIER = registerBlock("barrier", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> ARCANE_BORE_BASE = registerBlock("arcane_bore_base", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> ARCANE_BORE = registerBlock("arcane_bore", () -> new ArcaneBoreBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_VALVE = registerBlock("essentia_valve", () -> new EssentiaValveBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_TUBE = registerBlock("essentia_tube", () -> new TubeBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_PUMP = registerBlock("essentia_pump", () -> new EssentiaPumpBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_BUFFER = registerBlock("essentia_buffer", () -> new EssentiaBufferBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_MIRROR = registerBlock("essentia_mirror", () -> new EssentiaMirrorBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_FILTER = registerBlock("essentia_filter", () -> new EssentiaFilterBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_JAR = registerBlock("essentia_jar", () -> new EssentiaJarBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_JAR_FILLED = registerBlock("essentia_jar_filled", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_JAR_VOID = registerBlock("essentia_jar_void", () -> new EssentiaJarBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_JAR_BRAIN = registerBlock("essentia_jar_brain", () -> new EssentiaJarBlock(magicalDevice()));
    public static final RegistryObject<Block> ESSENTIA_CENTRIFUGE = registerBlock("essentia_centrifuge", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> EVERFULL_URN = registerBlock("everfull_urn", () -> new EverfullUrnBlock(magicalDevice()));
    public static final RegistryObject<Block> PATTERN_CRAFTER = registerBlock("pattern_crafter", () -> new PatternCrafterBlock(magicalDevice()));
    public static final RegistryObject<Block> SPA = registerBlock("spa", () -> new SpaBlock(magicalDevice()));
    public static final RegistryObject<Block> THAUMATORIUM = registerBlock("thaumatorium", () -> new ThaumatoriumBlock(magicalDevice()));
    public static final RegistryObject<Block> THAUMATORIUM_TOP = registerBlock("thaumatorium_top", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> VOID_SIPHON = registerBlock("void_siphon", () -> new Block(magicalDevice()));
    public static final RegistryObject<Block> BRAIN_IN_A_JAR = registerBlock("brain_in_a_jar", () -> new BrainInAJarBlock(magicalDevice()));
    // Stabilizer placeholder registration (block class to be added)
    public static final RegistryObject<Block> STABILIZER = registerBlock("stabilizer", () -> new Block(magicalDevice().noOcclusion().lightLevel(s -> 4)));

    // Devices added for this migration batch
    public static final RegistryObject<Block> VIS_GENERATOR = registerBlock("vis_generator", () -> new VisGeneratorBlock(magicalDevice().noOcclusion()));
    public static final RegistryObject<Block> LEVITATOR = registerBlock("levitator", () -> new LevitatorBlock(magicalDevice()));
}

