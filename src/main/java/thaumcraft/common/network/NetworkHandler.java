package thaumcraft.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import thaumcraft.Thaumcraft;

/**
 * 1.16.5 networking entrypoint using SimpleChannel.
 * New packets should be registered here with encode/decode/handle methods.
 */
public final class NetworkHandler {
    private NetworkHandler() {}

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Thaumcraft.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void init() {
        // Register packets here as they are ported to 1.16.5 style.
        // Clientbound messages
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientTileUpdateMessage.class,
                thaumcraft.common.network.msg.ClientTileUpdateMessage::encode,
                thaumcraft.common.network.msg.ClientTileUpdateMessage::decode,
                thaumcraft.common.network.msg.ClientTileUpdateMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientSyncKnowledgeMessage.class,
                thaumcraft.common.network.msg.ClientSyncKnowledgeMessage::encode,
                thaumcraft.common.network.msg.ClientSyncKnowledgeMessage::decode,
                thaumcraft.common.network.msg.ClientSyncKnowledgeMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientBiomeChangeMessage.class,
                thaumcraft.common.network.msg.ClientBiomeChangeMessage::encode,
                thaumcraft.common.network.msg.ClientBiomeChangeMessage::decode,
                thaumcraft.common.network.msg.ClientBiomeChangeMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientKnowledgeGainMessage.class,
                thaumcraft.common.network.msg.ClientKnowledgeGainMessage::encode,
                thaumcraft.common.network.msg.ClientKnowledgeGainMessage::decode,
                thaumcraft.common.network.msg.ClientKnowledgeGainMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientNoteMessage.class,
                thaumcraft.common.network.msg.ClientNoteMessage::encode,
                thaumcraft.common.network.msg.ClientNoteMessage::decode,
                thaumcraft.common.network.msg.ClientNoteMessage::handle);

        // Serverbound
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestNoteMessage.class,
                thaumcraft.common.network.msg.RequestNoteMessage::encode,
                thaumcraft.common.network.msg.RequestNoteMessage::decode,
                thaumcraft.common.network.msg.RequestNoteMessage::handle);
    }

    private static int id() { return nextId++; }

    // Convenience send helpers
    public static void sendTo(ServerPlayerEntity player, Object msg) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendToAllAround(Object msg, ServerWorld world, BlockPos pos, double radius) {
        CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, radius, world.dimension())), msg);
    }
}


