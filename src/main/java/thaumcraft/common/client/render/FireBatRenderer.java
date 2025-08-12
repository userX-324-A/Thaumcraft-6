package thaumcraft.common.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.monster.FireBatEntity;

public class FireBatRenderer extends MobRenderer<FireBatEntity, FireBatRenderer.FireBatModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("thaumcraft", "textures/entity/fire_bat.png");

    public FireBatRenderer(EntityRendererManager manager) {
        super(manager, new FireBatModel(), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(FireBatEntity entity) { return TEXTURE; }

    public static class FireBatModel extends EntityModel<FireBatEntity> {
        private final ModelRenderer body;
        private final ModelRenderer wingLeft;
        private final ModelRenderer wingRight;

        public FireBatModel() {
            this.texWidth = 32;
            this.texHeight = 32;
            body = new ModelRenderer(this, 0, 0);
            body.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);

            wingLeft = new ModelRenderer(this, 0, 8);
            wingLeft.setPos(2.0F, -1.0F, 0.0F);
            wingLeft.addBox(0.0F, 0.0F, -1.0F, 6, 1, 2);

            wingRight = new ModelRenderer(this, 0, 11);
            wingRight.setPos(-2.0F, -1.0F, 0.0F);
            wingRight.addBox(-6.0F, 0.0F, -1.0F, 6, 1, 2);
        }

        @Override
        public void setupAnim(FireBatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            float flap = (float)Math.sin(ageInTicks * 0.6F) * 0.6F;
            wingLeft.yRot = flap;
            wingRight.yRot = -flap;
        }

        @Override
        public void renderToBuffer(MatrixStack stack, IVertexBuilder vertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            body.render(stack, vertexBuilder, packedLight, packedOverlay);
            wingLeft.render(stack, vertexBuilder, packedLight, packedOverlay);
            wingRight.render(stack, vertexBuilder, packedLight, packedOverlay);
        }
    }
}


