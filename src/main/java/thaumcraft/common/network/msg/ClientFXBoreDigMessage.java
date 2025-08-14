package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: BoreDig (stub). */
public class ClientFXBoreDigMessage {
    private int x, y, z;
    private int boreEntityId;
    private int delay;

    public ClientFXBoreDigMessage() {}

    public ClientFXBoreDigMessage(int x, int y, int z, int boreEntityId, int delay) {
        this.x = x; this.y = y; this.z = z; this.boreEntityId = boreEntityId; this.delay = delay;
    }

    public static void encode(ClientFXBoreDigMessage m, PacketBuffer buf) {
        buf.writeInt(m.x); buf.writeInt(m.y); buf.writeInt(m.z);
        buf.writeInt(m.boreEntityId); buf.writeInt(m.delay);
    }

    public static ClientFXBoreDigMessage decode(PacketBuffer buf) {
        ClientFXBoreDigMessage m = new ClientFXBoreDigMessage();
        m.x = buf.readInt(); m.y = buf.readInt(); m.z = buf.readInt();
        m.boreEntityId = buf.readInt(); m.delay = buf.readInt();
        return m;
    }

    public static void handle(ClientFXBoreDigMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.boreDig(msg.x, msg.y, msg.z, msg.boreEntityId, msg.delay));
        ctx.get().setPacketHandled(true);
    }
}



