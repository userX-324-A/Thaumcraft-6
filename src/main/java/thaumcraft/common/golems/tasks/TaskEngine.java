package thaumcraft.common.golems.tasks;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.api.golems.tasks.Task;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

/**
 * Per-world task queues with simple enqueue/poll. Persistence and assignment TBD.
 */
public final class TaskEngine {
    private static final Map<RegistryKey<World>, Queue<Task>> queuesByWorld = new HashMap<>();

    private TaskEngine() {}

    private static Queue<Task> queue(RegistryKey<World> key) {
        return queuesByWorld.computeIfAbsent(key, k -> new ArrayDeque<>());
    }

    public static void enqueue(Task task) {
        if (task == null) return;
        RegistryKey<World> key = task.getDimensionKey();
        if (key == null) return; // require dimension context going forward
        queue(key).offer(task);
    }

    public static Task poll(RegistryKey<World> key) {
        return queue(key).poll();
    }

    public static int size(RegistryKey<World> key) { return queue(key).size(); }

    public static void serverTick(ServerWorld world) {
        // Persist queued tasks periodically
        TaskWorldData twd = TaskWorldData.get(world);
        Queue<Task> q = queue(world.dimension());
        for (Task t : q) twd.upsert(t);

        // Cleanup expired reservations
        long now = System.currentTimeMillis();
        for (Task t : twd.all()) {
            if (t.isReserved() && (now - t.getReservationTimestampMs()) > 20_000L) { // 20s timeout
                t.setReserved(false);
                t.setGolemUUID(null);
                twd.upsert(t);
            }
        }
    }

    public static Task reserveNext(ServerWorld world, UUID golemUuid) {
        Queue<Task> q = queue(world.dimension());
        Task t = q.poll();
        if (t == null) return null;
        t.setReserved(true);
        t.setGolemUUID(golemUuid);
        TaskWorldData.get(world).upsert(t);
        return t;
    }

    public static void complete(ServerWorld world, Task task) {
        if (task == null) return;
        TaskWorldData.get(world).remove(task.getId());
    }
}


