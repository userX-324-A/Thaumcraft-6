package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Clientbound: syncs a tile's update tag to the client and triggers a render update.
 */
public class ClientTileUpdateMessage {
    private BlockPos blockPos;
    private CompoundNBT updateTag;

    public ClientTileUpdateMessage() {}

    public ClientTileUpdateMessage(BlockPos pos, CompoundNBT tag) {
        this.blockPos = pos;
        this.updateTag = tag == null ? new CompoundNBT() : tag.copy();
    }

    public static void encode(ClientTileUpdateMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.blockPos);
        buf.writeNbt(msg.updateTag);
    }

    public static ClientTileUpdateMessage decode(PacketBuffer buf) {
        ClientTileUpdateMessage msg = new ClientTileUpdateMessage();
        msg.blockPos = buf.readBlockPos();
        msg.updateTag = buf.readNbt();
        if (msg.updateTag == null) msg.updateTag = new CompoundNBT();
        return msg;
    }

    public static void handle(ClientTileUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            TileEntity te = mc.level.getBlockEntity(msg.blockPos);
            if (te != null) {
                te.handleUpdateTag(mc.level.getBlockState(msg.blockPos), msg.updateTag);
                mc.level.sendBlockUpdated(msg.blockPos,
                        mc.level.getBlockState(msg.blockPos),
                        mc.level.getBlockState(msg.blockPos),
                        3);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


