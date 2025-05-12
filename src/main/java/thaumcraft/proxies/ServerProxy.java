package thaumcraft.proxies;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy extends CommonProxy
{
    // The preInit override can be removed if it only calls super(), 
    // as CommonProxy already implements IProxy.preInit.
    // For clarity, if it had server-specific logic beyond CommonProxy's preInit, it would stay.
    // Assuming it was just a pass-through for now.
    /* 
    @Override
    public void preInit(FMLCommonSetupEvent event) { // Parameter would need to change to FMLCommonSetupEvent
        super.preInit(event);
        // Any server-specific pre-init logic would go here
    }
    */
    
    // getWorld(int dim) is removed as it's not in IProxy and DimensionManager is obsolete.
    // Callers will need to be updated to use modern world/dimension handling.
}
