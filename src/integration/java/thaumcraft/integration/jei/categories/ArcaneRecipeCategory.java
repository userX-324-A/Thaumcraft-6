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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.registers.ModBlocks;

public class ArcaneRecipeCategory implements IRecipeCategory<IArcaneRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "arcane");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public ArcaneRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.ARCANE_WORKBENCH.get()));
        this.background = guiHelper.createBlankDrawable(150, 60);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends IArcaneRecipe> getRecipeClass() {
        return IArcaneRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei.thaumcraft.category.arcane").getString();
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
    public void setIngredients(IArcaneRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IArcaneRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        int x = 4;
        int y = 22;
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            stacks.init(i, true, x + i * 18, y);
            stacks.set(i, ingredients.getInputs(VanillaTypes.ITEM).get(i));
        }
        int outIndex = recipe.getIngredients().size();
        stacks.init(outIndex, false, 120, y);
        stacks.set(outIndex, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}



