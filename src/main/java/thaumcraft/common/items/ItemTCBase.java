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
        super(new Item.Properties().group(ItemGroup.MISC));
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
    public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> items) {
        if (this.isInGroup(tab)) {
            if (this.getHasSubtypes()) {
                for (int meta = 0; meta < this.VARIANTS.length; ++meta) {
                    items.add(new ItemStack(this, 1, meta));
                }
            } else {
                super.fillItemGroup(tab, items);
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        if (this.getHasSubtypes() && itemStack.getDamage() < this.VARIANTS.length && this.VARIANTS[itemStack.getDamage()] != this.BASE_NAME) {
            return String.format(super.getTranslationKey() + ".%s", this.VARIANTS[itemStack.getDamage()]);
        }
        return super.getTranslationKey(itemStack);
    }
}

