package thaumcraft.api.crafting;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.inventory.IInventory;
import thaumcraft.api.aspects.AspectList;
import net.minecraft.item.crafting.Ingredient;

public interface ICrucibleRecipe extends IRecipe<IInventory> {
    AspectList getAspects();

    String getResearch();

    Ingredient getCatalyst();
} 
