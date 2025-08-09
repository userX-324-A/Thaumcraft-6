package thaumcraft.world.crafting.arcane;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.registry.Registration;

import javax.annotation.Nullable;

public class ArcaneWorkbenchTile extends TileEntity implements ISidedInventory, ITickableTileEntity {
    public static final int SLOT_GRID_START = 0; // 3x3 crafting grid (0..8)
    public static final int SLOT_GRID_END = 8;
    public static final int SLOT_CATALYST = 9; // focus/catalyst/vis source
    public static final int SLOT_OUTPUT = 10;
    private final NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);

    public ArcaneWorkbenchTile() {
        super(Registration.ARCANE_WORKBENCH_TILE.get());
    }

    @Override
    public void tick() {
        // server-side recipe check stub
        if (level != null && !level.isClientSide) {
            // TODO: integrate IArcaneRecipe matching, research/vis gating, output preview
        }
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.thaumcraft.arcane_workbench");
    }

    public Container createMenu(int id, net.minecraft.entity.player.PlayerInventory inv, PlayerEntity player) {
        return ArcaneWorkbenchContainer.createServer(id, inv, this);
    }

    // Inventory
    @Override
    public int getContainerSize() { return items.size(); }
    @Override
    public boolean isEmpty() { for (ItemStack s : items) if (!s.isEmpty()) return false; return true; }
    @Override
    public ItemStack getItem(int index) { return items.get(index); }
    @Override
    public ItemStack removeItem(int index, int count) { return ItemStackHelper.removeItem(items, index, count); }
    @Override
    public ItemStack removeItemNoUpdate(int index) { ItemStack s = items.get(index); items.set(index, ItemStack.EMPTY); return s; }
    @Override
    public void setItem(int index, ItemStack stack) { items.set(index, stack); setChanged(); }
    @Override
    public boolean stillValid(PlayerEntity player) { return player.distanceToSqr(worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5) <= 64.0; }
    @Override
    public void clearContent() { items.clear(); }

    @Override
    public int[] getSlotsForFace(Direction side) { return new int[0]; }
    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) { return false; }
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) { return false; }

    @Override
    public void load(CompoundNBT tag) {
        super.load(tag);
        ItemStackHelper.loadAllItems(tag, this.items);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        ItemStackHelper.saveAllItems(tag, this.items);
        return tag;
    }
}



