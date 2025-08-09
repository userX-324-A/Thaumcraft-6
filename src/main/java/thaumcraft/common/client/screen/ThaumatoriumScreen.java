package thaumcraft.common.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import thaumcraft.Thaumcraft;
import thaumcraft.common.menu.ThaumatoriumMenu;

public class ThaumatoriumScreen extends ContainerScreen<ThaumatoriumMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumatorium.png");

    public ThaumatoriumScreen(ThaumatoriumMenu container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        this.getMinecraft().getTextureManager().bind(TEXTURE);
        blit(ms, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        // progress bar: x=79,y=34 size 24x16 (example), scaled by cook progress
        int w = this.menu.getCookProgressScaled(24);
        if (w > 0) blit(ms, leftPos + 79, topPos + 34, 176, 0, w, 16);
    }
}



