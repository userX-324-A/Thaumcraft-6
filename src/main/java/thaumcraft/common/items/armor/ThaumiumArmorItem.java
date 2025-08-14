package thaumcraft.common.items.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import thaumcraft.api.ThaumcraftMaterials;

public class ThaumiumArmorItem extends ArmorItem {
    public ThaumiumArmorItem(EquipmentSlotType slot, Item.Properties properties) {
        super(ThaumcraftMaterials.ArmorMaterials.THAUMIUM, slot, properties);
    }
}



