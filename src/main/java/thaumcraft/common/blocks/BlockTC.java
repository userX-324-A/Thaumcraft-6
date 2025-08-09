package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thaumcraft.api.items.IThaumcraftItems;

public class BlockTC extends Block implements IThaumcraftItems {
    public BlockTC(Properties properties) {
        super(properties);
    }

    @Override
    public Item asItem() {
        return new BlockItemTC(this, new Item.Properties().group(ItemGroup.MISC));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }
}

