package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SmeltingBonusRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmeltingBonusRecipe> {
    @Override
    public SmeltingBonusRecipe fromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack bonus = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "bonus"), true);
        float chance = JSONUtils.getAsFloat(json, "chance", 0.33f);
        return new SmeltingBonusRecipe(id, input, bonus, chance);
    }

    @Override
    public SmeltingBonusRecipe fromNetwork(ResourceLocation id, PacketBuffer buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        ItemStack bonus = buf.readItem();
        float chance = buf.readFloat();
        return new SmeltingBonusRecipe(id, input, bonus, chance);
    }

    @Override
    public void toNetwork(PacketBuffer buf, SmeltingBonusRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        buf.writeItem(recipe.getResultItem());
        buf.writeFloat(recipe.getChance());
    }
}



