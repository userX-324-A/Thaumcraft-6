package thaumcraft.api.casters;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ICaster {
    float getConsumptionModifier(ItemStack stack, PlayerEntity player, boolean crafting);
    
    boolean consumeVis(ItemStack stack, PlayerEntity player, float amount, boolean crafting, boolean simulate);
}

