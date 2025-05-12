package thaumcraft.common.fluids;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import thaumcraft.Thaumcraft;
// import thaumcraft.common.lib.SoundsTC; // If you have custom sounds

import java.util.function.Consumer;

public class FluxGooFluidType extends net.minecraftforge.fluids.FluidType {

    public static final ResourceLocation FLUX_GOO_STILL_RL = new ResourceLocation(Thaumcraft.MODID, "fluid/flux_goo_still");
    public static final ResourceLocation FLUX_GOO_FLOWING_RL = new ResourceLocation(Thaumcraft.MODID, "fluid/flux_goo_flowing");
    public static final ResourceLocation FLUX_GOO_OVERLAY_RL = new ResourceLocation(Thaumcraft.MODID, "fluid/flux_goo_overlay"); // Optional

    public FluxGooFluidType() {
        super(FluidAttributes.builder(FLUX_GOO_STILL_RL, FLUX_GOO_FLOWING_RL)
                .translationKey("block.thaumcraft.flux_goo") // For lang file
                // .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY) // Default sounds
                // .sound(SoundsTC.GORE, SoundsTC.GORE) // If you have custom gore sounds for fill/empty
                .sound(SoundActions.BUCKET_FILL, SoundEvents.ITEM_BUCKET_FILL) // Using default sounds for now
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.ITEM_BUCKET_EMPTY)
                .overlay(FLUX_GOO_OVERLAY_RL) // Optional: if you have an overlay texture like lava
                .density(1500) // Example: water is 1000, lava is 3000
                .viscosity(3000) // Example: water is 1000, lava is 6000 (slower flow)
                .temperature(300) // Example: water is 300K
                .luminosity(5) // Example: if it glows slightly, 0-15
                .color(0xFF660066) // Original color was 0xFF660066 (alpha FF, red 66, green 00, blue 66)
        );
    }

    // This is needed for 1.18+ fluid rendering if it's not translucent by default.
    // For 1.16.5, RenderTypeLookup in client setup is more common.
    /*
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return FLUX_GOO_STILL_RL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLUX_GOO_FLOWING_RL;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return FLUX_GOO_OVERLAY_RL;
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return 0xFF660066;
            }
            
            @Override
            public @Nullable Vector3d getFlowingFogColor(FluidState fluidState, Entity entity, IBlockDisplayReader world, BlockPos pos, float partialTicks, Vector3d cameraPos) {
                return new Vector3d(0.4, 0.0, 0.4); // Purplish fog color
            }

            @Override
            public @Nullable Vector3d getStillFogColor(FluidState fluidState, Entity entity, IBlockDisplayReader world, BlockPos pos, float partialTicks, Vector3d cameraPos) {
                return new Vector3d(0.4, 0.0, 0.4); // Purplish fog color
            }
        });
    }
    */
} 