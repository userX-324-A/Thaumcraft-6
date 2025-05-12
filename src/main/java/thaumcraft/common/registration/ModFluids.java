package thaumcraft.common.registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.ForgeFlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.world.taint.BlockFluxGoo; // We will create/update this next
import thaumcraft.common.fluids.FluxGooFluidType;

public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = 
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Thaumcraft.MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Thaumcraft.MODID);

    // Flux Goo Fluid Type
    public static final RegistryObject<FluidType> FLUX_GOO_FLUID_TYPE = FLUID_TYPES.register("flux_goo",
            FluxGooFluidType::new);

    // Flux Goo Fluid
    public static final RegistryObject<ForgeFlowingFluid> STILL_FLUX_GOO = FLUIDS.register("flux_goo",
            () -> new ForgeFlowingFluid.Source(ModFluids.FLUX_GOO_PROPERTIES));
    public static final RegistryObject<ForgeFlowingFluid> FLOWING_FLUX_GOO = FLUIDS.register("flowing_flux_goo",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.FLUX_GOO_PROPERTIES));

    // Flux Goo Block
    // Moved to ModBlocks.java to keep block registrations centralized
    // public static final RegistryObject<FlowingFluidBlock> FLUX_GOO_BLOCK = ModBlocks.BLOCKS.register("flux_goo",
    //        () -> new BlockFluxGoo(() -> ModFluids.STILL_FLUX_GOO.get(),
    //                AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));

    // Flux Goo Bucket
    // Moved to ModItems.java to keep item registrations centralized
    // public static final RegistryObject<Item> FLUX_GOO_BUCKET = ModItems.ITEMS.register("flux_goo_bucket",
    //        () -> new BucketItem(() -> ModFluids.STILL_FLUX_GOO.get(),
    //                new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));


    public static final ForgeFlowingFluid.Properties FLUX_GOO_PROPERTIES = new ForgeFlowingFluid.Properties(
            FLUX_GOO_FLUID_TYPE,
            STILL_FLUX_GOO,
            FLOWING_FLUX_GOO
    )
    .block(() -> ModBlocks.FLUX_GOO_BLOCK.get()) // Link to the registered block
    .bucket(() -> ModItems.FLUX_GOO_BUCKET.get()); // Link to the registered bucket

    public static void register() {
        // Method for explicit registration call if needed, usually handled by EventBus
    }
} 