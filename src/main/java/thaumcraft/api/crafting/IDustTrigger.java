package thaumcraft.api.crafting;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDustTrigger {
    Placement getValidFace(World world, PlayerEntity player, BlockPos pos, Direction face);
    
    void execute(World world, PlayerEntity player, BlockPos pos, Placement placement, Direction side);
    
    default List<BlockPos> sparkle(World world, PlayerEntity player, BlockPos pos, Placement placement) {
        return new ArrayList<BlockPos>();
    }
    
    public static class Placement {
        public int xOffset;
        public int yOffset;
        public int zOffset;
        public Direction facing;
        
        public Placement(int xOffset, int yOffset, int zOffset, Direction facing) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            this.facing = facing;
        }
    }
}

