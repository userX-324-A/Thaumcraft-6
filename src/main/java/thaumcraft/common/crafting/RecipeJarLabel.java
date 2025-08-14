package thaumcraft.common.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.items.ItemsTC;
// Avoid hard dependency on internal ItemPhial class; rely on API item identity

public class RecipeJarLabel extends SpecialRecipe {
    public RecipeJarLabel(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean hasLabel = false;
        boolean hasPhial = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ItemsTC.label) {
                    hasLabel = true;
                } else if (stack.getItem() == ItemsTC.phial) {
                    hasPhial = true;
                } else {
                    return false;
                }
            }
        }

        return hasLabel && hasPhial;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack label = ItemStack.EMPTY;
        ItemStack phial = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ItemsTC.label) {
                    label = stack;
                } else if (stack.getItem() == ItemsTC.phial) {
                    phial = stack;
                }
            }
        }

        if (label.isEmpty() || phial.isEmpty()) {
            return ItemStack.EMPTY;
        }

        AspectList aspects = ((IEssentiaContainerItem) phial.getItem()).getAspects(phial);
        if (aspects == null || aspects.size() == 0) {
            return ItemStack.EMPTY;
        }

        Aspect aspect = aspects.getAspects()[0];
        ItemStack result = new ItemStack(ItemsTC.label, 1);
        ((IEssentiaContainerItem) result.getItem()).setAspects(result, new AspectList().add(aspect, 1));

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() { return thaumcraft.common.registers.ModRecipes.JAR_LABEL.get(); }
}


