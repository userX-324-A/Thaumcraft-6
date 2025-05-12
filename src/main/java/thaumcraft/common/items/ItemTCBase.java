package thaumcraft.common.items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thaumcraft.common.config.ConfigItems;


public class ItemTCBase extends Item implements IThaumcraftItems
{
    protected String BASE_NAME;
    protected String[] VARIANTS;
    protected int[] VARIANTS_META;
    
    public ItemTCBase(String name, Item.Properties props, String... variants) {
        super(props);
        BASE_NAME = name;
        if (variants.length == 0) {
            VARIANTS = new String[] { name };
        }
        else {
            VARIANTS = variants;
        }
        VARIANTS_META = new int[VARIANTS.length];
        for (int m = 0; m < VARIANTS.length; ++m) {
            VARIANTS_META[m] = m;
        }
        if (VARIANTS.length > 1) {
            this.setHasSubtypes(true);
        }
    }
    
    @Override
    public String getTranslationKey(ItemStack itemStack) {
        if (getHasSubtypes() && itemStack.getDamageValue() < VARIANTS.length && VARIANTS[itemStack.getDamageValue()] != BASE_NAME) {
            return String.format(super.getTranslationKey() + ".%s", VARIANTS[itemStack.getDamageValue()]);
        }
        return super.getTranslationKey(itemStack);
    }
    
    @Override
    public void fillItemCategory(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == ConfigItems.TABTC || tab == CreativeTabs.SEARCH) {
            if (!getHasSubtypes()) {
                super.fillItemCategory(tab, items);
            }
            else {
                for (int meta = 0; meta < VARIANTS.length; ++meta) {
                    items.add(new ItemStack(this, 1, meta));
                }
            }
        }
    }
    
    public Item getItem() {
        return this;
    }
    
    public String[] getVariantNames() {
        return VARIANTS;
    }
    
    public int[] getVariantMeta() {
        return VARIANTS_META;
    }
    
    /**
     * @deprecated Model handling has changed significantly. This will be removed or refactored.
     */
    /* @Deprecated // Method removed
    public ItemMeshDefinition getCustomMesh() {
        return null;
    }*/
    
    /**
     * @deprecated Model handling has changed significantly. This will be removed or refactored.
     */
    /* @Deprecated // Method removed
    public ModelResourceLocation getCustomModelResourceLocation(String variant) {
        if (variant.equals(BASE_NAME)) {
            return new ModelResourceLocation("thaumcraft:" + BASE_NAME);
        }
        return new ModelResourceLocation("thaumcraft:" + BASE_NAME, variant);
    }*/
}
