package thaumcraft.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Forge config for Thaumcraft (1.16.5).
 * - skipTheorycraftRegistration: when true, do not register theorycraft cards/aids (default true while porting)
 * - wussMode: when true, suppress warp application from research stages (compat with 1.12 option)
 * - enableResearchWarpGrants: when true, research progression can grant warp (default true)
 */
public final class ModConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    public static final class Common {
        public final ForgeConfigSpec.BooleanValue skipTheorycraftRegistration;
        public final ForgeConfigSpec.BooleanValue wussMode;
        public final ForgeConfigSpec.BooleanValue arcaneEnforceResearchInMatches;
        public final ForgeConfigSpec.BooleanValue enableResearchWarpGrants;
        public final ForgeConfigSpec.ConfigValue<String> defaultGolemMaterial;
        public final ForgeConfigSpec.ConfigValue<String> defaultGolemHead;
        public final ForgeConfigSpec.ConfigValue<String> defaultGolemArms;
        public final ForgeConfigSpec.ConfigValue<String> defaultGolemLegs;
        public final ForgeConfigSpec.ConfigValue<String> defaultGolemAddon;
        public final ForgeConfigSpec.BooleanValue enableSeals;
        // Curios effects
        public final ForgeConfigSpec.DoubleValue curiosityRingBonusChance;
        public final ForgeConfigSpec.IntValue verdantRegenIntervalTicks;
        public final ForgeConfigSpec.IntValue verdantRegenAmount;
        public final ForgeConfigSpec.IntValue voidseerCleanseIntervalTicks;
        public final ForgeConfigSpec.IntValue cloudRingSlowFallDurationTicks;

        // HUD
        public final ForgeConfigSpec.IntValue hudKnowledgeDisplayTicks;
        public final ForgeConfigSpec.IntValue auraSyncCadenceTicks;

        // Worldgen toggles and rates
        public final ForgeConfigSpec.BooleanValue wgEnableCrystals;
        public final ForgeConfigSpec.BooleanValue wgEnableAuraSeed;
        public final ForgeConfigSpec.BooleanValue wgEnableTrees;
        public final ForgeConfigSpec.BooleanValue wgEnablePlants;
        public final ForgeConfigSpec.BooleanValue wgEnableOres;
        public final ForgeConfigSpec.IntValue wgCrystalsPerChunk;
        public final ForgeConfigSpec.IntValue wgAuraSeedsPerChunk;
        public final ForgeConfigSpec.IntValue wgGreatwoodPerChunk;
        public final ForgeConfigSpec.IntValue wgSilverwoodPerChunk;
        public final ForgeConfigSpec.IntValue wgCinderpearlPerChunk;
        public final ForgeConfigSpec.IntValue wgShimmerleafPerChunk;
        public final ForgeConfigSpec.IntValue wgVishroomPerChunk;
        public final ForgeConfigSpec.IntValue wgCinnabarVeinSize;
        public final ForgeConfigSpec.IntValue wgCinnabarVeinsPerChunk;
        public final ForgeConfigSpec.IntValue wgCinnabarMaxY;
        public final ForgeConfigSpec.IntValue wgQuartzVeinSize;
        public final ForgeConfigSpec.IntValue wgQuartzVeinsPerChunk;
        public final ForgeConfigSpec.IntValue wgQuartzMaxY;
        public final ForgeConfigSpec.IntValue wgAmberVeinSize;
        public final ForgeConfigSpec.IntValue wgAmberVeinsPerChunk;
        public final ForgeConfigSpec.IntValue wgAmberMaxY;

        // Essentia transfer rates
        public final ForgeConfigSpec.IntValue tubePullRate;
        public final ForgeConfigSpec.IntValue tubePushRate;
        public final ForgeConfigSpec.IntValue pumpPullRate;
        public final ForgeConfigSpec.IntValue pumpPushRate;
        public final ForgeConfigSpec.IntValue filterTransferRate;

        // Causality Collapser
        public final ForgeConfigSpec.IntValue collapserRadius;
        public final ForgeConfigSpec.DoubleValue collapserFluxDrainTotal;
        public final ForgeConfigSpec.DoubleValue collapserVisDrainTotal;
        public final ForgeConfigSpec.IntValue collapserCooldownSeconds;

        // Resonator
        public final ForgeConfigSpec.IntValue resonatorScanRadius;
        public final ForgeConfigSpec.IntValue resonatorHudCadenceTicks;

        // Sanity Checker
        public final ForgeConfigSpec.BooleanValue sanityOverlayEnabled;
        public final ForgeConfigSpec.IntValue sanityOverlayCadenceTicks;
        public final ForgeConfigSpec.IntValue sanityWhispersThreshold;

        // Hand Mirror
        public final ForgeConfigSpec.DoubleValue mirrorVisCost;
        public final ForgeConfigSpec.IntValue mirrorCooldownSeconds;

