package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

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
        buf.writeCompoundTag(msg.data == null ? new CompoundNBT() : msg.data);
    }

    public static ClientSyncKnowledgeMessage decode(PacketBuffer buf) {
        ClientSyncKnowledgeMessage msg = new ClientSyncKnowledgeMessage();
        msg.data = buf.readCompoundTag();
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
                // Note: toast/popups logic should be invoked elsewhere on the next tick based on flags if needed
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


