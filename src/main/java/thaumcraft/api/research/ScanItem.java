package thaumcraft.api.research;

import net.minecraft.entity.item.ItemEntity;
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
        return false;
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return null;
    }
}

