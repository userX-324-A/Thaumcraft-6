package thaumcraft.common.golems.seals;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Per-dimension persistence for placed seals. Stores serialized seal entities keyed by composite key.
 */
public class SealWorldData extends WorldSavedData {
    private static final String DATA_NAME = "thaumcraft_seals";

    private final Map<Long, ISealEntity> byKey = new HashMap<>();

    public SealWorldData() {
        super(DATA_NAME);
    }

    public static SealWorldData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(SealWorldData::new, DATA_NAME);
    }

    public static long posKey(BlockPos pos, Direction face) {
        // Mix BlockPos long with face id in lower 3 bits
        return (pos.asLong() << 3) | (face.get3DDataValue() & 7L);
    }

    public ISealEntity get(BlockPos pos, Direction face) { return byKey.get(posKey(pos, face)); }

    public void put(ISealEntity entity) {
        SealPos sp = entity.getSealPos();
        byKey.put(posKey(sp.pos, sp.face), entity);
        setDirty();
    }

    public void remove(BlockPos pos, Direction face) {
        byKey.remove(posKey(pos, face));
        setDirty();
    }

    public Iterable<ISealEntity> all() { return byKey.values(); }

    public boolean contains(BlockPos pos, Direction face) {
        return byKey.containsKey(posKey(pos, face));
    }

    public ISealEntity get(SealPos pos) { return byKey.get(posKey(pos.pos, pos.face)); }

    @Override
    public void load(CompoundNBT nbt) {
        byKey.clear();
        ListNBT list = nbt.getList("list", 10); // CompoundNBT tag id
        for (INBT inbt : list) {
            CompoundNBT ce = (CompoundNBT) inbt;
            String type = ce.getString("type");
            ISeal seal = SealRegistry.create(type);
            if (seal == null) continue;
            SealEntity entity = new SealEntity();
            entity.setSeal(seal);
            entity.readNBT(ce);
            SealPos sp = entity.getSealPos();
            if (sp != null) {
                // Optional: validate placement on load to avoid invalid seals persisting
                // This check will be re-validated during server ticks as well.
                byKey.put(posKey(sp.pos, sp.face), entity);
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (ISealEntity ent : new ArrayList<>(byKey.values())) {
            CompoundNBT ce = ent.writeNBT();
            if (ce != null) list.add(ce);
        }
        nbt.put("list", list);
        return nbt;
    }
}


