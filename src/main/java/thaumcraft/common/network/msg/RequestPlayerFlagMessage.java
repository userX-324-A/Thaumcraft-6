package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: generic player flag update. Replaces PacketPlayerFlagToServer.
 */
public class RequestPlayerFlagMessage {
    private byte flag;

    public RequestPlayerFlagMessage() {}

    public RequestPlayerFlagMessage(int flag) { this.flag = (byte) flag; }

    public static void encode(RequestPlayerFlagMessage msg, PacketBuffer buf) {
        buf.writeByte(msg.flag);
    }

    public static RequestPlayerFlagMessage decode(PacketBuffer buf) {
        RequestPlayerFlagMessage m = new RequestPlayerFlagMessage();
        m.flag = buf.readByte();
        return m;
    }

    public static void handle(RequestPlayerFlagMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            switch (msg.flag) {
                case 1:
                    player.fallDistance = 0.0f;
                    break;
                default:
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


