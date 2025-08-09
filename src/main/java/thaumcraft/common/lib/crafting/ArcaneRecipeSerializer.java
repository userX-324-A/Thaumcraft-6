package thaumcraft.common.lib.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;

public class ArcaneRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArcaneRecipe> {
    @Override
    public ArcaneRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = JSONUtils.getAsString(json, "group", "");
        String research = JSONUtils.getAsString(json, "research", "");
        int vis = JSONUtils.getAsInt(json, "vis", 0);
        JsonObject crystalsObject = new JsonObject();
        if (json.has("crystals")) {
            crystalsObject.add("crystals", json.get("crystals"));
        }
        AspectList crystals = AspectList.parse(crystalsObject, "crystals");
        NonNullList<Ingredient> ingredients = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for arcane recipe");
        }
        ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
        return new ArcaneRecipe(recipeId, group, research, vis, crystals, result, ingredients);
    }

    private static NonNullList<Ingredient> itemsFromJson(JsonArray json) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int i = 0; i < json.size(); i++) {
            Ingredient ingredient = Ingredient.fromJson(json.get(i));
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @Override
    public ArcaneRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        String group = buffer.readUtf(32767);
        String research = buffer.readUtf(32767);
        int vis = buffer.readVarInt();
        AspectList crystals = AspectList.read(buffer);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
        for (int j = 0; j < ingredients.size(); j++) {
            ingredients.set(j, Ingredient.fromNetwork(buffer));
        }
        ItemStack result = buffer.readItem();
        return new ArcaneRecipe(recipeId, group, research, vis, crystals, result, ingredients);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, ArcaneRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeUtf(recipe.getResearch());
        buffer.writeVarInt(recipe.getVis());
        recipe.getCrystals().write(buffer);
        buffer.writeVarInt(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }
        buffer.writeItem(recipe.getResultItem());
    }
} 
