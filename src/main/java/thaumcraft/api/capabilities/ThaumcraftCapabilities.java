package thaumcraft.api.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.common.capabilities.CapabilityInit;

import javax.annotation.Nonnull;

public class ThaumcraftCapabilities {
    public static IPlayerKnowledge getKnowledge(@Nonnull PlayerEntity player) {
        if (CapabilityInit.PLAYER_KNOWLEDGE_CAP == null) return null;
        LazyOptional<IPlayerKnowledge> opt = player.getCapability(CapabilityInit.PLAYER_KNOWLEDGE_CAP);
        return opt.orElse(null);
    }

    public static boolean knowsResearch(@Nonnull PlayerEntity player, @Nonnull String... research) {
        IPlayerKnowledge knowledge = getKnowledge(player);
        if (knowledge == null) return false;
        if (research == null || research.length == 0) return true;
        for (String query : research) {
            if (query == null || query.isEmpty()) continue;
            // Support key@stage syntax via getResearchStage semantics (>= 0 means known and stage satisfied)
            if (knowledge.getResearchStage(query) < 0) return false;
        }
        return true;
    }

    public static boolean knowsResearchStrict(@Nonnull PlayerEntity player, @Nonnull String... research) {
        IPlayerKnowledge knowledge = getKnowledge(player);
        if (knowledge == null) return false;
        if (research == null || research.length == 0) return true;
        for (String query : research) {
            if (query == null || query.isEmpty()) continue;
            if (query.indexOf('@') >= 0) {
                // If a stage is specified, treat satisfied stage as enough
                if (knowledge.getResearchStage(query) < 0) return false;
            } else {
                if (!knowledge.isResearchComplete(query)) return false;
            }
        }
        return true;
    }

    public static IPlayerWarp getWarp(@Nonnull PlayerEntity player) {
        if (thaumcraft.common.capabilities.CapabilityInit.PLAYER_WARP_CAP == null) return null;
        net.minecraftforge.common.util.LazyOptional<IPlayerWarp> opt = player.getCapability(thaumcraft.common.capabilities.CapabilityInit.PLAYER_WARP_CAP);
        return opt.orElse(null);
    }
}

