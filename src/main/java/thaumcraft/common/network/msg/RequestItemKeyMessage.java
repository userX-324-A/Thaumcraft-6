package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.Thaumcraft;

import java.util.function.Supplier;

/**
 * Serverbound: notifies the server of an item key press (e.g., mode toggle). Actual handling TBD during item port.
 */
public class RequestItemKeyMessage {
    private int key;
    private int modifier;

    public RequestItemKeyMessage() {}

    public RequestItemKeyMessage(int key, int modifier) {
        this.key = key;
        this.modifier = modifier;
    }

    public static void encode(RequestItemKeyMessage msg, PacketBuffer buf) {
        buf.writeVarInt(msg.key);
        buf.writeVarInt(msg.modifier);
    }

    public static RequestItemKeyMessage decode(PacketBuffer buf) {
        return new RequestItemKeyMessage(buf.readVarInt(), buf.readVarInt());
    }

    public static void handle(RequestItemKeyMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity sender = ctx.get().getSender();
            if (sender == null) return;
            ServerWorld world = ctx.get().getSender().getLevel();
            if (world == null) return;
            if (msg.key == 1) {
                ItemStack main = sender.getMainHandItem();
                ItemStack off = sender.getOffhandItem();
                boolean toggled = false;
                if (!main.isEmpty() && main.getItem().getRegistryName() != null
                        && Thaumcraft.MODID.equals(main.getItem().getRegistryName().getNamespace())) {
                    int cur = main.getOrCreateTag().getInt("tc_misc");
                    main.getOrCreateTag().putInt("tc_misc", cur ^ (1 << (msg.modifier & 7)));
                    toggled = true;
                }
                if (!toggled && !off.isEmpty() && off.getItem().getRegistryName() != null
                        && Thaumcraft.MODID.equals(off.getItem().getRegistryName().getNamespace())) {
                    int cur = off.getOrCreateTag().getInt("tc_misc");
                    off.getOrCreateTag().putInt("tc_misc", cur ^ (1 << (msg.modifier & 7)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



