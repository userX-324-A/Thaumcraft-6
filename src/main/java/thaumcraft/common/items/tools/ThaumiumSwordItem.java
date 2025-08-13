package thaumcraft.common.items.tools;

import net.minecraft.item.SwordItem;
import net.minecraft.item.Item;
import thaumcraft.api.ThaumcraftMaterials;

public class ThaumiumSwordItem extends SwordItem {
    public ThaumiumSwordItem(Item.Properties properties) {
        super(ThaumcraftMaterials.ToolTiers.THAUMIUM, 3, -2.4F, properties);
    }
}


