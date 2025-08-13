package thaumcraft.common.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.misc.GrappleProjectileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.entity.LivingEntity;

public class GrappleProjectileRenderer extends EntityRenderer<GrappleProjectileEntity> {
    public GrappleProjectileRenderer(EntityRendererManager manager) { super(manager); }

    @Override
    public ResourceLocation getTextureLocation(GrappleProjectileEntity entity) {
        return new ResourceLocation("minecraft", "textures/item/lead.png");
    }

    @Override
    public void render(GrappleProjectileEntity entity, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, ms, buffer, packedLight);
        if (!(entity.getOwner() instanceof LivingEntity)) return;
        LivingEntity owner = (LivingEntity) entity.getOwner();
        Vector3d start = new Vector3d(owner.getX(), owner.getEyeY(), owner.getZ());
        Vector3d end = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
        IVertexBuilder vb = buffer.getBuffer(RenderType.lines());
        ms.pushPose();
        com.mojang.blaze3d.matrix.MatrixStack.Entry entry = ms.last();
        // Subdivide to give a slight sagging effect
        int segments = 12;
        for (int i = 0; i < segments; i++) {
            double t0 = i / (double) segments;
            double t1 = (i + 1) / (double) segments;
            double x0 = start.x + (end.x - start.x) * t0;
            double y0 = start.y + (end.y - start.y) * t0 - 0.1 * Math.sin(Math.PI * t0);
            double z0 = start.z + (end.z - start.z) * t0;
            double x1 = start.x + (end.x - start.x) * t1;
            double y1 = start.y + (end.y - start.y) * t1 - 0.1 * Math.sin(Math.PI * t1);
            double z1 = start.z + (end.z - start.z) * t1;
            vb.vertex(entry.pose(), (float) x0, (float) y0, (float) z0).color(230, 230, 230, 255).endVertex();
            vb.vertex(entry.pose(), (float) x1, (float) y1, (float) z1).color(200, 200, 200, 255).endVertex();
        }
        ms.popPose();
    }
}


