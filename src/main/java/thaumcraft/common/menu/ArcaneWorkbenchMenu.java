package thaumcraft.common.menu;

import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.blocks.world.ArcaneWorkbenchBlockEntity;
import thaumcraft.common.registers.ModMenus;
import thaumcraft.common.registers.ModRecipes;

public class ArcaneWorkbenchMenu extends Container {
    private final IWorldPosCallable access;
    private final World level;
    private final ArcaneWorkbenchBlockEntity blockEntity;
    private final PlayerEntity player;

    private final CraftResultInventory resultContainer = new CraftResultInventory();

    public ArcaneWorkbenchMenu(int id, PlayerInventory playerInv, IWorldPosCallable access) {
        super(ModMenus.ARCANE_WORKBENCH.get(), id);
        this.access = access;
        this.level = playerInv.player.level;
        this.player = playerInv.player;
        BlockPos pos = access.evaluate((lvl, p) -> p).orElse(BlockPos.ZERO);
        TileEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ArcaneWorkbenchBlockEntity)) throw new IllegalStateException("Missing Arcane Workbench at " + pos);
        this.blockEntity = (ArcaneWorkbenchBlockEntity) be;

        // Crafting grid 3x3
        IItemHandler grid = blockEntity.getCraftingGrid();
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new NotifyingSlotItemHandler(grid, index++, 40 + col * 24, 40 + row * 24));
            }
        }

        // Crystal inputs around
        IItemHandler crystals = blockEntity.getCrystalInput();
        addSlot(new NotifyingSlotItemHandler(crystals, 0, 64, 13));
        addSlot(new NotifyingSlotItemHandler(crystals, 1, 17, 35));
        addSlot(new NotifyingSlotItemHandler(crystals, 2, 112, 35));
        addSlot(new NotifyingSlotItemHandler(crystals, 3, 17, 93));
        addSlot(new NotifyingSlotItemHandler(crystals, 4, 112, 93));
        addSlot(new NotifyingSlotItemHandler(crystals, 5, 64, 115));

        // Result slot (ghost, computed dynamically)  use a non-insertable slot
        addSlot(new Slot(resultContainer, 0, 160, 64) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
            @Override public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                access.execute((lvl, pos) -> {
                    IArcaneRecipe recipe = findMatchingArcaneRecipe();
                    if (recipe == null) return;

                    String research = recipe.getResearch();
                    if (research != null && !research.isEmpty()) {
                        if (!ThaumcraftCapabilities.knowsResearch(player, research)) return;
                    }

                    int visCost = recipe.getVis();
                    if (visCost > 0) {
                        float drained = 0f;
                        try {
                            drained = AuraHelper.drainVis(lvl, pos, visCost);
                        } catch (Throwable t) {
                            // signature variant (world, pos, amount, simulate)
                            try { drained = AuraHelper.drainVis(lvl, pos, (float) visCost, false) ? visCost : 0f; } catch (Throwable ignore) {}
                        }
                        if (drained < visCost) return;
                    }

                    AspectList required = recipe.getCrystals();
                    int crystalsNeeded = required != null ? required.visSize() : 0;
                    if (crystalsNeeded > 0) {
                        int remaining = crystalsNeeded;
                        for (int i = 0; i < blockEntity.getCrystalInput().getSlots() && remaining > 0; i++) {
                            ItemStack cs = blockEntity.getCrystalInput().getStackInSlot(i);
                            if (!cs.isEmpty()) {
                                int toExtract = Math.min(remaining, cs.getCount());
                                blockEntity.getCrystalInput().extractItem(i, toExtract, false);
                                remaining -= toExtract;
                            }
                        }
                        if (remaining > 0) return;
                    }

                    for (int i = 0; i < blockEntity.getCraftingGrid().getSlots(); i++) {
                        ItemStack is = blockEntity.getCraftingGrid().getStackInSlot(i);
                        if (!is.isEmpty()) {
                            blockEntity.getCraftingGrid().extractItem(i, 1, false);
                        }
                    }

                    updateResult();
                });
                return super.onTake(player, stack);
            }
        });

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 16 + col * 18, 151 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 16 + col * 18, 209));
        }

        updateResult();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(access, player, level.getBlockState(access.evaluate((lvl, p) -> p).orElse(BlockPos.ZERO)).getBlock());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            original = stackInSlot.copy();

            int gridStart = 0;
            int gridEnd = gridStart + 9; // 0..8
            int crystalStart = gridEnd; // 9
            int crystalEnd = crystalStart + 6; // 9..14
            int resultIndex = crystalEnd; // 15
            int playerInvStart = resultIndex + 1; // 16
            int playerHotbarStart = playerInvStart + 27; // 43
            int playerEnd = playerHotbarStart + 9; // 52 (exclusive)

            if (index == resultIndex) {
                // Move crafted result into player inventory
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stackInSlot, original);
            } else if (index >= playerInvStart && index < playerEnd) {
                // From player inventory  try grid first, then crystals
                if (!this.moveItemStackTo(stackInSlot, gridStart, gridEnd, false)) {
                    if (!this.moveItemStackTo(stackInSlot, crystalStart, crystalEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // From grid/crystals  player inventory
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, playerEnd, false)) {
                    return ItemStack.EMPTY;
                }
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

    private void updateResult() {
        if (level.isClientSide) return;
        ItemStack result = ItemStack.EMPTY;
        IArcaneRecipe recipe = findMatchingArcaneRecipe();
        if (recipe != null && canCraftWithGates(recipe)) {
            result = recipe.getResultItem().copy();
        }
        // 1.16: CraftResultInventory#setItem; older mappings used setInventorySlotContents
        try {
            resultContainer.setItem(0, result);
        } catch (Throwable t) {
            // fallback for older MCP methods if present in this workspace
            try {
                java.lang.reflect.Method m = resultContainer.getClass().getMethod("setInventorySlotContents", int.class, ItemStack.class);
                m.invoke(resultContainer, 0, result);
            } catch (Exception ignore) {}
        }
        // 1.16: broadcastChanges; fallback to detectAndSendChanges for older mappings
        this.broadcastChanges();
    }

    private boolean canCraftWithGates(IArcaneRecipe recipe) {
        String research = recipe.getResearch();
        if (research != null && !research.isEmpty()) {
            if (!ThaumcraftCapabilities.knowsResearch(player, research)) return false;
        }
        final boolean[] ok = {true};
        access.execute((lvl, pos) -> {
            int vis = recipe.getVis();
            if (vis > 0) {
                float drained = 0f;
                try {
                    // simulate drain
                    drained = AuraHelper.drainVis(lvl, pos, (float) vis, true) ? vis : 0f;
                } catch (Throwable t) {
                    try { drained = AuraHelper.drainVis(lvl, pos, vis); } catch (Throwable ignore) {}
                }
                if (drained < vis) ok[0] = false;
            }
        });
        if (!ok[0]) return false;
        AspectList required = recipe.getCrystals();
        int crystalsNeeded = required != null ? required.visSize() : 0;
        if (crystalsNeeded > 0) {
            int available = 0;
            for (int i = 0; i < blockEntity.getCrystalInput().getSlots(); i++) {
                available += blockEntity.getCrystalInput().getStackInSlot(i).getCount();
            }
            if (available < crystalsNeeded) return false;
        }
        return true;
    }

    private IArcaneRecipe findMatchingArcaneRecipe() {
        CraftingInventory temp = buildCraftingInventory();
        java.util.List<net.minecraft.item.crafting.IRecipe<CraftingInventory>> list;
        list = (java.util.List) level.getRecipeManager().getAllRecipesFor(ModRecipes.ARCANE_RECIPE_TYPE);
        for (net.minecraft.item.crafting.IRecipe<CraftingInventory> r : list) {
            if (r instanceof IArcaneRecipe && r.matches(temp, level)) return (IArcaneRecipe) r;
        }
        return null;
    }

    private CraftingInventory buildCraftingInventory() {
        CraftingInventory inv = new CraftingInventory(new Container(null, -1) {
            @Override public boolean stillValid(PlayerEntity player) { return true; }
        }, 3, 3);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, blockEntity.getCraftingGrid().getStackInSlot(i).copy());
        }
        return inv;
    }

    private class NotifyingSlotItemHandler extends SlotItemHandler {
        public NotifyingSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        // map to contentsChanged in newer mappings
        @Override
        public void setChanged() { super.setChanged(); updateResult(); }
    }
}




