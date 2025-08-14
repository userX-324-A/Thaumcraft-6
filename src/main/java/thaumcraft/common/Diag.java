package thaumcraft.common;

/**
 * Diagnostics feature flags that can be flipped via config or JVM system properties.
 * System properties take precedence to simplify bisecting via command-line.
 *
 * -Dtc.diag.disableResearchReload=true
 * -Dtc.diag.disableLegacyResearchParse=true
 * -Dtc.diag.disableAuraEvents=true
 * -Dtc.diag.disableSealEngine=true
 * -Dtc.diag.disableAll=true (short-circuit most mod init; for last-resort isolation)
 */
public final class Diag {
    private Diag() {}

    private static boolean sys(String key) {
        try {
            return Boolean.getBoolean(key);
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean disableAll() {
        return sys("tc.diag.disableAll");
    }

    public static boolean disableResearchReload() {
        return thaumcraft.common.config.ModConfig.COMMON.diagDisableResearchReload.get() || sys("tc.diag.disableResearchReload");
    }

    public static boolean disableLegacyResearchParse() {
        return thaumcraft.common.config.ModConfig.COMMON.diagDisableLegacyResearchParse.get() || sys("tc.diag.disableLegacyResearchParse");
    }

    public static boolean disableAuraEvents() {
        return thaumcraft.common.config.ModConfig.COMMON.diagDisableAuraEvents.get() || sys("tc.diag.disableAuraEvents");
    }

    public static boolean disableSealEngine() {
        return thaumcraft.common.config.ModConfig.COMMON.diagDisableSealEngine.get() || sys("tc.diag.disableSealEngine");
    }
}



