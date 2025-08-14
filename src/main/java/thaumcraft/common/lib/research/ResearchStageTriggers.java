package thaumcraft.common.lib.research;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.research.ResearchStage;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ResearchStageTriggers {
    private ResearchStageTriggers() {}

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == null || player.level.isClientSide) return;
        ItemStack crafted = event.getCrafting();
        if (crafted == null || crafted.isEmpty()) return;
        int code = ResearchManager.createItemStackHash(crafted.copy());
        processCraftOrObtain((ServerPlayerEntity) player, crafted, code, true);
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack picked = event.getItem().getItem();
        if (picked == null || picked.isEmpty()) return;
        int code = ResearchManager.createItemStackHash(picked.copy());
        processCraftOrObtain(player, picked, code, false);
    }

    private static void processCraftOrObtain(ServerPlayerEntity player, ItemStack stack, int code, boolean isCraft) {
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
        if (knowledge == null) return;
        for (String catKey : ResearchCategories.researchCategories.keySet()) {
            ResearchCategory cat = ResearchCategories.getResearchCategory(catKey);
            if (cat == null) continue;
            for (ResearchEntry entry : cat.research.values()) {
                if (entry == null || entry.getStages() == null || entry.getStages().length == 0) continue;
                if (!knowledge.isResearchKnown(entry.getKey()) || knowledge.isResearchComplete(entry.getKey())) continue;
                int stageIdx = knowledge.getResearchStage(entry.getKey());
                if (stageIdx < 0 || stageIdx >= entry.getStages().length) continue;
                ResearchStage stage = entry.getStages()[stageIdx];

                boolean shouldAdvance = false;
                if (isCraft) {
                    int[] refs = stage.getCraftReference();
                    if (refs != null) {
                        for (int r : refs) { if (r == code) { shouldAdvance = true; break; } }
                    }
                } else {
                    Object[] obtain = stage.getObtain();
                    if (obtain != null) {
                        for (Object obj : obtain) {
                            if (obj instanceof ItemStack) {
                                ItemStack want = ((ItemStack) obj);
                                if (want.getItem() == stack.getItem()) { shouldAdvance = true; break; }
                            }
                            // oredict entries are ignored in minimal port
                        }
                    }
                }

                if (shouldAdvance) {
                    ResearchManager.progressResearch(player, entry.getKey(), true);
                }
            }
        }
    }
}



