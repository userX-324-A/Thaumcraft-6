package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.Thaumcraft;

import java.util.function.Supplier;

/**
 * Serverbound: request to change the current focus on the caster in hand.
 * TODO: Implement once caster items and CasterManager are ported.
 */
public class RequestFocusChangeMessage {
    private String focus;

    public RequestFocusChangeMessage() {}

    public RequestFocusChangeMessage(String focus) { this.focus = focus; }

    public static void encode(RequestFocusChangeMessage msg, PacketBuffer buf) {
        buf.writeUtf(msg.focus);
    }

    public static RequestFocusChangeMessage decode(PacketBuffer buf) {
        RequestFocusChangeMessage msg = new RequestFocusChangeMessage();
        msg.focus = buf.readUtf(64);
        return msg;
    }

    public static void handle(RequestFocusChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity sender = ctx.get().getSender();
            if (sender == null) return;
            ItemStack main = sender.getMainHandItem();
            ItemStack off = sender.getOffhandItem();
            boolean applied = false;
            if (!main.isEmpty() && main.getItem().getRegistryName() != null
                    && Thaumcraft.MODID.equals(main.getItem().getRegistryName().getNamespace())) {
                main.getOrCreateTag().putString("tc_focus", msg.focus);
                applied = true;
            }
            if (!applied && !off.isEmpty() && off.getItem().getRegistryName() != null
                    && Thaumcraft.MODID.equals(off.getItem().getRegistryName().getNamespace())) {
                off.getOrCreateTag().putString("tc_focus", msg.focus);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


