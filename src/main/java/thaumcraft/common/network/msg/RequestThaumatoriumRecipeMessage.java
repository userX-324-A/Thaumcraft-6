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
                // TODO: implement selection once Thaumatorium logic is ported
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


