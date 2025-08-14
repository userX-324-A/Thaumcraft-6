package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound: set an item stack in an open container slot. Replacement for PacketItemToClientContainer. */
public class ClientItemToContainerMessage {
    private int containerId;
    private int slot;
    private ItemStack item;

    public ClientItemToContainerMessage() {}

    public ClientItemToContainerMessage(int containerId, int slot, ItemStack item) {
        this.containerId = containerId; this.slot = slot; this.item = item.copy();
    }

    public static void encode(ClientItemToContainerMessage m, PacketBuffer buf) {
        buf.writeVarInt(m.containerId);
        buf.writeVarInt(m.slot);
        buf.writeItem(m.item);
    }

    public static ClientItemToContainerMessage decode(PacketBuffer buf) {
        ClientItemToContainerMessage m = new ClientItemToContainerMessage();
        m.containerId = buf.readVarInt();
        m.slot = buf.readVarInt();
        m.item = buf.readItem();
        return m;
    }

    public static void handle(ClientItemToContainerMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            Container c = mc.player.containerMenu; // 1.16 mapped name
            if (c != null && c.containerId == msg.containerId) {
                try {
                    c.setItem(msg.slot, msg.item);
                } catch (Throwable t) {
                    // fallback to older naming if necessary
                    try {
                        java.lang.reflect.Method m = c.getClass().getMethod("setItem", int.class, ItemStack.class);
                        m.invoke(c, msg.slot, msg.item);
                    } catch (Exception ignore) {}
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



