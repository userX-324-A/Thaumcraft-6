package thaumcraft.common.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.world.ArcaneEarBlock;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.RequestNoteMessage;
// Use GLFW directly to avoid mapping issues with InputConstants in some setups
import net.minecraftforge.client.event.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import thaumcraft.common.client.screen.ThaumonomiconScreen;

/**
 * Lightweight client tick/look handler to request Arcane Ear/Note block note values while looking at them.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private static int lookTick;
    private static KeyBinding keyOpenThaumonomicon;

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

    // Lightweight keybind for opening the minimal Thaumonomicon screen
    public static void initKeybind() {
        if (keyOpenThaumonomicon == null) {
            keyOpenThaumonomicon = new KeyBinding("key.thaumcraft.open_thaumonomicon", GLFW.GLFW_KEY_K, "key.categories.thaumcraft");
            ClientRegistry.registerKeyBinding(keyOpenThaumonomicon);
        }
    }

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (keyOpenThaumonomicon == null) initKeybind();
        if (keyOpenThaumonomicon.consumeClick()) {
            mc.setScreen(new ThaumonomiconScreen());
        }
    }

    @SubscribeEvent
    public static void onClientLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        // Clear client-side seal cache on logout/world unload
        thaumcraft.client.golems.ClientSealCache.clearAll();
    }
}



