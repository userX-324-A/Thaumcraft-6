package thaumcraft.common.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.registers.ModRecipes;
import thaumcraft.common.blocks.basic.BlockCandle;

public class RecipeCandleDye extends SpecialRecipe {
    public RecipeCandleDye(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean hasCandle = false;
        boolean hasDye = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof DyeItem) {
                    hasDye = true;
                } else if (net.minecraft.block.Block.byItem(item) instanceof BlockCandle) {
                    hasCandle = true;
                } else {
                    return false;
                }
            }
        }

        return hasCandle && hasDye;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack candle = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof DyeItem) {
                    dye = stack;
                } else if (net.minecraft.block.Block.byItem(item) instanceof BlockCandle) {
                    candle = stack;
                }
            }
        }

        if (candle.isEmpty() || dye.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(BlocksTC.candles.get(((DyeItem) dye.getItem()).getDyeColor()));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() { return ModRecipes.CANDLE_DYE.get(); }
}

