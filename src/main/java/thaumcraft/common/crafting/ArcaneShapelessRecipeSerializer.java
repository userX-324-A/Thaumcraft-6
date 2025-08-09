package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;

public class ArcaneShapelessRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArcaneShapelessRecipe> {
    private static final ShapelessRecipe.Serializer VANILLA_SERIALIZER = new ShapelessRecipe.Serializer();

    @Override
    public ArcaneShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ShapelessRecipe recipe = VANILLA_SERIALIZER.fromJson(recipeId, json);
        int vis = JSONUtils.getAsInt(json, "vis", 0);
        String research = JSONUtils.getAsString(json, "research", "");
        AspectList crystals = AspectList.parse(json, "crystals");
        return new ArcaneShapelessRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients(), vis, research, crystals);
    }

    @Nullable
    @Override
    public ArcaneShapelessRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        ShapelessRecipe recipe = VANILLA_SERIALIZER.fromNetwork(recipeId, buffer);
        int vis = buffer.readVarInt();
        String research = buffer.readUtf();
        AspectList crystals = AspectList.read(buffer);
        return new ArcaneShapelessRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients(), vis, research, crystals);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, ArcaneShapelessRecipe recipe) {
        VANILLA_SERIALIZER.toNetwork(buffer, recipe);
        buffer.writeVarInt(recipe.getVis());
        buffer.writeUtf(recipe.getResearch());
        recipe.getCrystals().write(buffer);
    }
} 
