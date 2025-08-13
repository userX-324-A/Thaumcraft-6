package thaumcraft.common.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import thaumcraft.common.registers.ModMenus;

/**
 * Minimal Research Table menu used while theorycraft is gated. Provides only player inventory slots
 * and a hook to the block position via IWorldPosCallable for future expansion.
 */
public class ResearchTableMenu extends Container {
    private final IWorldPosCallable access;

    public ResearchTableMenu(int id, PlayerInventory playerInv, IWorldPosCallable access) {
        super(ModMenus.RESEARCH_TABLE.get(), id);
        this.access = access;

        // Player inventory (3 rows)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        // Always valid for now; future: gate by block presence
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            original = stackInSlot.copy();

            int playerInvStart = 0;
            int playerEnd = this.slots.size();

            if (!this.moveItemStackTo(stackInSlot, playerInvStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == original.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }
        return original;
    }
}