        // Grapple Gun
        public final ForgeConfigSpec.DoubleValue grappleMaxLength;
        public final ForgeConfigSpec.DoubleValue grappleReelSpeedPerTick;
        public final ForgeConfigSpec.BooleanValue grappleMitigateFallDamage;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("theorycraft");
            skipTheorycraftRegistration = builder
                .comment("If true, skip registering theorycraft cards and aids (temporary during 1.16.5 port)")
                .define("skipTheorycraftRegistration", true);
            builder.pop();

            builder.push("misc");
            wussMode = builder
                .comment("If true, disables warp application from research stages (legacy option)")
                .define("wussMode", false);
            enableResearchWarpGrants = builder
                .comment("If true, research progression can grant warp as defined by stages")
                .define("enableResearchWarpGrants", true);
            arcaneEnforceResearchInMatches = builder
                .comment("If true, Arcane recipes will enforce research in matches() to hide ghost results (optional; server safety remains in container)")
                .define("arcaneEnforceResearchInMatches", false);
            builder.pop();

            builder.push("items.causality_collapser");
            collapserRadius = builder
                .comment("Radius in blocks over which vis/flux drain is distributed")
                .defineInRange("radius", 12, 1, 128);
            collapserFluxDrainTotal = builder
                .comment("Total flux to drain across the area")
                .defineInRange("fluxDrainTotal", 120.0, 0.0, 10000.0);
            collapserVisDrainTotal = builder
                .comment("Total vis to drain across the area")
                .defineInRange("visDrainTotal", 60.0, 0.0, 10000.0);
            collapserCooldownSeconds = builder
                .comment("Cooldown in seconds after activation")
                .defineInRange("cooldownSeconds", 30, 0, 600);
            builder.pop();

            builder.push("items.resonator");
            resonatorScanRadius = builder
                .comment("Search radius for nearest essentia-capable block entity")
                .defineInRange("scanRadius", 8, 1, 64);
            resonatorHudCadenceTicks = builder
                .comment("Client HUD update cadence in ticks while held/selected")
                .defineInRange("hudCadenceTicks", 10, 1, 200);
            builder.pop();

            builder.push("items.sanity_checker");
            sanityOverlayEnabled = builder
                .comment("If false, disables the sanity overlay updates while held")
                .define("overlayEnabled", true);
            sanityOverlayCadenceTicks = builder
                .comment("Ticks between overlay refresh while held and selected")
                .defineInRange("overlayCadenceTicks", 40, 1, 400);
            sanityWhispersThreshold = builder
                .comment("When total warp >= threshold, play whispers audio cue on refresh")
                .defineInRange("whispersThreshold", 20, 0, 1000);
            builder.pop();

            builder.push("items.hand_mirror");
            mirrorVisCost = builder
                .comment("Vis cost to teleport when using a linked hand mirror")
                .defineInRange("visCost", 50.0, 0.0, 10000.0);
            mirrorCooldownSeconds = builder
                .comment("Cooldown in seconds after teleport")
                .defineInRange("cooldownSeconds", 5, 0, 600);
            builder.pop();

            builder.push("items.grapple_gun");
            grappleMaxLength = builder
                .comment("Maximum rope length in blocks when attached")
                .defineInRange("maxLength", 20.0, 2.0, 128.0);
            grappleReelSpeedPerTick = builder
                .comment("Speed in blocks per tick to adjust effective rope length while crouching (reel in) or jumping (reel out)")
                .defineInRange("reelSpeedPerTick", 0.06, 0.0, 1.0);
            grappleMitigateFallDamage = builder
                .comment("If true, reduces or cancels fall damage when tensioned on the rope")
                .define("mitigateFallDamage", true);
            builder.pop();

            builder.push("golems");
            enableSeals = builder
                .comment("If false, disables registration and recipes for seals.")
                .define("enableSeals", true);
            defaultGolemMaterial = builder
                .comment("Default material id for new golems (resource location)")
                .define("defaultGolemMaterial", "thaumcraft:material/wood");
            defaultGolemHead = builder
                .comment("Default head id for new golems")
                .define("defaultGolemHead", "thaumcraft:head/basic");
            defaultGolemArms = builder
                .comment("Default arms id for new golems")
                .define("defaultGolemArms", "thaumcraft:arms/basic");
            defaultGolemLegs = builder
                .comment("Default legs id for new golems")
                .define("defaultGolemLegs", "thaumcraft:legs/walker");
            defaultGolemAddon = builder
                .comment("Default addon id for new golems")
                .define("defaultGolemAddon", "thaumcraft:addon/none");
            builder.pop();

