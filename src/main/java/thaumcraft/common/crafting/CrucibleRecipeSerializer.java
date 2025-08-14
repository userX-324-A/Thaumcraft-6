package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;

public class CrucibleRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrucibleRecipe> {
    @Override
    public CrucibleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String research = json.has("research") ? json.get("research").getAsString() : "";
        AspectList aspects = AspectList.parse(json, "aspects");
        Ingredient catalyst = Ingredient.fromJson(json.get("catalyst"));
        ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
        return new CrucibleRecipe(recipeId, research, aspects, catalyst, result);
    }

    @Nullable
    @Override
    public CrucibleRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        String research = buffer.readUtf();
        AspectList aspects = new AspectList();
        aspects.readFromNBT(buffer.readNbt());
        Ingredient catalyst = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        return new CrucibleRecipe(recipeId, research, aspects, catalyst, result);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, CrucibleRecipe recipe) {
        buffer.writeUtf(recipe.getResearch());
        net.minecraft.nbt.CompoundNBT nbt = new net.minecraft.nbt.CompoundNBT();
        recipe.getAspects().writeToNBT(nbt);
        buffer.writeNbt(nbt);
        recipe.getCatalyst().toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
    }
} 

