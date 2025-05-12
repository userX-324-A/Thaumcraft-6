package thaumcraft.common.lib.network.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.IFocusElement;
import java.util.function.Supplier;


public class PacketFXFocusEffect
{
    float x;
    float y;
    float z;
    float mx;
    float my;
    float mz;
    String parts;

    public PacketFXFocusEffect() {
    }

    public PacketFXFocusEffect(float x, float y, float z, float mx, float my, float mz, String[] parts) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
        this.parts = String.join("%", parts);
    }
    
    public PacketFXFocusEffect(PacketBuffer buffer) {
        x = buffer.readFloat();
        y = buffer.readFloat();
        z = buffer.readFloat();
        mx = buffer.readFloat();
        my = buffer.readFloat();
        mz = buffer.readFloat();
        parts = buffer.readUtf(32767); // Read string
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
        buffer.writeFloat(mx);
        buffer.writeFloat(my);
        buffer.writeFloat(mz);
        buffer.writeUtf(parts); // Write string
    }


    public static void handle(PacketFXFocusEffect message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client-side world access
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return; // Extra safety check
            
            String[] partKeys = message.parts.split("%", -1); // Use -1 limit to include trailing empty strings if needed
            int amt = Math.max(1, 10 / partKeys.length);
            for (String k : partKeys) {
                IFocusElement part = FocusEngine.getElement(k);
                if (part instanceof FocusEffect) {
                    for (int a = 0; a < amt; ++a) {
                        ((FocusEffect)part).renderParticleFX(world, 
                            message.x, message.y, message.z, 
                            message.mx + world.random.nextGaussian() / 20.0, 
                            message.my + world.random.nextGaussian() / 20.0, 
                            message.mz + world.random.nextGaussian() / 20.0);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
