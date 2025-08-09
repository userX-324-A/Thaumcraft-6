package thaumcraft.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IInfusionRecipe;
import thaumcraft.common.registers.ModRecipes;

public class InfusionRecipe implements IInfusionRecipe {
    private final ResourceLocation id;
    private final String research;
    private final AspectList aspects;
    private final Ingredient centralIngredient;
    private final NonNullList<Ingredient> components;
    private final ItemStack result;
    private final int instability;

    public InfusionRecipe(ResourceLocation id, String research, AspectList aspects, Ingredient centralIngredient, NonNullList<Ingredient> components, ItemStack result, int instability) {
        this.id = id;
        this.research = research;
        this.aspects = aspects;
        this.centralIngredient = centralIngredient;
        this.components = components;
        this.result = result;
        this.instability = instability;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSION_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.INFUSION_RECIPE_TYPE;
    }

    @Override
    public AspectList getAspects(IInventory inv) {
        return aspects;
    }

    @Override
    public String getResearch() {
        return research;
    }

    @Override
    public NonNullList<Ingredient> getComponents() {
        return components;
    }

    @Override
    public Ingredient getCentralIngredient() {
        return centralIngredient;
    }

    @Override
    public int getInstability() {
        return instability;
    }
}

