package thaumcraft.api.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IRechargable {
    int getMaxCharge(ItemStack stack, LivingEntity player);
    
    EnumChargeDisplay showInHud(ItemStack stack, LivingEntity player);
    
    public enum EnumChargeDisplay {
        NORMAL, 
        NEVER, 
        PARENTS;
    }
}

