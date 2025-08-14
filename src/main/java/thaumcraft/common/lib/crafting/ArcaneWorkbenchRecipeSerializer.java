package thaumcraft.common.lib.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.common.crafting.ArcaneShapedRecipe;
import thaumcraft.common.crafting.ArcaneShapedRecipeSerializer;
import thaumcraft.common.crafting.ArcaneShapelessRecipe;
import thaumcraft.common.crafting.ArcaneShapelessRecipeSerializer;

/**
 * Accepts both shaped and shapeless arcane workbench JSON under a single type: thaumcraft:arcane_workbench
 * If the JSON contains a "pattern" array, treats it as shaped; otherwise shapeless.
 */
public class ArcaneWorkbenchRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<IRecipe<?>> {
    private static final ArcaneShapedRecipeSerializer SHAPED = new ArcaneShapedRecipeSerializer();
    private static final ArcaneShapelessRecipeSerializer SHAPELESS = new ArcaneShapelessRecipeSerializer();

    @Override
    public IRecipe<?> fromJson(ResourceLocation id, JsonObject json) {
        if (json.has("pattern")) {
            return SHAPED.fromJson(id, json);
        }
        return SHAPELESS.fromJson(id, json);
    }

    @Override
    public IRecipe<?> fromNetwork(ResourceLocation id, PacketBuffer buffer) {
        // First boolean indicates shaped vs shapeless
        boolean shaped = buffer.readBoolean();
        return shaped ? SHAPED.fromNetwork(id, buffer) : SHAPELESS.fromNetwork(id, buffer);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, IRecipe<?> recipe) {
        if (recipe instanceof ArcaneShapedRecipe) {
            buffer.writeBoolean(true);
            SHAPED.toNetwork(buffer, (ArcaneShapedRecipe) recipe);
        } else if (recipe instanceof ArcaneShapelessRecipe) {
            buffer.writeBoolean(false);
            SHAPELESS.toNetwork(buffer, (ArcaneShapelessRecipe) recipe);
        } else {
            throw new IllegalArgumentException("Unexpected arcane workbench recipe type: " + recipe);
        }
    }
}




