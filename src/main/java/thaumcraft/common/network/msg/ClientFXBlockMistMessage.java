package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: BlockMist (stub). */
public class ClientFXBlockMistMessage {
    private long loc;
    private int color;

    public ClientFXBlockMistMessage() {}

    public ClientFXBlockMistMessage(long loc, int color) {
        this.loc = loc; this.color = color;
    }

    public static void encode(ClientFXBlockMistMessage m, PacketBuffer buf) {
        buf.writeLong(m.loc); buf.writeInt(m.color);
    }

    public static ClientFXBlockMistMessage decode(PacketBuffer buf) {
        ClientFXBlockMistMessage m = new ClientFXBlockMistMessage();
        m.loc = buf.readLong(); m.color = buf.readInt();
        return m;
    }

    public static void handle(ClientFXBlockMistMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.blockMist(net.minecraft.util.math.BlockPos.of(msg.loc), msg.color));
        ctx.get().setPacketHandled(true);
    }
}


