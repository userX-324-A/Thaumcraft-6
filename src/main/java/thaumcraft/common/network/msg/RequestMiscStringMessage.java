package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: generic string message with an id for routing. Replace old PacketMiscStringToServer.
 */
public class RequestMiscStringMessage {
    private int id;
    private String text;

    public RequestMiscStringMessage() {}

    public RequestMiscStringMessage(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static void encode(RequestMiscStringMessage msg, PacketBuffer buf) {
        buf.writeVarInt(msg.id);
        buf.writeUtf(msg.text);
    }

    public static RequestMiscStringMessage decode(PacketBuffer buf) {
        RequestMiscStringMessage msg = new RequestMiscStringMessage();
        msg.id = buf.readVarInt();
        msg.text = buf.readUtf(256);
        return msg;
    }

    public static void handle(RequestMiscStringMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            // id==0: logistics search string (parity with 1.12 ContainerLogistics)
            if (msg.id == 0 && ctx.get().getSender().containerMenu instanceof thaumcraft.common.menu.ThaumatoriumMenu) {
                // No logistics container yet in 1.16 port; leave stub for later. Intentionally no-op.
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


