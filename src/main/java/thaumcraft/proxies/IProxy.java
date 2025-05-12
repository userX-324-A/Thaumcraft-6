package thaumcraft.proxies;
// import net.minecraft.item.ItemBlock; // Obsolete for model registration in this manner
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent; // Changed
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent; // Added for client-specific init
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent; // Changed
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent; // Added
// import net.minecraftforge.fml.common.event.FMLPostInitializationEvent; // Replaced
// import net.minecraftforge.fml.common.event.FMLPreInitializationEvent; // Replaced

public interface IProxy
{
    void preInit(FMLCommonSetupEvent event); // Changed parameter
    
    void init(FMLCommonSetupEvent event);    // Changed parameter
    
    void postInit(); // Changed: No specific event, called from commonSetup or dedicated client/server setup
    
    void clientInit(FMLClientSetupEvent event); // Added for client-specific setup like renderers, keybindings
    
    World getClientWorld(); // Needs review: Minecraft.getInstance().world
    
    boolean getSingleplayer(); // Needs review: Minecraft.getInstance().isSingleplayer()
    
    // World getWorld(int dimensionId); // Needs major review/rework due to dimension changes, commenting out for now
    
    boolean isShiftKeyDown(); // Needs review: e.g., Screen.hasShiftDown()
    
    // void registerModel(ItemBlock itemBlock); // Obsolete: Model registration is now event-based and JSON-driven
    
    void enqueueInterModComs(InterModEnqueueEvent event); // Renamed and parameter changed
    
    void processInterModComs(InterModProcessEvent event); // Added for processing IMC
}
