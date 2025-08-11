package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: FocusPartImpactBurst (stub). */
public class ClientFXFocusPartImpactBurstMessage {
    private float x, y, z;
    private String parts;

    public ClientFXFocusPartImpactBurstMessage() {}

    public ClientFXFocusPartImpactBurstMessage(float x, float y, float z, String partsJoined) {
        this.x = x; this.y = y; this.z = z; this.parts = partsJoined;
    }

    public static void encode(ClientFXFocusPartImpactBurstMessage m, PacketBuffer buf) {
        buf.writeFloat(m.x); buf.writeFloat(m.y); buf.writeFloat(m.z);
        buf.writeUtf(m.parts);
    }

    public static ClientFXFocusPartImpactBurstMessage decode(PacketBuffer buf) {
        ClientFXFocusPartImpactBurstMessage m = new ClientFXFocusPartImpactBurstMessage();
        m.x = buf.readFloat(); m.y = buf.readFloat(); m.z = buf.readFloat();
        m.parts = buf.readUtf(32767);
        return m;
    }

    public static void handle(ClientFXFocusPartImpactBurstMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.focusImpactBurst(msg.x, msg.y, msg.z));
        ctx.get().setPacketHandled(true);
    }
}


