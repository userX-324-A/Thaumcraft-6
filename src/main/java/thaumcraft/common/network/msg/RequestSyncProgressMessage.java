package thaumcraft.common.network.msg;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.function.Supplier;

/**
 * Serverbound: progress a research key. Replaces PacketSyncProgressToServer.
 */
public class RequestSyncProgressMessage {
    private String key;
    private boolean first;
    private boolean checks;
    private boolean noFlags;

    public RequestSyncProgressMessage() {}

    public RequestSyncProgressMessage(String key, boolean first, boolean checks, boolean noFlags) {
        this.key = key;
        this.first = first;
        this.checks = checks;
        this.noFlags = noFlags;
    }

    public static void encode(RequestSyncProgressMessage msg, PacketBuffer buf) {
        buf.writeUtf(msg.key);
        buf.writeBoolean(msg.first);
        buf.writeBoolean(msg.checks);
        buf.writeBoolean(msg.noFlags);
    }

    public static RequestSyncProgressMessage decode(PacketBuffer buf) {
        RequestSyncProgressMessage m = new RequestSyncProgressMessage();
        m.key = buf.readUtf(256);
        m.first = buf.readBoolean();
        m.checks = buf.readBoolean();
        m.noFlags = buf.readBoolean();
        return m;
    }

    public static void handle(RequestSyncProgressMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            // Parity with 1.12 logic, simplified to direct call
            boolean knows = thaumcraft.api.capabilities.ThaumcraftCapabilities.knowsResearch(player, msg.key);
            if (player != null && msg.first != knows) {
                if (msg.noFlags) ResearchManager.noFlags = true;
                ResearchManager.progressResearch((net.minecraft.entity.player.EntityPlayer) player, msg.key);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


