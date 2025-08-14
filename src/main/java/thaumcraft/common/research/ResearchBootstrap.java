package thaumcraft.common.research;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.api.research.ResearchCategories;

/**
 * Registers default research categories and hooks the datapack research reload listener.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ResearchBootstrap {
    private ResearchBootstrap() {}

    public static void registerDefaultCategories() {
        // Minimal set of categories for 1.16.5; assets may be refined later
        ResearchCategories.registerCategory("BASICS",
                new ResourceLocation(Thaumcraft.MODID, "textures/items/thaumonomicon_cheat.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_1.jpg"));
        ResearchCategories.registerCategory("AUROMANCY",
                new ResourceLocation(Thaumcraft.MODID, "textures/research/cat_auromancy.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_2.jpg"));
        ResearchCategories.registerCategory("ALCHEMY",
                new ResourceLocation(Thaumcraft.MODID, "textures/research/cat_alchemy.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_3.jpg"));
        ResearchCategories.registerCategory("ARTIFICE",
                new ResourceLocation(Thaumcraft.MODID, "textures/research/cat_artifice.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_4.jpg"));
        ResearchCategories.registerCategory("INFUSION",
                new ResourceLocation(Thaumcraft.MODID, "textures/research/cat_infusion.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_7.jpg"));
        ResearchCategories.registerCategory("GOLEMANCY",
                new ResourceLocation(Thaumcraft.MODID, "textures/research/cat_golemancy.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_5.jpg"));
        ResearchCategories.registerCategory("ELDRITCH",
                new ResourceLocation(Thaumcraft.MODID, "textures/research/cat_eldritch.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        if (!thaumcraft.common.Diag.disableAll() && !thaumcraft.common.Diag.disableResearchReload()) {
            event.addListener((net.minecraft.resources.IFutureReloadListener) new ResearchReloadListener());
        } else {
            thaumcraft.Thaumcraft.LOGGER.warn("Diagnostics: datapack research reload listener disabled via config");
        }
    }
}



