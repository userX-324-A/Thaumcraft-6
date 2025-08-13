package thaumcraft.common.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import thaumcraft.common.registers.ModEntities;

public class ItemGrappleGun extends Item {
    public ItemGrappleGun(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if (!level.isClientSide) {
            thaumcraft.common.entities.misc.GrappleProjectileEntity proj = new thaumcraft.common.entities.misc.GrappleProjectileEntity(ModEntities.GRAPPLE_PROJECTILE.get(), level);
            proj.setPos(player.getX(), player.getEyeY(), player.getZ());
            proj.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 2.0f, 0.1f);
            proj.setOwner(player);
            level.addFreshEntity(proj);
            // Safety and cooldowns
            player.getCooldowns().addCooldown(this, 10);
            // Play fire sound for feedback (placeholder)
            player.playSound(net.minecraft.util.SoundEvents.CROSSBOW_SHOOT, 0.6f, 1.0f);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
    }
}


