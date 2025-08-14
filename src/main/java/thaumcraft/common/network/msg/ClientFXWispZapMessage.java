package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: WispZap (stub). */
public class ClientFXWispZapMessage {
    private int source;
    private int target;

    public ClientFXWispZapMessage() {}

    public ClientFXWispZapMessage(int source, int target) { this.source = source; this.target = target; }

    public static void encode(ClientFXWispZapMessage m, PacketBuffer buf) {
        buf.writeInt(m.source); buf.writeInt(m.target);
    }

    public static ClientFXWispZapMessage decode(PacketBuffer buf) {
        ClientFXWispZapMessage m = new ClientFXWispZapMessage();
        m.source = buf.readInt(); m.target = buf.readInt();
        return m;
    }

    public static void handle(ClientFXWispZapMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.wispZap(msg.source, msg.target));
        ctx.get().setPacketHandled(true);
    }
}



