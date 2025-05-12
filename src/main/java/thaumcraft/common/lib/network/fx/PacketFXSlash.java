package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thaumcraft.client.fx.FXDispatcher;


public class PacketFXSlash
{
    private int source;
    private int target;
    
    public PacketFXSlash() {
    }
    
    public PacketFXSlash(int source, int target) {
        this.source = source;
        this.target = target;
    }
    
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(source);
        buffer.writeInt(target);
    }
    
    public PacketFXSlash(PacketBuffer buffer) {
        source = buffer.readInt();
        target = buffer.readInt();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void handle(PacketFXSlash message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            World world = mc.world;
            if (world == null) return;

            Entity sourceEntity = getEntityByID(message.source, mc, world);
            Entity targetEntity = getEntityByID(message.target, mc, world);

            if (sourceEntity != null && targetEntity != null) {
                FXDispatcher.INSTANCE.drawSlash(
                        sourceEntity.getPosX(),
                        sourceEntity.getBoundingBox().minY + sourceEntity.getHeight() / 2.0f,
                        sourceEntity.getPosZ(),
                        targetEntity.getPosX(),
                        targetEntity.getBoundingBox().minY + targetEntity.getHeight() / 2.0f,
                        targetEntity.getPosZ(),
                        8);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static Entity getEntityByID(int par1, Minecraft mc, World world) {
        return (mc.player != null && par1 == mc.player.getEntityId()) ? mc.player : world.getEntityByID(par1);
    }
}
