package thaumcraft.api.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IGoggles {
    boolean showIngamePopups(ItemStack stack, LivingEntity player);
}


