package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.blocks.world.ResearchTableBlockEntity;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Serverbound: start a theorycrafting session at a research table.
 */
public class RequestStartTheoryMessage {
    private BlockPos pos;
    private java.util.Set<String> aids;

    public RequestStartTheoryMessage() {}

    public RequestStartTheoryMessage(BlockPos pos, Set<String> aids) {
        this.pos = pos; this.aids = aids;
    }

    public static void encode(RequestStartTheoryMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeVarInt(msg.aids.size());
        for (String s : msg.aids) buf.writeUtf(s);
    }

    public static RequestStartTheoryMessage decode(PacketBuffer buf) {
        RequestStartTheoryMessage msg = new RequestStartTheoryMessage();
        msg.pos = buf.readBlockPos();
        int n = buf.readVarInt();
        java.util.Set<String> set = new java.util.HashSet<>();
        for (int i = 0; i < n; i++) set.add(buf.readUtf(64));
        msg.aids = set;
        return msg;
    }

    public static void handle(RequestStartTheoryMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerWorld world = ctx.get().getSender().getLevel();
            if (world == null) return;
            TileEntity te = world.getBlockEntity(msg.pos);
            if (te instanceof ResearchTableBlockEntity) {
                // TODO: implement startTheory once research table is ported
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


