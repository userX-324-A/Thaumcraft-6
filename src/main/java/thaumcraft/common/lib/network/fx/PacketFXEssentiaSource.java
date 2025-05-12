package thaumcraft.common.lib.network.fx;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.lib.events.EssentiaHandler;
import java.util.function.Supplier;


public class PacketFXEssentiaSource
{
    private int x;
    private int y;
    private int z;
    private byte dx;
    private byte dy;
    private byte dz;
    private int color;
    private int ext;

    public PacketFXEssentiaSource() {
    }

    public PacketFXEssentiaSource(BlockPos p1, byte dx, byte dy, byte dz, int color, int e) {
        x = p1.getX();
        y = p1.getY();
        z = p1.getZ();
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.color = color;
        ext = e;
    }
    
    public PacketFXEssentiaSource(PacketBuffer buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        color = buffer.readInt();
        dx = buffer.readByte();
        dy = buffer.readByte();
        dz = buffer.readByte();
        ext = buffer.readShort();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(color);
        buffer.writeByte(dx);
        buffer.writeByte(dy);
        buffer.writeByte(dz);
        buffer.writeShort(ext);
    }


    public static void handle(PacketFXEssentiaSource message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            int tx = message.x - message.dx;
            int ty = message.y - message.dy;
            int tz = message.z - message.dz;
            String key = message.x + ":" + message.y + ":" + message.z + ":" + tx + ":" + ty + ":" + tz + ":" + message.color;
            EssentiaHandler.sourceFX.put(key, new EssentiaHandler.EssentiaSourceFX(new BlockPos(message.x, message.y, message.z), new BlockPos(tx, ty, tz), message.color, message.ext));
        });
        ctx.get().setPacketHandled(true);
    }
}
