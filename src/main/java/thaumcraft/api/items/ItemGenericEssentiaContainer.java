package thaumcraft.api.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemGenericEssentiaContainer extends Item {
    public ItemGenericEssentiaContainer(Properties properties) {
        super(properties);
    }
    
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    }
    
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
    }
    
    public CompoundNBT getAura(ItemStack stack) {
        return null;
    }
}

