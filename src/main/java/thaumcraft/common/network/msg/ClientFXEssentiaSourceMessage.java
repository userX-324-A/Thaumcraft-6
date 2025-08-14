package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: EssentiaSource (stub). */
public class ClientFXEssentiaSourceMessage {
    private int x, y, z;
    private byte dx, dy, dz;
    private int color;
    private int ext;

    public ClientFXEssentiaSourceMessage() {}

    public ClientFXEssentiaSourceMessage(int x, int y, int z, byte dx, byte dy, byte dz, int color, int ext) {
        this.x = x; this.y = y; this.z = z;
        this.dx = dx; this.dy = dy; this.dz = dz;
        this.color = color; this.ext = ext;
    }

    public static void encode(ClientFXEssentiaSourceMessage m, PacketBuffer buf) {
        buf.writeInt(m.x); buf.writeInt(m.y); buf.writeInt(m.z);
        buf.writeInt(m.color);
        buf.writeByte(m.dx); buf.writeByte(m.dy); buf.writeByte(m.dz);
        buf.writeShort(m.ext);
    }

    public static ClientFXEssentiaSourceMessage decode(PacketBuffer buf) {
        ClientFXEssentiaSourceMessage m = new ClientFXEssentiaSourceMessage();
        m.x = buf.readInt(); m.y = buf.readInt(); m.z = buf.readInt();
        m.color = buf.readInt();
        m.dx = buf.readByte(); m.dy = buf.readByte(); m.dz = buf.readByte();
        m.ext = buf.readShort();
        return m;
    }

    public static void handle(ClientFXEssentiaSourceMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.essentiaSource(msg.x, msg.y, msg.z, msg.dx, msg.dy, msg.dz, msg.color, msg.ext));
        ctx.get().setPacketHandled(true);
    }
}



