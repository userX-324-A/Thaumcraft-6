package thaumcraft.common.network.msg;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: sync research flags for a research key. Replaces PacketSyncResearchFlagsToServer.
 */
public class RequestSyncResearchFlagsMessage {
    private String key;
    private byte flags; // bit0=PAGE, bit1=POPUP, bit2=RESEARCH (parity with old Utils.pack order)

    public RequestSyncResearchFlagsMessage() {}

    public RequestSyncResearchFlagsMessage(String key, byte flags) {
        this.key = key;
        this.flags = flags;
    }

    public static void encode(RequestSyncResearchFlagsMessage msg, PacketBuffer buf) {
        buf.writeUtf(msg.key);
        buf.writeByte(msg.flags);
    }

    public static RequestSyncResearchFlagsMessage decode(PacketBuffer buf) {
        RequestSyncResearchFlagsMessage m = new RequestSyncResearchFlagsMessage();
        m.key = buf.readUtf(256);
        m.flags = buf.readByte();
        return m;
    }

    public static void handle(RequestSyncResearchFlagsMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            CompoundNBT root = player.getPersistentData().getCompound("tc_research_flags");
            root.putByte(msg.key, msg.flags);
            player.getPersistentData().put("tc_research_flags", root);
        });
        ctx.get().setPacketHandled(true);
    }
}



