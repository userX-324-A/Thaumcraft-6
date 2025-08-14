package thaumcraft.client.golems;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.common.golems.seals.SealEntity;
import thaumcraft.common.golems.seals.SealRegistry;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side mirror of seals near the player for rendering/GUI.
 */
public final class ClientSealCache {
    private ClientSealCache() {}

    private static final Map<Long, ISealEntity> byKey = new ConcurrentHashMap<>();
    // Optional filter cache
    private static final Map<Long, FilterState> filterByKey = new ConcurrentHashMap<>();
    private static final Map<Long, boolean[]> togglesByKey = new ConcurrentHashMap<>();

    private static long posKey(BlockPos pos, Direction face) {
        return (pos.asLong() << 3) | (face.get3DDataValue() & 7L);
    }

    public static void upsert(BlockPos pos, Direction face, String type, byte priority, byte color,
                              boolean locked, boolean redstone, String owner, Long areaOpt) {
        ISeal seal = SealRegistry.create(type);
        if (seal == null) return;
        long key = posKey(pos, face);
        SealEntity ent = (SealEntity) byKey.getOrDefault(key, new SealEntity());
        ent.setSealPos(new SealPos(pos, face));
        ent.setSeal(seal);
        ent.setPriority(priority);
        ent.setColor(color);
        ent.setLocked(locked);
        ent.setRedstoneSensitive(redstone);
        ent.setOwner(owner);
        if (areaOpt != null) {
            ent.setArea(BlockPos.of(areaOpt));
        }
        byKey.put(key, ent);
    }

    public static void updateProps(BlockPos pos, Direction face, byte priority, byte color,
                                   boolean locked, boolean redstone, Long areaOpt) {
        long key = posKey(pos, face);
        ISealEntity existing = byKey.get(key);
        if (existing instanceof SealEntity) {
            SealEntity ent = (SealEntity) existing;
            ent.setPriority(priority);
            ent.setColor(color);
            ent.setLocked(locked);
            ent.setRedstoneSensitive(redstone);
            if (areaOpt != null) ent.setArea(BlockPos.of(areaOpt));
        }
    }

    public static void remove(BlockPos pos, Direction face) {
        long k = posKey(pos, face);
        byKey.remove(k);
        filterByKey.remove(k);
        togglesByKey.remove(k);
    }

    public static ISealEntity get(BlockPos pos, Direction face) {
        return byKey.get(posKey(pos, face));
    }

    public static Collection<ISealEntity> all() { return byKey.values(); }

    // Clear all entries (e.g., on world unload)
    public static void clearAll() {
        byKey.clear();
        filterByKey.clear();
        togglesByKey.clear();
    }

    // Filter helpers (used by ClientSealFilterMessage handler and UI)
    public static void putFilter(BlockPos pos, Direction face, int size,
                                 net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> stacks,
                                 net.minecraft.util.NonNullList<Integer> counts,
                                 boolean blacklist) {
        filterByKey.put(posKey(pos, face), new FilterState(size, stacks, counts, blacklist));
    }

    public static FilterState getFilter(BlockPos pos, Direction face) {
        return filterByKey.get(posKey(pos, face));
    }

    // Toggle helpers
    public static void putToggles(BlockPos pos, Direction face, boolean[] toggles) {
        togglesByKey.put(posKey(pos, face), toggles);
    }
    public static boolean[] getToggles(BlockPos pos, Direction face) {
        return togglesByKey.get(posKey(pos, face));
    }

    public static final class FilterState {
        public final byte size;
        public final net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> stacks;
        public final net.minecraft.util.NonNullList<Integer> counts;
        public final boolean blacklist;
        public FilterState(int size,
                           net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> stacks,
                           net.minecraft.util.NonNullList<Integer> counts,
                           boolean blacklist) {
            this.size = (byte) size;
            this.stacks = stacks;
            this.counts = counts;
            this.blacklist = blacklist;
        }
    }
}



