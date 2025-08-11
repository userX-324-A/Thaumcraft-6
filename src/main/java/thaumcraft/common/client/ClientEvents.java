package thaumcraft.common.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.world.ArcaneEarBlock;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.RequestNoteMessage;

/**
 * Lightweight client tick/look handler to request Arcane Ear/Note block note values while looking at them.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private static int lookTick;

    private ClientEvents() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        lookTick++;
        if (lookTick % 5 != 0) return; // every ~5 ticks

        // Ray trace up to 6 blocks
        Vector3d eye = mc.player.getEyePosition(1.0f);
        Vector3d look = mc.player.getLookAngle();
        Vector3d reach = eye.add(look.scale(6.0));
        RayTraceResult rtr = mc.level.clip(new RayTraceContext(
                eye, reach,
                RayTraceContext.BlockMode.OUTLINE,
                RayTraceContext.FluidMode.NONE,
                mc.player));
        if (rtr == null || rtr.getType() != RayTraceResult.Type.BLOCK) return;
        BlockPos pos = ((BlockRayTraceResult) rtr).getBlockPos();
        if (!(mc.level.getBlockState(pos).getBlock() instanceof ArcaneEarBlock)) return; // note blocks could be added later

        NetworkHandler.sendToServer(new RequestNoteMessage(pos));
    }
}


