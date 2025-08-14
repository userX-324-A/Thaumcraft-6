package thaumcraft.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.crafting.ICrucibleRecipe;
import java.util.Collections;
import thaumcraft.common.registers.ModBlocks;

public class CrucibleRecipeCategory implements IRecipeCategory<ICrucibleRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "crucible");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public CrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.CRUCIBLE.get()));
        this.background = guiHelper.createBlankDrawable(150, 60);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends ICrucibleRecipe> getRecipeClass() {
        return ICrucibleRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei.thaumcraft.category.crucible").getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ICrucibleRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getCatalyst()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ICrucibleRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        int x = 4;
        int y = 22;
        stacks.init(0, true, x, y);
        stacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        stacks.init(1, false, 120, y);
        stacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}



