package thaumcraft.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import thaumcraft.common.lib.crafting.ArcaneRecipe;
import thaumcraft.common.registers.ModBlocks;
import thaumcraft.common.registers.ModRecipes;
import thaumcraft.integration.jei.categories.ArcaneRecipeCategory;
import thaumcraft.integration.jei.categories.CrucibleRecipeCategory;
import thaumcraft.integration.jei.categories.InfusionRecipeCategory;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.api.crafting.IInfusionRecipe;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
@OnlyIn(Dist.CLIENT)
public class ThaumcraftJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation("thaumcraft", "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    private static boolean jeiPresent() {
        return ModList.get().isLoaded("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (!jeiPresent()) return;
        registration.addRecipeCategories(
                new ArcaneRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CrucibleRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new InfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!jeiPresent()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;
        // Arcane
        List<ArcaneRecipe> arcane = mc.level.getRecipeManager().getAllRecipesFor(ModRecipes.ARCANE_RECIPE_TYPE)
                .stream().map(r -> (ArcaneRecipe) r).collect(Collectors.toList());
        registration.addRecipes(ArcaneRecipeCategory.RECIPE_TYPE, arcane);
        // Crucible
        List<ICrucibleRecipe> crucible = mc.level.getRecipeManager().getAllRecipesFor(ModRecipes.CRUCIBLE_RECIPE_TYPE)
                .stream().map(r -> (ICrucibleRecipe) r).collect(Collectors.toList());
        registration.addRecipes(CrucibleRecipeCategory.RECIPE_TYPE, crucible);
        // Infusion
        List<IInfusionRecipe> infusion = mc.level.getRecipeManager().getAllRecipesFor(ModRecipes.INFUSION_RECIPE_TYPE)
                .stream().map(r -> (IInfusionRecipe) r).collect(Collectors.toList());
        registration.addRecipes(InfusionRecipeCategory.RECIPE_TYPE, infusion);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (!jeiPresent()) return;
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ARCANE_WORKBENCH.get()), ArcaneRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRUCIBLE.get()), CrucibleRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_MATRIX.get()), InfusionRecipeCategory.RECIPE_TYPE);
    }
}


