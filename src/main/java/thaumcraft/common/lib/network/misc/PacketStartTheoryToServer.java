package thaumcraft.common.lib.network.misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.tiles.crafting.TileResearchTable;

import java.util.function.Supplier;

public class PacketStartTheoryToServer {
    private final long pos;
    private final Set<String> aids;
    
    public PacketStartTheoryToServer(BlockPos pos, Set<String> aids) {
        this.pos = pos.toLong();
        this.aids = new HashSet<>(aids);
    }
    
    private PacketStartTheoryToServer(long pos, Set<String> aids) {
        this.pos = pos;
        this.aids = aids;
    }
    
    public static void encode(PacketStartTheoryToServer message, PacketBuffer buffer) {
        buffer.writeLong(message.pos);
        buffer.writeVarInt(message.aids.size());
        for (String aid : message.aids) {
            buffer.writeUtf(aid);
        }
    }
    
    public static PacketStartTheoryToServer decode(PacketBuffer buffer) {
        long pos = buffer.readLong();
        int size = buffer.readVarInt();
        Set<String> aids = new HashSet<>(size);
        for (int i = 0; i < size; ++i) {
            aids.add(buffer.readUtf(32767));
        }
        return new PacketStartTheoryToServer(pos, aids);
    }
    
    public static void handle(PacketStartTheoryToServer message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                ServerWorld world = player.getLevel();
                BlockPos bp = BlockPos.of(message.pos);
                if (world != null && world.isLoaded(bp)) {
                    TileEntity te = world.getBlockEntity(bp);
                    if (te instanceof TileResearchTable) {
                        ((TileResearchTable) te).startNewTheory(player, message.aids);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
