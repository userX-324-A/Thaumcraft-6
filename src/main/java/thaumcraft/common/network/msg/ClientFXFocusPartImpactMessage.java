package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: FocusPartImpact (stub). */
public class ClientFXFocusPartImpactMessage {
    private float x, y, z;
    private String parts;

    public ClientFXFocusPartImpactMessage() {}

    public ClientFXFocusPartImpactMessage(float x, float y, float z, String partsJoined) {
        this.x = x; this.y = y; this.z = z; this.parts = partsJoined;
    }

    public static void encode(ClientFXFocusPartImpactMessage m, PacketBuffer buf) {
        buf.writeFloat(m.x); buf.writeFloat(m.y); buf.writeFloat(m.z);
        buf.writeUtf(m.parts);
    }

    public static ClientFXFocusPartImpactMessage decode(PacketBuffer buf) {
        ClientFXFocusPartImpactMessage m = new ClientFXFocusPartImpactMessage();
        m.x = buf.readFloat(); m.y = buf.readFloat(); m.z = buf.readFloat();
        m.parts = buf.readUtf(32767);
        return m;
    }

    public static void handle(ClientFXFocusPartImpactMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.focusImpact(msg.x, msg.y, msg.z));
        ctx.get().setPacketHandled(true);
    }
}



