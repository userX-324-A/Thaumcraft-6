package thaumcraft.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IInfusionRecipe;
import thaumcraft.common.registers.ModRecipes;

/**
 * Generic infusion enchantment recipe that upgrades a central item by tagging it with
 * an infusion enchantment record in its NBT. The specific enchantment and rules are
 * handled by the serializer and recipe payload.
 */
public class InfusionEnchantmentRecipe extends InfusionRecipe implements IInfusionRecipe {

    private final String enchantmentId; // e.g. "BURROWING", "ARCING", etc.

    public InfusionEnchantmentRecipe(ResourceLocation id,
                                     String research,
                                     AspectList aspects,
                                     Ingredient centralIngredient,
                                     NonNullList<Ingredient> components,
                                     int instability,
                                     String enchantmentId) {
        super(id, research, aspects, centralIngredient, components, ItemStack.EMPTY, instability);
        this.enchantmentId = enchantmentId;
    }

    public String getEnchantmentId() {
        return enchantmentId;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        ItemStack central = findCentralMatch(inv);
        if (central.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = central.copy();
        CompoundNBT tag = result.getOrCreateTag();
        CompoundNBT ie = tag.getCompound("tc_infusion_enchantments");
        int current = ie.getInt(enchantmentId);
        ie.putInt(enchantmentId, current + 1);
        tag.put("tc_infusion_enchantments", ie);
        return result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSION_ENCHANTMENT_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.INFUSION_RECIPE_TYPE;
    }

    private ItemStack findCentralMatch(IInventory inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && getCentralIngredient().test(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}



