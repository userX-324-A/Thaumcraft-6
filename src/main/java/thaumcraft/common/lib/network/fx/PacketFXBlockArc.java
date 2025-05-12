package thaumcraft.common.lib.network.fx;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.fx.FXDispatcher;

import java.util.function.Supplier;

public class PacketFXBlockArc {
    private final int x;
    private final int y;
    private final int z;
    private final float tx;
    private final float ty;
    private final float tz;
    private final float r;
    private final float g;
    private final float b;
    
    public PacketFXBlockArc(BlockPos pos, Entity source, float r, float g, float b) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        Vector3d sourcePos = source.position();
        AxisAlignedBB sourceBB = source.getBoundingBox();
        this.tx = (float) sourcePos.x();
        this.ty = (float) (sourceBB.minY + source.getEyeHeight() * 0.75F);
        this.tz = (float) sourcePos.z();
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public PacketFXBlockArc(BlockPos pos, BlockPos pos2, float r, float g, float b) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.tx = pos2.getX() + 0.5f;
        this.ty = pos2.getY() + 0.5f;
        this.tz = pos2.getZ() + 0.5f;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    private PacketFXBlockArc(int x, int y, int z, float tx, float ty, float tz, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static void encode(PacketFXBlockArc message, PacketBuffer buffer) {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
        buffer.writeFloat(message.tx);
        buffer.writeFloat(message.ty);
        buffer.writeFloat(message.tz);
        buffer.writeFloat(message.r);
        buffer.writeFloat(message.g);
        buffer.writeFloat(message.b);
    }

    public static PacketFXBlockArc decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        float tx = buffer.readFloat();
        float ty = buffer.readFloat();
        float tz = buffer.readFloat();
        float r = buffer.readFloat();
        float g = buffer.readFloat();
        float b = buffer.readFloat();
        return new PacketFXBlockArc(x, y, z, tx, ty, tz, r, g, b);
    }

    public static void handle(PacketFXBlockArc message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            processMessage(message);
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void processMessage(PacketFXBlockArc message) {
        FXDispatcher.INSTANCE.arcLightning(message.tx, message.ty, message.tz, message.x + 0.5, message.y + 0.5, message.z + 0.5, message.r, message.g, message.b, 0.5f);
    }
}
