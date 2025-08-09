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
 * Infusion recipe that augments the central item with Runic Shielding.
 * The result is the central stack with its TC.RUNIC value incremented by 1.
 */
public class InfusionRunicAugmentRecipe extends InfusionRecipe implements IInfusionRecipe {

    public InfusionRunicAugmentRecipe(ResourceLocation id,
                                      String research,
                                      AspectList aspects,
                                      Ingredient centralIngredient,
                                      NonNullList<Ingredient> components,
                                      int instability) {
        super(id, research, aspects, centralIngredient, components, ItemStack.EMPTY, instability);
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        ItemStack central = findCentralMatch(inv);
        if (central.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = central.copy();
        CompoundNBT tag = result.getOrCreateTag();
        byte current = tag.getByte("TC.RUNIC");
        tag.putByte("TC.RUNIC", (byte) Math.min(127, current + 1));
        return result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSION_RUNIC_SERIALIZER.get();
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



