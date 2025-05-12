package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.api.distmarker.Dist;
import thaumcraft.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.tiles.devices.TileArcaneEar;

import java.util.function.Supplier;

public class PacketNote implements IMessage
{
    private BlockPos pos;
    private RegistryKey<World> dim;
    private byte note;
    
    public PacketNote() {
    }
    
    public PacketNote(BlockPos pos, RegistryKey<World> dim) {
        this.pos = pos;
        this.dim = dim;
        this.note = -1;
    }
    
    public PacketNote(BlockPos pos, RegistryKey<World> dim, byte note) {
        this.pos = pos;
        this.dim = dim;
        this.note = note;
    }

    public PacketNote(PacketBuffer buffer) {
        fromBytes(buffer);
    }
    
    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeResourceLocation(dim.getLocation());
        buffer.writeByte(note);
    }
    
    public void fromBytes(PacketBuffer buffer) {
        pos = buffer.readBlockPos();
        dim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation());
        note = buffer.readByte();
    }
    
    public static void handle(PacketNote message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null && message.note == -1) {
                ServerWorld world = player.getServer().getWorld(message.dim);
                if (world == null) return;
                
                TileEntity tile = world.getTileEntity(message.pos);
                byte actualNote = -1;
                if (tile instanceof TileEntityNote) {
                    actualNote = ((TileEntityNote)tile).note;
                }
                else if (tile instanceof TileArcaneEar) {
                    actualNote = ((TileArcaneEar)tile).note;
                }
                
                if (actualNote >= 0) {
                    PacketNote response = new PacketNote(message.pos, message.dim, actualNote);
                    PacketDistributor.TargetPoint target = new PacketDistributor.TargetPoint(message.pos.getX() + 0.5, message.pos.getY() + 0.5, message.pos.getZ() + 0.5, 8.0, message.dim);
                    PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> target), response);
                }
            }
            else if (player == null && message.note >= 0) {
                 DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
                    World world = Minecraft.getInstance().world;
                    if (world != null && world.getDimensionKey() == message.dim) { 
                        TileEntity tile = world.getTileEntity(message.pos);
                        if (tile instanceof TileEntityNote) {
                            ((TileEntityNote)tile).note = message.note;
                        } else if (tile instanceof TileArcaneEar) {
                            ((TileArcaneEar)tile).note = message.note;
                        }
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}
