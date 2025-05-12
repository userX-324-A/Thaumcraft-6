package thaumcraft.common.lib.network.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.utils.Utils;

import java.util.function.Supplier;

public class PacketFXBlockBamf {
    private final double x;
    private final double y;
    private final double z;
    private final int color;
    private final byte flags;
    private final byte faceIndex;
    
    public PacketFXBlockBamf(double x, double y, double z, int color, boolean sound, boolean flair, Direction side) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        int f = 0;
        if (sound) {
            f = Utils.setBit(f, 0);
        }
        if (flair) {
            f = Utils.setBit(f, 1);
        }
        this.flags = (byte)f;
        this.faceIndex = (side != null) ? (byte)side.get3DDataValue() : -1;
    }
    
    public PacketFXBlockBamf(BlockPos pos, int color, boolean sound, boolean flair, Direction side) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, color, sound, flair, side);
    }
    
    private PacketFXBlockBamf(double x, double y, double z, int color, byte flags, byte faceIndex) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.flags = flags;
        this.faceIndex = faceIndex;
    }
    
    public static void encode(PacketFXBlockBamf message, PacketBuffer buffer) {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeInt(message.color);
        buffer.writeByte(message.flags);
        buffer.writeByte(message.faceIndex);
    }
    
    public static PacketFXBlockBamf decode(PacketBuffer buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int color = buffer.readInt();
        byte flags = buffer.readByte();
        byte faceIndex = buffer.readByte();
        return new PacketFXBlockBamf(x, y, z, color, flags, faceIndex);
    }
    
    public static void handle(PacketFXBlockBamf message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            processMessage(message);
        });
        context.setPacketHandled(true);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void processMessage(PacketFXBlockBamf message) {
        Direction side = null;
        if (message.faceIndex >= 0) {
            side = Direction.from3DDataValue(message.faceIndex);
        }
        boolean sound = Utils.getBit(message.flags, 0);
        boolean flair = Utils.getBit(message.flags, 1);
        
        if (message.color != -9999) {
            FXDispatcher.INSTANCE.drawBamf(message.x, message.y, message.z, message.color, sound, flair, side);
        } else {
            FXDispatcher.INSTANCE.drawBamf(message.x, message.y, message.z, sound, flair, side);
        }
    }
}
