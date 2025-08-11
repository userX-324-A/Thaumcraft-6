package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.registers.SoundsTC;

import java.util.function.Supplier;

/**
 * Clientbound: notifies the player of warp changes and plays whispers when appropriate.
 * Replacement for 1.12 PacketWarpMessage.
 */
public class ClientWarpMessage {
    private int amount;
    private byte type; // 0: normal, 1: sticky, else: temp

    public ClientWarpMessage() {}

    public ClientWarpMessage(byte type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public static void encode(ClientWarpMessage msg, PacketBuffer buf) {
        buf.writeInt(msg.amount);
        buf.writeByte(msg.type);
    }

    public static ClientWarpMessage decode(PacketBuffer buf) {
        ClientWarpMessage m = new ClientWarpMessage();
        m.amount = buf.readInt();
        m.type = buf.readByte();
        return m;
    }

    public static void handle(ClientWarpMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.amount == 0) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            ITextComponent text = null;
            if (msg.type == 0) {
                if (msg.amount > 0) {
                    text = new TranslationTextComponent("tc.addwarp");
                    mc.player.playSound(SoundsTC.WHISPERS.get(), 0.5f, 1.0f);
                } else {
                    text = new TranslationTextComponent("tc.removewarp");
                }
            } else if (msg.type == 1) {
                if (msg.amount > 0) {
                    text = new TranslationTextComponent("tc.addwarpsticky");
                    mc.player.playSound(SoundsTC.WHISPERS.get(), 0.5f, 1.0f);
                } else {
                    text = new TranslationTextComponent("tc.removewarpsticky");
                }
            } else {
                text = new TranslationTextComponent(msg.amount > 0 ? "tc.addwarptemp" : "tc.removewarptemp");
            }

            if (text != null) {
                // show as status message (action bar) like 1.12 sendStatusMessage(..., true)
                try {
                    mc.player.displayClientMessage(text, true);
                } catch (Throwable t) {
                    // Fallback for older naming in some mappings
                    mc.player.sendStatusMessage(text, true);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


