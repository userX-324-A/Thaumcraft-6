package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thaumcraft.Thaumcraft;
import thaumcraft.client.fx.other.FXShieldRunes;


public class PacketFXShield
{
    private int source;
    private int target;
    
    public PacketFXShield() {
    }
    
    public PacketFXShield(int source, int target) {
        this.source = source;
        this.target = target;
    }
    
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(source);
        buffer.writeInt(target);
    }
    
    public PacketFXShield(PacketBuffer buffer) {
        source = buffer.readInt();
        target = buffer.readInt();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void handle(PacketFXShield message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            World world = mc.world;
            if (world == null) return;

            Entity p = world.getEntityByID(message.source);
            if (p == null) {
                return;
            }
            float pitch = 0.0f;
            float yaw = 0.0f;
            if (message.target >= 0) {
                Entity t = world.getEntityByID(message.target);
                if (t != null) {
                    double d0 = p.getPosX() - t.getPosX();
                    double d2 = (p.getBoundingBox().minY + p.getBoundingBox().maxY) / 2.0 - (t.getBoundingBox().minY + t.getBoundingBox().maxY) / 2.0;
                    double d3 = p.getPosZ() - t.getPosZ();
                    double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
                    float f = (float)(MathHelper.atan2(d3, d0) * (180D / Math.PI)) - 90.0f;
                    pitch = (float)(-(MathHelper.atan2(d2, d4) * (180D / Math.PI)));
                    yaw = f;
                }
                else {
                    pitch = 90.0f;
                    yaw = 0.0f;
                }
                FXShieldRunes fb = new FXShieldRunes(world, p.getPosX(), p.getPosY(), p.getPosZ(), p, 8, yaw, pitch);
                mc.particles.addEffect(fb);
            }
            else if (message.target == -1) {
                FXShieldRunes fb2 = new FXShieldRunes(world, p.getPosX(), p.getPosY(), p.getPosZ(), p, 8, 0.0f, 90.0f);
                mc.particles.addEffect(fb2);
                fb2 = new FXShieldRunes(world, p.getPosX(), p.getPosY(), p.getPosZ(), p, 8, 0.0f, 270.0f);
                mc.particles.addEffect(fb2);
            }
            else if (message.target == -2) {
                FXShieldRunes fb2 = new FXShieldRunes(world, p.getPosX(), p.getPosY(), p.getPosZ(), p, 8, 0.0f, 270.0f);
                mc.particles.addEffect(fb2);
            }
            else if (message.target == -3) {
                FXShieldRunes fb2 = new FXShieldRunes(world, p.getPosX(), p.getPosY(), p.getPosZ(), p, 8, 0.0f, 90.0f);
                mc.particles.addEffect(fb2);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
