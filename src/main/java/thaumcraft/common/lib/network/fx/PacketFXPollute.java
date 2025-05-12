package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.fx.FXDispatcher;

import java.util.function.Supplier;


public class PacketFXPollute
{
    private int x;
    private int y;
    private int z;
    private byte amount;
    
    public PacketFXPollute() {
    }
    
    public PacketFXPollute(BlockPos pos, float amt) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        if (amt < 1.0f && amt > 0.0f) {
            amt = 1.0f;
        }
        amount = (byte)amt;
    }
    
    public PacketFXPollute(BlockPos pos, float amt, boolean vary) {
        this(pos, amt);
    }
    
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeByte(amount);
    }
    
    public PacketFXPollute(PacketBuffer buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        amount = buffer.readByte();
    }
    
    public static void handle(PacketFXPollute message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                 for (int a = 0; a < Math.min(40, message.amount); ++a) {
                     FXDispatcher.INSTANCE.drawPollutionParticles(new BlockPos(message.x, message.y, message.z));
                 }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
