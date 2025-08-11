package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: InfusionSource (stub). */
public class ClientFXInfusionSourceMessage {
    private long p1, p2;
    private int color;

    public ClientFXInfusionSourceMessage() {}

    public ClientFXInfusionSourceMessage(long p1, long p2, int color) {
        this.p1 = p1; this.p2 = p2; this.color = color;
    }

    public static void encode(ClientFXInfusionSourceMessage m, PacketBuffer buf) {
        buf.writeLong(m.p1); buf.writeLong(m.p2); buf.writeInt(m.color);
    }

    public static ClientFXInfusionSourceMessage decode(PacketBuffer buf) {
        ClientFXInfusionSourceMessage m = new ClientFXInfusionSourceMessage();
        m.p1 = buf.readLong(); m.p2 = buf.readLong(); m.color = buf.readInt();
        return m;
    }

    public static void handle(ClientFXInfusionSourceMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.infusionSource(
                net.minecraft.util.math.BlockPos.of(msg.p1),
                net.minecraft.util.math.BlockPos.of(msg.p2),
                msg.color));
        ctx.get().setPacketHandled(true);
    }
}


