package thaumcraft.api.crafting;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.Ingredient.IItemList;
import net.minecraft.item.crafting.Ingredient.SingleItemList;
import thaumcraft.api.ThaumcraftInvHelper;



public class IngredientNBTTC extends Ingredient {
    private final ItemStack stack;

    public IngredientNBTTC(ItemStack stack) {
        super(java.util.stream.Stream.<IItemList>of(new SingleItemList(stack)));
        this.stack = stack.copy();
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null) return false;
        if (input.isEmpty()) return false;
        if (stack.getItem() != input.getItem()) return false;
        return ThaumcraftInvHelper.areItemStackTagsEqualRelaxed(stack, input);
    }

    @Override
    public boolean isSimple() { return false; }
}

