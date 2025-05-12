package thaumcraft.common.blocks.world.taint;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks; // For fallback AIR and potentially FLUX_GOO if not in ModBlocks
import net.minecraft.block.material.Material; // Assuming ThaumcraftMaterials.MATERIAL_TAINT is a Material
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.block.BlockRenderType; // For render type if needed
import net.minecraft.block.SoundType; // For sound type in properties

// Thaumcraft specific (placeholders if not yet updated)
import thaumcraft.api.ThaumcraftMaterials; // Placeholder for Material
import thaumcraft.api.aura.AuraHelper; // Placeholder
// import thaumcraft.common.blocks.BlockTC; // Assuming this is the base class
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler; // Placeholder
import thaumcraft.common.registration.ModBlocks; // For FLUX_GOO if registered
// import thaumcraft.common.lib.SoundsTC; // Placeholder for SoundType

import javax.annotation.Nullable; // Keep if getStateForPlacement truly can return null, else remove
import java.util.Random;

// If BlockTC is not available or not suitable, change "extends BlockTC" to "extends Block"
// and adjust constructor super call.
public class BlockTaintFeature extends Block implements ITaintBlock { // Changed BlockTC to Block for now

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected static final VoxelShape SHAPE_UP = Block.box(2, 10, 2, 14, 16, 14);
    protected static final VoxelShape SHAPE_DOWN = Block.box(2, 0, 2, 14, 6, 14);
    protected static final VoxelShape SHAPE_NORTH = Block.box(2, 2, 10, 14, 14, 16);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(2, 2, 0, 14, 14, 6);
    protected static final VoxelShape SHAPE_WEST = Block.box(10, 2, 2, 16, 14, 14); // Corrected x-end to 16
    protected static final VoxelShape SHAPE_EAST = Block.box(0, 2, 2, 6, 14, 14);

    public BlockTaintFeature(AbstractBlock.Properties properties) {
        // super(ThaumcraftMaterials.MATERIAL_TAINT, "taint_feature", properties); // If using BlockTC
        super(properties); // If extending Block directly
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
        // Removed setTickRandomly(true) - handle via properties .randomTicks()
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable // Keep this if it can truly return null, otherwise BlockState
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Ensure the block can be placed on the clicked face, and that the face itself is solid.
        // Basic implementation:
        Direction clickedFace = context.getClickedFace();
        BlockPos placePos = context.getClickedPos();
        World world = context.getLevel();

        // Check if the surface it's being placed on can support it
        // For example, if it needs a solid face:
        // BlockPos supportPos = placePos.relative(clickedFace.getOpposite());
        // BlockState supportState = world.getBlockState(supportPos);
        // if (!supportState.isFaceSturdy(world, supportPos, clickedFace)) {
        //     return null; // Cannot be placed here
        // }
        return this.defaultBlockState().setValue(FACING, clickedFace);
    }

    @Override
    @SuppressWarnings("deprecation") // Still uses VoxelShape getShape
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING)) {
            case UP:    return SHAPE_UP;
            case DOWN:  return SHAPE_DOWN;
            case NORTH: return SHAPE_NORTH;
            case SOUTH: return SHAPE_SOUTH;
            case WEST:  return SHAPE_WEST;
            case EAST:  return SHAPE_EAST;
            default:    return VoxelShapes.block(); // Should use full block or empty as default
        }
    }

    // Consider if this block should be transparent or opaque for lighting.
    // If it's a thin feature, true is likely fine.
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true; // Allows skylight to pass through
    }

    // getShadeBrightness is used for emissive lighting.
    // If it glows, return a value > 0. Max is 1.0F.
    // This does NOT add a light source to the world, that's done by properties.lightLevel(...)
    // The old setLightLevel(0.625f) should be translated to .lightLevel(state -> (int)(0.625f * 15))) in properties.
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.8F; // Example: if it has a slight glow on its own model.
                     // Or 0.2F if it's mostly dark unless lit by external light.
                     // The old BlockTC setLightLevel(0.625f) would be block property.
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true; // Must also have .randomTicks() in block properties
    }

    @Override
    @SuppressWarnings("deprecation") // onRemove is deprecated
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide && !isMoving && newState.getBlock() != this) {
            // Spawn EntityTaintCrawler
            EntityTaintCrawler crawler = new EntityTaintCrawler(ModEntities.TAINT_CRAWLER.get(), world);
            crawler.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.addFreshEntity(crawler);

            // Pollute aura
            AuraHelper.polluteAura(world, pos, 0.1f, false);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
    
    @Override
    public void die(World world, BlockPos pos, BlockState blockState) {
        // Check if ModBlocks.FLUX_GOO is available and not null
        if (ModBlocks.FLUX_GOO != null && ModBlocks.FLUX_GOO.get() != null) {
            world.setBlock(pos, ModBlocks.FLUX_GOO.get().defaultBlockState(), 3);
        } else {
            // Fallback if FLUX_GOO isn't registered or available
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    // No getBlockFaceShape needed if it's based on VoxelShape interactions.
    // If specific face culling or interaction is needed, override it.
    // Default is fine for many custom shape blocks.
    // The old "UNDEFINED" would mean it doesn't offer a solid face for things like torches/ladders.

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClientSide) {
            if (!TaintHelper.isNearTaintSeed(world, pos)) {
                die(world, pos, state);
            } else {
                TaintHelper.spreadFibres(world, pos);
            }
        }
    }

    // Check if the block should break if the supporting block is removed.
    // This is similar to how torches pop off if their wall is broken.
    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isClientSide) {
            Direction attachedToFace = state.getValue(FACING);
            BlockPos supportPos = pos.relative(attachedToFace.getOpposite());
            if (!worldIn.getBlockState(supportPos).isFaceSturdy(worldIn, supportPos, attachedToFace)) {
                // If the supporting block is no longer sturdy on the face this block is attached to, break this block.
                worldIn.destroyBlock(pos, true); // true to drop items
            }
        }
    }
    
    // Consider render type if not a standard cube.
    // For cutout textures (like glass or plants):
    // @Override
    // public BlockRenderType getRenderShape(BlockState state) {
    //    return BlockRenderType.MODEL; // default
    // }
    // In 1.16.5, transparency is often handled by ClientSetup and RenderTypeLookup.setRenderLayer(ModBlocks.MY_BLOCK.get(), RenderType.cutout());

    // Static method to provide the block properties for registration
    public static AbstractBlock.Properties TaintFeatureProperties() {
        return AbstractBlock.Properties.of(ThaumcraftMaterials.MATERIAL_TAINT, MaterialColor.COLOR_PURPLE)
                .strength(0.1f)
                .lightLevel(state -> 9) // 0.625f * 15
                .randomTicks()
                .sound(SoundType.GRASS) // Or SoundsTC.TAINT if available
                .noCollission()
                .noOcclusion();
    }
}
