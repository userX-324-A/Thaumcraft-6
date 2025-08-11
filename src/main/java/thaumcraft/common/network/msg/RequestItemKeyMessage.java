package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: notifies the server of an item key press (e.g., mode toggle). Actual handling TBD during item port.
 */
public class RequestItemKeyMessage {
    private int key;
    private int modifier;

    public RequestItemKeyMessage() {}

    public RequestItemKeyMessage(int key, int modifier) {
        this.key = key;
        this.modifier = modifier;
    }

    public static void encode(RequestItemKeyMessage msg, PacketBuffer buf) {
        buf.writeVarInt(msg.key);
        buf.writeVarInt(msg.modifier);
    }

    public static RequestItemKeyMessage decode(PacketBuffer buf) {
        return new RequestItemKeyMessage(buf.readVarInt(), buf.readVarInt());
    }

    public static void handle(RequestItemKeyMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerWorld world = ctx.get().getSender().getLevel();
            if (world == null) return;
            // TODO: Call appropriate item-handling hooks once items are ported.
        });
        ctx.get().setPacketHandled(true);
    }
}


