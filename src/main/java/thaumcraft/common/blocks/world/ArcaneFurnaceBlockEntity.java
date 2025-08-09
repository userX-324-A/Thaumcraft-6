package thaumcraft.common.blocks.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArcaneFurnaceBlockEntity extends TileEntity implements ITickableTileEntity {
    // Simplified furnace: 3 slots (input, fuel, output)
    private final ItemStackHandler inventory = new ItemStackHandler(3);
    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> inventory);

    private int burnTime;          // remaining fuel burn time
    private int burnTimeTotal;     // total burn time for current fuel
    private int cookTime;          // progress
    private int cookTimeTotal = 100; // base speed; bellows can reduce this

    public ArcaneFurnaceBlockEntity() {
        super(ModBlockEntities.ARCANE_FURNACE.get());
    }

    @Override
    public void tick() {
        World level = world;
        if (level == null || level.isRemote) return;
        BlockPos pos = getPos();

        boolean dirty = false;

        // fuel consumption
        if (burnTime > 0) burnTime--;

        // Determine bellows nearby to boost
        int bellowsCount = getAdjacentBellows(level, pos);
        int speedBoost = 5 * Math.min(3, bellowsCount); // up to -15 ticks per op
        int cookTarget = Math.max(40, cookTimeTotal - speedBoost);

        IItemHandler inv = inventory;
        // Very basic smelt: input -> output if vanilla furnace recipe exists
        net.minecraft.item.ItemStack input = inv.getStackInSlot(0);
        if (!input.isEmpty()) {
            java.util.Optional<net.minecraft.item.crafting.BlastingRecipe> dummy = java.util.Optional.empty();
            java.util.Optional<net.minecraft.item.crafting.SmeltingRecipe> recipeOpt = level.getRecipeManager()
                    .getRecipeFor(net.minecraft.item.crafting.IRecipeType.SMELTING, new net.minecraft.inventory.Inventory(new net.minecraft.item.ItemStack[]{input}), level);
            if (recipeOpt.isPresent()) {
                net.minecraft.item.crafting.SmeltingRecipe recipe = recipeOpt.get();
                boolean canOutput = canOutput(inv, recipe.getResultItem());
                if (canOutput) {
                    // ensure burning
                    if (burnTime == 0) {
                        int fuel = net.minecraftforge.common.ForgeHooks.getBurnTime(inv.getStackInSlot(1));
                        if (fuel > 0) {
                            burnTime = burnTimeTotal = fuel;
                            inv.extractItem(1, 1, false);
                            dirty = true;
                        }
                    }
                    if (burnTime > 0) {
                        cookTime++;
                        if (cookTime >= cookTarget) {
                            // complete smelt
                            inv.extractItem(0, 1, false);
                            net.minecraft.item.ItemStack result = recipe.getResultItem().copy();
                            placeOutput(inv, result);
                            cookTime = 0;
                            dirty = true;
                        }
                    } else {
                        cookTime = 0;
                    }
                } else {
                    cookTime = 0;
                }
            } else {
                cookTime = 0;
            }
        } else {
            cookTime = 0;
        }

        if (dirty) markDirty();
    }

    private static int getAdjacentBellows(World level, BlockPos pos) {
        int count = 0;
        for (net.minecraft.util.Direction d : net.minecraft.util.Direction.values()) {
            TileEntity te = level.getTileEntity(pos.offset(d));
            if (te instanceof BellowsBlockEntity) count++;
        }
        return count;
    }

    private static boolean canOutput(IItemHandler inv, net.minecraft.item.ItemStack out) {
        net.minecraft.item.ItemStack current = inv.getStackInSlot(2);
        if (current.isEmpty()) return true;
        if (!net.minecraft.item.ItemStack.areItemsEqual(current, out)) return false;
        return current.getCount() + out.getCount() <= current.getMaxStackSize();
    }

    private static void placeOutput(IItemHandler inv, net.minecraft.item.ItemStack out) {
        net.minecraft.item.ItemStack current = inv.getStackInSlot(2);
        if (current.isEmpty()) {
            inv.insertItem(2, out, false);
        } else if (net.minecraft.item.ItemStack.areItemsEqual(current, out)) {
            current.grow(out.getCount());
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return itemCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        itemCap.invalidate();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        burnTime = tag.getInt("Burn");
        burnTimeTotal = tag.getInt("BurnTot");
        cookTime = tag.getInt("Cook");
        cookTimeTotal = tag.getInt("CookTot");
        inventory.deserializeNBT(tag.getCompound("Inv"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("Burn", burnTime);
        tag.putInt("BurnTot", burnTimeTotal);
        tag.putInt("Cook", cookTime);
        tag.putInt("CookTot", cookTimeTotal);
        tag.put("Inv", inventory.serializeNBT());
        return super.write(tag);
    }

    // Comparator helpers
    public int getCookTime() { return cookTime; }
    public int getCookTimeTotal() { return cookTimeTotal; }
}

