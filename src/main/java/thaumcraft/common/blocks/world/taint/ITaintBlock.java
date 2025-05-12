package thaumcraft.common.blocks.world.taint;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public interface ITaintBlock
{
    void die(World world, BlockPos pos, BlockState blockState);
}
