package thaumcraft.common.lib.network.fx;

import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.IFocusElement;


public class PacketFXFocusPartImpactBurst
{
    private double x;
    private double y;
    private double z;
    private String parts;

    public PacketFXFocusPartImpactBurst() {
    }

    public PacketFXFocusPartImpactBurst(double x, double y, double z, String[] parts) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parts = String.join("%", parts);
    }
    
    public PacketFXFocusPartImpactBurst(PacketBuffer buffer) {
        x = buffer.readFloat();
        y = buffer.readFloat();
        z = buffer.readFloat();
        parts = buffer.readUtf(32767); // Read string
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeFloat((float) x);
        buffer.writeFloat((float) y);
        buffer.writeFloat((float) z);
        buffer.writeUtf(parts); // Write string
    }


    public static void handle(PacketFXFocusPartImpactBurst message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return;
            
            String[] partKeys = message.parts.split("%", -1);
            int amt = Math.max(1, 20 / partKeys.length);
            Random r = world.random;
            for (String k : partKeys) {
                IFocusElement part = FocusEngine.getElement(k);
                if (part instanceof FocusEffect) {
                    for (int a = 0; a < amt; ++a) {
                        ((FocusEffect)part).renderParticleFX(world, message.x, message.y, message.z, 
                                                             r.nextGaussian() * 0.4, r.nextGaussian() * 0.4, r.nextGaussian() * 0.4);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
