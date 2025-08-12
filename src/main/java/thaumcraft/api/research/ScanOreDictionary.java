package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApiHelper;

public class ScanOreDictionary implements IScanThing {
    private String research;
    private String ore;
    
    public ScanOreDictionary(String research, String ore) {
        this.research = research;
        this.ore = ore;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        if (obj == null) return false;
        ItemStack is = ScanningManager.getItemFromParms(player, obj);
        if (is == null || is.isEmpty()) return false;
        ResourceLocation tagId = ThaumcraftApiHelper.convertLegacyOreNameToItemTag(ore);
        if (tagId == null) return false;
        net.minecraft.tags.ITag<net.minecraft.item.Item> tag = TagCollectionManager.getInstance().getItems().getTag(tagId);
        return tag != null && tag.contains(is.getItem());
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return research;
    }
}

