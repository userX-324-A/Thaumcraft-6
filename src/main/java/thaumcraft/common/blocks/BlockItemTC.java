package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thaumcraft.api.items.IThaumcraftItems;

public class BlockItemTC extends BlockItem implements IThaumcraftItems {
    public BlockItemTC(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(new ItemStack(this));
        }
    }
}


