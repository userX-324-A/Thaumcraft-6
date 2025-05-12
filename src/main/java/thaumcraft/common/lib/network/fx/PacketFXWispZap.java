package thaumcraft.common.lib.network.fx;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.monster.EntityWisp;

import java.awt.Color;
import java.util.function.Supplier;


public class PacketFXWispZap
{
    private final int source;
    private final int target;

    public PacketFXWispZap(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(source);
        buffer.writeInt(target);
    }

    public static PacketFXWispZap decode(PacketBuffer buffer) {
        return new PacketFXWispZap(buffer.readInt(), buffer.readInt());
    }

    public static void handle(PacketFXWispZap message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            World world = mc.world;
            Entity var2 = getEntityByID(message.source, mc, world);
            Entity var3 = getEntityByID(message.target, mc, world);
            if (var2 != null && var3 != null) {
                float r = 1.0f;
                float g = 1.0f;
                float b = 1.0f;
                if (var2 instanceof EntityWisp) {
                    Color c = new Color(Aspect.getAspect(((EntityWisp)var2).getType()).getColor());
                    r = c.getRed() / 255.0f;
                    g = c.getGreen() / 255.0f;
                    b = c.getBlue() / 255.0f;
                }
                FXDispatcher.INSTANCE.arcBolt(var2.getPosX(), var2.getPosY(), var2.getPosZ(), var3.getPosX(), var3.getPosY(), var3.getPosZ(), r, g, b, 0.6f);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static Entity getEntityByID(int par1, Minecraft mc, World world) {
        return (par1 == mc.player.getEntityId()) ? mc.player : world.getEntityByID(par1);
    }
}
