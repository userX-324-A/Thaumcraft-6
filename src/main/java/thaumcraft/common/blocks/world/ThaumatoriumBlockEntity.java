package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThaumatoriumBlockEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(2);
    private int cookTime = 0;
    private int cookTimeTotal = 100; // placeholder throughput/heat
    private final LazyOptional<ItemStackHandler> invCap = LazyOptional.of(() -> inventory);
    private final LazyOptional<IEssentiaTransport> essentia = LazyOptional.of(() -> new EssentiaTransportCapability.BasicEssentiaTransport(128, 30));

    public ThaumatoriumBlockEntity() {
        super(ModBlockEntities.THAUMATORIUM.get());
    }

    // Expose an internal method the top shim can call to access essentia
    LazyOptional<IEssentiaTransport> getEssentiaInternal() { return essentia; }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        IEssentiaTransport et = essentia.orElse(null);
        if (et == null) return;
        if (inventory.getStackInSlot(0).isEmpty()) { cookTime = 0; return; }
        AspectList current = new AspectList();
        if (et.getEssentiaType(Direction.UP) != null) {
            thaumcraft.api.aspects.Aspect type = et.getEssentiaType(Direction.UP);
            int amt = et.getEssentiaAmount(Direction.UP);
            if (type != null && amt > 0) current.add(type, amt);
        }
        ICrucibleRecipe match = findCrucibleMatch(level, current, inventory.getStackInSlot(0));
        if (match == null) { cookTime = 0; return; }
        // advance cook time; only craft when full
        cookTime++;
        setChangedAndUpdate();
        if (cookTime < cookTimeTotal) return;
        boolean ok = true;
        for (thaumcraft.api.aspects.Aspect a : match.getAspects().getAspects()) {
            if (current.getAmount(a) < match.getAspects().getAmount(a)) { ok = false; break; }
        }
        if (!ok) return;
        for (thaumcraft.api.aspects.Aspect a : match.getAspects().getAspects()) {
            int need = match.getAspects().getAmount(a);
            int taken = 0;
            while (taken < need) {
                int got = et.takeEssentia(a, need - taken, Direction.UP);
                if (got <= 0) break;
                taken += got;
            }
            if (taken < need) return;
        }
        inventory.extractItem(0, 1, false);
        net.minecraft.item.ItemStack out = match.getResultItem().copy();
        net.minecraft.item.ItemStack remainder = inventory.insertItem(1, out, false);
        if (!remainder.isEmpty()) {
            net.minecraft.entity.item.ItemEntity drop = new net.minecraft.entity.item.ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, remainder);
            level.addFreshEntity(drop);
        }
        cookTime = 0;
        setChangedAndUpdate();
    }

    private static ICrucibleRecipe findCrucibleMatch(net.minecraft.world.World level, AspectList current, net.minecraft.item.ItemStack catalyst) {
        for (ICrucibleRecipe r : ThaumcraftCraftingManager.getCrucibleRecipes(level)) {
            if (r.getCatalyst().test(catalyst)) {
                boolean ok = true;
                for (thaumcraft.api.aspects.Aspect a : r.getAspects().getAspects()) {
                    if (current.getAmount(a) < r.getAspects().getAmount(a)) { ok = false; break; }
                }
                if (ok) return r;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return invCap.cast();
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentia.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        invCap.invalidate();
        essentia.invalidate();
    }

    // ---- API for GUI/progress ----
    public int getCookTime() { return cookTime; }
    public int getCookTimeTotal() { return cookTimeTotal; }

    @Override
    public void load(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.load(state, tag);
        if (tag.contains("CookTime")) this.cookTime = tag.getInt("CookTime");
        if (tag.contains("CookTimeTotal")) this.cookTimeTotal = tag.getInt("CookTimeTotal");
    }

    @Override
    public net.minecraft.nbt.CompoundNBT save(net.minecraft.nbt.CompoundNBT tag) {
        tag = super.save(tag);
        tag.putInt("CookTime", cookTime);
        tag.putInt("CookTimeTotal", cookTimeTotal);
        return tag;
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}


