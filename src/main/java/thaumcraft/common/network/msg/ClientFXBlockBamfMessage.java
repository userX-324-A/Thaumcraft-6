package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Clientbound: renders a BAMF particle effect at a position with flags and optional face.
 * This is a thin port of the old PacketFXBlockBamf, now client-only.
 */
public class ClientFXBlockBamfMessage {
    private double x;
    private double y;
    private double z;
    private int color;
    private byte flags; // bit0: sound, bit1: flair
    private byte face;  // -1 for none; otherwise Direction ordinal

    public ClientFXBlockBamfMessage() {}

    public ClientFXBlockBamfMessage(double x, double y, double z, int color, boolean sound, boolean flair, Direction face) {
        this.x = x; this.y = y; this.z = z;
        this.color = color;
        int f = 0;
        if (sound) f |= 1;
        if (flair) f |= 2;
        this.flags = (byte) f;
        this.face = (byte) (face == null ? -1 : face.ordinal());
    }

    public static void encode(ClientFXBlockBamfMessage msg, PacketBuffer buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeInt(msg.color);
        buf.writeByte(msg.flags);
        buf.writeByte(msg.face);
    }

    public static ClientFXBlockBamfMessage decode(PacketBuffer buf) {
        ClientFXBlockBamfMessage msg = new ClientFXBlockBamfMessage();
        msg.x = buf.readDouble();
        msg.y = buf.readDouble();
        msg.z = buf.readDouble();
        msg.color = buf.readInt();
        msg.flags = buf.readByte();
        msg.face = buf.readByte();
        return msg;
    }

    public static void handle(ClientFXBlockBamfMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            thaumcraft.client.fx.FXClient.blockBamf(msg.x, msg.y, msg.z, msg.color,
                    (msg.flags & 1) != 0, (msg.flags & 2) != 0,
                    msg.face >= 0 ? net.minecraft.util.Direction.from3DDataValue(msg.face) : null);
        });
        ctx.get().setPacketHandled(true);
    }
}


