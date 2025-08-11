package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: request to change the current focus on the caster in hand.
 * TODO: Implement once caster items and CasterManager are ported.
 */
public class RequestFocusChangeMessage {
    private String focus;

    public RequestFocusChangeMessage() {}

    public RequestFocusChangeMessage(String focus) { this.focus = focus; }

    public static void encode(RequestFocusChangeMessage msg, PacketBuffer buf) {
        buf.writeUtf(msg.focus);
    }

    public static RequestFocusChangeMessage decode(PacketBuffer buf) {
        RequestFocusChangeMessage msg = new RequestFocusChangeMessage();
        msg.focus = buf.readUtf(64);
        return msg;
    }

    public static void handle(RequestFocusChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            // No-op until caster system is ported
        });
        ctx.get().setPacketHandled(true);
    }
}


