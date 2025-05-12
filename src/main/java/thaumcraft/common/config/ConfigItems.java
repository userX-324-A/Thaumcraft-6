package thaumcraft.common.config;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thaumcraft.api.OreDictionaryEntries;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.FocusMediumRoot;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.construct.ItemTurretPlacer;
import thaumcraft.common.golems.ItemGolemBell;
import thaumcraft.common.golems.ItemGolemPlacer;
import thaumcraft.common.golems.seals.ItemSealPlacer;
import thaumcraft.common.golems.seals.SealBreaker;
import thaumcraft.common.golems.seals.SealBreakerAdvanced;
import thaumcraft.common.golems.seals.SealButcher;
import thaumcraft.common.golems.seals.SealEmpty;
import thaumcraft.common.golems.seals.SealEmptyAdvanced;
import thaumcraft.common.golems.seals.SealFill;
import thaumcraft.common.golems.seals.SealFillAdvanced;
import thaumcraft.common.golems.seals.SealGuard;
import thaumcraft.common.golems.seals.SealGuardAdvanced;
import thaumcraft.common.golems.seals.SealHandler;
import thaumcraft.common.golems.seals.SealHarvest;
import thaumcraft.common.golems.seals.SealLumber;
import thaumcraft.common.golems.seals.SealPickup;
import thaumcraft.common.golems.seals.SealPickupAdvanced;
import thaumcraft.common.golems.seals.SealProvide;
import thaumcraft.common.golems.seals.SealStock;
import thaumcraft.common.golems.seals.SealUse;
import thaumcraft.common.items.IThaumcraftItems;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.items.armor.ItemBootsTraveller;
import thaumcraft.common.items.armor.ItemCultistBoots;
import thaumcraft.common.items.armor.ItemCultistLeaderArmor;
import thaumcraft.common.items.armor.ItemCultistPlateArmor;
import thaumcraft.common.items.armor.ItemCultistRobeArmor;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.items.armor.ItemGoggles;
import thaumcraft.common.items.armor.ItemRobeArmor;
import thaumcraft.common.items.armor.ItemThaumiumArmor;
import thaumcraft.common.items.armor.ItemVoidArmor;
import thaumcraft.common.items.armor.ItemVoidRobeArmor;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.baubles.ItemBaubles;
import thaumcraft.common.items.baubles.ItemCharmUndying;
import thaumcraft.common.items.baubles.ItemCloudRing;
import thaumcraft.common.items.baubles.ItemCuriosityBand;
import thaumcraft.common.items.baubles.ItemVerdantCharm;
import thaumcraft.common.items.baubles.ItemVoidseerCharm;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.casters.ItemFocusPouch;
import thaumcraft.common.items.casters.foci.FocusEffectAir;
import thaumcraft.common.items.casters.foci.FocusEffectBreak;
import thaumcraft.common.items.casters.foci.FocusEffectCurse;
import thaumcraft.common.items.casters.foci.FocusEffectEarth;
import thaumcraft.common.items.casters.foci.FocusEffectExchange;
import thaumcraft.common.items.casters.foci.FocusEffectFire;
import thaumcraft.common.items.casters.foci.FocusEffectFlux;
import thaumcraft.common.items.casters.foci.FocusEffectFrost;
import thaumcraft.common.items.casters.foci.FocusEffectHeal;
import thaumcraft.common.items.casters.foci.FocusEffectRift;
import thaumcraft.common.items.casters.foci.FocusMediumBolt;
import thaumcraft.common.items.casters.foci.FocusMediumCloud;
import thaumcraft.common.items.casters.foci.FocusMediumMine;
import thaumcraft.common.items.casters.foci.FocusMediumPlan;
import thaumcraft.common.items.casters.foci.FocusMediumProjectile;
import thaumcraft.common.items.casters.foci.FocusMediumSpellBat;
import thaumcraft.common.items.casters.foci.FocusMediumTouch;
import thaumcraft.common.items.casters.foci.FocusModScatter;
import thaumcraft.common.items.casters.foci.FocusModSplitTarget;
import thaumcraft.common.items.casters.foci.FocusModSplitTrajectory;
import thaumcraft.common.items.consumables.ItemAlumentum;
import thaumcraft.common.items.consumables.ItemBathSalts;
import thaumcraft.common.items.consumables.ItemBottleTaint;
import thaumcraft.common.items.consumables.ItemCausalityCollapser;
import thaumcraft.common.items.consumables.ItemChunksEdible;
import thaumcraft.common.items.consumables.ItemLabel;
import thaumcraft.common.items.consumables.ItemPhial;
import thaumcraft.common.items.consumables.ItemSanitySoap;
import thaumcraft.common.items.consumables.ItemTripleMeatTreat;
import thaumcraft.common.items.consumables.ItemZombieBrain;
import thaumcraft.common.items.curios.ItemCelestialNotes;
import thaumcraft.common.items.curios.ItemCurio;
import thaumcraft.common.items.curios.ItemEnchantmentPlaceholder;
import thaumcraft.common.items.curios.ItemLootBag;
import thaumcraft.common.items.curios.ItemPechWand;
import thaumcraft.common.items.curios.ItemPrimordialPearl;
import thaumcraft.common.items.curios.ItemThaumonomicon;
import thaumcraft.common.items.misc.ItemCreativeFluxSponge;
import thaumcraft.common.items.resources.ItemCrystalEssence;
import thaumcraft.common.items.resources.ItemMagicDust;
import thaumcraft.common.items.tools.ItemCrimsonBlade;
import thaumcraft.common.items.tools.ItemElementalAxe;
import thaumcraft.common.items.tools.ItemElementalHoe;
import thaumcraft.common.items.tools.ItemElementalPickaxe;
import thaumcraft.common.items.tools.ItemElementalShovel;
import thaumcraft.common.items.tools.ItemElementalSword;
import thaumcraft.common.items.tools.ItemGrappleGun;
import thaumcraft.common.items.tools.ItemHandMirror;
import thaumcraft.common.items.tools.ItemPrimalCrusher;
import thaumcraft.common.items.tools.ItemResonator;
import thaumcraft.common.items.tools.ItemSanityChecker;
import thaumcraft.common.items.tools.ItemScribingTools;
import thaumcraft.common.items.tools.ItemThaumiumAxe;
import thaumcraft.common.items.tools.ItemThaumiumHoe;
import thaumcraft.common.items.tools.ItemThaumiumPickaxe;
import thaumcraft.common.items.tools.ItemThaumiumShovel;
import thaumcraft.common.items.tools.ItemThaumiumSword;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.items.tools.ItemVoidAxe;
import thaumcraft.common.items.tools.ItemVoidHoe;
import thaumcraft.common.items.tools.ItemVoidPickaxe;
import thaumcraft.common.items.tools.ItemVoidShovel;
import thaumcraft.common.items.tools.ItemVoidSword;
import thaumcraft.common.lib.CreativeTabThaumcraft;


