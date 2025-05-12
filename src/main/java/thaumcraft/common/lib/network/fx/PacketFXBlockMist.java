package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.fx.FXDispatcher;
import java.util.function.Supplier;


public class PacketFXBlockMist
{
    private long loc;
    private int color;

    public PacketFXBlockMist() {
    }

    public PacketFXBlockMist(BlockPos pos, int color) {
        loc = pos.toLong();
        this.color = color;
    }
    
    public PacketFXBlockMist(PacketBuffer buffer) {
        loc = buffer.readLong();
        color = buffer.readInt();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeLong(loc);
        buffer.writeInt(color);
    }


    public static void handle(PacketFXBlockMist message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            FXDispatcher.INSTANCE.drawBlockMistParticles(BlockPos.fromLong(message.loc), message.color);
        });
        ctx.get().setPacketHandled(true);
    }
}
