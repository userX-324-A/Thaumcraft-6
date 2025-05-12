package thaumcraft.common.lib.network.fx;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.events.ServerEvents;

import java.util.function.Supplier;

public class PacketFXBoreDig {
    private final int x;
    private final int y;
    private final int z;
    private final int boreEntityId;
    private final int delay;

    public PacketFXBoreDig(BlockPos pos, int boreEntityId, int delay) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.boreEntityId = boreEntityId;
        this.delay = delay;
    }

    private PacketFXBoreDig(int x, int y, int z, int boreEntityId, int delay) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.boreEntityId = boreEntityId;
        this.delay = delay;
    }

    public static void encode(PacketFXBoreDig message, PacketBuffer buffer) {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
        buffer.writeInt(message.boreEntityId);
        buffer.writeInt(message.delay);
    }

    public static PacketFXBoreDig decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        int boreEntityId = buffer.readInt();
        int delay = buffer.readInt();
        return new PacketFXBoreDig(x, y, z, boreEntityId, delay);
    }

    public static void handle(PacketFXBoreDig message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            processMessage(message);
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void processMessage(PacketFXBoreDig message) {
        try {
            World world = Minecraft.getInstance().level;
            if (world == null) return;

            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            Entity entity = world.getEntity(message.boreEntityId);
            if (entity == null) {
                return;
            }

            BlockState blockState = world.getBlockState(pos);
            if (blockState.isAir()) {
                return;
            }

            int placeholderMetaValue = 0;

            for (int a = 0; a < message.delay; ++a) {
                final BlockPos finalPos = pos;
                final BlockState finalBlockState = blockState;
                final Entity finalEntity = entity;
                final int finalPlaceholderMetaValue = placeholderMetaValue;
                final int finalDelay = message.delay;

                ServerEvents.addRunnableClient(world, () -> {
                    FXDispatcher.INSTANCE.boreDigFx(finalPos.getX(), finalPos.getY(), finalPos.getZ(), finalEntity, finalBlockState, finalPlaceholderMetaValue, finalDelay);
                }, a);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
