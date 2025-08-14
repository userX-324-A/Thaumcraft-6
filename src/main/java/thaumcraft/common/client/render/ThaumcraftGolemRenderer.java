package thaumcraft.common.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.model.CowModel; // placeholder bipedless model
import thaumcraft.common.entities.golem.ThaumcraftGolemEntity;

/**
 * Temporary renderer using a stock model and a neutral texture until custom model is ported.
 */
public class ThaumcraftGolemRenderer extends MobRenderer<ThaumcraftGolemEntity, CowModel<ThaumcraftGolemEntity>> {
    private static final ResourceLocation TEX = new ResourceLocation("minecraft", "textures/entity/cow/cow.png");

    public ThaumcraftGolemRenderer(EntityRendererManager mgr) {
        super(mgr, new CowModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ThaumcraftGolemEntity entity) {
        return TEX;
    }
}



