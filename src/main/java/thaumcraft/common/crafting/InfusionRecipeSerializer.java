package thaumcraft.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;

public class InfusionRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusionRecipe> {
    @Override
    public InfusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String research = JSONUtils.getAsString(json, "research", "");
        AspectList aspects = AspectList.parse(json, "aspects");
        Ingredient centralIngredient = Ingredient.fromJson(json.get("central_ingredient"));
        NonNullList<Ingredient> components = NonNullList.create();
        for (JsonElement element : JSONUtils.getAsJsonArray(json, "components")) {
            components.add(Ingredient.fromJson(element));
        }
        ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
        int instability = JSONUtils.getAsInt(json, "instability", 0);
        return new InfusionRecipe(recipeId, research, aspects, centralIngredient, components, result, instability);
    }

    @Nullable
    @Override
    public InfusionRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        String research = buffer.readUtf();
        AspectList aspects = AspectList.read(buffer);
        Ingredient centralIngredient = Ingredient.fromNetwork(buffer);
        NonNullList<Ingredient> components = NonNullList.create();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            components.add(Ingredient.fromNetwork(buffer));
        }
        ItemStack result = buffer.readItem();
        int instability = buffer.readVarInt();
        return new InfusionRecipe(recipeId, research, aspects, centralIngredient, components, result, instability);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, InfusionRecipe recipe) {
        buffer.writeUtf(recipe.getResearch());
        recipe.getAspects(null).write(buffer);
        recipe.getCentralIngredient().toNetwork(buffer);
        buffer.writeVarInt(recipe.getComponents().size());
        for (Ingredient ingredient : recipe.getComponents()) {
            ingredient.toNetwork(buffer);
        }
        buffer.writeItem(recipe.getResultItem());
        buffer.writeVarInt(recipe.getInstability());
    }
} 

