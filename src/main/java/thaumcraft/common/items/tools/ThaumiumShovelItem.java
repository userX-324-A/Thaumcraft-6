package thaumcraft.common.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;
import thaumcraft.api.ThaumcraftMaterials;

public class ThaumiumShovelItem extends ShovelItem {
    public ThaumiumShovelItem(Item.Properties properties) {
        super(ThaumcraftMaterials.ToolTiers.THAUMIUM, 1.5F, -3.0F, properties);
    }
}


