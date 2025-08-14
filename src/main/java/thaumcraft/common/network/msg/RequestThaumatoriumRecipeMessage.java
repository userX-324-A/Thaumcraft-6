package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.blocks.world.ThaumatoriumBlockEntity;

import java.util.function.Supplier;

/**
 * Serverbound: select a Thaumatorium recipe at a position.
 */
public class RequestThaumatoriumRecipeMessage {
    private BlockPos pos;
    private int recipeHash;

    public RequestThaumatoriumRecipeMessage() {}

    public RequestThaumatoriumRecipeMessage(BlockPos pos, int recipeHash) {
        this.pos = pos; this.recipeHash = recipeHash;
    }

    public static void encode(RequestThaumatoriumRecipeMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.recipeHash);
    }

    public static RequestThaumatoriumRecipeMessage decode(PacketBuffer buf) {
        RequestThaumatoriumRecipeMessage msg = new RequestThaumatoriumRecipeMessage();
        msg.pos = buf.readBlockPos();
        msg.recipeHash = buf.readInt();
        return msg;
    }

    public static void handle(RequestThaumatoriumRecipeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            ServerWorld world = ctx.get().getSender().getLevel();
            if (world == null) return;
            TileEntity te = world.getBlockEntity(msg.pos);
            if (te instanceof ThaumatoriumBlockEntity) {
                // Minimal behavior: toggle current recipe hash in a transient list on the block entity NBT
                net.minecraft.nbt.CompoundNBT tag = te.getTileData();
                String list = tag.getString("tc_recipe_sel");
                java.util.LinkedHashSet<Integer> set = new java.util.LinkedHashSet<>();
                if (!list.isEmpty()) {
                    for (String s : list.split(",")) {
                        try { set.add(Integer.parseInt(s)); } catch (Exception ignored) {}
                    }
                }
                if (set.contains(msg.recipeHash)) set.remove(msg.recipeHash); else set.add(msg.recipeHash);
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (Integer i : set) {
                    if (!first) sb.append(',');
                    sb.append(i);
                    first = false;
                }
                tag.putString("tc_recipe_sel", sb.toString());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



