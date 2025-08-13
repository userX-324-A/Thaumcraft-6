package thaumcraft.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thaumcraft.api.items.IThaumcraftItems;

public class ItemTCBase extends Item implements IThaumcraftItems {
    protected final String BASE_NAME;
    protected final String[] VARIANTS;
    protected final int[] VARIANTS_META;

    public ItemTCBase(String name, String... variants) {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC));
        this.setRegistryName(name);
        this.BASE_NAME = name;
        if (variants.length == 0) {
            this.VARIANTS = new String[]{name};
        } else {
            this.VARIANTS = variants;
        }
        this.VARIANTS_META = new int[this.VARIANTS.length];
        for (int m = 0; m < this.VARIANTS.length; ++m) {
            this.VARIANTS_META[m] = m;
        }
    }

    @Override
    // Keep default item group behavior for now; variants handled by separate items in 1.16
    public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) { super.fillItemCategory(tab, items); }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        if (itemStack.getDamageValue() < this.VARIANTS.length && !this.VARIANTS[itemStack.getDamageValue()].equals(this.BASE_NAME)) {
            return String.format(super.getDescriptionId() + ".%s", this.VARIANTS[itemStack.getDamageValue()]);
        }
        return super.getDescriptionId(itemStack);
    }
}

