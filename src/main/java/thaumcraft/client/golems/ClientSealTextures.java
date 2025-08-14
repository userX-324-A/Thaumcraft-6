package thaumcraft.client.golems;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.seals.ISeal;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, value = Dist.CLIENT)
public final class ClientSealTextures {
    private ClientSealTextures() {}

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        // If seal icons are not the same as item textures, stitch here. For now, assume item textures are used.
        // Example of adding a sprite:
        for (ISeal s : thaumcraft.common.golems.seals.SealRegistry.all().values()) {
            ResourceLocation rl = s.getSealIcon();
            if (rl != null) {
                try {
                    event.addSprite(rl);
                } catch (Throwable ignored) {}
            }
        }
    }
}