            // Curios effects tuning
            builder.push("items.curios");
            curiosityRingBonusChance = builder
                .comment("Chance [0..1] per knowledge grant to gain +1 extra when Ring of Curiosity equipped")
                .defineInRange("curiosityRingBonusChance", 0.20, 0.0, 1.0);
            verdantRegenIntervalTicks = builder
                .comment("Tick interval for Verdant Charm passive heal while below full health")
                .defineInRange("verdantRegenIntervalTicks", 80, 1, 1200);
            verdantRegenAmount = builder
                .comment("Amount of health restored by Verdant Charm per interval")
                .defineInRange("verdantRegenAmount", 1, 1, 20);
            voidseerCleanseIntervalTicks = builder
                .comment("Tick interval for Voidseer Charm to cleanse minor negative effects")
                .defineInRange("voidseerCleanseIntervalTicks", 200, 1, 12000);
            cloudRingSlowFallDurationTicks = builder
                .comment("Duration in ticks of Slow Falling effect while Ring of Clouds detects falling")
                .defineInRange("cloudRingSlowFallDurationTicks", 10, 1, 200);
            builder.pop();

            builder.push("hud");
            hudKnowledgeDisplayTicks = builder
                .comment("Total ticks to display knowledge gain HUD text")
                .defineInRange("knowledgeDisplayTicks", 60, 10, 400);
            auraSyncCadenceTicks = builder
                .comment("Server cadence in ticks to sync aura values to clients (HUD)")
                .defineInRange("auraSyncCadenceTicks", 20, 1, 1200);
            builder.pop();

            builder.push("worldgen");
            wgEnableCrystals = builder.define("enableCrystals", true);
            wgEnableAuraSeed = builder.define("enableAuraSeed", true);
            wgEnableTrees = builder.define("enableTrees", true);
            wgEnablePlants = builder.define("enablePlants", true);
            wgEnableOres = builder.define("enableOres", true);
            wgCrystalsPerChunk = builder.defineInRange("crystalsPerChunk", 2, 0, 64);
            wgAuraSeedsPerChunk = builder.defineInRange("auraSeedsPerChunk", 1, 0, 64);
            wgGreatwoodPerChunk = builder.defineInRange("greatwoodPerChunk", 1, 0, 32);
            wgSilverwoodPerChunk = builder.defineInRange("silverwoodPerChunk", 1, 0, 32);
            wgCinderpearlPerChunk = builder.defineInRange("cinderpearlPerChunk", 2, 0, 64);
            wgShimmerleafPerChunk = builder.defineInRange("shimmerleafPerChunk", 1, 0, 64);
            wgVishroomPerChunk = builder.defineInRange("vishroomPerChunk", 1, 0, 64);
            wgCinnabarVeinSize = builder.defineInRange("cinnabar.veinSize", 6, 1, 64);
            wgCinnabarVeinsPerChunk = builder.defineInRange("cinnabar.veinsPerChunk", 8, 0, 64);
            wgCinnabarMaxY = builder.defineInRange("cinnabar.maxY", 32, 1, 255);
            wgQuartzVeinSize = builder.defineInRange("quartz.veinSize", 8, 1, 64);
            wgQuartzVeinsPerChunk = builder.defineInRange("quartz.veinsPerChunk", 8, 0, 64);
            wgQuartzMaxY = builder.defineInRange("quartz.maxY", 48, 1, 255);
            wgAmberVeinSize = builder.defineInRange("amber.veinSize", 6, 1, 64);
            wgAmberVeinsPerChunk = builder.defineInRange("amber.veinsPerChunk", 3, 0, 64);
            wgAmberMaxY = builder.defineInRange("amber.maxY", 80, 1, 255);
            builder.pop();

            builder.push("essentia");
            tubePullRate = builder
                .comment("Essentia units a tube pulls per neighbor check")
                .defineInRange("tubePullRate", 1, 0, 64);
            tubePushRate = builder
                .comment("Essentia units a tube attempts to push per tick")
                .defineInRange("tubePushRate", 2, 0, 64);
            pumpPullRate = builder
                .comment("Essentia units a pump pulls per tick from source")
                .defineInRange("pumpPullRate", 2, 0, 64);
            pumpPushRate = builder
                .comment("Essentia units a pump pushes per tick to destination")
                .defineInRange("pumpPushRate", 4, 0, 64);
            filterTransferRate = builder
                .comment("Essentia units per operation the filter allows when aspect matches")
                .defineInRange("filterTransferRate", 10, 0, 64);
            builder.pop();
        }
    }

    public static final class Client {
        public final ForgeConfigSpec.BooleanValue enableHudOverlays;
        public final ForgeConfigSpec.BooleanValue enableParticles;
        public final ForgeConfigSpec.BooleanValue showKnowledgeToasts;
        public final ForgeConfigSpec.BooleanValue showSanityOverlay;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("client.hud");
            enableHudOverlays = builder
                .comment("Master toggle for client HUD overlays rendered by the mod")
                .define("enableHudOverlays", true);
            showKnowledgeToasts = builder
                .comment("Show knowledge gain overlay text")
                .define("showKnowledgeToasts", true);
            showSanityOverlay = builder
                .comment("Allow the sanity checker overlay text when using the item")
                .define("showSanityOverlay", true);
            builder.pop();

            builder.push("client.particles");
            enableParticles = builder
                .comment("Master toggle for Thaumcraft particle effects")
                .define("enableParticles", true);
            builder.pop();
        }
    }
}


