package thaumcraft.common.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import thaumcraft.api.ThaumcraftMaterials;

public class ThaumiumPickaxeItem extends PickaxeItem {
    public ThaumiumPickaxeItem(Item.Properties properties) {
        super(ThaumcraftMaterials.ToolTiers.THAUMIUM, 1, -2.8F, properties);
    }
}



