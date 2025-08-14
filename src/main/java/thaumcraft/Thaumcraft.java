package thaumcraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
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
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(thaumcraft.client.ClientOnlySetup::clientInit));
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        // Ensure aura handlers are loaded; SealEngine uses @Mod.EventBusSubscriber
        if (!thaumcraft.common.Diag.disableAll() && !thaumcraft.common.Diag.disableAuraEvents()) {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(thaumcraft.common.world.aura.AuraEvents.class);
        } else {
            LOGGER.warn("Diagnostics: aura events disabled via config");
        }
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
            if (!thaumcraft.common.Diag.disableAll() && !thaumcraft.common.Diag.disableLegacyResearchParse()) {
                thaumcraft.common.lib.research.ResearchManager.parseAllResearch();
            } else {
                LOGGER.warn("Diagnostics: legacy embedded research JSON parse disabled via config");
            }
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
        LOGGER.info("ServerStarting: initializing crucible recipe cache");
        try {
            ThaumcraftCraftingManager.getCrucibleRecipes(event.getServer().overworld());
            LOGGER.info("ServerStarting: crucible recipes loaded");
        } catch (Throwable t) {
            LOGGER.error("ServerStarting: error while loading crucible recipes", t);
        }
    }

    // Additional lifecycle logs to pinpoint stalls
    public void serverAboutToStart(final net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent event) {
        LOGGER.info("ServerAboutToStart: creating and loading level data");
    }

    public void serverStarted(final net.minecraftforge.fml.event.server.FMLServerStartedEvent event) {
        LOGGER.info("ServerStarted: server is running. World name: {}", event.getServer().getWorldData().getLevelName());
    }
}


