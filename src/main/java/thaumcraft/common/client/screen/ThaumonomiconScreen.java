package thaumcraft.common.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.Thaumcraft;

import java.util.Map;

/**
 * Minimal Thaumonomicon screen for 1.16.5 port:
 * - Draws categories in a scrollable canvas with their bounds.
 * - Draws entry dots within category bounds. No interactions yet.
 */
public class ThaumonomiconScreen extends Screen {
    private static final ResourceLocation BOOK_BG = new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_1.jpg");
    private int left;
    private int top;
    private int innerWidth = 240;
    private int innerHeight = 180;

    public ThaumonomiconScreen() {
        super((ITextComponent) new StringTextComponent("Thaumonomicon"));
    }

    @Override
    protected void init() {
        super.init();
        this.left = (this.width - innerWidth) / 2;
        this.top = (this.height - innerHeight) / 2;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        Minecraft.getInstance().getTextureManager().bind(BOOK_BG);
        blit(ms, left, top, 0, 0, innerWidth, innerHeight);

        // Layout: iterate categories, draw a title and a simple rectangle for bounds, then entries as dots
        int y = top + 8;
        for (Map.Entry<String, ResearchCategory> entry : ResearchCategories.researchCategories.entrySet()) {
            String catKey = entry.getKey();
            ResearchCategory cat = entry.getValue();
            drawString(ms, this.font, ResearchCategories.getCategoryName(catKey), left + 8, y, 0xFFEED0);
            int cLeft = left + 8;
            int cTop = y + 10;
            int cRight = left + innerWidth - 8;
            int cBottom = Math.min(top + innerHeight - 8, cTop + 48);
            // simple border using fill rectangles
            fill(ms, cLeft, cTop, cRight, cTop + 1, 0x80FFFFFF);
            fill(ms, cLeft, cBottom - 1, cRight, cBottom, 0x80FFFFFF);
            fill(ms, cLeft, cTop, cLeft + 1, cBottom, 0x80FFFFFF);
            fill(ms, cRight - 1, cTop, cRight, cBottom, 0x80FFFFFF);

            // Plot entries within bounds, normalized from category min/max
            int minX = Math.min(cat.minDisplayColumn, 0);
            int minY = Math.min(cat.minDisplayRow, 0);
            int maxX = Math.max(cat.maxDisplayColumn, minX + 1);
            int maxY = Math.max(cat.maxDisplayRow, minY + 1);
            int w = cRight - cLeft - 8;
            int h = cBottom - cTop - 8;
            for (ResearchEntry re : cat.research.values()) {
                float nx = (re.getDisplayColumn() - minX) / (float)(maxX - minX);
                float ny = (re.getDisplayRow() - minY) / (float)(maxY - minY);
                int px = cLeft + 4 + Math.round(nx * w);
                int py = cTop + 4 + Math.round(ny * h);
                fill(ms, px - 2, py - 2, px + 2, py + 2, 0xFFFFCC00);
            }

            y = cBottom + 8;
            if (y > top + innerHeight - 24) break; // minimal paging; no scrolling yet
        }

        super.render(ms, mouseX, mouseY, partialTicks);
    }
}


