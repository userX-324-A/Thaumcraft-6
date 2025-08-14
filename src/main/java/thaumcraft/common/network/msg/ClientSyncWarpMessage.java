package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.function.Supplier;

/**
 * Clientbound: syncs the player's warp capability data.
 */
public class ClientSyncWarpMessage {
    private net.minecraft.nbt.CompoundNBT tag;

    public ClientSyncWarpMessage() {}

    public ClientSyncWarpMessage(net.minecraft.nbt.CompoundNBT tag) {
        this.tag = tag;
    }

    public static void encode(ClientSyncWarpMessage msg, PacketBuffer buf) {
        buf.writeNbt(msg.tag);
    }

    public static ClientSyncWarpMessage decode(PacketBuffer buf) {
        return new ClientSyncWarpMessage(buf.readNbt());
    }

    public static void handle(ClientSyncWarpMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player == null) return;
            IPlayerWarp cap = ThaumcraftCapabilities.getWarp(mc.player);
            if (cap != null && msg.tag != null) {
                cap.deserializeNBT(msg.tag);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



