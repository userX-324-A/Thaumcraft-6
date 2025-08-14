package thaumcraft.common.network.msg;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: set the focus name on the focal manipulator tile. Replaces PacketFocusNameToServer.
 */
public class RequestFocusNameMessage {
    private BlockPos pos;
    private String name;

    public RequestFocusNameMessage() {}

    public RequestFocusNameMessage(BlockPos pos, String name) { this.pos = pos; this.name = name; }

    public static void encode(RequestFocusNameMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeUtf(msg.name);
    }

    public static RequestFocusNameMessage decode(PacketBuffer buf) {
        RequestFocusNameMessage m = new RequestFocusNameMessage();
        m.pos = buf.readBlockPos();
        m.name = buf.readUtf(64);
        return m;
    }

    public static void handle(RequestFocusNameMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            TileEntity te = player.level.getBlockEntity(msg.pos);
            if (te != null) {
                te.getTileData().putString("tc_focus_name", msg.name);
                te.setChanged();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



