package thaumcraft.integration.jade;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@OnlyIn(Dist.CLIENT)
@WailaPlugin
public class ThaumcraftJadePlugin implements IWailaPlugin {
    private static boolean jadePresent() {
        return ModList.get().isLoaded("jade");
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        if (!jadePresent()) return;
        registration.registerBlockComponent(new providers.EssentiaTooltipProvider(), thaumcraft.common.blocks.world.EssentiaJarBlock.class);
        registration.registerBlockComponent(new providers.EssentiaTooltipProvider(), thaumcraft.common.blocks.world.TubeBlock.class);
        registration.registerBlockComponent(new providers.EssentiaTooltipProvider(), thaumcraft.common.blocks.world.EssentiaPumpBlock.class);
        registration.registerBlockComponent(new providers.EssentiaTooltipProvider(), thaumcraft.common.blocks.world.EssentiaBufferBlock.class);
        registration.registerBlockComponent(new providers.EssentiaTooltipProvider(), thaumcraft.common.blocks.world.EssentiaCentrifugeBlock.class);
        registration.registerBlockComponent(new providers.InfusionMatrixTooltipProvider(), thaumcraft.common.blocks.world.InfusionMatrixBlock.class);
    }
}


