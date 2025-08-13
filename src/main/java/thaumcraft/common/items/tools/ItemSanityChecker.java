package thaumcraft.common.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import thaumcraft.client.hud.HudHandler;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class ItemSanityChecker extends Item {
    public ItemSanityChecker(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        // Replace chat spam with a HUD overlay message cadence; server computes, client receives text
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
        int perm = warp != null ? warp.get(IPlayerWarp.EnumWarpType.PERMANENT) : 0;
        int norm = warp != null ? warp.get(IPlayerWarp.EnumWarpType.NORMAL) : 0;
        int temp = warp != null ? warp.get(IPlayerWarp.EnumWarpType.TEMPORARY) : 0;
        String text = "Warp: "+perm+"/"+norm+"/"+temp;
        int ticks = Math.max(1, thaumcraft.common.config.ModConfig.COMMON.sanityOverlayCadenceTicks.get());
        boolean enabled = thaumcraft.common.config.ModConfig.COMMON.sanityOverlayEnabled.get();
        if (enabled) {
            HudHandler.setSanityOverlay(text, ticks);
            int threshold = thaumcraft.common.config.ModConfig.COMMON.sanityWhispersThreshold.get();
            if (perm + norm + temp >= threshold) {
                player.playSound(thaumcraft.common.registers.SoundsTC.WHISPERS.get(), 0.6f, 1.0f);
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World level, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!(entity instanceof PlayerEntity) || level.isClientSide || !selected) return;
        boolean enabled = thaumcraft.common.config.ModConfig.COMMON.sanityOverlayEnabled.get();
        if (!enabled) return;
        int cadence = Math.max(1, thaumcraft.common.config.ModConfig.COMMON.sanityOverlayCadenceTicks.get());
        PlayerEntity player = (PlayerEntity) entity;
        if (player.tickCount % cadence != 0) return;
        thaumcraft.api.capabilities.IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
        int perm = warp != null ? warp.get(thaumcraft.api.capabilities.IPlayerWarp.EnumWarpType.PERMANENT) : 0;
        int norm = warp != null ? warp.get(thaumcraft.api.capabilities.IPlayerWarp.EnumWarpType.NORMAL) : 0;
        int temp = warp != null ? warp.get(thaumcraft.api.capabilities.IPlayerWarp.EnumWarpType.TEMPORARY) : 0;
        String text = "Warp: "+perm+"/"+norm+"/"+temp;
        thaumcraft.client.hud.HudHandler.setSanityOverlay(text, cadence);
        int threshold = thaumcraft.common.config.ModConfig.COMMON.sanityWhispersThreshold.get();
        if (perm + norm + temp >= threshold) {
            player.playSound(thaumcraft.common.registers.SoundsTC.WHISPERS.get(), 0.4f, 1.0f);
        }
    }
}


