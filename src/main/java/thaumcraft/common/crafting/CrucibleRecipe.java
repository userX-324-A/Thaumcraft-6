package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.common.registers.ModRecipes;

import javax.annotation.Nullable;

public class CrucibleRecipe implements ICrucibleRecipe {
    private final ResourceLocation id;
    private final String research;
    private final AspectList aspects;
    private final Ingredient catalyst;
    private final ItemStack result;

    public CrucibleRecipe(ResourceLocation id, String research, AspectList aspects, Ingredient catalyst, ItemStack result) {
        this.id = id;
        this.research = research;
        this.aspects = aspects;
        this.catalyst = catalyst;
        this.result = result;
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
        return ModRecipes.CRUCIBLE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.CRUCIBLE_RECIPE_TYPE;
    }

    @Override
    public AspectList getAspects() {
        return aspects;
    }

    @Override
    public String getResearch() {
        return research;
    }

    @Override
    public Ingredient getCatalyst() {
        return catalyst;
    }
}


