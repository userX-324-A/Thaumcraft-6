package thaumcraft.common.items.tools;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import thaumcraft.api.ThaumcraftMaterials;

public class ThaumiumAxeItem extends AxeItem {
    public ThaumiumAxeItem(Item.Properties properties) {
        super(ThaumcraftMaterials.ToolTiers.THAUMIUM, 6.0F, -3.1F, properties);
    }
}


