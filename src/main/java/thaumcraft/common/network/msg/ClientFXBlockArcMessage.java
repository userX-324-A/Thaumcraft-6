package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: BlockArc (stub; hook to FX system later). */
public class ClientFXBlockArcMessage {
    private int x, y, z;
    private float tx, ty, tz;
    private float r, g, b;

    public ClientFXBlockArcMessage() {}

    public ClientFXBlockArcMessage(int x, int y, int z, float tx, float ty, float tz, float r, float g, float b) {
        this.x = x; this.y = y; this.z = z;
        this.tx = tx; this.ty = ty; this.tz = tz;
        this.r = r; this.g = g; this.b = b;
    }

    public static void encode(ClientFXBlockArcMessage m, PacketBuffer buf) {
        buf.writeInt(m.x); buf.writeInt(m.y); buf.writeInt(m.z);
        buf.writeFloat(m.tx); buf.writeFloat(m.ty); buf.writeFloat(m.tz);
        buf.writeFloat(m.r); buf.writeFloat(m.g); buf.writeFloat(m.b);
    }

    public static ClientFXBlockArcMessage decode(PacketBuffer buf) {
        ClientFXBlockArcMessage m = new ClientFXBlockArcMessage();
        m.x = buf.readInt(); m.y = buf.readInt(); m.z = buf.readInt();
        m.tx = buf.readFloat(); m.ty = buf.readFloat(); m.tz = buf.readFloat();
        m.r = buf.readFloat(); m.g = buf.readFloat(); m.b = buf.readFloat();
        return m;
    }

    public static void handle(ClientFXBlockArcMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.blockArc(msg.x, msg.y, msg.z, msg.tx, msg.ty, msg.tz, msg.r, msg.g, msg.b));
        ctx.get().setPacketHandled(true);
    }
}


