package thaumcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.ResourceLocation;

public class ShapelessArcaneRecipe extends ShapelessRecipe {
    public ShapelessArcaneRecipe(ResourceLocation id, String group, ItemStack output, net.minecraft.item.crafting.Ingredient[] ingredients) {
        super(id, group, output, ingredients);
    }
}

