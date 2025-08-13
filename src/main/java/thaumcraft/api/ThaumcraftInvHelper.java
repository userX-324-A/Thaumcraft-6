package thaumcraft.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import thaumcraft.common.lib.utils.TagUtils;

/**
 * Inventory helper utilities adapted for 1.16.5 (capabilities + wrappers).
 */
public class ThaumcraftInvHelper {

    public static class InvFilter {
        public boolean igDmg;
        public boolean igNBT;
        public boolean useOre;
        public boolean useMod;
        public boolean relaxedNBT = false;

        public InvFilter(boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
            igDmg = ignoreDamage;
            igNBT = ignoreNBT;
            this.useOre = useOre;
            this.useMod = useMod;
        }

        public InvFilter setRelaxedNBT() {
            relaxedNBT = true;
            return this;
        }

        public static final InvFilter STRICT = new InvFilter(false, false, false, false);
        public static final InvFilter BASEORE = new InvFilter(false, false, true, false);
    }

    public static IItemHandler getItemHandlerAt(World world, BlockPos pos, Direction side) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile == null) return null;

        LazyOptional<IItemHandler> cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
        if (cap.isPresent()) {
            return cap.orElse(null);
        }

        if (tile instanceof ISidedInventory && side != null) {
            return new SidedInvWrapper((ISidedInventory) tile, side);
        }
        if (tile instanceof IInventory) {
            return new InvWrapper((IInventory) tile);
        }
        return null;
    }

    public static IItemHandler wrapInventory(IInventory inventory, Direction side) {
        if (inventory instanceof ISidedInventory && side != null) {
            return new SidedInvWrapper((ISidedInventory) inventory, side);
        }
        return new InvWrapper(inventory);
    }

    public static ItemStack insertStackAt(World world, BlockPos pos, Direction side, ItemStack stack, boolean simulate) {
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        IItemHandler handler = getItemHandlerAt(world, pos, side);
        if (handler == null) return stack;

        ItemStack remaining = stack.copy();
        for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
            remaining = handler.insertItem(slot, remaining, simulate);
        }
        return remaining;
    }

    public static int countTotalItemsIn(World world, BlockPos pos, Direction side, ItemStack stack, InvFilter filter) {
        IItemHandler handler = getItemHandlerAt(world, pos, side);
        if (handler == null) return 0;
        return countTotalItemsIn(handler, stack, filter);
    }

    public static int countTotalItemsIn(IItemHandler inventory, ItemStack stack, InvFilter filter) {
        if (inventory == null || stack == null || stack.isEmpty()) return 0;
        int total = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (!slotStack.isEmpty() && areItemsEqual(slotStack, stack, filter)) {
                total += slotStack.getCount();
            }
        }
        return total;
    }

    /**
     * Relaxed tag equality: treats null/empty tags as equal and only compares when both have tags.
     */
    public static boolean areItemStackTagsEqualRelaxed(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        boolean aHas = a.hasTag();
        boolean bHas = b.hasTag();
        if (!aHas && !bHas) return true;
        if (aHas != bHas) return false;
        return a.getTag().equals(b.getTag());
    }

    private static boolean areItemsEqual(ItemStack a, ItemStack b, InvFilter filter) {
        if (a.isEmpty() || b.isEmpty()) return false;
        if (filter.useMod) {
            String m1 = a.getItem().getRegistryName() != null ? a.getItem().getRegistryName().getNamespace() : "";
            String m2 = b.getItem().getRegistryName() != null ? b.getItem().getRegistryName().getNamespace() : "";
            return m1.equals(m2);
        }
        if (filter.useOre && TagUtils.itemsShareAnyForgeTag(a, b)) {
            return true;
        }
        boolean nbtEqual = true;
        if (!filter.igNBT) {
            nbtEqual = filter.relaxedNBT ? areItemStackTagsEqualRelaxed(a, b) : ItemStack.tagMatches(a, b);
        }
        boolean damageEqual = filter.igDmg || a.getDamageValue() == b.getDamageValue() || a.getDamageValue() == 32767 || b.getDamageValue() == 32767;
        return a.getItem() == b.getItem() && damageEqual && nbtEqual;
    }
}

