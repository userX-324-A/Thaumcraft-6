package thaumcraft.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.common.registers.ModRecipes;

/**
 * Represents a smelting bonus rule used by special furnaces like the Infernal Furnace.
 * If the input matches {@link #input}, an extra {@link #bonusResult} may be produced with {@link #chance}.
 */
public class SmeltingBonusRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack bonusResult;
    private final float chance;

    public SmeltingBonusRecipe(ResourceLocation id, Ingredient input, ItemStack bonusResult, float chance) {
        this.id = id;
        this.input = input;
        this.bonusResult = bonusResult;
        this.chance = chance;
    }

    public Ingredient getInput() { return input; }
    public ItemStack getBonusResult() { return bonusResult; }
    public float getChance() { return chance; }

    public boolean matches(ItemStack stack) {
        return input.test(stack);
    }

    @Override
    public boolean matches(IInventory inv, World world) { return false; }

    @Override
    public ItemStack assemble(IInventory inv) { return ItemStack.EMPTY; }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return false; }

    @Override
    public ItemStack getResultItem() { return bonusResult; }

    @Override
    public ResourceLocation getId() { return id; }

    @Override
    public IRecipeSerializer<?> getSerializer() { return ModRecipes.SMELTING_BONUS_SERIALIZER.get(); }

    @Override
    public IRecipeType<?> getType() { return ModRecipes.SMELTING_BONUS_RECIPE_TYPE; }
}



