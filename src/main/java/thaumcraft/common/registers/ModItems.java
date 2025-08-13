package thaumcraft.common.registers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.common.ForgeSpawnEggItem;
import thaumcraft.common.items.tools.ItemThaumometer;

public class ModItems {

    public static void init() {}

    private static RegistryObject<Item> registerItem(String name) {
        return RegistryManager.ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    //Curios
    public static final RegistryObject<Item> THAUMONOMICON = registerItem("thaumonomicon");
    public static final RegistryObject<Item> CURIO = registerItem("curio");
    public static final RegistryObject<Item> LOOT_BAG = registerItem("loot_bag");
    public static final RegistryObject<Item> PRIMORDIAL_PEARL = registerItem("primordial_pearl");
    public static final RegistryObject<Item> ELDRITCH_EYE = registerItem("eldritch_eye");
    public static final RegistryObject<Item> RUNED_TABLET = registerItem("runed_tablet");
    public static final RegistryObject<Item> PECH_WAND = registerItem("pech_wand");
    public static final RegistryObject<Item> CELESTIAL_NOTES = registerItem("celestial_notes");

    //Resources
    public static final RegistryObject<Item> ALUMENTUM = registerItem("alumentum");
    public static final RegistryObject<Item> AMBER = registerItem("amber");
    public static final RegistryObject<Item> QUICKSILVER = registerItem("quicksilver");
    public static final RegistryObject<Item> THAUMIUM_INGOT = registerItem("thaumium_ingot");
    public static final RegistryObject<Item> VOID_INGOT = registerItem("void_ingot");
    public static final RegistryObject<Item> BRASS_INGOT = registerItem("brass_ingot");
    public static final RegistryObject<Item> THAUMIUM_NUGGET = registerItem("thaumium_nugget");
    public static final RegistryObject<Item> VOID_NUGGET = registerItem("void_nugget");
    public static final RegistryObject<Item> BRASS_NUGGET = registerItem("brass_nugget");
    public static final RegistryObject<Item> QUARTZ_NUGGET = registerItem("quartz_nugget");
    public static final RegistryObject<Item> QUICKSILVER_NUGGET = registerItem("quicksilver_nugget");
    public static final RegistryObject<Item> CLUSTERS = registerItem("clusters");
    public static final RegistryObject<Item> CRYSTAL_ESSENCE = registerItem("crystal_essence");
    public static final RegistryObject<Item> TALLOW = registerItem("tallow");
    public static final RegistryObject<Item> FABRIC = registerItem("fabric");
    public static final RegistryObject<Item> MECHANISM_SIMPLE = registerItem("mechanism_simple");
    public static final RegistryObject<Item> MECHANISM_COMPLEX = registerItem("mechanism_complex");
    public static final RegistryObject<Item> PLATE = registerItem("plate");
    public static final RegistryObject<Item> VOID_SEED = registerItem("void_seed");
    public static final RegistryObject<Item> SALIS_MUNDUS = registerItem("salis_mundus");
    public static final RegistryObject<Item> MIRRORED_GLASS = registerItem("mirrored_glass");
    public static final RegistryObject<Item> FILTER = registerItem("filter");
    public static final RegistryObject<Item> MIND = registerItem("mind");
    public static final RegistryObject<Item> MORPHIC_RESONATOR = registerItem("morphic_resonator");
    public static final RegistryObject<Item> MODULES = registerItem("modules");
    public static final RegistryObject<Item> VIS_RESONATOR = registerItem("vis_resonator");


    //Consumables
    public static final RegistryObject<Item> CHUNKS = registerItem("chunks");
    public static final RegistryObject<Item> TRIPLE_MEAT_TREAT = registerItem("triple_meat_treat");
    public static final RegistryObject<Item> BRAIN = registerItem("brain");
    public static final RegistryObject<Item> PHIAL = RegistryManager.ITEMS.register("phial", () -> new thaumcraft.common.items.ItemPhial(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> LABEL = RegistryManager.ITEMS.register("label", () -> new thaumcraft.common.items.ItemLabel(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> BOTTLE_TAINT = registerItem("bottle_taint");
    public static final RegistryObject<Item> JAR_BRACE = RegistryManager.ITEMS.register("jar_brace", () -> new thaumcraft.common.items.ItemJarBrace(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> CAUSALITY_COLLAPSER = RegistryManager.ITEMS.register("causality_collapser", () -> new thaumcraft.common.items.ItemCausalityCollapser(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    //Tools
    public static final RegistryObject<Item> SCRIBING_TOOLS = RegistryManager.ITEMS.register("scribing_tools", () -> new thaumcraft.common.items.tools.ItemScribingTools(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> THAUMOMETER = RegistryManager.ITEMS.register("thaumometer", () -> new ItemThaumometer(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> THAUMIUM_AXE = RegistryManager.ITEMS.register("thaumium_axe", () -> new thaumcraft.common.items.tools.ThaumiumAxeItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> THAUMIUM_SWORD = RegistryManager.ITEMS.register("thaumium_sword", () -> new thaumcraft.common.items.tools.ThaumiumSwordItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> THAUMIUM_SHOVEL = RegistryManager.ITEMS.register("thaumium_shovel", () -> new thaumcraft.common.items.tools.ThaumiumShovelItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> THAUMIUM_PICK = RegistryManager.ITEMS.register("thaumium_pick", () -> new thaumcraft.common.items.tools.ThaumiumPickaxeItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> THAUMIUM_HOE = RegistryManager.ITEMS.register("thaumium_hoe", () -> new thaumcraft.common.items.tools.ThaumiumHoeItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> ELEMENTAL_AXE = registerItem("elemental_axe");
    public static final RegistryObject<Item> ELEMENTAL_SWORD = registerItem("elemental_sword");
    public static final RegistryObject<Item> ELEMENTAL_SHOVEL = registerItem("elemental_shovel");
    public static final RegistryObject<Item> ELEMENTAL_PICK = registerItem("elemental_pick");
    public static final RegistryObject<Item> ELEMENTAL_HOE = registerItem("elemental_hoe");
    public static final RegistryObject<Item> VOID_AXE = registerItem("void_axe");
    public static final RegistryObject<Item> VOID_SWORD = registerItem("void_sword");
    public static final RegistryObject<Item> VOID_SHOVEL = registerItem("void_shovel");
    public static final RegistryObject<Item> VOID_PICK = registerItem("void_pick");
    public static final RegistryObject<Item> VOID_HOE = registerItem("void_hoe");
    public static final RegistryObject<Item> CRIMSON_BLADE = registerItem("crimson_blade");
    public static final RegistryObject<Item> PRIMAL_CRUSHER = registerItem("primal_crusher");
    public static final RegistryObject<Item> SANITY_CHECKER = RegistryManager.ITEMS.register("sanity_checker", () -> new thaumcraft.common.items.tools.ItemSanityChecker(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> RESONATOR = RegistryManager.ITEMS.register("resonator", () -> new thaumcraft.common.items.tools.ItemResonator(new Item.Properties().tab(ItemGroup.TAB_TOOLS)) {
        @Override
        public void appendHoverText(net.minecraft.item.ItemStack stack, net.minecraft.world.World level, java.util.List<net.minecraft.util.text.ITextComponent> lines, net.minecraft.client.util.ITooltipFlag flag) {
            int r = thaumcraft.common.config.ModConfig.COMMON.resonatorScanRadius.get();
            int t = thaumcraft.common.config.ModConfig.COMMON.resonatorHudCadenceTicks.get();
            lines.add(new net.minecraft.util.text.StringTextComponent("Scan radius: "+r+" | HUD cadence: "+t+"t"));
        }
    });
    public static final RegistryObject<Item> HAND_MIRROR = RegistryManager.ITEMS.register("hand_mirror", () -> new thaumcraft.common.items.tools.ItemHandMirror(new Item.Properties().tab(ItemGroup.TAB_TOOLS)) {
        @Override
        public void appendHoverText(net.minecraft.item.ItemStack stack, net.minecraft.world.World level, java.util.List<net.minecraft.util.text.ITextComponent> lines, net.minecraft.client.util.ITooltipFlag flag) {
            double cost = thaumcraft.common.config.ModConfig.COMMON.mirrorVisCost.get();
            int cd = thaumcraft.common.config.ModConfig.COMMON.mirrorCooldownSeconds.get();
            lines.add(new net.minecraft.util.text.StringTextComponent("Vis cost: "+(int)cost+" | Cooldown: "+cd+"s"));
            lines.add(new net.minecraft.util.text.StringTextComponent("Sneak-Use to link; Use to teleport (cross-dimension supported)"));
        }
    });
    public static final RegistryObject<Item> GRAPPLE_GUN = RegistryManager.ITEMS.register("grapple_gun", () -> new thaumcraft.common.items.tools.ItemGrappleGun(new Item.Properties().tab(ItemGroup.TAB_TOOLS)) {
        @Override
        public void appendHoverText(net.minecraft.item.ItemStack stack, net.minecraft.world.World level, java.util.List<net.minecraft.util.text.ITextComponent> lines, net.minecraft.client.util.ITooltipFlag flag) {
            double len = thaumcraft.common.config.ModConfig.COMMON.grappleMaxLength.get();
            double spd = thaumcraft.common.config.ModConfig.COMMON.grappleReelSpeedPerTick.get();
            boolean mit = thaumcraft.common.config.ModConfig.COMMON.grappleMitigateFallDamage.get();
            lines.add(new net.minecraft.util.text.StringTextComponent("Max length: "+(int)len+" | Reel: "+String.format("%.2f", spd)+"/t"));
            if (mit) lines.add(new net.minecraft.util.text.StringTextComponent("Fall damage mitigation: enabled"));
            lines.add(new net.minecraft.util.text.StringTextComponent("Crouch to reel in."));
        }
    });
    public static final RegistryObject<Item> GRAPPLE_GUN_TIP = registerItem("grapple_gun_tip");
    public static final RegistryObject<Item> GRAPPLE_GUN_SPOOL = registerItem("grapple_gun_spool");

    //Armor
    public static final RegistryObject<Item> GOGGLES = registerItem("goggles");
    public static final RegistryObject<Item> TRAVELLER_BOOTS = registerItem("traveller_boots");
    public static final RegistryObject<Item> THAUMIUM_HELM = RegistryManager.ITEMS.register("thaumium_helm", () -> new thaumcraft.common.items.armor.ThaumiumArmorItem(net.minecraft.inventory.EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> THAUMIUM_CHEST = RegistryManager.ITEMS.register("thaumium_chest", () -> new thaumcraft.common.items.armor.ThaumiumArmorItem(net.minecraft.inventory.EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> THAUMIUM_LEGS = RegistryManager.ITEMS.register("thaumium_legs", () -> new thaumcraft.common.items.armor.ThaumiumArmorItem(net.minecraft.inventory.EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> THAUMIUM_BOOTS = RegistryManager.ITEMS.register("thaumium_boots", () -> new thaumcraft.common.items.armor.ThaumiumArmorItem(net.minecraft.inventory.EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CLOTH_CHEST = registerItem("cloth_chest");
    public static final RegistryObject<Item> CLOTH_LEGS = registerItem("cloth_legs");
    public static final RegistryObject<Item> CLOTH_BOOTS = registerItem("cloth_boots");
    public static final RegistryObject<Item> FORTRESS_HELM = registerItem("fortress_helm");
    public static final RegistryObject<Item> FORTRESS_CHEST = registerItem("fortress_chest");
    public static final RegistryObject<Item> FORTRESS_LEGS = registerItem("fortress_legs");
    public static final RegistryObject<Item> VOID_HELM = RegistryManager.ITEMS.register("void_helm", () -> new thaumcraft.common.items.armor.VoidArmorItem(net.minecraft.inventory.EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> VOID_CHEST = RegistryManager.ITEMS.register("void_chest", () -> new thaumcraft.common.items.armor.VoidArmorItem(net.minecraft.inventory.EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> VOID_LEGS = RegistryManager.ITEMS.register("void_legs", () -> new thaumcraft.common.items.armor.VoidArmorItem(net.minecraft.inventory.EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> VOID_BOOTS = RegistryManager.ITEMS.register("void_boots", () -> new thaumcraft.common.items.armor.VoidArmorItem(net.minecraft.inventory.EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> VOID_ROBE_HELM = registerItem("void_robe_helm");
    public static final RegistryObject<Item> VOID_ROBE_CHEST = registerItem("void_robe_chest");
    public static final RegistryObject<Item> VOID_ROBE_LEGS = registerItem("void_robe_legs");
    public static final RegistryObject<Item> CRIMSON_BOOTS = registerItem("crimson_boots");

    public static final RegistryObject<Item> CRIMSON_PLATE_HELM = registerItem("crimson_plate_helm");
    public static final RegistryObject<Item> CRIMSON_PLATE_CHEST = registerItem("crimson_plate_chest");
    public static final RegistryObject<Item> CRIMSON_PLATE_LEGS = registerItem("crimson_plate_legs");
    public static final RegistryObject<Item> CRIMSON_ROBE_HELM = registerItem("crimson_robe_helm");
    public static final RegistryObject<Item> CRIMSON_ROBE_CHEST = registerItem("crimson_robe_chest");
    public static final RegistryObject<Item> CRIMSON_ROBE_LEGS = registerItem("crimson_robe_legs");
    public static final RegistryObject<Item> CRIMSON_PRAETOR_HELM = registerItem("crimson_praetor_helm");
    public static final RegistryObject<Item> CRIMSON_PRAETOR_CHEST = registerItem("crimson_praetor_chest");
    public static final RegistryObject<Item> CRIMSON_PRAETOR_LEGS = registerItem("crimson_praetor_legs");

    //Baubles
    public static final RegistryObject<Item> BAUBLES = registerItem("baubles");
    public static final RegistryObject<Item> AMULET_VIS = RegistryManager.ITEMS.register("amulet_vis", () -> new thaumcraft.common.items.baubles.AmuletVisItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> RING_CURIOSITY = RegistryManager.ITEMS.register("ring_curiosity", () -> new thaumcraft.common.items.baubles.CurioItems.CuriosityRingItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> CHARM_VERDANT = RegistryManager.ITEMS.register("charm_verdant", () -> new thaumcraft.common.items.baubles.CurioItems.VerdantCharmItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> CHARM_VOIDSEER = RegistryManager.ITEMS.register("charm_voidseer", () -> new thaumcraft.common.items.baubles.CurioItems.VoidseerCharmItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> BAND_CURIOSITY = RegistryManager.ITEMS.register("band_curiosity", () -> new thaumcraft.common.items.baubles.CurioItems.CuriosityRingItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> RING_CLOUD = RegistryManager.ITEMS.register("ring_cloud", () -> new thaumcraft.common.items.baubles.CurioItems.CloudRingItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> CHARM_UNDYING = RegistryManager.ITEMS.register("charm_undying", () -> new thaumcraft.common.items.baubles.CurioItems.UndyingCharmItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    //Misc
    public static final RegistryObject<Item> CREATIVE_PLACER = registerItem("creative_placer");
    public static final RegistryObject<Item> CREATIVE_FLUX_SPONGE = registerItem("creative_flux_sponge");
    public static final RegistryObject<Item> BATH_SALTS = registerItem("bath_salts");
    public static final RegistryObject<Item> SANITY_SOAP = registerItem("sanity_soap");
    public static final RegistryObject<Item> TURRET_PLACER = registerItem("turret_placer");
    public static final RegistryObject<Item> ENCHANTED_PLACEHOLDER = registerItem("enchanted_placeholder");

    //casters & foci
    public static final RegistryObject<Item> CASTER_BASIC = registerItem("caster_basic");
    public static final RegistryObject<Item> FOCUS1 = registerItem("focus1");
    public static final RegistryObject<Item> FOCUS2 = registerItem("focus2");
    public static final RegistryObject<Item> FOCUS3 = registerItem("focus3");
    public static final RegistryObject<Item> FOCUS_POUCH = registerItem("focus_pouch");

    //golems
    public static final RegistryObject<Item> GOLEM_BELL = registerItem("golem_bell");
    public static final RegistryObject<Item> GOLEM_PLACER = registerItem("golem_placer");
    public static final RegistryObject<Item> SEAL_PLACER = RegistryManager.ITEMS.register("seal_placer",
            () -> new thaumcraft.common.golems.seals.ItemSealPlacer(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> SEAL_FETCH = RegistryManager.ITEMS.register("seal_fetch",
            () -> new thaumcraft.common.golems.seals.ItemSealPlacer(new Item.Properties().tab(ItemGroup.TAB_MISC), "thaumcraft:fetch"));
    public static final RegistryObject<Item> SEAL_GUARD = RegistryManager.ITEMS.register("seal_guard",
            () -> new thaumcraft.common.golems.seals.ItemSealPlacer(new Item.Properties().tab(ItemGroup.TAB_MISC), "thaumcraft:guard"));
    public static final RegistryObject<Item> SEAL_HARVEST = RegistryManager.ITEMS.register("seal_harvest",
            () -> new thaumcraft.common.golems.seals.ItemSealPlacer(new Item.Properties().tab(ItemGroup.TAB_MISC), "thaumcraft:harvest"));
    public static final RegistryObject<Item> SEAL_EMPTY = RegistryManager.ITEMS.register("seal_empty",
            () -> new thaumcraft.common.golems.seals.ItemSealPlacer(new Item.Properties().tab(ItemGroup.TAB_MISC), "thaumcraft:empty"));

    // Testing: Fire Bat spawn egg
    public static final RegistryObject<Item> FIRE_BAT_SPAWN_EGG = RegistryManager.ITEMS.register("fire_bat_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FIRE_BAT, 0x5b1d0f, 0xf27a2b, new Item.Properties().tab(ItemGroup.TAB_MISC)));

    // Eldritch Crab spawn egg
    public static final RegistryObject<Item> ELDRITCH_CRAB_SPAWN_EGG = RegistryManager.ITEMS.register("eldritch_crab_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ELDRITCH_CRAB, 0x2a2a2a, 0x6e3020, new Item.Properties().tab(ItemGroup.TAB_MISC)));

    // Thaumcraft Golem spawn egg (for testing)
    public static final RegistryObject<Item> TC_GOLEM_SPAWN_EGG = RegistryManager.ITEMS.register("tc_golem_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TC_GOLEM, 0x7a5a2b, 0xc9b07c, new Item.Properties().tab(ItemGroup.TAB_MISC)));
} 
