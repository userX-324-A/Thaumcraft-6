package thaumcraft.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import thaumcraft.common.registers.ModBlocks;
import thaumcraft.common.registers.ModMenus;

@OnlyIn(Dist.CLIENT)
public final class ClientOnlySetup {
    private ClientOnlySetup() {}

    public static void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            thaumcraft.common.network.ClientNet.register();
            net.minecraft.client.gui.ScreenManager.register(ModMenus.ARCANE_WORKBENCH.get(), thaumcraft.common.client.screen.ArcaneWorkbenchScreen::new);
            net.minecraft.client.gui.ScreenManager.register(ModMenus.THAUMATORIUM.get(), thaumcraft.common.client.screen.ThaumatoriumScreen::new);
            net.minecraft.client.gui.ScreenManager.register(ModMenus.RESEARCH_TABLE.get(), thaumcraft.common.client.screen.ResearchTableScreen::new);
            if (thaumcraft.common.config.ModConfig.COMMON.enableSeals.get()) {
                net.minecraft.client.gui.ScreenManager.register(ModMenus.SEAL.get(), thaumcraft.common.client.screen.SealScreen::new);
            }

            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.FIRE_BAT.get(),
                    manager -> new thaumcraft.common.client.render.FireBatRenderer(manager));
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.ELDRITCH_CRAB.get(),
                    manager -> new thaumcraft.common.client.render.EldritchCrabRenderer(manager));
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.TC_GOLEM.get(),
                    manager -> new thaumcraft.common.client.render.ThaumcraftGolemRenderer(manager));
            net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(
                    thaumcraft.common.registers.ModEntities.GRAPPLE_PROJECTILE.get(),
                    manager -> new thaumcraft.common.client.render.GrappleProjectileRenderer(manager));

            thaumcraft.common.client.ClientEvents.initKeybind();

            RenderType cutout = RenderType.cutout();
            RenderType translucent = RenderType.translucent();
            RenderTypeLookup.setRenderLayer(ModBlocks.SEAL.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ARCANE_LAMP.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_JAR.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_JAR_VOID.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_JAR_BRAIN.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_MIRROR.get(), translucent);
            RenderTypeLookup.setRenderLayer(ModBlocks.EVERFULL_URN.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ARCANE_WORKBENCH_CHARGER.get(), RenderType.solid());
            RenderTypeLookup.setRenderLayer(ModBlocks.BELLOWS.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.TUBE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_TUBE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_FILTER.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_PUMP.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.ESSENTIA_VALVE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.RESEARCH_TABLE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SPA.get(), translucent);
            RenderTypeLookup.setRenderLayer(ModBlocks.LEAVES_GREATWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.LEAVES_SILVERWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SAPLING_GREATWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SAPLING_SILVERWOOD.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.SHIMMERLEAF.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_AIR.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_EARTH.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_FIRE.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_WATER.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_ORDER.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_ENTROPY.get(), cutout);
            RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_TAINT.get(), cutout);
        });
        thaumcraft.common.golems.GolemBootstrap.registerDefaults();
    }
}


