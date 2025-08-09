package thaumcraft.common.blocks.world;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.common.registers.ModBlockEntities;

public class HungryChestBlockEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            HungryChestBlockEntity.this.setChanged();
        }
    };
    private LazyOptional<IItemHandler> itemHandlerOptional = LazyOptional.empty();

    public HungryChestBlockEntity() { super(ModBlockEntities.HUNGRY_CHEST.get()); }

    // 1.16 tick pattern: implement in TileEntity via ITickableTileEntity if needed. Here we keep logic in block entity for parity
    public static void tick(net.minecraft.world.World level, BlockPos pos, BlockState state, HungryChestBlockEntity be) {
        if (level.isRemote) return;
        if ((level.getGameTime() % 10L) != 0L) return;
        java.util.List<net.minecraft.entity.item.EntityItem> items = level.getEntitiesWithinAABB(
                net.minecraft.entity.item.EntityItem.class,
                new net.minecraft.util.math.AxisAlignedBB(pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5, pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 1.5)
        );
        for (net.minecraft.entity.item.EntityItem entity : items) {
            net.minecraft.item.ItemStack stack = entity.getItem();
            if (stack.isEmpty()) continue;
            for (int i = 0; i < be.inventory.getSlots() && !stack.isEmpty(); i++) {
                stack = be.inventory.insertItem(i, stack, false);
            }
            if (stack.isEmpty()) {
                entity.remove();
                be.markDirty();
                break;
            } else {
                entity.setItem(stack);
                be.markDirty();
            }
        }
    }

    @Override
    public void tick() {
        if (world == null) return;
        tick(world, pos, getBlockState(), this);
    }

    public void dropAllContents() {
        if (this.world == null || this.world.isRemote) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            net.minecraft.item.ItemStack stack = inventory.getStackInSlot(i).copy();
            if (!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, stack);
                inventory.setStackInSlot(i, net.minecraft.item.ItemStack.EMPTY);
            }
        }
        this.markDirty();
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("Items", inventory.serializeNBT());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        if (tag.contains("Items")) {
            inventory.deserializeNBT(tag.getCompound("Items"));
        }
    }

    @Override
    public void remove() { super.remove(); itemHandlerOptional.invalidate(); }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (!itemHandlerOptional.isPresent()) {
                itemHandlerOptional = LazyOptional.of(() -> inventory);
            }
            return itemHandlerOptional.cast();
        }
        return super.getCapability(cap, side);
    }
} 
