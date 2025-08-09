package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;

import java.util.function.Supplier;

/**
 * Clientbound: notifies the client that knowledge has been gained.
 * The visual/HUD feedback will be hooked up in the client system later in the port.
 */
public class ClientKnowledgeGainMessage {
    private byte typeOrdinal;
    private String categoryKey; // may be null/empty when type has no fields

    public ClientKnowledgeGainMessage() {}

    public ClientKnowledgeGainMessage(byte typeOrdinal, String categoryKey) {
        this.typeOrdinal = typeOrdinal;
        this.categoryKey = categoryKey == null ? "" : categoryKey;
    }

    public static void encode(ClientKnowledgeGainMessage msg, PacketBuffer buf) {
        buf.writeByte(msg.typeOrdinal);
        buf.writeBoolean(msg.categoryKey != null && !msg.categoryKey.isEmpty());
        if (msg.categoryKey != null && !msg.categoryKey.isEmpty()) {
            buf.writeString(msg.categoryKey);
        }
    }

    public static ClientKnowledgeGainMessage decode(PacketBuffer buf) {
        ClientKnowledgeGainMessage msg = new ClientKnowledgeGainMessage();
        msg.typeOrdinal = buf.readByte();
        boolean hasCat = buf.readBoolean();
        msg.categoryKey = hasCat ? buf.readString(64) : "";
        return msg;
    }

    public static void handle(ClientKnowledgeGainMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Placeholder hook for future HUD/sound feedback (RenderEventHandler/HudHandler port pending)
            // We resolve the types now to validate data during development.
            IPlayerKnowledge.EnumKnowledgeType type = IPlayerKnowledge.EnumKnowledgeType.values()[msg.typeOrdinal];
            ResearchCategory category = (msg.categoryKey == null || msg.categoryKey.isEmpty())
                    ? null
                    : ResearchCategories.getResearchCategory(msg.categoryKey);
            // No-op for now. Future: enqueue HUD tracker and sound here.
            // Guard read of client player to avoid classloading issues when running server-only.
            Minecraft.getInstance();
        });
        ctx.get().setPacketHandled(true);
    }
}


