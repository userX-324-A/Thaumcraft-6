package thaumcraft.common.items.baubles;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * Curios-equipped items with simple passive effects.
 */
public final class CurioItems {

    private CurioItems() {}

    public static class CloudRingItem extends CurioItemBase {
        public CloudRingItem(Item.Properties props) { super(props); }

        @Override
        public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World level, @Nonnull net.minecraft.entity.Entity entity, int slot, boolean selected) {
            if (level.isClientSide) return;
            if (!(entity instanceof PlayerEntity)) return;
            PlayerEntity p = (PlayerEntity) entity;
            if (!CuriosCompat.isEquipped(p, stack)) return;
            if (p.fallDistance > 3.0f && !p.isOnGround()) {
                // Apply a brief slow falling effect or reduce fall damage by resetting distance periodically
                p.addEffect(new EffectInstance(Effects.SLOW_FALLING, 10, 0, false, false));
            }
        }

        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> lines, @Nonnull ITooltipFlag flag) {
            super.appendHoverText(stack, level, lines, flag);
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.cloud_ring"));
        }
    }

    public static class UndyingCharmItem extends CurioItemBase {
        public UndyingCharmItem(Item.Properties props) { super(props); }

        @Override
        public boolean canBeHurtBy(@Nonnull DamageSource source) { return super.canBeHurtBy(source); }

        @Override
        public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World level, @Nonnull net.minecraft.entity.Entity entity, int slot, boolean selected) {
            if (level.isClientSide) return;
            if (!(entity instanceof PlayerEntity)) return;
            PlayerEntity p = (PlayerEntity) entity;
            if (!CuriosCompat.isEquipped(p, stack)) return;
            // No per-tick effect; undying handled on hurt event via Events class
        }

        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> lines, @Nonnull ITooltipFlag flag) {
            super.appendHoverText(stack, level, lines, flag);
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.undying_charm"));
        }
    }

    public static class CuriosityRingItem extends CurioItemBase {
        public CuriosityRingItem(Item.Properties props) { super(props); }

        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> lines, @Nonnull ITooltipFlag flag) {
            super.appendHoverText(stack, level, lines, flag);
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.curiosity_ring"));
        }
    }

    public static class VerdantCharmItem extends CurioItemBase {
        public VerdantCharmItem(Item.Properties props) { super(props); }
        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> lines, @Nonnull ITooltipFlag flag) {
            super.appendHoverText(stack, level, lines, flag);
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.verdant_charm"));
        }
    }

    public static class VoidseerCharmItem extends CurioItemBase {
        public VoidseerCharmItem(Item.Properties props) { super(props); }
        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> lines, @Nonnull ITooltipFlag flag) {
            super.appendHoverText(stack, level, lines, flag);
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.voidseer_charm"));
        }
    }
}



