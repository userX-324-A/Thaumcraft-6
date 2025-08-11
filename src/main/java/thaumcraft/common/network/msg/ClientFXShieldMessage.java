package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: Shield (stub). */
public class ClientFXShieldMessage {
    private int source;
    private int target;

    public ClientFXShieldMessage() {}

    public ClientFXShieldMessage(int source, int target) {
        this.source = source; this.target = target;
    }

    public static void encode(ClientFXShieldMessage m, PacketBuffer buf) {
        buf.writeInt(m.source); buf.writeInt(m.target);
    }

    public static ClientFXShieldMessage decode(PacketBuffer buf) {
        ClientFXShieldMessage m = new ClientFXShieldMessage();
        m.source = buf.readInt(); m.target = buf.readInt();
        return m;
    }

    public static void handle(ClientFXShieldMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.shield(msg.source, msg.target));
        ctx.get().setPacketHandled(true);
    }
}


