package thaumcraft.api.items;

import java.util.ArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IArchitect {
    RayTraceResult getArchitectMOP(ItemStack stack, World world, LivingEntity player);
    
    ArrayList<BlockPos> getArchitectBlocks(ItemStack stack, World world, BlockPos pos, Direction side, PlayerEntity player);
    
    boolean showAxis(ItemStack stack, World world, PlayerEntity player, Direction side, EnumAxis axis);
    
    public enum EnumAxis {
        X, 
        Y, 
        Z;
    }
}


