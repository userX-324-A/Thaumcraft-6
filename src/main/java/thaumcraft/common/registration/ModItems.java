package thaumcraft.common.registration;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import thaumcraft.common.registration.ModFluids;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);

    // Block Items
    public static final RegistryObject<Item> GREATWOOD_PLANKS_ITEM = ITEMS.register("greatwood_planks",
            () -> new BlockItem(ModBlocks.GREATWOOD_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVERWOOD_PLANKS_ITEM = ITEMS.register("silverwood_planks",
            () -> new BlockItem(ModBlocks.SILVERWOOD_PLANKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> GREATWOOD_LOG_ITEM = ITEMS.register("greatwood_log",
            () -> new BlockItem(ModBlocks.GREATWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVERWOOD_LOG_ITEM = ITEMS.register("silverwood_log",
            () -> new BlockItem(ModBlocks.SILVERWOOD_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> GREATWOOD_LEAVES_ITEM = ITEMS.register("greatwood_leaves",
            () -> new BlockItem(ModBlocks.GREATWOOD_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> SILVERWOOD_LEAVES_ITEM = ITEMS.register("silverwood_leaves",
            () -> new BlockItem(ModBlocks.SILVERWOOD_LEAVES.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> GREATWOOD_SAPLING_ITEM = ITEMS.register("greatwood_sapling",
            () -> new BlockItem(ModBlocks.GREATWOOD_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> SILVERWOOD_SAPLING_ITEM = ITEMS.register("silverwood_sapling",
            () -> new BlockItem(ModBlocks.SILVERWOOD_SAPLING.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    // TODO: Add other block items as their blocks are registered
    public static final RegistryObject<Item> ORE_AMBER_ITEM = ITEMS.register("ore_amber",
            () -> new BlockItem(ModBlocks.ORE_AMBER.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ORE_CINNABAR_ITEM = ITEMS.register("ore_cinnabar",
            () -> new BlockItem(ModBlocks.ORE_CINNABAR.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ORE_QUARTZ_ITEM = ITEMS.register("ore_quartz",
            () -> new BlockItem(ModBlocks.ORE_QUARTZ.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> TAINT_LOG_ITEM = ITEMS.register("taint_log",
            () -> new BlockItem(ModBlocks.TAINT_LOG.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    // Fluid Buckets
    public static final RegistryObject<Item> FLUX_GOO_BUCKET = ITEMS.register("flux_goo_bucket",
            () -> new BucketItem(() -> ModFluids.STILL_FLUX_GOO.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(Thaumcraft.TAB_TC)));

    public static void register() {
        // This method is not strictly necessary if using the event bus registration in the main mod file,
        // but can be useful for organizing registration calls if needed later.
    }
} 