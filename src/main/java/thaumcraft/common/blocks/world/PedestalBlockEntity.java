package thaumcraft.common.blocks.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.common.registers.ModBlockEntities;

public class PedestalBlockEntity extends TileEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private LazyOptional<ItemStackHandler> invCap = LazyOptional.empty();

    public PedestalBlockEntity() {
        super(ModBlockEntities.PEDESTAL.get());
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        invCap = LazyOptional.of(() -> inventory);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        invCap.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return invCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(net.minecraft.block.BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        inventory.deserializeNBT(tag.getCompound("Inv"));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("Inv", inventory.serializeNBT());
        return super.save(tag);
    }
}
