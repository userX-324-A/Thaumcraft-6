package thaumcraft.api.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RechargeHelper {
    public static float rechargeItem(World world, ItemStack stack, BlockPos pos, PlayerEntity player, int amount) {
        return 0.0f;
    }
    
    public static float rechargeItemBlindly(ItemStack stack, PlayerEntity player, int amount) {
        return 0.0f;
    }
    
    private static void addCharge(ItemStack stack, LivingEntity player, int amount) {
    }
    
    public static float getChargePercentage(ItemStack stack, PlayerEntity player) {
        return 0.0f;
    }
    
    public static boolean consumeCharge(ItemStack stack, LivingEntity player, int amount) {
        return false;
    }
}

