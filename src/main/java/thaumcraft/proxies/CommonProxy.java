package thaumcraft.proxies;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigAspects;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigRecipes;
import thaumcraft.common.config.ConfigResearch;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.lib.BehaviorDispenseAlumetum;
import thaumcraft.common.lib.InternalMethodHandler;
import thaumcraft.common.lib.capabilities.PlayerKnowledge;
import thaumcraft.common.lib.capabilities.PlayerWarp;
import thaumcraft.common.lib.events.CraftingEvents;
import thaumcraft.common.lib.events.WorldEvents;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.CropUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.ThaumcraftWorldGenerator;
import thaumcraft.common.world.biomes.BiomeHandler;


public class CommonProxy implements IGuiHandler, IProxy
{
    ProxyGUI proxyGUI;
    
    public CommonProxy() {
        proxyGUI = new ProxyGUI();
    }
    
    @Override
    public void preInit(FMLCommonSetupEvent event) {
        ThaumcraftApi.internalMethods = new InternalMethodHandler();
        PlayerKnowledge.preInit();
        PlayerWarp.preInit();
        PacketHandler.preInit();
        MinecraftForge.TERRAIN_GEN_BUS.register(WorldEvents.INSTANCE);
    }
    
    @Override
    public void init(FMLCommonSetupEvent event) {
        ConfigItems.init();
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemsTC.alumentum, new BehaviorDispenseAlumetum());
        ConfigResearch.init();
        ConfigRecipes.initializeSmelting();
    }
    
    @Override
    public void postInit() {
        ConfigEntities.postInitEntitySpawns();
        ConfigAspects.postInit();
        ConfigRecipes.postAspects();
        ModConfig.postInitLoot();
        ModConfig.postInitMisc();
        ConfigRecipes.compileGroups();
        ConfigResearch.postInit();
    }

    @Override
    public void clientInit(FMLClientSetupEvent event) {
        // Common proxy typically has no client-specific setup
    }
    
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return proxyGUI.getClientGuiElement(ID, player, world, x, y, z);
    }
    
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return proxyGUI.getServerGuiElement(ID, player, world, x, y, z);
    }
    
    @Override
    public boolean isShiftKeyDown() {
        return false;
    }
    
    @Override
    public World getClientWorld() {
        return null;
    }
    
    public void registerModel(ItemBlock itemBlock) {
    }
    
    @Override
    public void enqueueInterModComs(InterModEnqueueEvent event) {
        // This is for sending IMC messages. Thaumcraft's old IMC logic here was for receiving.
        // If Thaumcraft needs to send IMCs, that logic would go here.
    }

    @Override
    public void processInterModComs(InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            if (message.getMethodName().equals("portableHoleBlacklist") && message.getMessageSupplier().get() instanceof String) {
                BlockUtils.portableHoleBlackList.add((String)message.getMessageSupplier().get());
            }
            if (message.getMethodName().equals("harvestStandardCrop") && message.getMessageSupplier().get() instanceof ItemStack) {
                ItemStack crop = (ItemStack)message.getMessageSupplier().get();
                CropUtils.addStandardCrop(crop, crop.getDamageValue());
            }
            if (message.getMethodName().equals("harvestClickableCrop") && message.getMessageSupplier().get() instanceof ItemStack) {
                ItemStack crop = (ItemStack)message.getMessageSupplier().get();
                CropUtils.addClickableCrop(crop, crop.getDamageValue());
            }
            if (message.getMethodName().equals("harvestStackedCrop") && message.getMessageSupplier().get() instanceof ItemStack) {
                ItemStack crop = (ItemStack)message.getMessageSupplier().get();
                CropUtils.addStackedCrop(crop, crop.getDamageValue());
            }
            if (message.getMethodName().equals("nativeCluster") && message.getMessageSupplier().get() instanceof String) {
                String[] t = ((String)message.getMessageSupplier().get()).split(",");
                if (t != null && t.length == 5) {
                    try {
                        Thaumcraft.log.warn("IMC 'nativeCluster' requires update for item parsing: " + message.getMessageSupplier().get());
                    }
                    catch (Exception ex) {
                        Thaumcraft.log.error("Failed to parse IMC 'nativeCluster': " + message.getMessageSupplier().get(), ex);
                    }
                }
            }
            if (message.getMethodName().equals("lampBlacklist") && message.getMessageSupplier().get() instanceof ItemStack) {
                ItemStack crop = (ItemStack)message.getMessageSupplier().get();
                CropUtils.blacklistLamp(crop, crop.getDamageValue());
            }
            if (message.getMethodName().equals("dimensionBlacklist") && message.getMessageSupplier().get() instanceof String) {
                String[] t = ((String)message.getMessageSupplier().get()).split(":");
                if (t != null && t.length == 2) {
                    try {
                        Thaumcraft.log.warn("IMC 'dimensionBlacklist' requires update for dimension parsing: " + message.getMessageSupplier().get());
                    }
                    catch (Exception ex2) {
                         Thaumcraft.log.error("Failed to parse IMC 'dimensionBlacklist': " + message.getMessageSupplier().get(), ex2);
                    }
                }
            }
            if (message.getMethodName().equals("biomeBlacklist") && message.getMessageSupplier().get() instanceof String) {
                Thaumcraft.log.warn("IMC 'biomeBlacklist' requires update for biome parsing: " + message.getMessageSupplier().get());
            }
            if (message.getMethodName().equals("championWhiteList") && message.getMessageSupplier().get() instanceof String) {
                try {
                    Thaumcraft.log.warn("IMC 'championWhiteList' requires update for entity parsing: " + message.getMessageSupplier().get());
                }
                catch (Exception e) {
                    Thaumcraft.log.error("Failed to Whitelist [" + message.getMessageSupplier().get() + \"] with [ championWhiteList ] message.", e);
                }
            }
        });
    }
    
    @Override
    public boolean getSingleplayer() {
        return false;
    }
}
