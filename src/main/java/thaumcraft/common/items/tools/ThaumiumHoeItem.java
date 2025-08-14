package thaumcraft.common.items.tools;

import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import thaumcraft.api.ThaumcraftMaterials;

public class ThaumiumHoeItem extends HoeItem {
    public ThaumiumHoeItem(Item.Properties properties) {
        super(ThaumcraftMaterials.ToolTiers.THAUMIUM, -2, 0.0F, properties);
    }
}



