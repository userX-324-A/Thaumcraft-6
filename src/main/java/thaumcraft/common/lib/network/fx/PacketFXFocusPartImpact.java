package thaumcraft.common.lib.network.fx;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.IFocusElement;

public class PacketFXFocusPartImpact
{
    double x;
    double y;
    double z;
    String parts;
    
    public PacketFXFocusPartImpact() {
    }
    
    public PacketFXFocusPartImpact(double x, double y, double z, String[] partsArray) {
        this.x = x;
        this.y = y;
        this.z = z;
        StringBuilder sb = new StringBuilder();
        for (int a = 0; a < partsArray.length; ++a) {
            if (a > 0) {
                sb.append("%");
            }
            sb.append(partsArray[a]);
        }
        this.parts = sb.toString();
    }

    private PacketFXFocusPartImpact(double x, double y, double z, String partsString) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parts = partsString;
    }
    
    public static void encode(PacketFXFocusPartImpact msg, FriendlyByteBuf buf) {
        buf.writeFloat((float) msg.x);
        buf.writeFloat((float) msg.y);
        buf.writeFloat((float) msg.z);
        buf.writeUtf(msg.parts);
    }
    
    public static PacketFXFocusPartImpact decode(FriendlyByteBuf buf) {
        double x = buf.readFloat();
        double y = buf.readFloat();
        double z = buf.readFloat();
        String partsStr = buf.readUtf();
        return new PacketFXFocusPartImpact(x, y, z, partsStr);
    }
    
    public static void handle(PacketFXFocusPartImpact msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                performClientEffects(msg);
            });
        });
        ctx.setPacketHandled(true);
    }
    
    private static void performClientEffects(PacketFXFocusPartImpact message) {
        if (Minecraft.getInstance().level == null) return;

        String[] partKeys = message.parts.split("%");
        if (partKeys.length == 0) return;

        int amt = Math.max(1, 15 / partKeys.length);
        Random r = Minecraft.getInstance().level.random;
        
        for (String k : partKeys) {
            IFocusElement part = FocusEngine.getElement(k);
            if (part instanceof FocusEffect) {
                FocusEffect focusEffect = (FocusEffect) part;
                for (int a = 0; a < amt; ++a) {
                    focusEffect.renderParticleFX(Minecraft.getInstance().level, message.x, message.y, message.z, r.nextGaussian() * 0.15, r.nextGaussian() * 0.15, r.nextGaussian() * 0.15);
                }
            }
        }
    }
}
