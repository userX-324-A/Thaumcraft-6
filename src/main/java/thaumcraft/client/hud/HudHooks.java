package thaumcraft.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;

/**
 * Temporary client hooks to bridge HUD/sound feedback during migration.
 * Replace with proper event/render system once client-old is ported.
 */
public final class HudHooks {
    private HudHooks() {}

    public static void onKnowledgeGained(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category) {
        HudHandler.enqueueKnowledgeGain(type, category);
    }

    public static void playKnowledgeGainSound() {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return;
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("thaumcraft", "learn"));
        if (sound != null) {
            Minecraft.getInstance().level.playSound(
                    Minecraft.getInstance().player,
                    Minecraft.getInstance().player.blockPosition(),
                    sound,
                    SoundCategory.AMBIENT,
                    1.0f,
                    1.0f
            );
        }
    }

    public static void queueResearchToast(ResearchEntry entry) {
        // Until a proper toast is ported, reuse the HUD text for research completion
        if (entry == null) return;
        // Could be extended to show a real Toast using net.minecraft.client.gui.toasts system
        // For now, just enqueue a knowledge-like message
        HudHandler.enqueueResearchComplete(entry);
        playKnowledgeGainSound();
    }
}


