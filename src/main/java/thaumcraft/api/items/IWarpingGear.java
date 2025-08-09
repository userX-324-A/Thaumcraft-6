package thaumcraft.api.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IWarpingGear {
    int getWarp(ItemStack stack, PlayerEntity player);
}

