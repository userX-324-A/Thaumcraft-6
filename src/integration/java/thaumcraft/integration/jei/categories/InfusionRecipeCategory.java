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
import thaumcraft.api.crafting.IInfusionRecipe;
import thaumcraft.common.registers.ModBlocks;

public class InfusionRecipeCategory implements IRecipeCategory<IInfusionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "infusion");
    public static final RecipeType<IInfusionRecipe> RECIPE_TYPE = new RecipeType<>(UID, IInfusionRecipe.class);

    private final IDrawable icon;
    private final IDrawableStatic background;

    public InfusionRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.INFUSION_MATRIX.get()));
        this.background = guiHelper.createBlankDrawable(150, 60);
    }

    @Override
    public RecipeType<IInfusionRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return new TranslationTextComponent("jei.thaumcraft.category.infusion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, IInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView) {
        // Central item
        builder.addSlot(RecipeIngredientRole.INPUT, 66, 22).addIngredients(recipe.getCentralIngredient());
        // Components around - keep simple: list in a row
        int x = 4;
        int y = 4;
        for (int i = 0; i < recipe.getComponents().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, x + (i % 7) * 18, y + (i / 7) * 18)
                    .addIngredients(recipe.getComponents().get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 22)
                .addItemStack(recipe.getResultItem());
    }
}


