package thaumcraft.common.registers;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.common.blocks.world.*;
import thaumcraft.common.tiles.crafting.CrucibleBlockEntity;

public class ModBlockEntities {
    public static void init() {}

    public static final RegistryObject<TileEntityType<CrucibleBlockEntity>> CRUCIBLE = RegistryManager.BLOCK_ENTITIES.register(
            "crucible",
            () -> TileEntityType.Builder.create(CrucibleBlockEntity::new, ModBlocks.CRUCIBLE.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<TubeBlockEntity>> TUBE = RegistryManager.BLOCK_ENTITIES.register(
            "tube",
            () -> TileEntityType.Builder.create(TubeBlockEntity::new, ModBlocks.TUBE.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<PedestalBlockEntity>> PEDESTAL = RegistryManager.BLOCK_ENTITIES.register(
            "pedestal",
            () -> TileEntityType.Builder.create(PedestalBlockEntity::new, ModBlocks.PEDESTAL.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<HungryChestBlockEntity>> HUNGRY_CHEST = RegistryManager.BLOCK_ENTITIES.register(
            "hungry_chest",
            () -> TileEntityType.Builder.create(HungryChestBlockEntity::new, ModBlocks.HUNGRY_CHEST.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE = RegistryManager.BLOCK_ENTITIES.register(
            "research_table",
            () -> TileEntityType.Builder.create(ResearchTableBlockEntity::new, ModBlocks.RESEARCH_TABLE.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<BellowsBlockEntity>> BELLOWS = RegistryManager.BLOCK_ENTITIES.register(
            "bellows",
            () -> TileEntityType.Builder.create(BellowsBlockEntity::new, ModBlocks.BELLOWS.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<GolemBuilderBlockEntity>> GOLEM_BUILDER = RegistryManager.BLOCK_ENTITIES.register(
            "golem_builder",
            () -> TileEntityType.Builder.create(GolemBuilderBlockEntity::new, ModBlocks.GOLEM_BUILDER.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<PatternCrafterBlockEntity>> PATTERN_CRAFTER = RegistryManager.BLOCK_ENTITIES.register(
            "pattern_crafter",
            () -> TileEntityType.Builder.create(PatternCrafterBlockEntity::new, ModBlocks.PATTERN_CRAFTER.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EssentiaJarBlockEntity>> ESSENTIA_JAR = RegistryManager.BLOCK_ENTITIES.register(
            "essentia_jar",
            () -> TileEntityType.Builder.create(EssentiaJarBlockEntity::new, ModBlocks.ESSENTIA_JAR.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EverfullUrnBlockEntity>> EVERFULL_URN = RegistryManager.BLOCK_ENTITIES.register(
            "everfull_urn",
            () -> TileEntityType.Builder.create(EverfullUrnBlockEntity::new, ModBlocks.EVERFULL_URN.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<SpaBlockEntity>> SPA = RegistryManager.BLOCK_ENTITIES.register(
            "spa",
            () -> TileEntityType.Builder.create(SpaBlockEntity::new, ModBlocks.SPA.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EssentiaValveBlockEntity>> ESSENTIA_VALVE = RegistryManager.BLOCK_ENTITIES.register(
            "essentia_valve",
            () -> TileEntityType.Builder.create(EssentiaValveBlockEntity::new, ModBlocks.ESSENTIA_VALVE.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EssentiaFilterBlockEntity>> ESSENTIA_FILTER = RegistryManager.BLOCK_ENTITIES.register(
            "essentia_filter",
            () -> TileEntityType.Builder.create(EssentiaFilterBlockEntity::new, ModBlocks.ESSENTIA_FILTER.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EssentiaPumpBlockEntity>> ESSENTIA_PUMP = RegistryManager.BLOCK_ENTITIES.register(
            "essentia_pump",
            () -> TileEntityType.Builder.create(EssentiaPumpBlockEntity::new, ModBlocks.ESSENTIA_PUMP.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EssentiaBufferBlockEntity>> ESSENTIA_BUFFER = RegistryManager.BLOCK_ENTITIES.register(
            "essentia_buffer",
            () -> TileEntityType.Builder.create(EssentiaBufferBlockEntity::new, ModBlocks.ESSENTIA_BUFFER.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<EssentiaMirrorBlockEntity>> ESSENTIA_MIRROR = RegistryManager.BLOCK_ENTITIES.register(
            "essentia_mirror",
            () -> TileEntityType.Builder.create(EssentiaMirrorBlockEntity::new, ModBlocks.ESSENTIA_MIRROR.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH = RegistryManager.BLOCK_ENTITIES.register(
            "arcane_workbench",
            () -> TileEntityType.Builder.create(ArcaneWorkbenchBlockEntity::new, ModBlocks.ARCANE_WORKBENCH.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<InfusionMatrixBlockEntity>> INFUSION_MATRIX = RegistryManager.BLOCK_ENTITIES.register(
            "infusion_matrix",
            () -> TileEntityType.Builder.create(InfusionMatrixBlockEntity::new, ModBlocks.INFUSION_MATRIX.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<ThaumatoriumBlockEntity>> THAUMATORIUM = RegistryManager.BLOCK_ENTITIES.register(
            "thaumatorium",
            () -> TileEntityType.Builder.create(ThaumatoriumBlockEntity::new, ModBlocks.THAUMATORIUM.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<AlembicBlockEntity>> ALEMBIC = RegistryManager.BLOCK_ENTITIES.register(
            "alembic",
            () -> TileEntityType.Builder.create(AlembicBlockEntity::new, ModBlocks.ALEMBIC.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<ArcaneFurnaceBlockEntity>> ARCANE_FURNACE = RegistryManager.BLOCK_ENTITIES.register(
            "arcane_furnace",
            () -> TileEntityType.Builder.create(ArcaneFurnaceBlockEntity::new, ModBlocks.ARCANE_FURNACE.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<ArcaneEarBlockEntity>> ARCANE_EAR = RegistryManager.BLOCK_ENTITIES.register(
            "arcane_ear",
            () -> TileEntityType.Builder.create(ArcaneEarBlockEntity::new, ModBlocks.ARCANE_EAR.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<VisGeneratorBlockEntity>> VIS_GENERATOR = RegistryManager.BLOCK_ENTITIES.register(
            "vis_generator",
            () -> TileEntityType.Builder.create(VisGeneratorBlockEntity::new, ModBlocks.VIS_GENERATOR.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<LevitatorBlockEntity>> LEVITATOR = RegistryManager.BLOCK_ENTITIES.register(
            "levitator",
            () -> TileEntityType.Builder.create(LevitatorBlockEntity::new, ModBlocks.LEVITATOR.get()).build(null)
    );
}

