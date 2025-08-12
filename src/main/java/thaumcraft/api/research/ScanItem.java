package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ScanItem implements IScanThing {
    private String research;
    private ItemStack stack;
    
    public ScanItem(String research, ItemStack stack) {
        this.research = research;
        this.stack = stack;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        ItemStack is = ScanningManager.getItemFromParms(player, obj);
        if (is == null || is.isEmpty()) return false;
        if (stack == null || stack.isEmpty()) return false;
        // Basic match: item and (optionally) NBT and damage if provided
        if (is.getItem() != stack.getItem()) return false;
        if (stack.hasTag()) {
            if (!is.hasTag()) return false;
            // require all keys in template to be present and equal in candidate
            for (String k : stack.getTag().getAllKeys()) {
                if (!is.getTag().contains(k)) return false;
                if (!is.getTag().get(k).equals(stack.getTag().get(k))) return false;
            }
        }
        return true;
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return research;
    }
}

