package thaumcraft.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.capabilities.EssentiaTransportCapability;

public class ItemPhial extends Item {
    private static final String TAG_ASPECT = "Aspect";
    private static final String TAG_AMOUNT = "Amount";
    private static final int VIAL_AMOUNT = 8;

    public ItemPhial(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) return ActionResultType.SUCCESS;
        net.minecraft.tileentity.TileEntity be = world.getBlockEntity(context.getClickedPos());
        if (be == null) return ActionResultType.PASS;
        ItemStack held = context.getItemInHand();
        CompoundNBT tag = held.getOrCreateTag();
        thaumcraft.api.aspects.IEssentiaTransport et = be.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, context.getClickedFace()).orElse(null);
        if (et == null) return ActionResultType.PASS;

        PlayerEntity player = context.getPlayer();
        Direction face = context.getClickedFace();

        if (!tag.contains(TAG_ASPECT)) {
            Aspect available = et.getEssentiaType(face);
            if (available == null || et.getEssentiaAmount(face) < VIAL_AMOUNT) return ActionResultType.PASS;
            int taken = et.takeEssentia(available, VIAL_AMOUNT, face);
            if (taken < VIAL_AMOUNT) return ActionResultType.PASS;
            tag.putString(TAG_ASPECT, available.getTag());
            tag.putInt(TAG_AMOUNT, VIAL_AMOUNT);
            if (player != null) player.displayClientMessage(new TranslationTextComponent("thaumcraft.phial.fill", available.getName()), true);
            return ActionResultType.CONSUME;
        } else {
            Aspect stored = Aspect.getAspect(tag.getString(TAG_ASPECT));
            int amount = tag.getInt(TAG_AMOUNT);
            if (stored == null || amount <= 0) return ActionResultType.PASS;
            int added = et.addEssentia(stored, amount, face);
            if (added <= 0) return ActionResultType.PASS;
            amount -= added;
            if (amount <= 0) {
                tag.remove(TAG_ASPECT);
                tag.remove(TAG_AMOUNT);
            } else {
                tag.putInt(TAG_AMOUNT, amount);
            }
            if (player != null) player.displayClientMessage(new TranslationTextComponent("thaumcraft.phial.empty", stored.getName()), true);
            return ActionResultType.CONSUME;
        }
    }

    // No custom long-press behavior needed in 1.16.5
}



