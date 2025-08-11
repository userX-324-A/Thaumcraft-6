package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: Zap (stub). */
public class ClientFXZapMessage {
    private double sx, sy, sz;
    private double tx, ty, tz;
    private int color;
    private float width;

    public ClientFXZapMessage() {}

    public ClientFXZapMessage(Vector3d source, Vector3d target, int color, float width) {
        this.sx = source.x; this.sy = source.y; this.sz = source.z;
        this.tx = target.x; this.ty = target.y; this.tz = target.z;
        this.color = color; this.width = width;
    }

    public static void encode(ClientFXZapMessage m, PacketBuffer buf) {
        buf.writeDouble(m.sx); buf.writeDouble(m.sy); buf.writeDouble(m.sz);
        buf.writeDouble(m.tx); buf.writeDouble(m.ty); buf.writeDouble(m.tz);
        buf.writeInt(m.color); buf.writeFloat(m.width);
    }

    public static ClientFXZapMessage decode(PacketBuffer buf) {
        ClientFXZapMessage m = new ClientFXZapMessage();
        m.sx = buf.readDouble(); m.sy = buf.readDouble(); m.sz = buf.readDouble();
        m.tx = buf.readDouble(); m.ty = buf.readDouble(); m.tz = buf.readDouble();
        m.color = buf.readInt(); m.width = buf.readFloat();
        return m;
    }

    public static void handle(ClientFXZapMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            thaumcraft.client.fx.FXClient.zap(
                    new Vector3d(msg.sx, msg.sy, msg.sz),
                    new Vector3d(msg.tx, msg.ty, msg.tz),
                    msg.color, msg.width);
        });
        ctx.get().setPacketHandled(true);
    }
}


