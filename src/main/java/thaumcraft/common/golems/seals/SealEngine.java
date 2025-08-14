package thaumcraft.common.golems.seals;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.seals.ISealEntity;

/**
 * Server tick hook that advances all active seals per world.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class SealEngine {
    private SealEngine() {}

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (thaumcraft.common.Diag.disableAll() || thaumcraft.common.Diag.disableSealEngine()) return;
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) return;
        if (!(event.world instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) event.world;
        // Avoid early ticks while server is not fully started to prevent chunk loads during bootstrap
        if (!world.getServer().isRunning()) return;
        SealWorldData data = SealWorldData.get(world);
        data.all().forEach(se -> {
            // Only tick if the seal's chunk is loaded and placement is still valid
            if (se.getSealPos() == null) return;
            BlockPos pos = se.getSealPos().pos;
            if (!world.isLoaded(pos)) return;
            if (world.getGameTime() % 20L == 0L && se.getSeal() != null) {
                if (!se.getSeal().canPlaceAt(world, pos, se.getSealPos().face)) {
                    // Auto-remove invalid seals
                    ISealEntity ent = data.get(pos, se.getSealPos().face);
                    if (ent != null && ent.getSeal() != null) {
                        ent.getSeal().onRemoval(world, pos, se.getSealPos().face);
                    }
                    data.remove(pos, se.getSealPos().face);
                    thaumcraft.common.network.NetworkHandler.sendToAllAround(
                            new thaumcraft.common.network.msg.ClientSealMessage(pos, se.getSealPos().face, "REMOVE", (byte)0, (byte)0, false, false, ""),
                            world,
                            pos,
                            64);
                    return;
                }
            }
            se.tickSealEntity(world);
        });
        // Advance the minimal task engine (no-op for now)
        thaumcraft.common.golems.tasks.TaskEngine.serverTick(world);
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (thaumcraft.common.Diag.disableAll() || thaumcraft.common.Diag.disableSealEngine()) return;
        if (!(event.getWorld() instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) event.getWorld();
        BlockPos pos = event.getPos();
        // Try all 6 faces for attached seals
        for (Direction face : Direction.values()) {
            ISealEntity ent = SealWorldData.get(world).get(pos, face);
            if (ent != null) {
                // Notify seal implementation and remove
                if (ent.getSeal() != null) {
                    ent.getSeal().onRemoval(world, pos, face);
                }
                SealWorldData.get(world).remove(pos, face);
                // Broadcast removal to clients
                thaumcraft.common.network.NetworkHandler.sendToAllAround(
                        new thaumcraft.common.network.msg.ClientSealMessage(pos, face, "REMOVE", (byte)0, (byte)0, false, false, ""),
                        world,
                        pos,
                        64);
            }
        }
    }
}


