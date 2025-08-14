package thaumcraft.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.registers.ModRecipes;
// Optional client-only JEI ghost gating will be implemented separately; server safety is enforced in menu

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;

public class ArcaneShapelessRecipe extends ShapelessRecipe implements IArcaneRecipe {
    private final int vis;
    private final String research;
    private final AspectList crystals;

    public ArcaneShapelessRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn, int vis, String research, AspectList crystals) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
        this.vis = vis;
        this.research = research;
        this.crystals = crystals;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() { return ModRecipes.ARCANE_SHAPELESS_SERIALIZER.get(); }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.ARCANE_RECIPE_TYPE;
    }

    // keep default matches; gating occurs in ArcaneWorkbenchMenu

    @Override
    public int getVis() {
        return vis;
    }

    @Override
    public String getResearch() {
        return research;
    }

    @Override
    public AspectList getCrystals() {
        return crystals;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArcaneShapelessRecipe> {
        public static final ArcaneShapelessRecipe.Serializer INSTANCE = new ArcaneShapelessRecipe.Serializer();

        @Override
        public ArcaneShapelessRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapelessRecipe recipe = IRecipeSerializer.SHAPELESS_RECIPE.fromJson(recipeId, json);
            int vis = json.get("vis").getAsInt();
            String research = json.get("research").getAsString();
            AspectList crystals = AspectList.parse(json, "crystals");
            return new ArcaneShapelessRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients(), vis, research, crystals);
        }

        @Nullable
        @Override
        public ArcaneShapelessRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            ShapelessRecipe recipe = IRecipeSerializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer);
            int vis = buffer.readInt();
            String research = buffer.readUtf();
            AspectList crystals = new AspectList();
            crystals.readFromNBT(buffer.readNbt());
            return new ArcaneShapelessRecipe(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients(), vis, research, crystals);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ArcaneShapelessRecipe recipe) {
            IRecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
            buffer.writeInt(recipe.vis);
            buffer.writeUtf(recipe.research);
            CompoundNBT nbt = new CompoundNBT();
            recipe.crystals.writeToNBT(nbt);
            buffer.writeNbt(nbt);
        }
    }
} 

