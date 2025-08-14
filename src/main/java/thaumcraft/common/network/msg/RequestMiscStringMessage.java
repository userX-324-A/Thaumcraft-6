package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Serverbound: generic string message with an id for routing. Replace old PacketMiscStringToServer.
 */
public class RequestMiscStringMessage {
    private int id;
    private String text;

    public RequestMiscStringMessage() {}

    public RequestMiscStringMessage(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static void encode(RequestMiscStringMessage msg, PacketBuffer buf) {
        buf.writeVarInt(msg.id);
        buf.writeUtf(msg.text);
    }

    public static RequestMiscStringMessage decode(PacketBuffer buf) {
        RequestMiscStringMessage msg = new RequestMiscStringMessage();
        msg.id = buf.readVarInt();
        msg.text = buf.readUtf(256);
        return msg;
    }

    public static void handle(RequestMiscStringMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity sender = ctx.get().getSender();
            if (sender == null) return;
            if (msg.id == 0) {
                // Placeholder: echo back to player for now; replace when Logistics GUI is ported
                sender.displayClientMessage(new net.minecraft.util.text.TranslationTextComponent("chat.thaumcraft.search", msg.text), true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



