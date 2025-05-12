package thaumcraft.common.lib.network.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;
import thaumcraft.common.tiles.crafting.TilePedestal;
import java.util.function.Supplier;


public class PacketFXInfusionSource
{
    private long p1;
    private long p2;
    private int color;

    public PacketFXInfusionSource() {
    }

    public PacketFXInfusionSource(BlockPos pos, BlockPos pos2, int color) {
        p1 = pos.toLong();
        p2 = pos2.toLong();
        this.color = color;
    }
    
    public PacketFXInfusionSource(PacketBuffer buffer) {
        p1 = buffer.readLong();
        p2 = buffer.readLong();
        color = buffer.readInt();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeLong(p1);
        buffer.writeLong(p2);
        buffer.writeInt(color);
    }


    public static void handle(PacketFXInfusionSource message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) return;

            BlockPos p1Pos = BlockPos.of(message.p1);
            BlockPos p2Pos = BlockPos.of(message.p2);
            String key = p2Pos.getX() + ":" + p2Pos.getY() + ":" + p2Pos.getZ() + ":" + message.color;
            
            TileEntity tile = world.getBlockEntity(p1Pos);
            if (tile instanceof TileInfusionMatrix) {
                int count = 15;
                TileEntity pedestalTile = world.getBlockEntity(p2Pos);
                if (pedestalTile instanceof TilePedestal) {
                    count = 60;
                }
                TileInfusionMatrix is = (TileInfusionMatrix) tile;
                if (is.sourceFX.containsKey(key)) {
                    TileInfusionMatrix.SourceFX sf = is.sourceFX.get(key);
                    sf.ticks = count;
                    // No need to put it back explicitly unless the map implementation requires it 
                    // for ordering, but HashMap/ConcurrentHashMap don't.
                    // is.sourceFX.put(key, sf); 
                } else {
                    is.sourceFX.put(key, is.new SourceFX(p2Pos, count, message.color));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
