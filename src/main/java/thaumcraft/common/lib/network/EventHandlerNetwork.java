package thaumcraft.common.lib.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
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
    }
}

