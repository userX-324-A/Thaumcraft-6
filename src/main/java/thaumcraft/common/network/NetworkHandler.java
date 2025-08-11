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

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientAuraMessage.class,
                thaumcraft.common.network.msg.ClientAuraMessage::encode,
                thaumcraft.common.network.msg.ClientAuraMessage::decode,
                thaumcraft.common.network.msg.ClientAuraMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXBlockBamfMessage.class,
                thaumcraft.common.network.msg.ClientFXBlockBamfMessage::encode,
                thaumcraft.common.network.msg.ClientFXBlockBamfMessage::decode,
                thaumcraft.common.network.msg.ClientFXBlockBamfMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXPolluteMessage.class,
                thaumcraft.common.network.msg.ClientFXPolluteMessage::encode,
                thaumcraft.common.network.msg.ClientFXPolluteMessage::decode,
                thaumcraft.common.network.msg.ClientFXPolluteMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXBlockArcMessage.class,
                thaumcraft.common.network.msg.ClientFXBlockArcMessage::encode,
                thaumcraft.common.network.msg.ClientFXBlockArcMessage::decode,
                thaumcraft.common.network.msg.ClientFXBlockArcMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXBlockMistMessage.class,
                thaumcraft.common.network.msg.ClientFXBlockMistMessage::encode,
                thaumcraft.common.network.msg.ClientFXBlockMistMessage::decode,
                thaumcraft.common.network.msg.ClientFXBlockMistMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXBoreDigMessage.class,
                thaumcraft.common.network.msg.ClientFXBoreDigMessage::encode,
                thaumcraft.common.network.msg.ClientFXBoreDigMessage::decode,
                thaumcraft.common.network.msg.ClientFXBoreDigMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage.class,
                thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage::encode,
                thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage::decode,
                thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXFocusEffectMessage.class,
                thaumcraft.common.network.msg.ClientFXFocusEffectMessage::encode,
                thaumcraft.common.network.msg.ClientFXFocusEffectMessage::decode,
                thaumcraft.common.network.msg.ClientFXFocusEffectMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage.class,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage::encode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage::decode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage.class,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage::encode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage::decode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXInfusionSourceMessage.class,
                thaumcraft.common.network.msg.ClientFXInfusionSourceMessage::encode,
                thaumcraft.common.network.msg.ClientFXInfusionSourceMessage::decode,
                thaumcraft.common.network.msg.ClientFXInfusionSourceMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXScanSourceMessage.class,
                thaumcraft.common.network.msg.ClientFXScanSourceMessage::encode,
                thaumcraft.common.network.msg.ClientFXScanSourceMessage::decode,
                thaumcraft.common.network.msg.ClientFXScanSourceMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXShieldMessage.class,
                thaumcraft.common.network.msg.ClientFXShieldMessage::encode,
                thaumcraft.common.network.msg.ClientFXShieldMessage::decode,
                thaumcraft.common.network.msg.ClientFXShieldMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXSlashMessage.class,
                thaumcraft.common.network.msg.ClientFXSlashMessage::encode,
                thaumcraft.common.network.msg.ClientFXSlashMessage::decode,
                thaumcraft.common.network.msg.ClientFXSlashMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXSonicMessage.class,
                thaumcraft.common.network.msg.ClientFXSonicMessage::encode,
                thaumcraft.common.network.msg.ClientFXSonicMessage::decode,
                thaumcraft.common.network.msg.ClientFXSonicMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXWispZapMessage.class,
                thaumcraft.common.network.msg.ClientFXWispZapMessage::encode,
                thaumcraft.common.network.msg.ClientFXWispZapMessage::decode,
                thaumcraft.common.network.msg.ClientFXWispZapMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientFXZapMessage.class,
                thaumcraft.common.network.msg.ClientFXZapMessage::encode,
                thaumcraft.common.network.msg.ClientFXZapMessage::decode,
                thaumcraft.common.network.msg.ClientFXZapMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientWarpMessage.class,
                thaumcraft.common.network.msg.ClientWarpMessage::encode,
                thaumcraft.common.network.msg.ClientWarpMessage::decode,
                thaumcraft.common.network.msg.ClientWarpMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientItemToContainerMessage.class,
                thaumcraft.common.network.msg.ClientItemToContainerMessage::encode,
                thaumcraft.common.network.msg.ClientItemToContainerMessage::decode,
                thaumcraft.common.network.msg.ClientItemToContainerMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientSealMessage.class,
                thaumcraft.common.network.msg.ClientSealMessage::encode,
                thaumcraft.common.network.msg.ClientSealMessage::decode,
                thaumcraft.common.network.msg.ClientSealMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientSealFilterMessage.class,
                thaumcraft.common.network.msg.ClientSealFilterMessage::encode,
                thaumcraft.common.network.msg.ClientSealFilterMessage::decode,
                thaumcraft.common.network.msg.ClientSealFilterMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.ClientSyncWarpMessage.class,
                thaumcraft.common.network.msg.ClientSyncWarpMessage::encode,
                thaumcraft.common.network.msg.ClientSyncWarpMessage::decode,
                thaumcraft.common.network.msg.ClientSyncWarpMessage::handle);

        // Serverbound
        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestNoteMessage.class,
                thaumcraft.common.network.msg.RequestNoteMessage::encode,
                thaumcraft.common.network.msg.RequestNoteMessage::decode,
                thaumcraft.common.network.msg.RequestNoteMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestFocusChangeMessage.class,
                thaumcraft.common.network.msg.RequestFocusChangeMessage::encode,
                thaumcraft.common.network.msg.RequestFocusChangeMessage::decode,
                thaumcraft.common.network.msg.RequestFocusChangeMessage::handle);

        CHANNEL.registerMessage(id(), thaumcraft.common.network.msg.RequestItemKeyMessage.class,
                thaumcraft.common.network.msg.RequestItemKeyMessage::encode,
                thaumcraft.common.network.msg.RequestItemKeyMessage::decode,
                thaumcraft.common.network.msg.RequestItemKeyMessage::handle);

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


