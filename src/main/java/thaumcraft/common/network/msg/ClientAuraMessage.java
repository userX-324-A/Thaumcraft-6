package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.hud.HudHandler;

import java.util.function.Supplier;

/**
 * Clientbound: updates the client-side cached aura data for HUD.
 */
public class ClientAuraMessage {
    private short base;
    private float vis;
    private float flux;

    public ClientAuraMessage() {}

    public ClientAuraMessage(short base, float vis, float flux) {
        this.base = base;
        this.vis = vis;
        this.flux = flux;
    }

    public static void encode(ClientAuraMessage msg, PacketBuffer buf) {
        buf.writeShort(msg.base);
        buf.writeFloat(msg.vis);
        buf.writeFloat(msg.flux);
    }

    public static ClientAuraMessage decode(PacketBuffer buf) {
        ClientAuraMessage msg = new ClientAuraMessage();
        msg.base = buf.readShort();
        msg.vis = buf.readFloat();
        msg.flux = buf.readFloat();
        return msg;
    }

    public static void handle(ClientAuraMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            HudHandler.auraBase = msg.base;
            HudHandler.auraVis = msg.vis;
            HudHandler.auraFlux = msg.flux;
        });
        ctx.get().setPacketHandled(true);
    }
}



