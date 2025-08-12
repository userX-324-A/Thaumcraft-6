package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class ClientOpenThaumonomiconMessage {
    public static void encode(ClientOpenThaumonomiconMessage msg, PacketBuffer buf) {}
    public static ClientOpenThaumonomiconMessage decode(PacketBuffer buf) { return new ClientOpenThaumonomiconMessage(); }
    public static void handle(ClientOpenThaumonomiconMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc != null) mc.setScreen(new thaumcraft.common.client.screen.ThaumonomiconScreen());
        }));
        ctx.get().setPacketHandled(true);
    }
}


