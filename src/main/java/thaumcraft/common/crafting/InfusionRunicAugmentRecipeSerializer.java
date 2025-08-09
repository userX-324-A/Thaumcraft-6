package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;

/**
 * Serializer for runic augmentation infusion recipes. Same JSON as standard infusion,
 * but result is derived from the central item by increasing TC.RUNIC.
 */
public class InfusionRunicAugmentRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusionRunicAugmentRecipe> {

    @Override
    public InfusionRunicAugmentRecipe fromJson(ResourceLocation id, JsonObject json) {
        String research = JSONUtils.getAsString(json, "research", "");
        AspectList aspects = AspectList.parse(json, "aspects");
        Ingredient central = Ingredient.fromJson(json.get("central_ingredient"));
        NonNullList<Ingredient> components = NonNullList.create();
        JSONUtils.getAsJsonArray(json, "components").forEach(e -> components.add(Ingredient.fromJson(e)));
        int instability = JSONUtils.getAsInt(json, "instability", 0);
        return new InfusionRunicAugmentRecipe(id, research, aspects, central, components, instability);
    }

    @Override
    public InfusionRunicAugmentRecipe fromNetwork(ResourceLocation id, PacketBuffer buf) {
        String research = buf.readUtf();
        AspectList aspects = AspectList.read(buf);
        Ingredient central = Ingredient.fromNetwork(buf);
        int comp = buf.readVarInt();
        NonNullList<Ingredient> components = NonNullList.create();
        for (int i = 0; i < comp; i++) {
            components.add(Ingredient.fromNetwork(buf));
        }
        int instability = buf.readVarInt();
        return new InfusionRunicAugmentRecipe(id, research, aspects, central, components, instability);
    }

    @Override
    public void toNetwork(PacketBuffer buf, InfusionRunicAugmentRecipe recipe) {
        buf.writeUtf(recipe.getResearch());
        recipe.getAspects(null).write(buf);
        recipe.getCentralIngredient().toNetwork(buf);
        buf.writeVarInt(recipe.getComponents().size());
        recipe.getComponents().forEach(i -> i.toNetwork(buf));
        buf.writeVarInt(recipe.getInstability());
    }
}



