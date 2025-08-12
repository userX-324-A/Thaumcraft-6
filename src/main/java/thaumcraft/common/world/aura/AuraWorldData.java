package thaumcraft.common.world.aura;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-dimension saved aura/flux values keyed by chunk.
 * Stores three maps: vis, flux, and base (baseline vis for HUD/logic).
 */
public class AuraWorldData extends WorldSavedData {

    private static final String DATA_NAME = "thaumcraft_aura";

    private final Map<Long, Float> visByChunk = new HashMap<>();
    private final Map<Long, Float> fluxByChunk = new HashMap<>();
    private final Map<Long, Float> baseByChunk = new HashMap<>();

    public AuraWorldData() { super(DATA_NAME); }

    @Override
    public void load(CompoundNBT nbt) {
        readMap(nbt, "vis", visByChunk);
        readMap(nbt, "flux", fluxByChunk);
        readMap(nbt, "base", baseByChunk);
    }

    private void readMap(CompoundNBT nbt, String key, Map<Long, Float> target) {
        ListNBT list = nbt.getList(key, 10); // 10: CompoundNBT
        for (INBT inbt : list) {
            CompoundNBT e = (CompoundNBT) inbt;
            long k = e.getLong("k");
            float v = e.getFloat("v");
            target.put(k, v);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        writeMap(nbt, "vis", visByChunk);
        writeMap(nbt, "flux", fluxByChunk);
        writeMap(nbt, "base", baseByChunk);
        return nbt;
    }

    private void writeMap(CompoundNBT nbt, String key, Map<Long, Float> source) {
        ListNBT list = new ListNBT();
        for (Map.Entry<Long, Float> e : source.entrySet()) {
            CompoundNBT ce = new CompoundNBT();
            ce.putLong("k", e.getKey());
            ce.putFloat("v", e.getValue());
            list.add(ce);
        }
        nbt.put(key, list);
    }

    public static AuraWorldData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(AuraWorldData::new, DATA_NAME);
    }

    public float getVis(BlockPos pos) {
        long key = keyFor(pos);
        return visByChunk.getOrDefault(key, getBase(pos));
    }

    public float getFlux(BlockPos pos) {
        long key = keyFor(pos);
        return fluxByChunk.getOrDefault(key, 0f);
    }

    public float getBase(BlockPos pos) {
        long key = keyFor(pos);
        return baseByChunk.getOrDefault(key, 100f);
    }

    public void seedBaseline(BlockPos pos, float baseline) {
        long key = keyFor(pos);
        // Set base only if not present; vis snaps to at least baseline if first time
        if (!baseByChunk.containsKey(key)) {
            baseByChunk.put(key, baseline);
            visByChunk.putIfAbsent(key, baseline);
            setDirty();
        }
    }

    public void addVis(BlockPos pos, float amount) {
        long key = keyFor(pos);
        float current = getVis(pos);
        visByChunk.put(key, current + amount);
        setDirty();
    }

    public void addFlux(BlockPos pos, float amount) {
        long key = keyFor(pos);
        float current = getFlux(pos);
        fluxByChunk.put(key, current + amount);
        setDirty();
    }

    public boolean drainVis(BlockPos pos, float amount, boolean simulate) {
        float current = getVis(pos);
        if (current < amount) return false;
        if (!simulate) {
            addVis(pos, -amount);
        }
        return true;
    }

    public float drainVis(BlockPos pos, float amount) {
        float current = getVis(pos);
        float taken = Math.min(current, amount);
        addVis(pos, -taken);
        return taken;
    }

    public boolean drainFlux(BlockPos pos, float amount, boolean simulate) {
        float current = getFlux(pos);
        if (current < amount) return false;
        if (!simulate) {
            addFlux(pos, -amount);
        }
        return true;
    }

    public float drainFlux(BlockPos pos, float amount) {
        float current = getFlux(pos);
        float taken = Math.min(current, amount);
        addFlux(pos, -taken);
        return taken;
    }

    private static long keyFor(BlockPos pos) {
        return new ChunkPos(pos).toLong();
    }
}


