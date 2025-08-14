package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: Sonic (stub). */
public class ClientFXSonicMessage {
    private int source;

    public ClientFXSonicMessage() {}

    public ClientFXSonicMessage(int source) { this.source = source; }

    public static void encode(ClientFXSonicMessage m, PacketBuffer buf) {
        buf.writeInt(m.source);
    }

    public static ClientFXSonicMessage decode(PacketBuffer buf) {
        ClientFXSonicMessage m = new ClientFXSonicMessage();
        m.source = buf.readInt();
        return m;
    }

    public static void handle(ClientFXSonicMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.sonic(msg.source));
        ctx.get().setPacketHandled(true);
    }
}



