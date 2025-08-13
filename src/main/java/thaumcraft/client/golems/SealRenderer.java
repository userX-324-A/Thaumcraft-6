package thaumcraft.client.golems;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
// Imports for future icon billboard (left commented until implemented to avoid lints)
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
// import net.minecraft.util.math.vector.Quaternion;
// import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.seals.ISealEntity;

/**
     * Very light world overlay: draws area boxes for seals with an area set, plus a faint textured overlay.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class SealRenderer {
    private SealRenderer() {}

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {
        MatrixStack ms = event.getMatrixStack();
        IRenderTypeBuffer.Impl buf = Minecraft.getInstance().renderBuffers().bufferSource();
        double camX = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().x;
        double camY = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y;
        double camZ = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().z;
        for (ISealEntity ent : thaumcraft.client.golems.ClientSealCache.all()) {
            BlockPos pos = ent.getSealPos() == null ? null : ent.getSealPos().pos;
            BlockPos area = ent.getArea();
            if (pos == null) continue;
            // Cull by distance (64 blocks)
            if (Minecraft.getInstance().player != null && !pos.closerThan(Minecraft.getInstance().player.blockPosition(), 64.0)) continue;
            if (area != null) {
                AxisAlignedBB box = new AxisAlignedBB(pos).inflate(area.getX() - 1, area.getY() - 1, area.getZ() - 1);
                WorldRenderer.renderLineBox(ms, buf.getBuffer(RenderType.lines()), box.move(-camX, -camY, -camZ), 0f, 1f, 1f, 1f);

                // Textured overlay on top face (preview). Keep extremely light to avoid overdraw.
                ms.pushPose();
                ms.translate(-camX, -camY, -camZ);
                Matrix4f mat = ms.last().pose();
                IVertexBuilder vtx = buf.getBuffer(RenderType.text(new ResourceLocation(Thaumcraft.MODID, "textures/misc/seal_area.png")));
                float a = 0.25f;
                float minX = (float) box.minX;
                float minY = (float) (box.maxY + 0.01);
                float minZ = (float) box.minZ;
                float maxX = (float) box.maxX;
                float maxZ = (float) box.maxZ;
                vtx.vertex(mat, minX, minY, minZ).color(1f, 1f, 1f, a).uv(0f, 0f).endVertex();
                vtx.vertex(mat, minX, minY, maxZ).color(1f, 1f, 1f, a).uv(0f, 1f).endVertex();
                vtx.vertex(mat, maxX, minY, maxZ).color(1f, 1f, 1f, a).uv(1f, 1f).endVertex();
                vtx.vertex(mat, maxX, minY, minZ).color(1f, 1f, 1f, a).uv(1f, 0f).endVertex();
                ms.popPose();
            }
        }
        buf.endBatch();
    }
}


