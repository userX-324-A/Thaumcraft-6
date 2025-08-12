package thaumcraft.common.lib.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.common.network.msg.ClientSyncWarpMessage;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.ClientSyncKnowledgeMessage;

@Mod.EventBusSubscriber
public class EventHandlerNetwork {
    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) event.getPlayer();
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(sp);
        if (knowledge != null) {
            NetworkHandler.sendTo(sp, new ClientSyncKnowledgeMessage(knowledge));
        }
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(sp);
        if (warp != null) {
            NetworkHandler.sendTo(sp, new ClientSyncWarpMessage(warp.serializeNBT()));
        }
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) event.getPlayer();
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(sp);
        if (knowledge != null) {
            NetworkHandler.sendTo(sp, new ClientSyncKnowledgeMessage(knowledge));
        }
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(sp);
        if (warp != null) {
            NetworkHandler.sendTo(sp, new ClientSyncWarpMessage(warp.serializeNBT()));
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) event.getPlayer();
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(sp);
        if (knowledge != null) {
            NetworkHandler.sendTo(sp, new ClientSyncKnowledgeMessage(knowledge));
        }
    }

    // Flush queued knowledge syncs about once per second to avoid spam
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        // Simple 20-tick cadence using server tick count
        int tick = (int)(server.overworld().getGameTime() % 20);
        if (tick != 0) return;
        if (thaumcraft.common.lib.research.ResearchManager.syncList.isEmpty()) return;
        server.getPlayerList().getPlayers().forEach(p -> {
            String name = p.getName().getString();
            if (thaumcraft.common.lib.research.ResearchManager.syncList.remove(name) != null) {
                IPlayerKnowledge k = ThaumcraftCapabilities.getKnowledge(p);
                if (k != null) NetworkHandler.sendTo(p, new ClientSyncKnowledgeMessage(k));
            }
        });
    }
}

