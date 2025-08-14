package thaumcraft.common.lib.crafting;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.registers.ModRecipes;

public class ArcaneRecipe implements IArcaneRecipe {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final String research;
    private final int vis;
    private final AspectList crystals;

    public ArcaneRecipe(ResourceLocation id, String group, String research, int vis, AspectList crystals, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.vis = vis;
        this.crystals = crystals;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        int inputs = 0;
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                inputs++;
                stacks.add(stack);
            }
        }
        if (inputs != this.ingredients.size()) return false;

        boolean[] matched = new boolean[stacks.size()];
        for (Ingredient ing : this.ingredients) {
            boolean found = false;
            for (int i = 0; i < stacks.size(); i++) {
                if (!matched[i] && ing.test(stacks.get(i))) {
                    matched[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.ARCANE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.ARCANE_RECIPE_TYPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public String getResearch() {
        return this.research;
    }

    @Override
    public int getVis() {
        return this.vis;
    }

    @Override
    public AspectList getCrystals() {
        return this.crystals;
    }

    @Override
    public String getGroup() {
        return this.group;
    }
} 

