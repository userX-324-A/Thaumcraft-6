package thaumcraft.common.lib.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.network.NetworkDirection;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.network.fx.PacketFXBlockBamf;
import thaumcraft.common.lib.network.fx.PacketFXBlockMist;
import thaumcraft.common.lib.network.fx.PacketFXBoreDig;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.lib.network.fx.PacketFXFocusEffect;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpactBurst;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSource;
import thaumcraft.common.lib.network.fx.PacketFXPollute;
import thaumcraft.common.lib.network.fx.PacketFXScanSource;
import thaumcraft.common.lib.network.fx.PacketFXShield;
import thaumcraft.common.lib.network.fx.PacketFXSlash;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;
import thaumcraft.common.lib.network.fx.PacketFXZap;
import thaumcraft.common.lib.network.misc.PacketAuraToClient;
import thaumcraft.common.lib.network.misc.PacketBiomeChange;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;
import thaumcraft.common.lib.network.misc.PacketItemKeyToServer;
import thaumcraft.common.lib.network.misc.PacketItemToClientContainer;
import thaumcraft.common.lib.network.misc.PacketKnowledgeGain;
import thaumcraft.common.lib.network.misc.PacketLogisticsRequestToServer;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.misc.PacketMiscStringToServer;
import thaumcraft.common.lib.network.misc.PacketNote;
import thaumcraft.common.lib.network.misc.PacketSealFilterToClient;
import thaumcraft.common.lib.network.misc.PacketSealToClient;
import thaumcraft.common.lib.network.misc.PacketSelectThaumotoriumRecipeToServer;
import thaumcraft.common.lib.network.misc.PacketStartTheoryToServer;
import thaumcraft.common.lib.network.playerdata.PacketFocusNameToServer;
import thaumcraft.common.lib.network.playerdata.PacketFocusNodesToServer;
import thaumcraft.common.lib.network.playerdata.PacketPlayerFlagToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncKnowledge;
import thaumcraft.common.lib.network.playerdata.PacketSyncProgressToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearchFlagsToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;
import thaumcraft.common.lib.network.tiles.PacketTileToClient;
import thaumcraft.common.lib.network.tiles.PacketTileToServer;


public class PacketHandler
{
    public static SimpleChannel INSTANCE;
    private static final String PROTOCOL_VERSION = "1";

