package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thaumcraft.Thaumcraft;
import thaumcraft.client.fx.other.FXSonic;


public class PacketFXSonic
{
    private int source;
    
    public PacketFXSonic() {
    }
    
    public PacketFXSonic(int source) {
        this.source = source;
    }
    
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(source);
    }
    
    public PacketFXSonic(PacketBuffer buffer) {
        source = buffer.readInt();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void handle(PacketFXSonic message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            World world = mc.world;
            if (world == null) return;

            Entity p = world.getEntityByID(message.source);
            if (p != null) {
                FXSonic fb = new FXSonic(world, p.getPosX(), p.getPosY(), p.getPosZ(), p, 10);
                mc.particles.addEffect(fb);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
