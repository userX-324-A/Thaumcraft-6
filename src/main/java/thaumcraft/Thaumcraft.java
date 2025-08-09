package thaumcraft;

import net.minecraft.client.gui.ScreenManager;
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

        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        RegistryManager.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);

        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    }

    private void setup(final FMLCommonSetupEvent event) {
        EssentiaTransportCapability.register();
        ModRecipes.registerRecipeTypes();
        NetworkHandler.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.gui.ScreenManager.register(ModMenus.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
            net.minecraft.client.gui.ScreenManager.register(ModMenus.THAUMATORIUM.get(), thaumcraft.common.client.screen.ThaumatoriumScreen::new);
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModEnqueueEvent event) {}

    public void serverStarting(final FMLServerStartingEvent event) {
        ThaumcraftCraftingManager.getCrucibleRecipes(event.getServer().overworld());
        LOGGER.info("Loaded crucible recipes");
    }
}

