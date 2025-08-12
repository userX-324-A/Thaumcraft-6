package thaumcraft.common.golems;

import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.parts.GolemAddon;
import thaumcraft.api.golems.parts.GolemArm;
import thaumcraft.api.golems.parts.GolemHead;
import thaumcraft.api.golems.parts.GolemLeg;
import thaumcraft.api.golems.parts.GolemMaterial;

import java.util.EnumSet;

/**
 * Registers a minimal set of golem parts (Phase 1) to unblock testing.
 */
public final class GolemBootstrap {
    private GolemBootstrap() {}

    public static void registerDefaults() {
        // Materials (ported from legacy)
        GolemProperties.registerMaterial(id("material/wood"), new GolemMaterial("wood", new String[]{"MATSTUDWOOD"}, 5059370, 6, 2, 1), EnumSet.of(EnumGolemTrait.LIGHT));
        GolemProperties.registerMaterial(id("material/iron"), new GolemMaterial("iron", new String[]{"MATSTUDIRON"}, 16777215, 20, 8, 3), EnumSet.of(EnumGolemTrait.HEAVY, EnumGolemTrait.FIREPROOF, EnumGolemTrait.BLASTPROOF));
        GolemProperties.registerMaterial(id("material/clay"), new GolemMaterial("clay", new String[]{"MATSTUDCLAY"}, 13071447, 10, 4, 2), EnumSet.of(EnumGolemTrait.FIREPROOF));
        GolemProperties.registerMaterial(id("material/brass"), new GolemMaterial("brass", new String[]{"MATSTUDBRASS"}, 15638812, 16, 6, 3), EnumSet.of(EnumGolemTrait.LIGHT));
        GolemProperties.registerMaterial(id("material/thaumium"), new GolemMaterial("thaumium", new String[]{"MATSTUDTHAUMIUM"}, 5257074, 24, 10, 4), EnumSet.of(EnumGolemTrait.HEAVY, EnumGolemTrait.FIREPROOF, EnumGolemTrait.BLASTPROOF));
        GolemProperties.registerMaterial(id("material/void"), new GolemMaterial("void", new String[]{"MATSTUDVOID"}, 1445161, 20, 6, 4), EnumSet.of(EnumGolemTrait.REPAIR));

        // Heads
        GolemProperties.registerHead(id("head/basic"), new GolemHead((byte)1, "basic", new String[]{"MINDCLOCKWORK"}, null), EnumSet.noneOf(EnumGolemTrait.class));
        GolemProperties.registerHead(id("head/smart"), new GolemHead((byte)2, "smart", new String[]{"MINDBIOTHAUMIC"}, null), EnumSet.of(EnumGolemTrait.SMART, EnumGolemTrait.FRAGILE));
        GolemProperties.registerHead(id("head/smart_armored"), new GolemHead((byte)3, "smart_armored", new String[]{"MINDBIOTHAUMIC", "GOLEMCOMBATADV"}, null), EnumSet.of(EnumGolemTrait.SMART));
        GolemProperties.registerHead(id("head/scout"), new GolemHead((byte)4, "scout", new String[]{"GOLEMVISION"}, null), EnumSet.of(EnumGolemTrait.SCOUT, EnumGolemTrait.FRAGILE));
        GolemProperties.registerHead(id("head/smart_scout"), new GolemHead((byte)5, "smart_scout", new String[]{"GOLEMVISION", "MINDBIOTHAUMIC"}, null), EnumSet.of(EnumGolemTrait.SCOUT, EnumGolemTrait.SMART, EnumGolemTrait.FRAGILE));

        // Arms
        GolemProperties.registerArms(id("arms/basic"), new GolemArm((byte)1, "basic", new String[]{"MINDCLOCKWORK"}, null), EnumSet.noneOf(EnumGolemTrait.class));
        GolemProperties.registerArms(id("arms/fine"), new GolemArm((byte)2, "fine", new String[]{"MATSTUDBRASS"}, null), EnumSet.of(EnumGolemTrait.DEFT, EnumGolemTrait.FRAGILE));
        GolemProperties.registerArms(id("arms/claws"), new GolemArm((byte)3, "claws", new String[]{"GOLEMCOMBATADV"}, null), EnumSet.of(EnumGolemTrait.FIGHTER, EnumGolemTrait.CLUMSY, EnumGolemTrait.BRUTAL));
        GolemProperties.registerArms(id("arms/breakers"), new GolemArm((byte)4, "breakers", new String[]{"GOLEMBREAKER"}, null), EnumSet.of(EnumGolemTrait.BREAKER, EnumGolemTrait.CLUMSY, EnumGolemTrait.BRUTAL));
        GolemProperties.registerArms(id("arms/darts"), new GolemArm((byte)5, "darts", new String[]{"GOLEMCOMBATADV"}, null), EnumSet.of(EnumGolemTrait.FIGHTER, EnumGolemTrait.CLUMSY, EnumGolemTrait.RANGED, EnumGolemTrait.FRAGILE));

        // Legs
        GolemProperties.registerLegs(id("legs/walker"), new GolemLeg((byte)1, "walker", new String[]{"MINDCLOCKWORK"}, null), EnumSet.noneOf(EnumGolemTrait.class));
        GolemProperties.registerLegs(id("legs/roller"), new GolemLeg((byte)2, "roller", new String[]{"MINDCLOCKWORK"}, null), EnumSet.of(EnumGolemTrait.WHEELED));
        GolemProperties.registerLegs(id("legs/climber"), new GolemLeg((byte)3, "climber", new String[]{"GOLEMCLIMBER"}, null), EnumSet.of(EnumGolemTrait.CLIMBER));
        GolemProperties.registerLegs(id("legs/flyer"), new GolemLeg((byte)4, "flyer", new String[]{"GOLEMFLYER"}, null), EnumSet.of(EnumGolemTrait.FLYER, EnumGolemTrait.FRAGILE));

        // Addons
        GolemProperties.registerAddon(id("addon/none"), new GolemAddon((byte)0, "none", new String[]{"MINDCLOCKWORK"}, null), EnumSet.noneOf(EnumGolemTrait.class));
        GolemProperties.registerAddon(id("addon/armored"), new GolemAddon((byte)1, "armored", new String[]{"GOLEMCOMBATADV"}, null), EnumSet.of(EnumGolemTrait.ARMORED, EnumGolemTrait.HEAVY));
        GolemProperties.registerAddon(id("addon/fighter"), new GolemAddon((byte)2, "fighter", new String[]{"SEALGUARD"}, null), EnumSet.of(EnumGolemTrait.FIGHTER));
        GolemProperties.registerAddon(id("addon/hauler"), new GolemAddon((byte)3, "hauler", new String[]{"MINDCLOCKWORK"}, null), EnumSet.of(EnumGolemTrait.HAULER));
    }

    private static ResourceLocation id(String path) { return new ResourceLocation(Thaumcraft.MODID, path); }
}


