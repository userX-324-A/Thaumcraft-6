package thaumcraft.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.constants.RecipeIngredientRole;
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
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.common.registers.ModBlocks;

public class CrucibleRecipeCategory implements IRecipeCategory<ICrucibleRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "crucible");
    public static final RecipeType<ICrucibleRecipe> RECIPE_TYPE = new RecipeType<>(UID, ICrucibleRecipe.class);

    private final IDrawable icon;
    private final IDrawableStatic background;

    public CrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.CRUCIBLE.get()));
        this.background = guiHelper.createBlankDrawable(150, 60);
    }

    @Override
    public RecipeType<ICrucibleRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return new TranslationTextComponent("jei.thaumcraft.category.crucible");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ICrucibleRecipe recipe, IRecipeSlotsView recipeSlotsView) {
        int x = 4;
        int y = 22;
        builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, y)
                .addItemStack(recipe.getResultItem());
    }
}