public class ConfigItems
{
    public static ItemStack startBook;
    public static CreativeTabs TABTC;
    public static List<IThaumcraftItems> ITEM_VARIANT_HOLDERS;
    public static ItemStack AIR_CRYSTAL;
    public static ItemStack FIRE_CRYSTAL;
    public static ItemStack WATER_CRYSTAL;
    public static ItemStack EARTH_CRYSTAL;
    public static ItemStack ORDER_CRYSTAL;
    public static ItemStack ENTROPY_CRYSTAL;
    public static ItemStack FLUX_CRYSTAL;
    
    public static void initMisc() {
        // OreDictionaryEntries.initializeOreDictionary(); // TODO: Convert to Tags system
        
        // These assume ThaumcraftApiHelper.makeCrystal uses ItemsTC.CRYSTAL_ESSENCE.get() or similar internally
        ConfigItems.AIR_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.AIR);
        ConfigItems.FIRE_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.FIRE);
        ConfigItems.WATER_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.WATER);
        ConfigItems.EARTH_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.EARTH);
        ConfigItems.ORDER_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.ORDER);
        ConfigItems.ENTROPY_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY);
        ConfigItems.FLUX_CRYSTAL = ThaumcraftApiHelper.makeCrystal(Aspect.FLUX);
        
        // Initialize the starting book. This seems to be a standard Written Book with custom NBT.
        ConfigItems.startBook = new ItemStack(Items.WRITTEN_BOOK);
        NBTTagCompound contents = new NBTTagCompound();
        // I18n.translateToLocal is legacy. Should be new StringTextComponent(key).getString() or similar for NBT.
        // For now, leaving as is, but this will need an update for proper localization.
        contents.putString("author", "Azanor"); // Standard author tag
        contents.putString("title", I18n.translateToLocal("book.start.title")); // Legacy I18n
        NBTTagList pages = new NBTTagList();
        pages.appendTag(NBTTagString.valueOf("{ \"text\": \"" + I18n.translateToLocal("book.start.1") + "\" }")); // Legacy I18n, formatting for 1.16.5 book NBT
        pages.appendTag(NBTTagString.valueOf("{ \"text\": \"" + I18n.translateToLocal("book.start.2") + "\" }")); // Legacy I18n
        pages.appendTag(NBTTagString.valueOf("{ \"text\": \"" + I18n.translateToLocal("book.start.3") + "\" }")); // Legacy I18n
        contents.setTag("pages", pages);
        ConfigItems.startBook.setTagCompound(contents);
    }
    
    public static void init() {
        initMisc(); // Keep this call
        // initResearch(); // This was called from CommonProxy.init, will be handled later
        // initPotions(); // This was called from CommonProxy.init, will be handled later
    }
    
    public static void preInitSeals() {
        SealHandler.registerSeal(new SealPickup());
        SealHandler.registerSeal(new SealPickupAdvanced());
        SealHandler.registerSeal(new SealFill());
        SealHandler.registerSeal(new SealFillAdvanced());
        SealHandler.registerSeal(new SealEmpty());
        SealHandler.registerSeal(new SealEmptyAdvanced());
        SealHandler.registerSeal(new SealHarvest());
        SealHandler.registerSeal(new SealButcher());
        SealHandler.registerSeal(new SealGuard());
        SealHandler.registerSeal(new SealGuardAdvanced());
        SealHandler.registerSeal(new SealLumber());
        SealHandler.registerSeal(new SealBreaker());
        SealHandler.registerSeal(new SealUse());
        SealHandler.registerSeal(new SealProvide());
        SealHandler.registerSeal(new SealStock());
        SealHandler.registerSeal(new SealBreakerAdvanced());
    }
    
    static {
        ConfigItems.TABTC = new CreativeTabThaumcraft(CreativeTabs.getNextID(), "thaumcraft");
        ITEM_VARIANT_HOLDERS = new ArrayList<IThaumcraftItems>();
    }
}
