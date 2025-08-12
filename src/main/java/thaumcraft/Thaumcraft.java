package thaumcraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.common.client.screen.ArcaneWorkbenchScreen;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.registers.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.research.TheorycraftBootstrap;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.network.NetworkHandler;

@Mod("thaumcraft")
public class Thaumcraft {
    public static final String MODID = "thaumcraft";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static IProxy proxy;
    public static Thaumcraft instance;

    public Thaumcraft() {
        instance = this;
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register config
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.COMMON_SPEC);

        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModEntities.init();
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        RegistryManager.register(modEventBus);

        modEventBus.addListener(this::setup);
        // Attributes will be registered when entity AI/attributes are finalized
        // Attributes will be registered per-entity when AI/attributes are implemented
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);

        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        // Ensure aura and golem handlers are loaded
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(thaumcraft.common.world.aura.AuraEvents.class);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(thaumcraft.common.golems.seals.SealEngine.class);
        // Research reload listener will be added in setup via ServerLifecycleHooks
    }

    private void setup(final FMLCommonSetupEvent event) {
        EssentiaTransportCapability.register();
        thaumcraft.common.capabilities.CapabilityInit.register();
        ModRecipes.registerRecipeTypes();
        NetworkHandler.init();
        event.enqueueWork(() -> {
            // Optional theorycraft registration (config-gated)
            TheorycraftBootstrap.maybeRegister();
            // Register default research categories
            thaumcraft.common.research.ResearchBootstrap.registerDefaultCategories();
            // Also parse legacy embedded research JSONs to populate graph until datapack files are complete
            thaumcraft.common.lib.research.ResearchManager.parseAllResearch();
            // Register scannables
            thaumcraft.common.research.ResearchInit.registerScannables();
            // FireBat spawn placement
            net.minecraft.entity.EntitySpawnPlacementRegistry.register(
                    thaumcraft.common.registers.ModEntities.FIRE_BAT.get(),
                    net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    thaumcraft.common.entities.monster.FireBatEntity::canSpawn);

            // Eldritch Crab spawn placement (temp generic predicate)
            net.minecraft.entity.EntitySpawnPlacementRegistry.register(
                    thaumcraft.common.registers.ModEntities.ELDRITCH_CRAB.get(),
                    net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    thaumcraft.common.entities.monster.EldritchCrabEntity::canSpawn);
            // Built-in research will now load via datapack reload listener
            // Register core placeholder seals
            thaumcraft.common.golems.seals.CoreSeals.register();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.gui.ScreenManager.register(ModMenus.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
            net.minecraft.client.gui.ScreenManager.register(ModMenus.THAUMATORIUM.get(), thaumcraft.common.client.screen.ThaumatoriumScreen::new);
            // Register entity renderers
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.FIRE_BAT.get(),
                    manager -> new thaumcraft.common.client.render.FireBatRenderer(manager));
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.ELDRITCH_CRAB.get(),
                    manager -> new thaumcraft.common.client.render.EldritchCrabRenderer(manager));
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.TC_GOLEM.get(),
                    manager -> new thaumcraft.common.client.render.ThaumcraftGolemRenderer(manager));
            // Client will pick up research via server sync and datapack reloads automatically
            thaumcraft.common.client.ClientEvents.initKeybind();
        });
        // Register minimal golem parts
        thaumcraft.common.golems.GolemBootstrap.registerDefaults();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModEnqueueEvent event) {}

    public void serverStarting(final FMLServerStartingEvent event) {
        ThaumcraftCraftingManager.getCrucibleRecipes(event.getServer().overworld());
        LOGGER.info("Loaded crucible recipes");
    }
}

