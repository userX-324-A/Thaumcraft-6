package thaumcraft.common.network.msg;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: push serialized focus nodes and name into a focal manipulator tile. Replaces PacketFocusNodesToServer.
 */
public class RequestFocusNodesMessage {
    private BlockPos pos;
    private CompoundNBT nodes; // serialized as NBT map of id->node NBT
    private String name;

    public RequestFocusNodesMessage() {}

    public RequestFocusNodesMessage(BlockPos pos, CompoundNBT nodes, String name) {
        this.pos = pos; this.nodes = nodes; this.name = name;
    }

    public static void encode(RequestFocusNodesMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeNbt(msg.nodes == null ? new CompoundNBT() : msg.nodes);
        buf.writeUtf(msg.name == null ? "" : msg.name);
    }

    public static RequestFocusNodesMessage decode(PacketBuffer buf) {
        RequestFocusNodesMessage m = new RequestFocusNodesMessage();
        m.pos = buf.readBlockPos();
        m.nodes = buf.readNbt();
        m.name = buf.readUtf(128);
        return m;
    }

    public static void handle(RequestFocusNodesMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            TileEntity te = player.level.getBlockEntity(msg.pos);
            if (te != null) {
                CompoundNBT tag = te.getTileData();
                tag.put("tc_focus_nodes", msg.nodes == null ? new CompoundNBT() : msg.nodes);
                tag.putString("tc_focus_name", msg.name == null ? "" : msg.name);
                te.setChanged();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


