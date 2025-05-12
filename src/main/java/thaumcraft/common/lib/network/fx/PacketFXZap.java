package thaumcraft.common.lib.network.fx;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.fx.FXDispatcher;

import java.awt.Color;
import java.util.function.Supplier;


public class PacketFXZap
{
    private final Vector3d source;
    private final Vector3d target;
    private final int color;
    private final float width;

    public PacketFXZap(Vector3d source, Vector3d target, int color, float width) {
        this.source = source;
        this.target = target;
        this.color = color;
        this.width = width;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeDouble(source.x);
        buffer.writeDouble(source.y);
        buffer.writeDouble(source.z);
        buffer.writeDouble(target.x);
        buffer.writeDouble(target.y);
        buffer.writeDouble(target.z);
        buffer.writeInt(color);
        buffer.writeFloat(width);
    }

    public static PacketFXZap decode(PacketBuffer buffer) {
        Vector3d src = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        Vector3d tar = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        int col = buffer.readInt();
        float wid = buffer.readFloat();
        return new PacketFXZap(src, tar, col, wid);
    }

    public static void handle(PacketFXZap message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Color c = new Color(message.color);
            FXDispatcher.INSTANCE.arcBolt(message.source.x, message.source.y, message.source.z,
                                          message.target.x, message.target.y, message.target.z,
                                          c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f,
                                          message.width);
        });
        ctx.get().setPacketHandled(true);
    }
}
