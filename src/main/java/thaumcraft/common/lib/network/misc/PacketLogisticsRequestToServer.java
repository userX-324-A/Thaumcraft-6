package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.common.lib.utils.Utils;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;


public class PacketLogisticsRequestToServer implements IMessage
{
    private BlockPos pos;
    private ItemStack stack;
    private Direction side;
    private int stacksize;
    
    public PacketLogisticsRequestToServer() {
    }
    
    public PacketLogisticsRequestToServer(BlockPos pos, Direction side, ItemStack stack, int size) {
        this.pos = pos;
        this.stack = stack;
        this.side = side;
        stacksize = size;
    }

    public PacketLogisticsRequestToServer(PacketBuffer buffer) {
        fromBytes(buffer);
    }
    
    public void toBytes(PacketBuffer buffer) {
        if (pos == null || side == null) {
            buffer.writeBoolean(false);
        }
        else {
            buffer.writeBoolean(true);
            buffer.writeBlockPos(pos);
            buffer.writeByte(side.getIndex());
        }
        Utils.writeItemStackToBuffer(buffer, stack);
        buffer.writeInt(stacksize);
    }
    
    public void fromBytes(PacketBuffer buffer) {
        if (buffer.readBoolean()) {
            pos = buffer.readBlockPos();
            side = Direction.values()[buffer.readByte()];
        }
        stack = Utils.readItemStackFromBuffer(buffer);
        stacksize = buffer.readInt();
    }
    
    public static void handle(PacketLogisticsRequestToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            ServerWorld world = player.getServerWorld();
            
            int ui = 0;
            while (message.stacksize > 0) {
                ItemStack s = message.stack.copy();
                s.setCount(Math.min(message.stacksize, s.getMaxStackSize()));
                message.stacksize -= s.getCount();
                if (message.pos != null) {
                    GolemHelper.requestProvisioning(world, message.pos, message.side, s, ui);
                }
                else {
                    GolemHelper.requestProvisioning(world, player, s, ui);
                }
                ++ui;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
