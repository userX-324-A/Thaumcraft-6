package thaumcraft.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;

public class ThaumcraftApiHelper {
    public static boolean areItemStackTagsEqualRelaxed(ItemStack stack0, ItemStack stack1) {
        return false;
    }
    
    public static AspectList getObjectAspects(ItemStack is) {
        return null;
    }
    
    public static AspectList getEntityAspects(Entity entity) {
        return null;
    }
    
    public static RayTraceResult rayTraceIgnoringSource(World world, Vector3d v1, Vector3d v2, boolean bool1, boolean bool2, boolean bool3) {
        return null;
    }
    
    public static boolean isResearchComplete(String username, String researchkey) {
        return false;
    }
    
    public static boolean isResearchKnown(String username, String researchkey) {
        return false;
    }
    
    public static ItemStack getStackInRowAndColumn(Object instance, int row, int col) {
        return null;
    }
}

