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

    static {
        Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
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

            builder.push("golems");
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
        }
    }
}


