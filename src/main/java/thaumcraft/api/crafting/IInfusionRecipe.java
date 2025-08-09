package thaumcraft.api.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import thaumcraft.api.aspects.AspectList;

public interface IInfusionRecipe extends IRecipe<IInventory> {
    AspectList getAspects(IInventory inv);

    String getResearch();

    NonNullList<Ingredient> getComponents();

    Ingredient getCentralIngredient();

    int getInstability();
} 
