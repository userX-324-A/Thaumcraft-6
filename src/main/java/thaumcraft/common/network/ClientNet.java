package thaumcraft.common.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientNet {
    private ClientNet() {}

    public static void register() {
        // All clientbound packet registrations live here to avoid classloading on servers
        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientTileUpdateMessage.class,
                thaumcraft.common.network.msg.ClientTileUpdateMessage::encode,
                thaumcraft.common.network.msg.ClientTileUpdateMessage::decode,
                thaumcraft.common.network.msg.ClientTileUpdateMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientSyncKnowledgeMessage.class,
                thaumcraft.common.network.msg.ClientSyncKnowledgeMessage::encode,
                thaumcraft.common.network.msg.ClientSyncKnowledgeMessage::decode,
                thaumcraft.common.network.msg.ClientSyncKnowledgeMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientBiomeChangeMessage.class,
                thaumcraft.common.network.msg.ClientBiomeChangeMessage::encode,
                thaumcraft.common.network.msg.ClientBiomeChangeMessage::decode,
                thaumcraft.common.network.msg.ClientBiomeChangeMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientKnowledgeGainMessage.class,
                thaumcraft.common.network.msg.ClientKnowledgeGainMessage::encode,
                thaumcraft.common.network.msg.ClientKnowledgeGainMessage::decode,
                thaumcraft.common.network.msg.ClientKnowledgeGainMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientNoteMessage.class,
                thaumcraft.common.network.msg.ClientNoteMessage::encode,
                thaumcraft.common.network.msg.ClientNoteMessage::decode,
                thaumcraft.common.network.msg.ClientNoteMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientAuraMessage.class,
                thaumcraft.common.network.msg.ClientAuraMessage::encode,
                thaumcraft.common.network.msg.ClientAuraMessage::decode,
                thaumcraft.common.network.msg.ClientAuraMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXBlockBamfMessage.class,
                thaumcraft.common.network.msg.ClientFXBlockBamfMessage::encode,
                thaumcraft.common.network.msg.ClientFXBlockBamfMessage::decode,
                thaumcraft.common.network.msg.ClientFXBlockBamfMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXPolluteMessage.class,
                thaumcraft.common.network.msg.ClientFXPolluteMessage::encode,
                thaumcraft.common.network.msg.ClientFXPolluteMessage::decode,
                thaumcraft.common.network.msg.ClientFXPolluteMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXBlockArcMessage.class,
                thaumcraft.common.network.msg.ClientFXBlockArcMessage::encode,
                thaumcraft.common.network.msg.ClientFXBlockArcMessage::decode,
                thaumcraft.common.network.msg.ClientFXBlockArcMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXBlockMistMessage.class,
                thaumcraft.common.network.msg.ClientFXBlockMistMessage::encode,
                thaumcraft.common.network.msg.ClientFXBlockMistMessage::decode,
                thaumcraft.common.network.msg.ClientFXBlockMistMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXBoreDigMessage.class,
                thaumcraft.common.network.msg.ClientFXBoreDigMessage::encode,
                thaumcraft.common.network.msg.ClientFXBoreDigMessage::decode,
                thaumcraft.common.network.msg.ClientFXBoreDigMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage.class,
                thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage::encode,
                thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage::decode,
                thaumcraft.common.network.msg.ClientFXEssentiaSourceMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXFocusEffectMessage.class,
                thaumcraft.common.network.msg.ClientFXFocusEffectMessage::encode,
                thaumcraft.common.network.msg.ClientFXFocusEffectMessage::decode,
                thaumcraft.common.network.msg.ClientFXFocusEffectMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage.class,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage::encode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage::decode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage.class,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage::encode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage::decode,
                thaumcraft.common.network.msg.ClientFXFocusPartImpactBurstMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXInfusionSourceMessage.class,
                thaumcraft.common.network.msg.ClientFXInfusionSourceMessage::encode,
                thaumcraft.common.network.msg.ClientFXInfusionSourceMessage::decode,
                thaumcraft.common.network.msg.ClientFXInfusionSourceMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXScanSourceMessage.class,
                thaumcraft.common.network.msg.ClientFXScanSourceMessage::encode,
                thaumcraft.common.network.msg.ClientFXScanSourceMessage::decode,
                thaumcraft.common.network.msg.ClientFXScanSourceMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXShieldMessage.class,
                thaumcraft.common.network.msg.ClientFXShieldMessage::encode,
                thaumcraft.common.network.msg.ClientFXShieldMessage::decode,
                thaumcraft.common.network.msg.ClientFXShieldMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXSlashMessage.class,
                thaumcraft.common.network.msg.ClientFXSlashMessage::encode,
                thaumcraft.common.network.msg.ClientFXSlashMessage::decode,
                thaumcraft.common.network.msg.ClientFXSlashMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXSonicMessage.class,
                thaumcraft.common.network.msg.ClientFXSonicMessage::encode,
                thaumcraft.common.network.msg.ClientFXSonicMessage::decode,
                thaumcraft.common.network.msg.ClientFXSonicMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXWispZapMessage.class,
                thaumcraft.common.network.msg.ClientFXWispZapMessage::encode,
                thaumcraft.common.network.msg.ClientFXWispZapMessage::decode,
                thaumcraft.common.network.msg.ClientFXWispZapMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientOpenThaumonomiconMessage.class,
                thaumcraft.common.network.msg.ClientOpenThaumonomiconMessage::encode,
                thaumcraft.common.network.msg.ClientOpenThaumonomiconMessage::decode,
                thaumcraft.common.network.msg.ClientOpenThaumonomiconMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientFXZapMessage.class,
                thaumcraft.common.network.msg.ClientFXZapMessage::encode,
                thaumcraft.common.network.msg.ClientFXZapMessage::decode,
                thaumcraft.common.network.msg.ClientFXZapMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientSyncWarpMessage.class,
                thaumcraft.common.network.msg.ClientSyncWarpMessage::encode,
                thaumcraft.common.network.msg.ClientSyncWarpMessage::decode,
                thaumcraft.common.network.msg.ClientSyncWarpMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientWarpMessage.class,
                thaumcraft.common.network.msg.ClientWarpMessage::encode,
                thaumcraft.common.network.msg.ClientWarpMessage::decode,
                thaumcraft.common.network.msg.ClientWarpMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientItemToContainerMessage.class,
                thaumcraft.common.network.msg.ClientItemToContainerMessage::encode,
                thaumcraft.common.network.msg.ClientItemToContainerMessage::decode,
                thaumcraft.common.network.msg.ClientItemToContainerMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientSealMessage.class,
                thaumcraft.common.network.msg.ClientSealMessage::encode,
                thaumcraft.common.network.msg.ClientSealMessage::decode,
                thaumcraft.common.network.msg.ClientSealMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientSealFilterMessage.class,
                thaumcraft.common.network.msg.ClientSealFilterMessage::encode,
                thaumcraft.common.network.msg.ClientSealFilterMessage::decode,
                thaumcraft.common.network.msg.ClientSealFilterMessage::handle);

        NetworkHandler.CHANNEL.registerMessage(next(), thaumcraft.common.network.msg.ClientSealPropsMessage.class,
                thaumcraft.common.network.msg.ClientSealPropsMessage::encode,
                thaumcraft.common.network.msg.ClientSealPropsMessage::decode,
                thaumcraft.common.network.msg.ClientSealPropsMessage::handle);
    }

    private static int next() {
        // Delegate to NetworkHandler's id counter without exposing it publicly
        try {
            java.lang.reflect.Field f = NetworkHandler.class.getDeclaredField("nextId");
            f.setAccessible(true);
            int v = (int) f.get(null);
            f.set(null, v + 1);
            return v;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}


