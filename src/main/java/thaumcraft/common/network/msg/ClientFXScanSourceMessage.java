package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound FX: ScanSource (stub). */
public class ClientFXScanSourceMessage {
    private long loc;
    private byte size;

    public ClientFXScanSourceMessage() {}

    public ClientFXScanSourceMessage(long loc, int size) {
        this.loc = loc; this.size = (byte) size;
    }

    public static void encode(ClientFXScanSourceMessage m, PacketBuffer buf) {
        buf.writeLong(m.loc); buf.writeByte(m.size);
    }

    public static ClientFXScanSourceMessage decode(PacketBuffer buf) {
        ClientFXScanSourceMessage m = new ClientFXScanSourceMessage();
        m.loc = buf.readLong(); m.size = buf.readByte();
        return m;
    }

    public static void handle(ClientFXScanSourceMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> thaumcraft.client.fx.FXClient.scanSource(
                net.minecraft.util.math.BlockPos.of(msg.loc), msg.size));
        ctx.get().setPacketHandled(true);
    }
}


