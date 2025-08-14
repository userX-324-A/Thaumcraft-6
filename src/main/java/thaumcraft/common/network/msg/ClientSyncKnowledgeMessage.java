package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;

import java.util.function.Supplier;

/**
 * Clientbound: replaces the local player's knowledge capability with server copy and shows queued popups.
 */
public class ClientSyncKnowledgeMessage {
    private CompoundNBT data;

    public ClientSyncKnowledgeMessage() {}

    public ClientSyncKnowledgeMessage(IPlayerKnowledge knowledge) {
        this.data = knowledge == null ? new CompoundNBT() : knowledge.serializeNBT();
    }

    public static void encode(ClientSyncKnowledgeMessage msg, PacketBuffer buf) {
        buf.writeNbt(msg.data == null ? new CompoundNBT() : msg.data);
    }

    public static ClientSyncKnowledgeMessage decode(PacketBuffer buf) {
        ClientSyncKnowledgeMessage msg = new ClientSyncKnowledgeMessage();
        msg.data = buf.readNbt();
        if (msg.data == null) msg.data = new CompoundNBT();
        return msg;
    }

    public static void handle(ClientSyncKnowledgeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null) return;
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
            if (knowledge != null) {
                knowledge.deserializeNBT(msg.data);
                // Show toasts next tick for any POPUP flags, then clear POPUP
                Minecraft.getInstance().tell(() -> {
                    for (String key : knowledge.getResearchList()) {
                        if (knowledge.hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP)) {
                            ResearchEntry ri = ResearchCategories.getResearch(key);
                            if (ri != null) {
                                thaumcraft.client.hud.HudHooks.queueResearchToast(ri);
                            }
                            knowledge.clearResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP);
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



