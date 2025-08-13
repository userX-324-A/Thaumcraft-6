package thaumcraft.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.blocks.ILabelable;

public class ItemLabel extends Item {
    public ItemLabel(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        if (world.isClientSide) return ActionResultType.SUCCESS;
        ItemStack stack = context.getItemInHand();
        net.minecraft.block.BlockState state = world.getBlockState(pos);
        net.minecraft.block.Block block = state.getBlock();
        if (block instanceof ILabelable) {
            boolean applied = ((ILabelable) block).applyLabel(context.getPlayer(), pos, side, stack);
            if (applied) {
                if (!context.getPlayer().abilities.instabuild) {
                    stack.shrink(1);
                }
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }
}


