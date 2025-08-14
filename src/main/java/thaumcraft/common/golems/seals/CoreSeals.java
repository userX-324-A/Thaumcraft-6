package thaumcraft.common.golems.seals;

/**
 * Minimal placeholder seals to unblock engine and persistence wiring.
 */
public final class CoreSeals {
    private CoreSeals() {}

    public static void register() {
        SealRegistry.register(new SealFetch());
        SealRegistry.register(new SealGuard());
        SealRegistry.register(new SealHarvest());
        SealRegistry.register(new SealEmpty());
    }
}



