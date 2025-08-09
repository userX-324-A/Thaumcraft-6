package thaumcraft.api.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IRevealer {
    boolean showNodes(ItemStack stack, LivingEntity player);
}

