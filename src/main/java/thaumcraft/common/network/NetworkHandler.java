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
        // Clientbound messages are registered from client setup (see ClientOnlySetup)

        // Serverbound
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestNoteMessage.class,
                thaumcraft.common.network.msg.RequestNoteMessage::encode,
                thaumcraft.common.network.msg.RequestNoteMessage::decode,
                thaumcraft.common.network.msg.RequestNoteMessage::handle);

        // Deferred until caster system is fully ported
        // CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestFocusChangeMessage.class,
        //         thaumcraft.common.network.msg.RequestFocusChangeMessage::encode,
        //         thaumcraft.common.network.msg.RequestFocusChangeMessage::decode,
        //         thaumcraft.common.network.msg.RequestFocusChangeMessage::handle);

        // CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestItemKeyMessage.class,
        //         thaumcraft.common.network.msg.RequestItemKeyMessage::encode,
        //         thaumcraft.common.network.msg.RequestItemKeyMessage::decode,
        //         thaumcraft.common.network.msg.RequestItemKeyMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestMiscStringMessage.class,
                thaumcraft.common.network.msg.RequestMiscStringMessage::encode,
                thaumcraft.common.network.msg.RequestMiscStringMessage::decode,
                thaumcraft.common.network.msg.RequestMiscStringMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestThaumatoriumRecipeMessage.class,
                thaumcraft.common.network.msg.RequestThaumatoriumRecipeMessage::encode,
                thaumcraft.common.network.msg.RequestThaumatoriumRecipeMessage::decode,
                thaumcraft.common.network.msg.RequestThaumatoriumRecipeMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestStartTheoryMessage.class,
                thaumcraft.common.network.msg.RequestStartTheoryMessage::encode,
                thaumcraft.common.network.msg.RequestStartTheoryMessage::decode,
                thaumcraft.common.network.msg.RequestStartTheoryMessage::handle);

        // Player data serverbound (migrated from Packet*ToServer)
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestPlayerFlagMessage.class,
                thaumcraft.common.network.msg.RequestPlayerFlagMessage::encode,
                thaumcraft.common.network.msg.RequestPlayerFlagMessage::decode,
                thaumcraft.common.network.msg.RequestPlayerFlagMessage::handle);
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestSyncProgressMessage.class,
                thaumcraft.common.network.msg.RequestSyncProgressMessage::encode,
                thaumcraft.common.network.msg.RequestSyncProgressMessage::decode,
                thaumcraft.common.network.msg.RequestSyncProgressMessage::handle);
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestSyncResearchFlagsMessage.class,
                thaumcraft.common.network.msg.RequestSyncResearchFlagsMessage::encode,
                thaumcraft.common.network.msg.RequestSyncResearchFlagsMessage::decode,
                thaumcraft.common.network.msg.RequestSyncResearchFlagsMessage::handle);
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestFocusNameMessage.class,
                thaumcraft.common.network.msg.RequestFocusNameMessage::encode,
                thaumcraft.common.network.msg.RequestFocusNameMessage::decode,
                thaumcraft.common.network.msg.RequestFocusNameMessage::handle);
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestFocusNodesMessage.class,
                thaumcraft.common.network.msg.RequestFocusNodesMessage::encode,
                thaumcraft.common.network.msg.RequestFocusNodesMessage::decode,
                thaumcraft.common.network.msg.RequestFocusNodesMessage::handle);

        // Serverbound seal edits
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestSealPropsChangeMessage.class,
                thaumcraft.common.network.msg.RequestSealPropsChangeMessage::encode,
                thaumcraft.common.network.msg.RequestSealPropsChangeMessage::decode,
                thaumcraft.common.network.msg.RequestSealPropsChangeMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestSealFilterChangeMessage.class,
                thaumcraft.common.network.msg.RequestSealFilterChangeMessage::encode,
                thaumcraft.common.network.msg.RequestSealFilterChangeMessage::decode,
                thaumcraft.common.network.msg.RequestSealFilterChangeMessage::handle);

        // Future: If we add a dedicated ClientSealTogglesMessage, register it above with other clientbound messages.
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


