package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: FocusEffect (stub). */
public class ClientFXFocusEffectMessage {
    private float x, y, z;
    private float mx, my, mz;
    private String parts;

    public ClientFXFocusEffectMessage() {}

    public ClientFXFocusEffectMessage(float x, float y, float z, float mx, float my, float mz, String partsJoined) {
        this.x = x; this.y = y; this.z = z;
        this.mx = mx; this.my = my; this.mz = mz;
        this.parts = partsJoined; // joined by %
    }

    public static void encode(ClientFXFocusEffectMessage m, PacketBuffer buf) {
        buf.writeFloat(m.x); buf.writeFloat(m.y); buf.writeFloat(m.z);
        buf.writeFloat(m.mx); buf.writeFloat(m.my); buf.writeFloat(m.mz);
        buf.writeUtf(m.parts);
    }

    public static ClientFXFocusEffectMessage decode(PacketBuffer buf) {
        ClientFXFocusEffectMessage m = new ClientFXFocusEffectMessage();
        m.x = buf.readFloat(); m.y = buf.readFloat(); m.z = buf.readFloat();
        m.mx = buf.readFloat(); m.my = buf.readFloat(); m.mz = buf.readFloat();
        m.parts = buf.readUtf(32767);
        return m;
    }

    public static void handle(ClientFXFocusEffectMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.focusEffect(msg.x, msg.y, msg.z));
        ctx.get().setPacketHandled(true);
    }
}


