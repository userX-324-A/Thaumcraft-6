package thaumcraft.common.items.baubles;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import thaumcraft.api.items.IVisDiscountGear;

import javax.annotation.Nullable;
import java.util.List;

public class AmuletVisItem extends CurioItemBase implements IVisDiscountGear {
    public AmuletVisItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getVisDiscount(ItemStack stack, PlayerEntity player) {
        // passive vis bonus only when equipped (via Curios) or worn-equivalent
        if (player == null) return 0;
        if (CuriosCompat.isLoaded()) {
            if (CuriosCompat.isEquipped(player, stack)) return 5;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> lines, ITooltipFlag flag) {
        super.appendHoverText(stack, level, lines, flag);
        // Conditional tooltip shows when Curios is present
        if (CuriosCompat.isLoaded()) {
            lines.add(new TranslationTextComponent("tc.visdiscount").append(new StringTextComponent(": +5%")));
            lines.add(new StringTextComponent("Curios: amulet"));
        } else {
            lines.add(new TranslationTextComponent("tc.visdiscount").append(new StringTextComponent(": +5%")));
        }
    }
}


