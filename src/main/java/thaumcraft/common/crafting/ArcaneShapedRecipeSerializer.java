package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;

public class ArcaneShapedRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArcaneShapedRecipe> {
    private static final ShapedRecipe.Serializer VANILLA_SERIALIZER = new ShapedRecipe.Serializer();

    @Override
    public ArcaneShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ShapedRecipe recipe = VANILLA_SERIALIZER.fromJson(recipeId, json);
        int vis = JSONUtils.getAsInt(json, "vis", 0);
        String research = JSONUtils.getAsString(json, "research", "");
        // Basic validation: if research is present, it must refer to a known research key at load time (best-effort)
        if (json.has("research") && research.isEmpty()) {
            throw new com.google.gson.JsonParseException("'research' must be a non-empty string when present");
        }
        AspectList crystals = AspectList.parse(json, "crystals");
        return new ArcaneShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem(), vis, research, crystals);
    }

    @Nullable
    @Override
    public ArcaneShapedRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        ShapedRecipe recipe = VANILLA_SERIALIZER.fromNetwork(recipeId, buffer);
        int vis = buffer.readVarInt();
        String research = buffer.readUtf();
        AspectList crystals = AspectList.read(buffer);
        return new ArcaneShapedRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem(), vis, research, crystals);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, ArcaneShapedRecipe recipe) {
        VANILLA_SERIALIZER.toNetwork(buffer, recipe);
        buffer.writeVarInt(recipe.getVis());
        buffer.writeUtf(recipe.getResearch());
        recipe.getCrystals().write(buffer);
    }
} 
