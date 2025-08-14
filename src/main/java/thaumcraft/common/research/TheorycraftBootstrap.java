package thaumcraft.common.research;

import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.common.config.ModConfig;

/**
 * Temporary bootstrap to optionally skip registering theorycraft cards/aids during the port.
 * Keep this minimal; actual registration will be restored as systems are ported.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TheorycraftBootstrap {
    private TheorycraftBootstrap() {}

    /**
     * Invoke during common setup enqueueWork if/when we want to register cards/aids.
     * With skip flag true (default while porting), this method becomes a no-op.
     */
    public static void maybeRegister() {
        if (ModConfig.COMMON.skipTheorycraftRegistration.get()) {
            Thaumcraft.LOGGER.info("Theorycraft registration skipped by config");
            return;
        }
        // Registration will be enabled once theorycraft package is fully ported
    }
}



