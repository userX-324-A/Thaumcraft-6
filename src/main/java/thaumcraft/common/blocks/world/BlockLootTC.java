package thaumcraft.common.blocks.world;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockLootTC extends Block {

    public enum LootType {
        COMMON_CRATE,
        UNCOMMON_CRATE,
        RARE_CRATE,
        COMMON_URN,
        UNCOMMON_URN,
        RARE_URN
    }

    protected final VoxelShape shape;
    protected final LootType lootType; // For potential future use, main logic via loot tables

    // Define shapes based on original AxisAlignedBB
    // Urn BB: (0.125, 0.0625, 0.125) to (0.875, 0.8125, 0.875) -> makeCuboidShape(2, 1, 2, 14, 13, 14)
    public static final VoxelShape URN_SHAPE = Block.makeCuboidShape(2.0D, 1.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    // Crate BB: (0.0625, 0.0, 0.0625) to (0.9375, 0.875, 0.9375) -> makeCuboidShape(1, 0, 1, 15, 14, 15)
    public static final VoxelShape CRATE_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public BlockLootTC(AbstractBlock.Properties properties, VoxelShape shape, LootType lootType) {
        super(properties.hardnessAndResistance(0.15f, 0.0f)); // Hardness and resistance from old BlockLoot
        this.shape = shape;
        this.lootType = lootType;
        // nonOpaque() should be part of properties if needed, typically for custom shapes
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shape;
    }

    // getDrops is now handled by JSON loot tables per block.
    // The loot table path will be thaumcraft:blocks/registry_name_of_block

    // canSilkHarvest() - default behavior with loot tables should be that silk touch drops the block item.
    // If specific items should drop on silk touch, it needs to be in the loot table.

    // isOpaqueCube and isFullCube are effectively handled by nonOpaque() in properties and custom getShape.
} 