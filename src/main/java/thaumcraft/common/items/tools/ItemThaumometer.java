package thaumcraft.common.items.tools;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import thaumcraft.api.research.ScanningManager;

public class ItemThaumometer extends Item {
    public ItemThaumometer(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            // Null means sky scan and other context-less scans may trigger
            ScanningManager.scanTheThing(player, null);
        }
        return ActionResult.success(player.getItemInHand(hand));
    }
}


