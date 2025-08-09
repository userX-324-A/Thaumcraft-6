package thaumcraft.api.capabilities;

import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;

public class ThaumcraftCapabilities {
    public static IPlayerKnowledge getKnowledge(@Nonnull PlayerEntity player) {
        // During migration we may not have a real Provider constant; return null to mean "no research"
        return null;
    }
    
    public static boolean knowsResearch(@Nonnull PlayerEntity player, @Nonnull String... research) {
        // With no persisted knowledge yet, treat as unknown unless empty string passed
        if (research == null) return false;
        for (String r : research) {
            if (r != null && !r.isEmpty()) return false;
        }
        return true;
    }
    
    public static boolean knowsResearchStrict(@Nonnull PlayerEntity player, @Nonnull String... research) {
        return knowsResearch(player, research);
    }
    
    public static IPlayerWarp getWarp(@Nonnull PlayerEntity player) {
        return null;
    }
}

