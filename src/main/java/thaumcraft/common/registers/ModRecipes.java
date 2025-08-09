package thaumcraft.common.registers;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
// import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.api.crafting.IInfusionRecipe;
import thaumcraft.common.crafting.*;
import thaumcraft.common.lib.crafting.ArcaneRecipe;
import thaumcraft.common.lib.crafting.ArcaneRecipeSerializer;
import thaumcraft.common.lib.crafting.ArcaneWorkbenchRecipeSerializer;

public class ModRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "thaumcraft");

    public static final RegistryObject<CrucibleRecipeSerializer> CRUCIBLE_SERIALIZER = RECIPE_SERIALIZERS.register("crucible", CrucibleRecipeSerializer::new);
    public static final RegistryObject<ArcaneRecipeSerializer> ARCANE_SERIALIZER = RECIPE_SERIALIZERS.register("arcane", ArcaneRecipeSerializer::new);
    public static final RegistryObject<ArcaneShapedRecipeSerializer> ARCANE_SHAPED_SERIALIZER = RECIPE_SERIALIZERS.register("arcane_shaped", ArcaneShapedRecipeSerializer::new);
    public static final RegistryObject<ArcaneShapelessRecipeSerializer> ARCANE_SHAPELESS_SERIALIZER = RECIPE_SERIALIZERS.register("arcane_shapeless", ArcaneShapelessRecipeSerializer::new);
    // Combined serializer to support existing JSON using type: thaumcraft:arcane_workbench (auto-detects shaped vs shapeless)
    public static final RegistryObject<IRecipeSerializer<?>> ARCANE_WORKBENCH_SERIALIZER = RECIPE_SERIALIZERS.register("arcane_workbench", () -> new ArcaneWorkbenchRecipeSerializer());
    public static final RegistryObject<InfusionRecipeSerializer> INFUSION_SERIALIZER = RECIPE_SERIALIZERS.register("infusion", InfusionRecipeSerializer::new);
    public static final RegistryObject<InfusionRunicAugmentRecipeSerializer> INFUSION_RUNIC_SERIALIZER = RECIPE_SERIALIZERS.register("infusion_runic", InfusionRunicAugmentRecipeSerializer::new);
    public static final RegistryObject<InfusionEnchantmentRecipeSerializer> INFUSION_ENCHANTMENT_SERIALIZER = RECIPE_SERIALIZERS.register("infusion_enchantment", InfusionEnchantmentRecipeSerializer::new);
    public static final RegistryObject<SmeltingBonusRecipeSerializer> SMELTING_BONUS_SERIALIZER = RECIPE_SERIALIZERS.register("smelting_bonus", SmeltingBonusRecipeSerializer::new);

    // Special (non-JSON) recipe serializers
    public static final RegistryObject<IRecipeSerializer<?>> JAR_LABEL = RECIPE_SERIALIZERS.register(
            "jar_label", () -> new SpecialRecipeSerializer<>(RecipeJarLabel::new)
    );
    public static final RegistryObject<IRecipeSerializer<?>> SCRIBING_TOOLS = RECIPE_SERIALIZERS.register(
            "scribing_tools", () -> new SpecialRecipeSerializer<>(RecipeScribingTools::new)
    );
    public static final RegistryObject<IRecipeSerializer<?>> CANDLE_DYE = RECIPE_SERIALIZERS.register(
            "candle_dye", () -> new SpecialRecipeSerializer<>(RecipeCandleDye::new)
    );

    public static IRecipeType<ICrucibleRecipe> CRUCIBLE_RECIPE_TYPE;
    public static IRecipeType<ArcaneRecipe> ARCANE_RECIPE_TYPE;
    public static IRecipeType<IInfusionRecipe> INFUSION_RECIPE_TYPE;
    public static IRecipeType<SmeltingBonusRecipe> SMELTING_BONUS_RECIPE_TYPE;

    public static void registerRecipeTypes() {
        CRUCIBLE_RECIPE_TYPE = registerRecipeType("thaumcraft:crucible");
        ARCANE_RECIPE_TYPE = registerRecipeType("thaumcraft:arcane");
        INFUSION_RECIPE_TYPE = registerRecipeType("thaumcraft:infusion");
        SMELTING_BONUS_RECIPE_TYPE = registerRecipeType("thaumcraft:smelting_bonus");
    }

    private static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(String name) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(name), new IRecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
} 