    public static void preInit() {
        int idx = 0;

        INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("thaumcraft", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
        );

        // Register PacketFXFocusPartImpact
        INSTANCE.messageBuilder(PacketFXFocusPartImpact.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(PacketFXFocusPartImpact::encode)
            .decoder(PacketFXFocusPartImpact::decode)
            .consumerMainThread(PacketFXFocusPartImpact::handle)
            .add();

        // Register PacketMiscEvent
        INSTANCE.messageBuilder(PacketMiscEvent.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketMiscEvent::encode)
                .decoder(PacketMiscEvent::decode)
                .consumerMainThread(PacketMiscEvent::handle)
                .add();

        // Register PacketBiomeChange
        INSTANCE.messageBuilder(PacketBiomeChange.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketBiomeChange::encode)
                .decoder(PacketBiomeChange::decode)
                .consumerMainThread(PacketBiomeChange::handle)
                .add();

        // Register PacketKnowledgeGain
        INSTANCE.messageBuilder(PacketKnowledgeGain.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketKnowledgeGain::encode)
                .decoder(PacketKnowledgeGain::decode)
                .consumerMainThread(PacketKnowledgeGain::handle)
                .add();

        // Register PacketStartTheoryToServer
        INSTANCE.messageBuilder(PacketStartTheoryToServer.class, idx++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketStartTheoryToServer::encode)
                .decoder(PacketStartTheoryToServer::decode)
                .consumerMainThread(PacketStartTheoryToServer::handle)
                .add();

        // Register PacketFXBoreDig
        INSTANCE.messageBuilder(PacketFXBoreDig.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXBoreDig::encode)
                .decoder(PacketFXBoreDig::decode)
                .consumerMainThread(PacketFXBoreDig::handle)
                .add();

        // Register PacketFXBlockArc
        INSTANCE.messageBuilder(PacketFXBlockArc.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXBlockArc::encode)
                .decoder(PacketFXBlockArc::decode)
                .consumerMainThread(PacketFXBlockArc::handle)
                .add();

        // Register PacketFXBlockBamf
        INSTANCE.messageBuilder(PacketFXBlockBamf.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXBlockBamf::encode)
                .decoder(PacketFXBlockBamf::decode)
                .consumerMainThread(PacketFXBlockBamf::handle)
                .add();

        // Register PacketFXBlockMist
        INSTANCE.messageBuilder(PacketFXBlockMist.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXBlockMist::toBytes)
                .decoder(PacketFXBlockMist::new)
                .consumerMainThread(PacketFXBlockMist::handle)
                .add();

        // Register PacketFXEssentiaSource
        INSTANCE.messageBuilder(PacketFXEssentiaSource.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXEssentiaSource::toBytes)
                .decoder(PacketFXEssentiaSource::new)
                .consumerMainThread(PacketFXEssentiaSource::handle)
                .add();

        // Register PacketFXFocusEffect
        INSTANCE.messageBuilder(PacketFXFocusEffect.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXFocusEffect::toBytes)
                .decoder(PacketFXFocusEffect::new)
                .consumerMainThread(PacketFXFocusEffect::handle)
                .add();

        // Register PacketFXFocusPartImpactBurst
        INSTANCE.messageBuilder(PacketFXFocusPartImpactBurst.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXFocusPartImpactBurst::toBytes)
                .decoder(PacketFXFocusPartImpactBurst::new)
                .consumerMainThread(PacketFXFocusPartImpactBurst::handle)
                .add();

        // Register PacketFXInfusionSource
        INSTANCE.messageBuilder(PacketFXInfusionSource.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXInfusionSource::toBytes)
                .decoder(PacketFXInfusionSource::new)
                .consumerMainThread(PacketFXInfusionSource::handle)
                .add();

        // Register PacketFXPollute
        INSTANCE.messageBuilder(PacketFXPollute.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXPollute::encode)
                .decoder(PacketFXPollute::new)
                .consumerMainThread(PacketFXPollute::handle)
                .add();

        // Register PacketFXScanSource
        INSTANCE.messageBuilder(PacketFXScanSource.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXScanSource::encode)
                .decoder(PacketFXScanSource::new)
                .consumerMainThread(PacketFXScanSource::handle)
                .add();

        // Register PacketFXShield
        INSTANCE.messageBuilder(PacketFXShield.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXShield::encode)
                .decoder(PacketFXShield::new)
                .consumerMainThread(PacketFXShield::handle)
                .add();

        // Register PacketFXSlash
        INSTANCE.messageBuilder(PacketFXSlash.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXSlash::encode)
                .decoder(PacketFXSlash::new)
                .consumerMainThread(PacketFXSlash::handle)
                .add();

        // Register PacketFXSonic
        INSTANCE.messageBuilder(PacketFXSonic.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXSonic::encode)
                .decoder(PacketFXSonic::new)
                .consumerMainThread(PacketFXSonic::handle)
                .add();

        // Register PacketFXWispZap
        INSTANCE.messageBuilder(PacketFXWispZap.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXWispZap::encode)
                .decoder(PacketFXWispZap::decode)
                .consumerMainThread(PacketFXWispZap::handle)
                .add();

        // Register PacketFXZap
        INSTANCE.messageBuilder(PacketFXZap.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketFXZap::encode)
                .decoder(PacketFXZap::decode)
                .consumerMainThread(PacketFXZap::handle)
                .add();

        // Register PacketItemToClientContainer
        INSTANCE.messageBuilder(PacketItemToClientContainer.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketItemToClientContainer::toBytes)
                .decoder(PacketItemToClientContainer::new)
                .consumerMainThread(PacketItemToClientContainer::handle)
                .add();

        // Register PacketAuraToClient
        INSTANCE.messageBuilder(PacketAuraToClient.class, idx++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketAuraToClient::encode)
                .decoder(PacketAuraToClient::decode)
                .consumerMainThread(PacketAuraToClient::handle)
                .add();

        // Register PacketFocusChangeToServer
        INSTANCE.messageBuilder(PacketFocusChangeToServer.class, idx++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketFocusChangeToServer::encode)
                .decoder(PacketFocusChangeToServer::decode)
                .consumerMainThread(PacketFocusChangeToServer::handle)
                .add();

        // Register PacketItemKeyToServer
        INSTANCE.messageBuilder(PacketItemKeyToServer.class, idx++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketItemKeyToServer::encode)
                .decoder(PacketItemKeyToServer::decode)
                .consumerMainThread(PacketItemKeyToServer::handle)
                .add();

        // Register PacketLogisticsRequestToServer
        INSTANCE.messageBuilder(PacketLogisticsRequestToServer.class, idx++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketLogisticsRequestToServer::toBytes)
                .decoder(PacketLogisticsRequestToServer::new)
                .consumerMainThread(PacketLogisticsRequestToServer::handle)
                .add();

        // Register PacketMiscStringToServer
        INSTANCE.messageBuilder(PacketMiscStringToServer.class, idx++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketMiscStringToServer::toBytes)
                .decoder(PacketMiscStringToServer::new)
                .consumerMainThread(PacketMiscStringToServer::handle)
                .add();

        // Register PacketNote (Client <-> Server)
        INSTANCE.messageBuilder(PacketNote.class, idx++, NetworkDirection.PLAY_TO_SERVER) // Primary direction: client request
                .encoder(PacketNote::toBytes)
                .decoder(PacketNote::new)
                .consumerMainThread(PacketNote::handle)
                .add();

        // TODO: All packet classes below need to be updated for the 1.16.5 networking system.
        // This includes adding static encode(MSG, FriendlyByteBuf), decode(FriendlyByteBuf),
        // and handle(MSG, Supplier<NetworkEvent.Context>) methods to each packet class.
        // The IMessageHandler interface is no longer used by the packet class itself.

        // Commenting out all old registrations. These need to be redone one by one.
        /*
        PacketHandler.INSTANCE.registerMessage(PacketBiomeChange.class, PacketBiomeChange.class, idx++, Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage(PacketMiscEvent.class, PacketMiscEvent.class, idx++, Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage(PacketKnowledgeGain.class, PacketKnowledgeGain.class, idx++, Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage(PacketStartTheoryToServer.class, PacketStartTheoryToServer.class, idx++, Side.SERVER);
        // ... and so on for all other packet registrations ...
        PacketHandler.INSTANCE.registerMessage(PacketFXBoreDig.class, PacketFXBoreDig.class, idx++, Side.CLIENT);
        */
    }
}
