package thaumcraft.common.lib.network.misc;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.lib.utils.Utils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;

import java.util.function.Supplier;


public class PacketItemToClientContainer implements IMessage
{
    private int windowId;
    private int slot;
    private ItemStack item;
    
    public PacketItemToClientContainer() {
    }
    
    public PacketItemToClientContainer(int windowIdIn, int slotIn, ItemStack itemIn) {
        windowId = windowIdIn;
        slot = slotIn;
        item = itemIn;
    }
    
    public PacketItemToClientContainer(PacketBuffer buffer) {
        fromBytes(buffer);
    }
    
    public void toBytes(ByteBuf dos) {
        dos.writeInt(windowId);
        dos.writeInt(slot);
        Utils.writeItemStackToBuffer(dos, item);
    }
    
    public void fromBytes(ByteBuf dat) {
        windowId = dat.readInt();
        slot = dat.readInt();
        item = Utils.readItemStackFromBuffer(dat);
    }
    
    public static void handle(PacketItemToClientContainer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
                try {
                     if (Minecraft.getInstance().player.openContainer != null && Minecraft.getInstance().player.openContainer.windowId == message.windowId) {
                        Minecraft.getInstance().player.openContainer.putStackInSlot(message.slot, message.item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
