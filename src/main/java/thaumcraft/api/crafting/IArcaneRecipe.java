package thaumcraft.api.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipe;
import thaumcraft.api.aspects.AspectList;

public interface IArcaneRecipe extends IRecipe<CraftingInventory> {
    int getVis();

    String getResearch();

    AspectList getCrystals();
}

