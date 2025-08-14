package thaumcraft.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.common.lib.crafting.ArcaneRecipe;
import thaumcraft.common.registers.ModBlocks;

public class ArcaneRecipeCategory implements IRecipeCategory<ArcaneRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "arcane");
    public static final RecipeType<ArcaneRecipe> RECIPE_TYPE = new RecipeType<>(UID, ArcaneRecipe.class);

    private final IDrawable icon;
    private final IDrawableStatic background;

    public ArcaneRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.ARCANE_WORKBENCH.get()));
        this.background = guiHelper.createBlankDrawable(150, 60);
    }

    @Override
    public RecipeType<ArcaneRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return new TranslationTextComponent("jei.thaumcraft.category.arcane");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneRecipe recipe, IRecipeSlotsView recipeSlotsView) {
        // Minimal layout: inputs in a row, output on right
        int x = 4;
        int y = 22;
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            builder.addSlot(mezz.jei.api.constants.RecipeIngredientRole.INPUT, x + i * 18, y)
                    .addIngredients(recipe.getIngredients().get(i));
        }
        builder.addSlot(mezz.jei.api.constants.RecipeIngredientRole.OUTPUT, 120, y)
                .addItemStack(recipe.getResultItem());
    }
}


