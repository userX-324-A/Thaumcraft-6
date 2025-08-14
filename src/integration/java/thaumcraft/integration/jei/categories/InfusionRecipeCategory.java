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
import thaumcraft.api.crafting.IInfusionRecipe;
import java.util.ArrayList;
import java.util.List;
import thaumcraft.common.registers.ModBlocks;

public class InfusionRecipeCategory implements IRecipeCategory<IInfusionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "infusion");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public InfusionRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.INFUSION_MATRIX.get()));
        this.background = guiHelper.createBlankDrawable(150, 60);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends IInfusionRecipe> getRecipeClass() {
        return IInfusionRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei.thaumcraft.category.infusion").getString();
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
    public void setIngredients(IInfusionRecipe recipe, IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        // central ingredient choices
        List<ItemStack> central = new ArrayList<>();
        for (ItemStack is : recipe.getCentralIngredient().getItems()) {
            central.add(is);
        }
        inputs.add(central);
        // components
        recipe.getComponents().forEach(ing -> {
            List<ItemStack> alts = new ArrayList<>();
            for (ItemStack is : ing.getItems()) {
                alts.add(is);
            }
            inputs.add(alts);
        });
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IInfusionRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        // central
        stacks.init(0, true, 66, 22);
        stacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        // components
        int base = 1;
        int x = 4;
        int y = 4;
        for (int i = 0; i < recipe.getComponents().size(); i++) {
            int sx = x + (i % 7) * 18;
            int sy = y + (i / 7) * 18;
            stacks.init(base + i, true, sx, sy);
            stacks.set(base + i, ingredients.getInputs(VanillaTypes.ITEM).get(Math.min(base + i, ingredients.getInputs(VanillaTypes.ITEM).size() - 1)));
        }
        int outIndex = base + recipe.getComponents().size();
        stacks.init(outIndex, false, 120, 22);
        stacks.set(outIndex, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}



