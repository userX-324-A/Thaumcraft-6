package thaumcraft.common.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import thaumcraft.Thaumcraft;
import thaumcraft.common.menu.ArcaneWorkbenchMenu;

public class ArcaneWorkbenchScreen extends ContainerScreen<ArcaneWorkbenchMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Thaumcraft.MODID, "textures/gui/arcaneworkbench.png");

    public ArcaneWorkbenchScreen(ArcaneWorkbenchMenu container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        this.imageWidth = 190;
        this.imageHeight = 234;
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        this.getMinecraft().getTextureManager().bind(TEXTURE);
        blit(ms, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}



