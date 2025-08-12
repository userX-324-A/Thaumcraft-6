package thaumcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ShapedArcaneRecipe extends ShapedRecipe {
    public ShapedArcaneRecipe(ResourceLocation id, String group, int width, int height, Ingredient[] ingredients, ItemStack result) {
        super(id, group, width, height, NonNullList.of(Ingredient.EMPTY, ingredients), result);
    }
}

