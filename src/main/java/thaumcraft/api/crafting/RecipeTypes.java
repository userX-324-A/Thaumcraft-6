package thaumcraft.api.crafting;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import thaumcraft.Thaumcraft;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.api.crafting.IInfusionRecipe;

public class RecipeTypes {
    public static final IRecipeType<IArcaneRecipe> ARCANE_CRAFTING = register(IArcaneRecipe.RECIPE_TYPE.toString());
    public static final IRecipeType<ICrucibleRecipe> CRUCIBLE = register(ICrucibleRecipe.RECIPE_TYPE.toString());
    public static final IRecipeType<IInfusionRecipe> INFUSION = register(IInfusionRecipe.RECIPE_TYPE.toString());

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Thaumcraft.MODID, key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }
} 
