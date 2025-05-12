package thaumcraft.proxies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import thaumcraft.client.ColorHandler;
import thaumcraft.client.lib.ender.ShaderHelper;
import thaumcraft.common.lib.events.KeyHandler;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.particles.FocusAirParticle;
import thaumcraft.client.fx.particles.ModParticles;
import thaumcraft.Thaumcraft;
import net.minecraft.client.particle.ParticleManager;

public class ClientProxy implements IProxy
{
    ProxyEntities proxyEntities;
    ProxyTESR proxyTESR;
    KeyHandler kh;
    
    public ClientProxy() {
        proxyEntities = new ProxyEntities();
        proxyTESR = new ProxyTESR();
        kh = new KeyHandler();
    }
    
    @Override
    public void preInit(FMLCommonSetupEvent event) {
        // OBJLoader.INSTANCE.addDomain("thaumcraft".toLowerCase());
        // ShaderHelper.initShaders();
    }
    
    @Override
    public void init(FMLCommonSetupEvent event) {
        // OBJLoader.INSTANCE.addDomain("thaumcraft".toLowerCase());
        // ShaderHelper.initShaders();
    }
    
    @Override
    public void clientInit(FMLClientSetupEvent event) {
        OBJLoader.INSTANCE.addDomain("thaumcraft".toLowerCase());
        ShaderHelper.initShaders();
        ColorHandler.registerColourHandlers();
        registerKeyBindings();
        proxyEntities.setupEntityRenderers();
        proxyTESR.setupTESR();

        // Register particle factories
        ParticleManager particleManager = Minecraft.getInstance().particles;
        particleManager.registerFactory(ModParticles.FOCUS_AIR.get(), FocusAirParticle.Factory::new);

        event.enqueueWork(() -> {
            ItemProperties.register(ItemsTC.LABEL.get(),
                new ResourceLocation(Thaumcraft.MODID, "filled_state"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    return itemStack.getDamageValue() == 1 ? 1.0f : 0.0f;
                });
            // You can add more ItemProperties.register calls here for other items
        });
    }
    
    @Override
    public void postInit() {
        // OBJLoader.INSTANCE.addDomain("thaumcraft".toLowerCase());
        // ShaderHelper.initShaders();
    }
    
    public void registerKeyBindings() {
        MinecraftForge.EVENT_BUS.register(kh);
    }
    
    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
    
    @Override
    public boolean getSingleplayer() {
        return Minecraft.getInstance().isSingleplayer();
    }
    
    @Override
    public boolean isShiftKeyDown() {
        return Screen.hasShiftDown();
    }
    
    public void setOtherBlockRenderers() {
    }
    
    @Override
    public void enqueueInterModComs(InterModEnqueueEvent event) {
        // TODO: Implement IMC message sending if needed
    }
    
    @Override
    public void processInterModComs(InterModProcessEvent event) {
        // TODO: Implement IMC message processing if needed
    }
}
