package thaumcraft.common.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import thaumcraft.common.menu.ResearchTableMenu;

/**
 * Minimal placeholder screen for the Research Table to prove the menu wiring.
 */
public class ResearchTableScreen extends ContainerScreen<ResearchTableMenu> {
    public ResearchTableScreen(ResearchTableMenu container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        // No custom background yet
    }
}


