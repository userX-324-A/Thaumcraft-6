package thaumcraft.common.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.monster.EldritchCrabEntity;

public class EldritchCrabRenderer extends MobRenderer<EldritchCrabEntity, EldritchCrabRenderer.CrabModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("thaumcraft", "textures/entity/crab.png");

    public EldritchCrabRenderer(EntityRendererManager manager) {
        super(manager, new CrabModel(), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(EldritchCrabEntity entity) { return TEXTURE; }

    public static class CrabModel extends EntityModel<EldritchCrabEntity> {
        private final ModelRenderer body;
        private final ModelRenderer clawLeft;
        private final ModelRenderer clawRight;

        public CrabModel() {
            this.texWidth = 64;
            this.texHeight = 32;
            body = new ModelRenderer(this, 0, 0);
            body.addBox(-4.0F, -3.0F, -4.0F, 8, 3, 8);

            clawLeft = new ModelRenderer(this, 0, 11);
            clawLeft.setPos(3.5F, -1.0F, -3.5F);
            clawLeft.addBox(0.0F, -1.0F, -2.0F, 3, 2, 3);

            clawRight = new ModelRenderer(this, 12, 11);
            clawRight.setPos(-3.5F, -1.0F, -3.5F);
            clawRight.addBox(-3.0F, -1.0F, -2.0F, 3, 2, 3);
        }

        @Override
        public void setupAnim(EldritchCrabEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            float open = (float)Math.sin(ageInTicks * 0.3F) * 0.2F;
            clawLeft.yRot = open;
            clawRight.yRot = -open;
        }

        @Override
        public void renderToBuffer(MatrixStack stack, IVertexBuilder vertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            body.render(stack, vertexBuilder, packedLight, packedOverlay);
            clawLeft.render(stack, vertexBuilder, packedLight, packedOverlay);
            clawRight.render(stack, vertexBuilder, packedLight, packedOverlay);
        }
    }
}



