package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.blocks.world.ArcaneEarBlockEntity;

import java.util.function.Supplier;

/**
 * Clientbound: updates the note value for Note Block or Arcane Ear at a position.
 * Server should send this after resolving the note value.
 */
public class ClientNoteMessage {
    private BlockPos pos;
    private byte note; // 0..24 typical; treat <0 as no-op

    public ClientNoteMessage() {}

    public ClientNoteMessage(BlockPos pos, byte note) {
        this.pos = pos;
        this.note = note;
    }

    public static void encode(ClientNoteMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeByte(msg.note);
    }

    public static ClientNoteMessage decode(PacketBuffer buf) {
        ClientNoteMessage msg = new ClientNoteMessage();
        msg.pos = buf.readBlockPos();
        msg.note = buf.readByte();
        return msg;
    }

    public static void handle(ClientNoteMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.note < 0) return;
            if (Minecraft.getInstance().level == null) return;
            TileEntity te = Minecraft.getInstance().level.getBlockEntity(msg.pos);
            if (te instanceof ArcaneEarBlockEntity) {
                ((ArcaneEarBlockEntity) te).note = msg.note;
            }
            // If a Note Block is needed later, add handling here.
        });
        ctx.get().setPacketHandled(true);
    }
}



