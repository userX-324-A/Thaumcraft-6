package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Clientbound: spawns pollution particles near a position, amount-capped.
 */
public class ClientFXPolluteMessage {
    private BlockPos pos;
    private byte amount;

    public ClientFXPolluteMessage() {}

    public ClientFXPolluteMessage(BlockPos pos, int amount) {
        this.pos = pos;
        int clamped = Math.max(0, Math.min(40, amount));
        this.amount = (byte) clamped;
    }

    public static void encode(ClientFXPolluteMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeByte(msg.amount);
    }

    public static ClientFXPolluteMessage decode(PacketBuffer buf) {
        ClientFXPolluteMessage msg = new ClientFXPolluteMessage();
        msg.pos = buf.readBlockPos();
        msg.amount = buf.readByte();
        return msg;
    }

    public static void handle(ClientFXPolluteMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            thaumcraft.client.fx.FXClient.pollute(msg.pos, msg.amount);
        });
        ctx.get().setPacketHandled(true);
    }
}



