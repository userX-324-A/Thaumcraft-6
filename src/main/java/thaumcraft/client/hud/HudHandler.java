package thaumcraft.client.hud;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Minimal 1.16.5 HUD handler showing knowledge gain toasts/text.
 * This intentionally omits the complex 1.12 GL stack; we just draw fading text.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class HudHandler {
    private static final int DISPLAY_TICKS = thaumcraft.common.config.ModConfig.COMMON.hudKnowledgeDisplayTicks.get();
    private static int sanityTicker;
    private static String lastSanityText;

    private static final Deque<KnowledgeGainTracker> queue = new ArrayDeque<>();
    private static final Deque<StringTextComponent> researchQueue = new ArrayDeque<>();

    // Minimal aura cache for HUD and other client feedback. Populated by ClientAuraMessage.
    public static volatile short auraBase;
    public static volatile float auraVis;
    public static volatile float auraFlux;

    private HudHandler() {}

    public static void enqueueKnowledgeGain(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category) {
        synchronized (queue) {
            queue.addLast(new KnowledgeGainTracker(type, category, DISPLAY_TICKS));
            // Bound queue size to avoid unbounded growth in rare spam cases
            while (queue.size() > 6) {
                queue.removeFirst();
            }
        }
    }

    public static void enqueueResearchComplete(thaumcraft.api.research.ResearchEntry entry) {
        synchronized (researchQueue) {
            researchQueue.addLast(new StringTextComponent(new TranslationTextComponent("hud.thaumcraft.research_complete", entry.getLocalizedName()).getString()));
            while (researchQueue.size() > 4) researchQueue.removeFirst();
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!thaumcraft.common.config.ModConfig.CLIENT.enableHudOverlays.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        // Render simple stacked text at top-center
        MatrixStack ms = event.getMatrixStack();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int y = 8;
        synchronized (queue) {
            // Remove expired entries first
            queue.removeIf(KnowledgeGainTracker::isExpired);
            for (KnowledgeGainTracker tracker : queue) {
                float alpha = tracker.getAlpha();
                if (alpha <= 0.01f) continue;
                String title = buildTitle(tracker);
                ITextComponent txt = new StringTextComponent(title);
                int textWidth = mc.font.width(txt.getString());
                int x = (screenWidth - textWidth) / 2;
                int color = ((int)(alpha * 255) << 24) | 0xFFFFFF; // fade alpha, white text
                mc.font.draw(ms, txt, x, y, color);
                y += 10;
                tracker.tick();
            }
        }
        // Render research completion messages below
        synchronized (researchQueue) {
            for (StringTextComponent msg : researchQueue) {
                int textWidth = mc.font.width(msg.getString());
                int x = (screenWidth - textWidth) / 2;
                mc.font.draw(ms, msg, x, y, 0xFFFFFF);
                y += 10;
            }
            researchQueue.clear();
        }

        // Sanity checker smooth cadence (draws for sanityTicker frames)
        if (sanityTicker > 0 && lastSanityText != null && !lastSanityText.isEmpty() && thaumcraft.common.config.ModConfig.CLIENT.showSanityOverlay.get()) {
            int textWidth = mc.font.width(lastSanityText);
            int x = (screenWidth - textWidth) / 2;
            mc.font.draw(ms, lastSanityText, x, y, 0xFFFFFF);
            sanityTicker--;
        }
    }

    public static void setSanityOverlay(String text, int ticks) {
        lastSanityText = text;
        sanityTicker = Math.max(0, ticks);
    }

    private static String buildTitle(KnowledgeGainTracker tracker) {
        String type = tracker.type.name();
        String cat = (tracker.category != null ? tracker.category.key : "");
        if (!thaumcraft.common.config.ModConfig.CLIENT.showKnowledgeToasts.get()) return "";
        if (cat.isEmpty()) {
            return new TranslationTextComponent("hud.thaumcraft.knowledge_gain", type, "").getString().replace(" []", "");
        }
        return new TranslationTextComponent("hud.thaumcraft.knowledge_gain", type, cat).getString();
    }

    private static final class KnowledgeGainTracker {
        private final IPlayerKnowledge.EnumKnowledgeType type;
        private final ResearchCategory category;
        private int ticksRemaining;

        private KnowledgeGainTracker(IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category, int ticks) {
            this.type = type;
            this.category = category;
            this.ticksRemaining = ticks;
        }

        private void tick() {
            if (ticksRemaining > 0) ticksRemaining--;
        }

        private boolean isExpired() {
            return ticksRemaining <= 0;
        }

        private float getAlpha() {
            // Fade out during last 20 ticks
            if (ticksRemaining <= 0) return 0f;
            if (ticksRemaining >= DISPLAY_TICKS - 5) {
                // quick fade-in
                int shown = DISPLAY_TICKS - ticksRemaining;
                return Math.min(1f, shown / 5f);
            }
            if (ticksRemaining <= 20) {
                return ticksRemaining / 20f;
            }
            return 1f;
        }
    }
}



