package thaumcraft.api.casters;

import java.util.HashMap;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CasterTriggerRegistry {
    private static HashMap<String, HashMap<BlockState, List<Trigger>>> triggers;
    
    public static void registerWandBlockTrigger(ICasterTriggerManager manager, int event, BlockState state, String modid) {
    }
    
    public static void registerCasterBlockTrigger(ICasterTriggerManager manager, int event, BlockState state) {
    }
    
    public static boolean hasTrigger(BlockState state) {
        return false;
    }
    
    public static boolean hasTrigger(BlockState state, String modid) {
        return false;
    }
    
    public static boolean performTrigger(World world, ItemStack casterStack, PlayerEntity player, BlockPos pos, Direction side, BlockState state) {
        return false;
    }
    
    public static boolean performTrigger(World world, ItemStack casterStack, PlayerEntity player, BlockPos pos, Direction side, BlockState state, String modid) {
        return false;
    }
    
    static {
        CasterTriggerRegistry.triggers = new HashMap<String, HashMap<BlockState, List<Trigger>>>();
    }
    
    private static class Trigger {
        public int event;
        public ICasterTriggerManager manager;
        
        public Trigger(ICasterTriggerManager manager, int event) {
            this.event = event;
            this.manager = manager;
        }
    }
}

