package thaumcraft.api.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.config.ModCreativeTabs;
import net.minecraft.item.BlockItem;
import net.minecraft.block.Block;
import thaumcraft.common.registration.ModBlocks;

// Import all specific item classes from thaumcraft.common.items...
// This list will be extensive.
import thaumcraft.common.entities.construct.ItemTurretPlacer;
import thaumcraft.common.golems.ItemGolemBell;
import thaumcraft.common.golems.ItemGolemPlacer;
import thaumcraft.common.golems.seals.ItemSealPlacer;
import thaumcraft.common.items.ItemTCBase;
import thaumcraft.common.items.armor.*;
import thaumcraft.common.items.baubles.*;
import thaumcraft.common.items.casters.*;
import thaumcraft.common.items.casters.foci.*;
import thaumcraft.common.items.consumables.*;
import thaumcraft.common.items.curios.*;
import thaumcraft.common.items.misc.ItemCreativeFluxSponge;
import thaumcraft.common.items.resources.ItemCrystalEssence;
import thaumcraft.common.items.resources.ItemMagicDust;
import thaumcraft.common.items.tools.*;


public class ItemsTC {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);

	private static Item.Properties thaumcraftProps() {
		return new Item.Properties().tab(ModCreativeTabs.TAB_THAUMCRAFT);
	}
	private static Item.Properties thaumcraftNoTabProps() {
		return new Item.Properties();
	}

	// Helper method for registering BlockItems
	private static <B extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<B> blockRegistryObject) {
		return ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), thaumcraftProps()));
	}
	
	private static <B extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<B> blockRegistryObject, Item.Properties properties) {
		return ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), properties));
	}

	//Curios
	public static final RegistryObject<Item> THAUMONOMICON = ITEMS.register("thaumonomicon", () -> new ItemThaumonomicon(thaumcraftProps()));
	/** "arcane", "preserved", "ancient", "eldritch", "knowledge", "twisted", "rites" */
	public static final RegistryObject<Item> CURIO_ARCANE = ITEMS.register("curio_arcane", () -> new ItemCurio("arcane", thaumcraftProps()));
	public static final RegistryObject<Item> CURIO_PRESERVED = ITEMS.register("curio_preserved", () -> new ItemCurio("preserved", thaumcraftProps()));
	public static final RegistryObject<Item> CURIO_ANCIENT = ITEMS.register("curio_ancient", () -> new ItemCurio("ancient", thaumcraftProps()));
	public static final RegistryObject<Item> CURIO_ELDRITCH = ITEMS.register("curio_eldritch", () -> new ItemCurio("eldritch", thaumcraftProps()));
	public static final RegistryObject<Item> CURIO_KNOWLEDGE = ITEMS.register("curio_knowledge", () -> new ItemCurio("knowledge", thaumcraftProps()));
	public static final RegistryObject<Item> CURIO_TWISTED = ITEMS.register("curio_twisted", () -> new ItemCurio("twisted", thaumcraftProps()));
	public static final RegistryObject<Item> CURIO_RITES = ITEMS.register("curio_rites", () -> new ItemCurio("rites", thaumcraftProps()));

	/** "common", "uncommon", "rare"  */
	public static final RegistryObject<Item> LOOT_BAG_COMMON = ITEMS.register("loot_bag_common", () -> new ItemLootBag("common", thaumcraftProps()));
	public static final RegistryObject<Item> LOOT_BAG_UNCOMMON = ITEMS.register("loot_bag_uncommon", () -> new ItemLootBag("uncommon", thaumcraftProps()));
	public static final RegistryObject<Item> LOOT_BAG_RARE = ITEMS.register("loot_bag_rare", () -> new ItemLootBag("rare", thaumcraftProps()));

	/** "mote", "nodule", "pearl"  */
	public static final RegistryObject<Item> PRIMORDIAL_MOTE = ITEMS.register("primordial_mote", () -> new ItemPrimordialPearl("mote", thaumcraftProps()));
	public static final RegistryObject<Item> PRIMORDIAL_NODULE = ITEMS.register("primordial_nodule", () -> new ItemPrimordialPearl("nodule", thaumcraftProps()));
	public static final RegistryObject<Item> PRIMORDIAL_PEARL = ITEMS.register("primordial_pearl", () -> new ItemPrimordialPearl("pearl", thaumcraftProps()));
	public static Item eldritchEye;
	public static Item runedTablet;
	public static Item pechWand;
	public static Item celestialNotes;

	//Resources
	public static final RegistryObject<Item> ALUMENTUM = ITEMS.register("alumentum", () -> new ItemAlumentum(thaumcraftProps()));
	public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new ItemTCBase("amber", thaumcraftProps()));
	public static final RegistryObject<Item> QUICKSILVER = ITEMS.register("quicksilver", () -> new ItemTCBase("quicksilver", thaumcraftProps()));
	/** "thaumium","void","brass"*/
	public static final RegistryObject<Item> THAUMIUM_INGOT = ITEMS.register("thaumium_ingot", () -> new ItemTCBase("thaumium_ingot", thaumcraftProps())); 
	/**"iron","copper","tin","silver","lead","quicksilver","thaumium","void","brass","quartz","rareearth"*/
	public static final RegistryObject<Item> IRON_NUGGET = ITEMS.register("iron_nugget", () -> new ItemTCBase("iron_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new ItemTCBase("copper_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> TIN_NUGGET = ITEMS.register("tin_nugget", () -> new ItemTCBase("tin_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new ItemTCBase("silver_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> LEAD_NUGGET = ITEMS.register("lead_nugget", () -> new ItemTCBase("lead_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> QUICKSILVER_NUGGET = ITEMS.register("quicksilver_nugget", () -> new ItemTCBase("quicksilver_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> THAUMIUM_NUGGET = ITEMS.register("thaumium_nugget", () -> new ItemTCBase("thaumium_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> VOID_METAL_NUGGET = ITEMS.register("void_metal_nugget", () -> new ItemTCBase("void_metal_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> BRASS_NUGGET = ITEMS.register("brass_nugget", () -> new ItemTCBase("brass_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> QUARTZ_NUGGET = ITEMS.register("quartz_nugget", () -> new ItemTCBase("quartz_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> RARE_EARTH_NUGGET = ITEMS.register("rare_earth_nugget", () -> new ItemTCBase("rare_earth_nugget", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> IRON_CLUSTER = ITEMS.register("iron_cluster", () -> new ItemTCBase("iron_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> GOLD_CLUSTER = ITEMS.register("gold_cluster", () -> new ItemTCBase("gold_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> COPPER_CLUSTER = ITEMS.register("copper_cluster", () -> new ItemTCBase("copper_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> TIN_CLUSTER = ITEMS.register("tin_cluster", () -> new ItemTCBase("tin_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> SILVER_CLUSTER = ITEMS.register("silver_cluster", () -> new ItemTCBase("silver_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> LEAD_CLUSTER = ITEMS.register("lead_cluster", () -> new ItemTCBase("lead_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> CINNABAR_ORE_CLUSTER = ITEMS.register("cinnabar_ore_cluster", () -> new ItemTCBase("cinnabar_ore_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> QUARTZ_ORE_CLUSTER = ITEMS.register("quartz_ore_cluster", () -> new ItemTCBase("quartz_ore_cluster", thaumcraftProps())); 
	/**"iron","gold","copper","tin","silver","lead","cinnabar","quartz"*/
	public static final RegistryObject<Item> CRYSTAL_ESSENCE = ITEMS.register("crystal_essence", () -> new ItemCrystalEssence(thaumcraftProps()));
	public static final RegistryObject<Item> TALLOW = ITEMS.register("tallow", () -> new ItemTCBase("tallow", thaumcraftProps()));
	public static final RegistryObject<Item> FABRIC = ITEMS.register("fabric", () -> new ItemTCBase("fabric", thaumcraftProps()));
	public static final RegistryObject<Item> MECHANISM_SIMPLE = ITEMS.register("mechanism_simple", () -> new ItemTCBase("mechanism_simple", thaumcraftProps()));
	public static final RegistryObject<Item> MECHANISM_COMPLEX = ITEMS.register("mechanism_complex", () -> new ItemTCBase("mechanism_complex", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> BRASS_PLATE = ITEMS.register("brass_plate", () -> new ItemTCBase("brass_plate", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate", () -> new ItemTCBase("iron_plate", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> THAUMIUM_PLATE = ITEMS.register("thaumium_plate", () -> new ItemTCBase("thaumium_plate", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> VOID_METAL_PLATE = ITEMS.register("void_metal_plate", () -> new ItemTCBase("void_metal_plate", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> VOID_SEED = ITEMS.register("void_seed", () -> new ItemTCBase("void_seed", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> SALIS_MUNDUS = ITEMS.register("salis_mundus", () -> new ItemMagicDust(thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> MIRRORED_GLASS_ITEM = ITEMS.register("mirrored_glass", () -> new ItemTCBase("mirrored_glass", thaumcraftProps()));
	/** "brass","iron","thaumium","void"*/
	public static final RegistryObject<Item> FILTER = ITEMS.register("filter", () -> new ItemTCBase("filter", thaumcraftProps()));
	/** "clockwork","biothaumic" */
	public static final RegistryObject<Item> MIND_CLOCKWORK = ITEMS.register("mind_clockwork", () -> new ItemTCBase("mind_clockwork", thaumcraftProps()));
	/** "clockwork","biothaumic" */
	public static final RegistryObject<Item> MIND_BIOTHAUMIC = ITEMS.register("mind_biothaumic", () -> new ItemTCBase("mind_biothaumic", thaumcraftProps()));
	/** "clockwork","biothaumic" */
	public static final RegistryObject<Item> MORPHIC_RESONATOR = ITEMS.register("morphic_resonator", () -> new ItemTCBase("morphic_resonator", thaumcraftProps()));
	/** "vision", "aggression" */
	public static final RegistryObject<Item> MODULE_VISION = ITEMS.register("module_vision", () -> new ItemTCBase("module_vision", thaumcraftProps()));
	/** "vision", "aggression" */
	public static final RegistryObject<Item> MODULE_AGGRESSION = ITEMS.register("module_aggression", () -> new ItemTCBase("module_aggression", thaumcraftProps()));
	/** "clockwork","biothaumic" */
	public static final RegistryObject<Item> VIS_RESONATOR_ITEM = ITEMS.register("vis_resonator", () -> new ItemTCBase("vis_resonator", thaumcraftProps()));

	//Consumables
	/** "beef","chicken","pork","fish","rabbit","mutton"*/
	public static final RegistryObject<Item> MEAT_NUGGETS = ITEMS.register("meat_nuggets", () -> new ItemChunksEdible(thaumcraftProps()));
	public static final RegistryObject<Item> TRIPLE_MEAT_TREAT = ITEMS.register("triple_meat_treat", () -> new ItemTripleMeatTreat(thaumcraftProps()));
	public static final RegistryObject<Item> ZOMBIE_BRAIN = ITEMS.register("zombie_brain", () -> new ItemZombieBrain(thaumcraftProps()));
	public static final RegistryObject<Item> BOTTLE_OF_TAINT = ITEMS.register("bottle_of_taint", () -> new ItemBottleTaint(thaumcraftProps()));
	public static final RegistryObject<Item> JAR_BRACE = ITEMS.register("jar_brace", () -> new ItemTCBase("jar_brace", thaumcraftProps()));
	public static final RegistryObject<Item> CAUSALITY_COLLAPSER = ITEMS.register("causality_collapser", () -> new ItemCausalityCollapser(thaumcraftProps()));
	public static final RegistryObject<Item> RESEARCH_NOTES = ITEMS.register("research_notes", () -> new ItemLabel(thaumcraftProps()));
	public static final RegistryObject<Item> EMPTY_PHIAL = ITEMS.register("empty_phial", () -> new ItemPhial(false, thaumcraftProps()));
	public static final RegistryObject<Item> FILLED_PHIAL = ITEMS.register("filled_phial", () -> new ItemPhial(true, thaumcraftNoTabProps()));

	//Tools
	public static final RegistryObject<Item> SCRIBING_TOOLS = ITEMS.register("scribing_tools", () -> new ItemScribingTools(thaumcraftProps()));
	public static final RegistryObject<Item> THAUMOMETER = ITEMS.register("thaumometer", () -> new ItemThaumometer(thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_AXE = ITEMS.register("thaumium_axe", () -> new ItemThaumiumAxe(ThaumcraftMaterials.TOOLMAT_THAUMIUM, 6.0F, -3.1F, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_SWORD = ITEMS.register("thaumium_sword", () -> new ItemThaumiumSword(ThaumcraftMaterials.TOOLMAT_THAUMIUM, 3, -2.4F, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_SHOVEL = ITEMS.register("thaumium_shovel", () -> new ItemThaumiumShovel(ThaumcraftMaterials.TOOLMAT_THAUMIUM, 1.5F, -3.0F, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_PICKAXE = ITEMS.register("thaumium_pickaxe", () -> new ItemThaumiumPickaxe(ThaumcraftMaterials.TOOLMAT_THAUMIUM, 1, -2.8F, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_HOE = ITEMS.register("thaumium_hoe", () -> new ItemThaumiumHoe(ThaumcraftMaterials.TOOLMAT_THAUMIUM, -2, -1.0F, thaumcraftProps()));
	
	public static final RegistryObject<Item> ELEMENTAL_AXE = ITEMS.register("elemental_axe", () -> new ItemElementalAxe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL, 5.0F, -3.0F, thaumcraftProps()));
	public static final RegistryObject<Item> ELEMENTAL_SWORD = ITEMS.register("elemental_sword", () -> new ItemElementalSword(ThaumcraftMaterials.TOOLMAT_ELEMENTAL, 3, -2.4F, thaumcraftProps()));
	public static final RegistryObject<Item> ELEMENTAL_SHOVEL = ITEMS.register("elemental_shovel", () -> new ItemElementalShovel(ThaumcraftMaterials.TOOLMAT_ELEMENTAL, 1.5F, -3.0F, thaumcraftProps()));
	public static final RegistryObject<Item> ELEMENTAL_PICKAXE = ITEMS.register("elemental_pickaxe", () -> new ItemElementalPickaxe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL, 1, -2.8F, thaumcraftProps()));
	public static final RegistryObject<Item> ELEMENTAL_HOE = ITEMS.register("elemental_hoe", () -> new ItemElementalHoe(ThaumcraftMaterials.TOOLMAT_ELEMENTAL, -3, 0.0F, thaumcraftProps()));
	
	public static final RegistryObject<Item> VOID_METAL_AXE = ITEMS.register("void_metal_axe", () -> new ItemVoidAxe(ThaumcraftMaterials.TOOLMAT_VOID, 6.0F, -3.0F, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_SWORD = ITEMS.register("void_metal_sword", () -> new ItemVoidSword(ThaumcraftMaterials.TOOLMAT_VOID, 3, -2.4F, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_SHOVEL = ITEMS.register("void_metal_shovel", () -> new ItemVoidShovel(ThaumcraftMaterials.TOOLMAT_VOID, 1.5F, -3.0F, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_PICKAXE = ITEMS.register("void_metal_pickaxe", () -> new ItemVoidPickaxe(ThaumcraftMaterials.TOOLMAT_VOID, 1, -2.8F, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_HOE = ITEMS.register("void_metal_hoe", () -> new ItemVoidHoe(ThaumcraftMaterials.TOOLMAT_VOID, -4, 0.0F, thaumcraftProps()));
	
	public static final RegistryObject<Item> CRIMSON_BLADE = ITEMS.register("crimson_blade", () -> new ItemCrimsonBlade(thaumcraftProps()));
	public static final RegistryObject<Item> PRIMAL_CRUSHER = ITEMS.register("primal_crusher", () -> new ItemPrimalCrusher(thaumcraftProps()));

	public static final RegistryObject<Item> SANITY_CHECKER = ITEMS.register("sanity_checker", () -> new ItemSanityChecker(thaumcraftProps()));
	public static final RegistryObject<Item> RESONATOR = ITEMS.register("resonator", () -> new ItemResonator(thaumcraftProps()));
	public static final RegistryObject<Item> HAND_MIRROR = ITEMS.register("hand_mirror", () -> new ItemHandMirror(thaumcraftProps()));
	public static final RegistryObject<Item> GRAPPLE_GUN = ITEMS.register("grapple_gun", () -> new ItemGrappleGun(thaumcraftProps()));
	public static final RegistryObject<Item> GRAPPLE_GUN_TIP = ITEMS.register("grapple_gun_tip", () -> new ItemTCBase("grapple_gun_tip", thaumcraftProps()));
	public static final RegistryObject<Item> GRAPPLE_GUN_SPOOL = ITEMS.register("grapple_gun_spool", () -> new ItemTCBase("grapple_gun_spool", thaumcraftProps()));

	//Armor
	public static final RegistryObject<Item> GOGGLES_OF_REVEALING = ITEMS.register("goggles_of_revealing", () -> new ItemGoggles(thaumcraftProps()));
	public static final RegistryObject<Item> BOOTS_OF_THE_TRAVELLER = ITEMS.register("boots_of_the_traveller", () -> new ItemBootsTraveller(ThaumcraftMaterials.ARMORMAT_SPECIAL, EntityEquipmentSlot.FEET, thaumcraftProps()));

	public static final RegistryObject<Item> THAUMIUM_HELMET = ITEMS.register("thaumium_helmet", () -> new ItemThaumiumArmor("thaumium_helmet", ThaumcraftMaterials.ARMORMAT_THAUMIUM, EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_CHESTPLATE = ITEMS.register("thaumium_chestplate", () -> new ItemThaumiumArmor("thaumium_chestplate", ThaumcraftMaterials.ARMORMAT_THAUMIUM, EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_LEGGINGS = ITEMS.register("thaumium_leggings", () -> new ItemThaumiumArmor("thaumium_leggings", ThaumcraftMaterials.ARMORMAT_THAUMIUM, EntityEquipmentSlot.LEGS, thaumcraftProps()));
	public static final RegistryObject<Item> THAUMIUM_BOOTS = ITEMS.register("thaumium_boots", () -> new ItemThaumiumArmor("thaumium_boots", ThaumcraftMaterials.ARMORMAT_THAUMIUM, EntityEquipmentSlot.FEET, thaumcraftProps()));

	public static final RegistryObject<Item> ROBE_CHESTPLATE = ITEMS.register("robe_chestplate", () -> new ItemRobeArmor("robe_chestplate", ThaumcraftMaterials.ARMORMAT_SPECIAL, EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> ROBE_LEGGINGS = ITEMS.register("robe_leggings", () -> new ItemRobeArmor("robe_leggings", ThaumcraftMaterials.ARMORMAT_SPECIAL, EntityEquipmentSlot.LEGS, thaumcraftProps()));
	public static final RegistryObject<Item> ROBE_BOOTS = ITEMS.register("robe_boots", () -> new ItemRobeArmor("robe_boots", ThaumcraftMaterials.ARMORMAT_SPECIAL, EntityEquipmentSlot.FEET, thaumcraftProps()));

	public static final RegistryObject<Item> FORTRESS_HELMET = ITEMS.register("fortress_helmet", () -> new ItemFortressArmor("fortress_helmet", ThaumcraftMaterials.ARMORMAT_FORTRESS, EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> FORTRESS_CHESTPLATE = ITEMS.register("fortress_chestplate", () -> new ItemFortressArmor("fortress_chestplate", ThaumcraftMaterials.ARMORMAT_FORTRESS, EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> FORTRESS_LEGGINGS = ITEMS.register("fortress_leggings", () -> new ItemFortressArmor("fortress_leggings", ThaumcraftMaterials.ARMORMAT_FORTRESS, EntityEquipmentSlot.LEGS, thaumcraftProps()));

	public static final RegistryObject<Item> VOID_METAL_HELMET = ITEMS.register("void_metal_helmet", () -> new ItemVoidArmor("void_metal_helmet", ThaumcraftMaterials.ARMORMAT_VOID, EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_CHESTPLATE = ITEMS.register("void_metal_chestplate", () -> new ItemVoidArmor("void_metal_chestplate", ThaumcraftMaterials.ARMORMAT_VOID, EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_LEGGINGS = ITEMS.register("void_metal_leggings", () -> new ItemVoidArmor("void_metal_leggings", ThaumcraftMaterials.ARMORMAT_VOID, EntityEquipmentSlot.LEGS, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_METAL_BOOTS = ITEMS.register("void_metal_boots", () -> new ItemVoidArmor("void_metal_boots", ThaumcraftMaterials.ARMORMAT_VOID, EntityEquipmentSlot.FEET, thaumcraftProps()));

	public static final RegistryObject<Item> VOID_ROBE_HELMET = ITEMS.register("void_robe_helmet", () -> new ItemVoidRobeArmor("void_robe_helmet", ThaumcraftMaterials.ARMORMAT_VOIDROBE, EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_ROBE_CHESTPLATE = ITEMS.register("void_robe_chestplate", () -> new ItemVoidRobeArmor("void_robe_chestplate", ThaumcraftMaterials.ARMORMAT_VOIDROBE, EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> VOID_ROBE_LEGGINGS = ITEMS.register("void_robe_leggings", () -> new ItemVoidRobeArmor("void_robe_leggings", ThaumcraftMaterials.ARMORMAT_VOIDROBE, EntityEquipmentSlot.LEGS, thaumcraftProps()));

	// Cultist armor - these were registered in ConfigItems with specific classes
	public static final RegistryObject<Item> CULTIST_ROBE_HELMET = ITEMS.register("cultist_robe_helmet", () -> new ItemCultistRobeArmor(EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> CULTIST_ROBE_CHESTPLATE = ITEMS.register("cultist_robe_chestplate", () -> new ItemCultistRobeArmor(EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> CULTIST_ROBE_LEGGINGS = ITEMS.register("cultist_robe_leggings", () -> new ItemCultistRobeArmor(EntityEquipmentSlot.LEGS, thaumcraftProps()));
	
	public static final RegistryObject<Item> CULTIST_BOOTS = ITEMS.register("cultist_boots", () -> new ItemCultistBoots(thaumcraftProps()));

	public static final RegistryObject<Item> CULTIST_PLATE_HELMET = ITEMS.register("cultist_plate_helmet", () -> new ItemCultistPlateArmor(EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> CULTIST_PLATE_CHESTPLATE = ITEMS.register("cultist_plate_chestplate", () -> new ItemCultistPlateArmor(EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> CULTIST_PLATE_LEGGINGS = ITEMS.register("cultist_plate_leggings", () -> new ItemCultistPlateArmor(EntityEquipmentSlot.LEGS, thaumcraftProps()));
	
	public static final RegistryObject<Item> CULTIST_LEADER_HELMET = ITEMS.register("cultist_leader_helmet", () -> new ItemCultistLeaderArmor(EntityEquipmentSlot.HEAD, thaumcraftProps()));
	public static final RegistryObject<Item> CULTIST_LEADER_CHESTPLATE = ITEMS.register("cultist_leader_chestplate", () -> new ItemCultistLeaderArmor(EntityEquipmentSlot.CHEST, thaumcraftProps()));
	public static final RegistryObject<Item> CULTIST_LEADER_LEGGINGS = ITEMS.register("cultist_leader_leggings", () -> new ItemCultistLeaderArmor(EntityEquipmentSlot.LEGS, thaumcraftProps()));

	//Baubles
	/** "amulet_mundane","ring_mundane","girdle_mundane","ring_apprentice","amulet_fancy","ring_fancy","girdle_fancy","girdle_fancy"*/
	public static final RegistryObject<Item> AMULET_MUNDANE = ITEMS.register("amulet_mundane", () -> new ItemBaubles("amulet_mundane", thaumcraftProps()));
	public static final RegistryObject<Item> RING_MUNDANE = ITEMS.register("ring_mundane", () -> new ItemBaubles("ring_mundane", thaumcraftProps()));
	public static final RegistryObject<Item> GIRDLE_MUNDANE = ITEMS.register("girdle_mundane", () -> new ItemBaubles("girdle_mundane", thaumcraftProps()));
	public static final RegistryObject<Item> RING_APPRENTICE = ITEMS.register("ring_apprentice", () -> new ItemBaubles("ring_apprentice", thaumcraftProps()));
	public static final RegistryObject<Item> AMULET_VIS = ITEMS.register("amulet_vis", () -> new ItemAmuletVis("amulet_vis", thaumcraftProps()));
	public static final RegistryObject<Item> CHARM_VERDANT = ITEMS.register("charm_verdant", () -> new ItemVerdantCharm("charm_verdant", thaumcraftProps()));
	public static final RegistryObject<Item> CHARM_VOIDSEER = ITEMS.register("charm_voidseer", () -> new ItemVoidseerCharm("charm_voidseer", thaumcraftProps()));
	public static final RegistryObject<Item> BAND_CURIOSITY = ITEMS.register("band_curiosity", () -> new ItemCuriosityBand("band_curiosity", thaumcraftProps()));
	public static final RegistryObject<Item> RING_CLOUD = ITEMS.register("ring_cloud", () -> new ItemCloudRing("ring_cloud", thaumcraftProps()));
	public static final RegistryObject<Item> CHARM_UNDYING = ITEMS.register("charm_undying", () -> new ItemCharmUndying("charm_undying", thaumcraftProps()));

	//Misc
	public static final RegistryObject<Item> CREATIVE_FLUX_SPONGE = ITEMS.register("creative_flux_sponge", () -> new ItemCreativeFluxSponge(thaumcraftProps()));
	public static final RegistryObject<Item> BATH_SALTS = ITEMS.register("bath_salts", () -> new ItemBathSalts(thaumcraftProps()));
	public static final RegistryObject<Item> SANITY_SOAP = ITEMS.register("sanity_soap", () -> new ItemSanitySoap(thaumcraftProps()));
	
	// ItemTurretPlacer needs its variants
	public static final RegistryObject<Item> TURRET_PLACER_BASIC = ITEMS.register("turret_placer_basic", () -> new ItemTurretPlacer("basic", thaumcraftProps()));
	public static final RegistryObject<Item> TURRET_PLACER_ADVANCED = ITEMS.register("turret_placer_advanced", () -> new ItemTurretPlacer("advanced", thaumcraftProps()));
	public static final RegistryObject<Item> TURRET_PLACER_BORE = ITEMS.register("turret_placer_bore", () -> new ItemTurretPlacer("bore", thaumcraftProps()));
	
	// enchantedPlaceholder not found in ConfigItems registration.
	// public static final RegistryObject<Item> ENCHANTED_PLACEHOLDER = ITEMS.register("enchanted_placeholder", () -> new ItemEnchantmentPlaceholder(thaumcraftProps()));

	//casters & foci
	public static final RegistryObject<Item> CASTER_BASIC = ITEMS.register("caster_basic", () -> new ItemCaster("caster_basic", 0, thaumcraftNoTabProps().stacksTo(1).rarity(Rarity.UNCOMMON)));
	
	// ItemFocus had variants like "focus_hellbat", "focus_fire_touch" etc. Needs specific registrations.
	// This requires knowing the exact constructor and parameters for each focus. Placeholdering.
	public static final RegistryObject<Item> FOCUS_BLANK = ITEMS.register("focus_blank", () -> new ItemFocus("blank", 10, thaumcraftNoTabProps().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FOCUS_EFFECT_AIR = ITEMS.register("focus_effect_air", () -> new ItemFocus("air", 25, thaumcraftNoTabProps().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FOCUS_EFFECT_BREAK = ITEMS.register("focus_effect_break", () -> new ItemFocus("break", 25, thaumcraftNoTabProps().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FOCUS_POUCH = ITEMS.register("focus_pouch", () -> new ItemFocusPouch(thaumcraftProps()));

	//golems
	public static final RegistryObject<Item> GOLEM_BELL = ITEMS.register("golem_bell", () -> new ItemGolemBell(thaumcraftProps()));
	public static final RegistryObject<Item> GOLEM_PLACER = ITEMS.register("golem_placer", () -> new ItemGolemPlacer(thaumcraftProps()));
	
	/** 
	 * damage 0 = blank seal
	 * use GolemHelper.getSealStack to return an itemstack of a specific seal 
	 * **/
	public static final RegistryObject<Item> SEAL_PLACER = ITEMS.register("seal_placer", () -> new ItemSealPlacer(thaumcraftProps()));

	//BLOCK ITEMS (Registered via helper)
	public static final RegistryObject<Item> GREATWOOD_PLANKS_ITEM = registerBlockItem("greatwood_planks", ModBlocks.GREATWOOD_PLANKS);
	public static final RegistryObject<Item> SILVERWOOD_PLANKS_ITEM = registerBlockItem("silverwood_planks", ModBlocks.SILVERWOOD_PLANKS);

	// Stone Block Items
	public static final RegistryObject<Item> STONE_ARCANE_ITEM = registerBlockItem("stone_arcane", ModBlocks.STONE_ARCANE);
	public static final RegistryObject<Item> STONE_ARCANE_BRICK_ITEM = registerBlockItem("stone_arcane_brick", ModBlocks.STONE_ARCANE_BRICK);
	public static final RegistryObject<Item> STONE_ANCIENT_ITEM = registerBlockItem("stone_ancient", ModBlocks.STONE_ANCIENT);
	public static final RegistryObject<Item> STONE_ANCIENT_TILE_ITEM = registerBlockItem("stone_ancient_tile", ModBlocks.STONE_ANCIENT_TILE);
	// STONE_ANCIENT_ROCK has no item because it's unbreakable and nodrops
	public static final RegistryObject<Item> STONE_ELDRITCH_TILE_ITEM = registerBlockItem("stone_eldritch_tile", ModBlocks.STONE_ELDRITCH_TILE);
	public static final RegistryObject<Item> MATRIX_SPEED_ITEM = registerBlockItem("matrix_speed", ModBlocks.MATRIX_SPEED);
	public static final RegistryObject<Item> MATRIX_COST_ITEM = registerBlockItem("matrix_cost", ModBlocks.MATRIX_COST);

	// Metal Block Items
	public static final RegistryObject<Item> METAL_BLOCK_BRASS_ITEM = registerBlockItem("metal_block_brass", ModBlocks.METAL_BLOCK_BRASS);
	public static final RegistryObject<Item> METAL_BLOCK_THAUMIUM_ITEM = registerBlockItem("metal_block_thaumium", ModBlocks.METAL_BLOCK_THAUMIUM);
	public static final RegistryObject<Item> METAL_BLOCK_VOID_ITEM = registerBlockItem("metal_block_void", ModBlocks.METAL_BLOCK_VOID);
	public static final RegistryObject<Item> METAL_ALCHEMICAL_ITEM = registerBlockItem("metal_alchemical", ModBlocks.METAL_ALCHEMICAL);
	public static final RegistryObject<Item> METAL_ALCHEMICAL_ADVANCED_ITEM = registerBlockItem("metal_alchemical_advanced", ModBlocks.METAL_ALCHEMICAL_ADVANCED);
}
