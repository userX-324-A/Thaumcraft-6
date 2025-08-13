package thaumcraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
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
    public static Thaumcraft instance;

    public Thaumcraft() {
        instance = this;
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register config
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ModConfig.CLIENT_SPEC);

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

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        // Ensure aura handlers are loaded; SealEngine uses @Mod.EventBusSubscriber
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(thaumcraft.common.world.aura.AuraEvents.class);
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
            // Register core placeholder seals (config-gated)
            if (thaumcraft.common.config.ModConfig.COMMON.enableSeals.get()) {
                thaumcraft.common.golems.seals.CoreSeals.register();
            }
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.gui.ScreenManager.register(ModMenus.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
            net.minecraft.client.gui.ScreenManager.register(ModMenus.THAUMATORIUM.get(), thaumcraft.common.client.screen.ThaumatoriumScreen::new);
            net.minecraft.client.gui.ScreenManager.register(ModMenus.RESEARCH_TABLE.get(), thaumcraft.common.client.screen.ResearchTableScreen::new);
            if (thaumcraft.common.config.ModConfig.COMMON.enableSeals.get()) {
                net.minecraft.client.gui.ScreenManager.register(ModMenus.SEAL.get(), thaumcraft.common.client.screen.SealScreen::new);
            }
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
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.GRAPPLE_PROJECTILE.get(),
                    manager -> new thaumcraft.common.client.render.GrappleProjectileRenderer(manager));
            // Client will pick up research via server sync and datapack reloads automatically
            thaumcraft.common.client.ClientEvents.initKeybind();

            // Render layers for translucent/cutout blocks
            RenderType cutout = RenderType.cutout();
            RenderType translucent = RenderType.translucent();
            RenderTypeLookup.setRenderLayer(ModBlocks.SEAL.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ARCANE_LAMP.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_JAR.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_JAR_VOID.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_JAR_BRAIN.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_MIRROR.get(), translucent);
            RenderTypeLookup.setRenderLayer(ModBlocks.EVERFULL_URN.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ARCANE_WORKBENCH_CHARGER.get(), RenderType.solid());
            // Additional translucent/cutout candidates
            RenderTypeLookup.setRenderLayer(ModBlocks.BELLOWS.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.TUBE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_TUBE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_FILTER.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_PUMP.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_VALVE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.RESEARCH_TABLE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SPA.get(), translucent);
            RenderTypeLookup.setRenderLayer(ModBlocks.LEAVES_GREATWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.LEAVES_SILVERWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SAPLING_GREATWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SAPLING_SILVERWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SHIMMERLEAF.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_AIR.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_EARTH.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_FIRE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_WATER.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_ORDER.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_ENTROPY.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_TAINT.get(), cutout);
        });
        // Register minimal golem parts
        thaumcraft.common.golems.GolemBootstrap.registerDefaults();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModEnqueueEvent event) {
        // Curios slot registration (reflection to avoid hard dependency)
        try {
            Class<?> builderClass = Class.forName("top.theillusivec4.curios.api.SlotTypeMessage$Builder");
            java.lang.reflect.Constructor<?> ctor = builderClass.getConstructor(String.class);
            java.lang.reflect.Method sizeM = builderClass.getMethod("size", int.class);
            java.lang.reflect.Method buildM = builderClass.getMethod("build");
            java.lang.reflect.Method sendTo = net.minecraftforge.fml.InterModComms.class.getMethod("sendTo", String.class, String.class, java.util.function.Supplier.class);

            java.util.function.Supplier<?> amulet = () -> {
                try {
                    Object b = ctor.newInstance("amulet");
                    sizeM.invoke(b, 1);
                    return buildM.invoke(b);
                } catch (Throwable t) { return null; }
            };
            java.util.function.Supplier<?> ring = () -> {
                try {
                    Object b = ctor.newInstance("ring");
                    sizeM.invoke(b, 2);
                    return buildM.invoke(b);
                } catch (Throwable t) { return null; }
            };
            java.util.function.Supplier<?> charm = () -> {
                try {
                    Object b = ctor.newInstance("charm");
                    sizeM.invoke(b, 1);
                    return buildM.invoke(b);
                } catch (Throwable t) { return null; }
            };
            sendTo.invoke(null, "curios", "register_type", amulet);
            sendTo.invoke(null, "curios", "register_type", ring);
            sendTo.invoke(null, "curios", "register_type", charm);
        } catch (Throwable ignored) {}
    }

    public void serverStarting(final FMLServerStartingEvent event) {
        ThaumcraftCraftingManager.getCrucibleRecipes(event.getServer().overworld());
        LOGGER.info("Loaded crucible recipes");
    }
}

