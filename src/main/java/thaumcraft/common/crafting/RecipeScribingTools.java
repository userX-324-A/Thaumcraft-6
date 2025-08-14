package thaumcraft.common.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.registers.ModRecipes;

public class RecipeScribingTools extends SpecialRecipe {
    public RecipeScribingTools(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean hasTools = false;
        boolean hasInk = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ItemsTC.scribingTools) {
                    hasTools = true;
                } else if (Tags.Items.DYES_BLACK.contains(stack.getItem())) {
                    hasInk = true;
                } else {
                    return false;
                }
            }
        }

        return hasTools && hasInk;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack tools = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() == ItemsTC.scribingTools) {
                tools = stack.copy();
                break;
            }
        }

        if (tools.isEmpty()) {
            return ItemStack.EMPTY;
        }

        tools.setDamageValue(0);
        return tools;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() { return ModRecipes.SCRIBING_TOOLS.get(); }
}


