package thaumcraft.common.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.registers.ModRecipes;
// Optional client-only JEI ghost gating will be implemented separately; server safety is enforced in menu

public class ArcaneShapedRecipe extends ShapedRecipe implements IArcaneRecipe {
    private final int vis;
    private final String research;
    private final AspectList crystals;

    public ArcaneShapedRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn, int vis, String research, AspectList crystals) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
        this.vis = vis;
        this.research = research;
        this.crystals = crystals;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() { return ModRecipes.ARCANE_SHAPED_SERIALIZER.get(); }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.ARCANE_RECIPE_TYPE;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return super.matches(inv, worldIn);
    }

    @Override
    public int getVis() {
        return vis;
    }

    @Override
    public String getResearch() {
        return research;
    }

    @Override
    public AspectList getCrystals() {
        return crystals;
    }
} 

