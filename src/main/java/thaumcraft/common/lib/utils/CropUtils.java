package thaumcraft.common.lib.utils;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.StemBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CropUtils {
    public static final Set<ResourceLocation> clickableCrops = new HashSet<>();
    public static final Set<ResourceLocation> standardCrops = new HashSet<>();
    public static final Set<ResourceLocation> stackedCrops = new HashSet<>();
    public static final Set<ResourceLocation> lampBlacklist = new HashSet<>();

    public static void addStandardCrop(ItemStack stack, int ignoredMeta) {
        Block block = Block.byItem(stack.getItem());
        if (block != null && block.getRegistryName() != null) {
            standardCrops.add(block.getRegistryName());
        }
    }

    public static void addStandardCrop(Block block, int ignoredMeta) {
        if (block != null && block.getRegistryName() != null) {
            standardCrops.add(block.getRegistryName());
        }
    }

    public static void addClickableCrop(ItemStack stack, int ignoredMeta) {
        Block block = Block.byItem(stack.getItem());
        if (block != null && block.getRegistryName() != null) {
            clickableCrops.add(block.getRegistryName());
        }
    }

    public static void addStackedCrop(ItemStack stack, int ignoredMeta) {
        Block block = Block.byItem(stack.getItem());
        addStackedCrop(block, ignoredMeta);
    }

    public static void addStackedCrop(Block block, int ignoredMeta) {
        if (block != null && block.getRegistryName() != null) {
            stackedCrops.add(block.getRegistryName());
        }
    }

    public static boolean isGrownCrop(World world, BlockPos pos) {
        if (world.isEmptyBlock(pos)) return false;
        Block block = world.getBlockState(pos).getBlock();
        ResourceLocation id = block.getRegistryName();
        boolean inLists = id != null && (standardCrops.contains(id) || clickableCrops.contains(id) || stackedCrops.contains(id));

        // Mature crops: IGrowable that cannot grow further and not a StemBlock
        if (block instanceof IGrowable && !(block instanceof StemBlock)) {
            // Heuristic: treat as mature if CropsBlock is at max age; otherwise skip IGrowable direct call
            if (block instanceof CropsBlock) {
                if (((CropsBlock) block).isMaxAge(world.getBlockState(pos))) return true;
            }
        }
        if (block instanceof CropsBlock) {
            CropsBlock crops = (CropsBlock) block;
            if (crops.isMaxAge(world.getBlockState(pos))) return true;
        }
        // Stacked crops mature if the block below is the same (e.g., sugar cane/cactus)
        if (id != null && stackedCrops.contains(id)) {
            Block below = world.getBlockState(pos.below()).getBlock();
            if (below == block) return true;
        }
        return inLists;
    }

    public static void blacklistLamp(ItemStack stack, int ignoredMeta) {
        Block block = Block.byItem(stack.getItem());
        if (block != null && block.getRegistryName() != null) {
            lampBlacklist.add(block.getRegistryName());
        }
    }

    public static boolean doesLampGrow(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        ResourceLocation id = block.getRegistryName();
        return id == null || !lampBlacklist.contains(id);
    }
}


