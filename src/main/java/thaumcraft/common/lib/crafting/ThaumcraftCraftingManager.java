package thaumcraft.common.lib.crafting;

import net.minecraft.world.World;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.api.crafting.RecipeTypes;

import java.util.Collection;
import java.util.stream.Collectors;

public class ThaumcraftCraftingManager {
    public static Collection<ICrucibleRecipe> getCrucibleRecipes(World world) {
        RecipeManager recipeManager = world.getRecipeManager();
        return recipeManager.getRecipesFor(RecipeTypes.CRUCIBLE, null, null)
                .stream()
                .map(recipe -> (ICrucibleRecipe) recipe)
                .collect(Collectors.toList());
    }
} 
