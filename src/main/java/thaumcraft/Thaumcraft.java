package thaumcraft;
import java.io.File;
// import net.minecraft.command.ICommand; // Will be replaced by new command system
import net.minecraftforge.common.MinecraftForge; // Added
// import net.minecraftforge.common.config.Config; // To be replaced by new config system
// import net.minecraftforge.common.config.ConfigManager; // To be replaced by new config system
// import net.minecraftforge.fluids.FluidRegistry; // Obsolete
// import net.minecraftforge.fml.client.event.ConfigChangedEvent; // To be replaced by new config system
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor; // Added
// import net.minecraftforge.fml.common.SidedProxy; // Obsolete
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent; // Ensure @SubscribeEvent is used for Forge bus events
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths; // Added for config directory
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.common.lib.CommandThaumcraft;
import thaumcraft.proxies.IProxy;
import thaumcraft.proxies.ClientProxy;
import thaumcraft.proxies.ServerProxy;
import thaumcraft.api.items.ItemsTC; // Added import for ItemsTC
import thaumcraft.client.fx.particles.ModParticles; // Corrected import for ModParticles
import thaumcraft.common.registration.ModBlocks; // Added import for ModBlocks
import thaumcraft.common.registration.ModItems; // Added import for ModItems
import net.minecraft.client.renderer.RenderTypeLookup; // Added import
import net.minecraft.client.renderer.RenderType; // Added import

@Mod(Thaumcraft.MODID) // Use constant for modid
public class Thaumcraft
{
    public static final String MODID = "thaumcraft"; // Made final
    public static final String MODNAME = "Thaumcraft"; // Made final
    public static final String VERSION = "6.1.BETA26"; // Made final
    // @SidedProxy(clientSide = "thaumcraft.proxies.ClientProxy", serverSide = "thaumcraft.proxies.ServerProxy") // Obsolete
    public static IProxy proxy;
    @Mod.Instance(MODID) // Use constant
    public static Thaumcraft instance;
    public File modDir;
    public static final Logger log = LogManager.getLogger(MODID.toUpperCase()); // Made final, initialized here

    public Thaumcraft() {
        instance = this;
        // Initialize proxy using DistExecutor
        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register Deferred Registers for items, blocks, etc.
        ItemsTC.ITEMS.register(modEventBus); // Added this line
        ModParticles.register(modEventBus); // Register particle types
        ModBlocks.BLOCKS.register(modEventBus); // Example for later
        ModItems.ITEMS.register(modEventBus); // Added this line
        // ModTileEntities.TILE_ENTITIES.register(modEventBus); // Example for later

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        // modEventBus.addListener(this::serverSetup); // serverSetup is for FMLDedicatedServerSetupEvent, typically registered directly
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
        // log.info("Thaumcraft instance created, setting up event listeners."); // Log moved to static block or end of constructor
        log.info("Thaumcraft constructor finished, event listeners registered.");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        log.info("Thaumcraft common setup starting...");
        this.modDir = FMLPaths.CONFIGDIR.get().resolve(MODID).toFile(); // Initialize modDir here
        if (!this.modDir.exists()) {
            try {
                this.modDir.mkdirs();
            } catch (Exception e) {
                log.error("Could not create mod config directory: " + this.modDir.toString(), e);
            }
        }

        proxy.preInit(event); 
        proxy.init(event);    
        proxy.postInit(); // Called after preInit and init
        log.info("Thaumcraft common setup complete.");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        log.info("Thaumcraft client setup starting...");
        proxy.clientInit(event); // Call clientInit on proxy

        // Set render layers for blocks that need it (e.g., cutout textures)
        RenderTypeLookup.setRenderLayer(ModBlocks.GREATWOOD_LEAVES.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(ModBlocks.SILVERWOOD_LEAVES.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(ModBlocks.GREATWOOD_SAPLING.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(ModBlocks.SILVERWOOD_SAPLING.get(), RenderType.cutoutMipped());

        log.info("Thaumcraft client setup complete.");
    }

    // serverSetup should be registered if there's specific server-only logic not in CommonProxy handled by FMLCommonSetupEvent
    // For now, let's assume ServerProxy can handle its setup within preInit/init called during commonSetup
    // If FMLDedicatedServerSetupEvent is needed later, it can be re-added.
    // private void serverSetup(final FMLDedicatedServerSetupEvent event) {
    //    log.info("Thaumcraft server setup starting...");
    //    log.info("Thaumcraft server setup complete.");
    // }
    
    @SubscribeEvent // Ensure @SubscribeEvent is used for Forge bus events
    public void onServerStarting(FMLServerStartingEvent event) {
        log.info("Thaumcraft server starting event...");
        CommandThaumcraft.register(event.getServer().getCommandManager().getDispatcher());
        log.info("Thaumcraft commands registered.");
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        log.info("Thaumcraft enqueueing IMC messages...");
        proxy.enqueueInterModComs(event); // Corrected method name to match IProxy
        log.info("Thaumcraft IMC messages enqueued.");
    }

    private void processIMC(final InterModProcessEvent event) {
        log.info("Thaumcraft processing IMC messages...");
        proxy.processInterModComs(event); // Corrected method name to match IProxy
        log.info("Thaumcraft IMC messages processed.");
    }
    
    // Static logger initialization was already present and good.
    // static {
    //    log = LogManager.getLogger(MODID.toUpperCase());
    //    log.info("Thaumcraft static initializer complete.");
    // }
}
