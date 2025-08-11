package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: Slash (stub). */
public class ClientFXSlashMessage {
    private int source;
    private int target;

    public ClientFXSlashMessage() {}

    public ClientFXSlashMessage(int source, int target) {
        this.source = source; this.target = target;
    }

    public static void encode(ClientFXSlashMessage m, PacketBuffer buf) {
        buf.writeInt(m.source); buf.writeInt(m.target);
    }

    public static ClientFXSlashMessage decode(PacketBuffer buf) {
        ClientFXSlashMessage m = new ClientFXSlashMessage();
        m.source = buf.readInt(); m.target = buf.readInt();
        return m;
    }

    public static void handle(ClientFXSlashMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.slash(msg.source, msg.target));
        ctx.get().setPacketHandled(true);
    }
}


