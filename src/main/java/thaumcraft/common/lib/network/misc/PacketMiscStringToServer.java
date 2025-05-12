package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.container.ContainerLogistics;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;


public class PacketMiscStringToServer implements IMessage
{
    private int id;
    private String text;
    
    public PacketMiscStringToServer() {
    }
    
    public PacketMiscStringToServer(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public PacketMiscStringToServer(PacketBuffer buffer) {
        fromBytes(buffer);
    }
    
    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(id);
        buffer.writeString(text);
    }
    
    public void fromBytes(PacketBuffer buffer) {
        id = buffer.readInt();
        text = buffer.readString(32767);
    }
    
    public static void handle(PacketMiscStringToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            if (message.id == 0 && player.openContainer instanceof ContainerLogistics) {
                ContainerLogistics container = (ContainerLogistics)player.openContainer;
                container.searchText = message.text;
                container.refreshItemList(true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
