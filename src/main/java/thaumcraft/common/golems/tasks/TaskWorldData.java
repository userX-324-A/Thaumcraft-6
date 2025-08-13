package thaumcraft.common.golems.tasks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import thaumcraft.api.golems.tasks.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Per-world storage for golem tasks (pending and reserved).
 */
public class TaskWorldData extends WorldSavedData {
    private static final String DATA_NAME = "thaumcraft_tasks";

    private final Map<Integer, Task> tasks = new HashMap<>();

    public TaskWorldData() { super(DATA_NAME); }

    public static TaskWorldData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(TaskWorldData::new, DATA_NAME);
    }

    public void upsert(Task t) {
        tasks.put(t.getId(), t);
        setDirty();
    }

    public Task get(int id) { return tasks.get(id); }

    public void remove(int id) {
        tasks.remove(id);
        setDirty();
    }

    public Collection<Task> all() { return tasks.values(); }

    @Override
    public void load(CompoundNBT nbt) {
        tasks.clear();
        ListNBT list = nbt.getList("list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT tnbt = list.getCompound(i);
            Task t = Task.deserializeNBT(tnbt);
            tasks.put(t.getId(), t);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (Task t : tasks.values()) {
            list.add(t.serializeNBT());
        }
        nbt.put("list", list);
        return nbt;
    }
}


