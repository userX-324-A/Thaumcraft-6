package thaumcraft.api.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IVisDiscountGear {
    int getVisDiscount(ItemStack stack, PlayerEntity player);
}

