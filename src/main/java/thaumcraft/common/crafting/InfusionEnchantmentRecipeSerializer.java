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
 * Serializer for infusion enchantment recipes. JSON:
 * {
 *   "type": "thaumcraft:infusion_enchantment",
 *   "research": "INFUSIONENCHANTMENT",
 *   "instability": 4,
 *   "aspects": { ... },
 *   "enchantment": "BURROWING",
 *   "central_ingredient": { ... },
 *   "components": [ ... ]
 * }
 */
public class InfusionEnchantmentRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusionEnchantmentRecipe> {

    @Override
    public InfusionEnchantmentRecipe fromJson(ResourceLocation id, JsonObject json) {
        String research = JSONUtils.getAsString(json, "research", "");
        AspectList aspects = AspectList.parse(json, "aspects");
        String enchantment = JSONUtils.getAsString(json, "enchantment");
        int instability = JSONUtils.getAsInt(json, "instability", 4);
        Ingredient central = Ingredient.fromJson(json.get("central_ingredient"));
        NonNullList<Ingredient> components = NonNullList.create();
        JSONUtils.getAsJsonArray(json, "components").forEach(e -> components.add(Ingredient.fromJson(e)));
        return new InfusionEnchantmentRecipe(id, research, aspects, central, components, instability, enchantment);
    }

    @Override
    public InfusionEnchantmentRecipe fromNetwork(ResourceLocation id, PacketBuffer buf) {
        String research = buf.readUtf();
        AspectList aspects = AspectList.read(buf);
        String enchantment = buf.readUtf();
        int instability = buf.readVarInt();
        Ingredient central = Ingredient.fromNetwork(buf);
        int comp = buf.readVarInt();
        NonNullList<Ingredient> components = NonNullList.create();
        for (int i = 0; i < comp; i++) {
            components.add(Ingredient.fromNetwork(buf));
        }
        return new InfusionEnchantmentRecipe(id, research, aspects, central, components, instability, enchantment);
    }

    @Override
    public void toNetwork(PacketBuffer buf, InfusionEnchantmentRecipe recipe) {
        buf.writeUtf(recipe.getResearch());
        recipe.getAspects(null).write(buf);
        buf.writeUtf(recipe.getEnchantmentId());
        buf.writeVarInt(recipe.getInstability());
        recipe.getCentralIngredient().toNetwork(buf);
        buf.writeVarInt(recipe.getComponents().size());
        recipe.getComponents().forEach(i -> i.toNetwork(buf));
    }
}




