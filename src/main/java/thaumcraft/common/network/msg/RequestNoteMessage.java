package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.blocks.world.ArcaneEarBlockEntity;
import thaumcraft.common.network.NetworkHandler;

import java.util.function.Supplier;

/**
 * Serverbound: client requests the current note at a position; server responds with ClientNoteMessage.
 */
public class RequestNoteMessage {
    private BlockPos pos;

    public RequestNoteMessage() {}

    public RequestNoteMessage(BlockPos pos) { this.pos = pos; }

    public static void encode(RequestNoteMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static RequestNoteMessage decode(PacketBuffer buf) {
        RequestNoteMessage msg = new RequestNoteMessage();
        msg.pos = buf.readBlockPos();
        return msg;
    }

    public static void handle(RequestNoteMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerWorld world = ctx.get().getSender().getLevel();
            TileEntity te = world.getBlockEntity(msg.pos);
            byte note = -1;
            if (te instanceof ArcaneEarBlockEntity) {
                note = (byte) ((ArcaneEarBlockEntity) te).note;
            }
            if (note >= 0) {
                NetworkHandler.sendToAllAround(new ClientNoteMessage(msg.pos, note), world, msg.pos, 8.0);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



