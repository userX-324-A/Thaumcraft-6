package thaumcraft.api.casters;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInteractWithCaster {
    boolean onCasterRightClick(World world, ItemStack casterStack, PlayerEntity player, BlockPos pos, Direction side, Hand hand);
}

