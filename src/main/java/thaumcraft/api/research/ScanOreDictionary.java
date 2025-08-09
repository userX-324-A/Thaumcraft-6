package thaumcraft.api.research;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ScanOreDictionary implements IScanThing {
    private String research;
    private String ore;
    
    public ScanOreDictionary(String research, String ore) {
        this.research = research;
        this.ore = ore;
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

