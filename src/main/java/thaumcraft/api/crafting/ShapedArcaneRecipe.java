package thaumcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.ResourceLocation;

public class ShapedArcaneRecipe extends ShapedRecipe {
    public ShapedArcaneRecipe(ResourceLocation id, String group, int width, int height, net.minecraft.item.crafting.Ingredient[] ingredients, ItemStack result) {
        super(id, group, width, height, ingredients, result);
    }
}

