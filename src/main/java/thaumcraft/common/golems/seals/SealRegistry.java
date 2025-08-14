package thaumcraft.common.golems.seals;

import thaumcraft.api.golems.seals.ISeal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple, in-memory registry for golem seals.
 * Mirrors the API hooks exposed via GolemHelper.registerSeal/getSeal.
 */
public final class SealRegistry {
    private static final Map<String, ISeal> KEY_TO_PROTOTYPE = new HashMap<>();

    private SealRegistry() {}

    public static void register(ISeal seal) {
        if (seal == null) return;
        KEY_TO_PROTOTYPE.put(seal.getKey(), seal);
    }

    public static ISeal get(String key) {
        return KEY_TO_PROTOTYPE.get(key);
    }

    /**
     * Create a new instance of the seal class registered under the given key.
     * Implementations are expected to have a public no-arg constructor.
     */
    public static ISeal create(String key) {
        ISeal proto = get(key);
        if (proto == null) return null;
        try {
            return proto.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static Map<String, ISeal> all() {
        return Collections.unmodifiableMap(KEY_TO_PROTOTYPE);
    }
}



