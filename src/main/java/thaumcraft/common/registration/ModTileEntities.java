package thaumcraft.common.registration;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import thaumcraft.common.tiles.TileCondenser;
import thaumcraft.common.tiles.TileStabilizer;
import thaumcraft.common.tiles.TileVoidSiphon;
import thaumcraft.common.tiles.TileBanner;
import thaumcraft.common.tiles.TileHole;
import thaumcraft.common.tiles.TileBarrierStone;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileArcaneWorkbenchCharger;
import thaumcraft.common.tiles.TileResearchTable;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileLampArcane;
import thaumcraft.common.tiles.TileLampFertility;
import thaumcraft.common.tiles.TileLampGrowth;
import net.minecraftforge.fml.RegistryObject;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Thaumcraft.MODID);

    public static final RegistryObject<TileEntityType<TileCondenser>> CONDENSER = TILE_ENTITIES.register("condenser",
            () -> TileEntityType.Builder.of(TileCondenser::new, ModBlocks.CONDENSER.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileStabilizer>> STABILIZER = TILE_ENTITIES.register("stabilizer",
            () -> TileEntityType.Builder.of(TileStabilizer::new, ModBlocks.STABILIZER.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileVoidSiphon>> VOID_SIPHON = TILE_ENTITIES.register("void_siphon",
            () -> TileEntityType.Builder.of(TileVoidSiphon::new, ModBlocks.VOID_SIPHON.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileBanner>> BANNER = TILE_ENTITIES.register("banner",
            () -> TileEntityType.Builder.of(TileBanner::new, ModBlocks.BANNER.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileHole>> HOLE = TILE_ENTITIES.register("hole",
            () -> TileEntityType.Builder.of(TileHole::new, ModBlocks.HOLE.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileBarrierStone>> BARRIER_STONE = TILE_ENTITIES.register("barrier_stone",
            () -> TileEntityType.Builder.of(TileBarrierStone::new, ModBlocks.BARRIER_STONE.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileArcaneWorkbench>> ARCANE_WORKBENCH = TILE_ENTITIES.register("arcane_workbench",
            () -> TileEntityType.Builder.of(TileArcaneWorkbench::new, ModBlocks.ARCANE_WORKBENCH.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileArcaneWorkbenchCharger>> ARCANE_WORKBENCH_CHARGER = TILE_ENTITIES.register("arcane_workbench_charger",
            () -> TileEntityType.Builder.of(TileArcaneWorkbenchCharger::new, ModBlocks.ARCANE_WORKBENCH_CHARGER.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileResearchTable>> RESEARCH_TABLE = TILE_ENTITIES.register("research_table",
            () -> TileEntityType.Builder.of(TileResearchTable::new, ModBlocks.RESEARCH_TABLE.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileCrucible>> CRUCIBLE = TILE_ENTITIES.register("crucible",
            () -> TileEntityType.Builder.of(TileCrucible::new, ModBlocks.CRUCIBLE.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileLampArcane>> LAMP_ARCANE = TILE_ENTITIES.register("lamp_arcane",
            () -> TileEntityType.Builder.of(TileLampArcane::new, ModBlocks.LAMP_ARCANE.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileLampFertility>> LAMP_FERTILITY = TILE_ENTITIES.register("lamp_fertility",
            () -> TileEntityType.Builder.of(TileLampFertility::new, ModBlocks.LAMP_FERTILITY.get()).build(null));
    
    public static final RegistryObject<TileEntityType<TileLampGrowth>> LAMP_GROWTH = TILE_ENTITIES.register("lamp_growth",
            () -> TileEntityType.Builder.of(TileLampGrowth::new, ModBlocks.LAMP_GROWTH.get()).build(null));

    public static void register() {
        // This method is not strictly necessary if using the event bus registration in the main mod file,
        // but can be useful for organizing registration calls if needed later.
    }
} 