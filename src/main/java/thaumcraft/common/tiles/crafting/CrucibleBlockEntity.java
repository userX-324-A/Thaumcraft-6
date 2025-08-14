package thaumcraft.common.tiles.crafting;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.api.crafting.ICrucibleRecipe;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.registers.ModBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import java.util.Collection;

public class CrucibleBlockEntity extends TileEntity implements ITickableTileEntity {
    public short heat = 0;
    public AspectList aspects = new AspectList();
    public int bellows = -1;
    private long counter = 0L;

    private final FluidTank tank = new FluidTank(1000);
    private LazyOptional<IFluidHandler> fluidHandlerOptional = LazyOptional.empty();

    public CrucibleBlockEntity() { super(ModBlockEntities.CRUCIBLE.get()); }

    // Server-side ticker
    public static void tick(World level, BlockPos pos, BlockState state, CrucibleBlockEntity be) {
        if (level.isClientSide) return;
        be.counter++;

        // Simple heat logic: heat up if lava/fire/magma below, cool otherwise
        BlockState below = level.getBlockState(pos.below());
        boolean heatSource = below.getBlock() == Blocks.LAVA || below.getBlock() == Blocks.FIRE || below.getBlock() == Blocks.MAGMA_BLOCK;
        short prevHeat = be.heat;
        if (be.tank.getFluidAmount() > 0) {
            if (heatSource) {
                // Bellows boost: increase heating rate by number of adjacent bellows (max 3)
                int bellows = countAdjacentBellows(level, pos);
                int inc = 1 + Math.min(3, bellows);
                if (be.heat < 200) be.heat = (short)Math.min(200, be.heat + inc);
            } else if (be.heat > 0) {
                be.heat--;
            }
        } else if (be.heat > 0) {
            be.heat--;
        }

        // Smelting/ingestion: absorb aspects from items when hot
        if (be.heat > 150 && be.tank.getFluidAmount() > 0 && (be.counter % 5 == 0)) {
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            java.util.List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, box);
            for (ItemEntity entity : itemEntities) {
                ItemStack stack = entity.getItem();
                if (stack.isEmpty()) continue;
                // First try craft resolution against crucible recipes
                ICrucibleRecipe match = findMatch(level, be.aspects, stack);
                if (match != null && be.tank.getFluidAmount() >= 50) {
                    // Research gate: if recipe requires research, ensure a nearby player knows it
                    String req = match.getResearch();
                    if (req != null && !req.isEmpty()) {
                        AxisAlignedBB expand = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).inflate(8);
                        java.util.List<net.minecraft.entity.player.PlayerEntity> players = level.getEntitiesOfClass(net.minecraft.entity.player.PlayerEntity.class, expand);
                        boolean allowed = players.isEmpty();
                        if (!allowed) {
                            net.minecraft.entity.player.PlayerEntity nearest = players.get(0);
                            double best = nearest.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                            for (net.minecraft.entity.player.PlayerEntity p : players) {
                                double d = p.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                                if (d < best) { nearest = p; best = d; }
                            }
                            allowed = thaumcraft.api.capabilities.ThaumcraftCapabilities.knowsResearch(nearest, req);
                        }
                        if (!allowed) {
                            continue;
                        }
                    }
                    // consume aspects and water, spawn result
                    be.aspects = be.aspects.copy().remove(match.getAspects());
                    be.tank.drain(50, IFluidHandler.FluidAction.EXECUTE);
                    ItemStack out = match.getResultItem().copy();
                    ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, out);
                    level.addFreshEntity(drop);
                    stack.shrink(1);
                    if (stack.isEmpty()) entity.remove(); else entity.setItem(stack);
                    be.setChanged();
                    break;
                } else {
                    // absorb one item worth of aspects as fallback
                    thaumcraft.api.aspects.AspectList gained = new thaumcraft.api.aspects.AspectList(stack);
                    if (gained.size() > 0) {
                        be.aspects.add(gained);
                        stack.shrink(1);
                        if (stack.isEmpty()) entity.remove(); else entity.setItem(stack);
                        // consume some water
                        be.tank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                        be.setChanged();
                        break; // throttle to 1 item each 5 ticks
                    }
                }
            }
        }

        // Periodic overflow/spill mitigation until aura system is ported
        if (be.aspects.visSize() > 500) {
            if (be.aspects.size() > 0) {
                thaumcraft.api.aspects.Aspect rand = be.aspects.getAspects()[level.random.nextInt(be.aspects.size())];
                be.aspects.remove(rand, 1);
                be.setChanged();
            }
        }
        if (be.counter >= 100L) {
            be.counter = 0L;
        }

        if (prevHeat != be.heat) be.setChanged();
    }

    @Override
    public void tick() {
        if (level == null) return;
        tick(level, worldPosition, getBlockState(), this);
    }

    private static int countAdjacentBellows(World level, BlockPos pos) {
        int c = 0;
        for (Direction d : Direction.values()) {
            TileEntity te = level.getBlockEntity(pos.relative(d));
            if (te instanceof thaumcraft.common.blocks.world.BellowsBlockEntity) c++;
        }
        return c;
    }

    private static thaumcraft.api.crafting.ICrucibleRecipe findMatch(World level, AspectList current, ItemStack catalyst) {
        Collection<thaumcraft.api.crafting.ICrucibleRecipe> recipes = thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.getCrucibleRecipes(level);
        for (thaumcraft.api.crafting.ICrucibleRecipe r : recipes) {
            if (r.getCatalyst().test(catalyst)) {
                AspectList req = r.getAspects();
                boolean ok = true;
                for (thaumcraft.api.aspects.Aspect a : req.getAspects()) {
                    if (current.getAmount(a) < req.getAmount(a)) { ok = false; break; }
                }
                if (ok) return r;
            }
        }
        return null;
    }

    // Capability exposure (fluids)
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (!fluidHandlerOptional.isPresent()) {
                fluidHandlerOptional = LazyOptional.of(() -> tank);
            }
            return fluidHandlerOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() { super.setRemoved(); fluidHandlerOptional.invalidate(); }

    // Convenience methods used by older logic
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        int filled = tank.fill(resource, action);
        if (filled > 0) setChanged();
        return filled;
    }

    @Nullable
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        FluidStack drained = tank.drain(resource, action);
        if (!drained.isEmpty()) setChanged();
        return drained;
    }

    public FluidStack drain(int amount, IFluidHandler.FluidAction action) {
        FluidStack drained = tank.drain(amount, action);
        if (!drained.isEmpty()) setChanged();
        return drained;
    }

    // NBT persistence
    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putShort("Heat", heat);
        tag.put("Tank", tank.writeToNBT(new CompoundNBT()));
        CompoundNBT aspectsTag = new CompoundNBT();
        aspects.writeToNBT(aspectsTag);
        tag.put("Aspects", aspectsTag);
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        heat = tag.getShort("Heat");
        if (tag.contains("Tank")) {
            tank.readFromNBT(tag.getCompound("Tank"));
        }
        if (tag.contains("Aspects")) {
            aspects.readFromNBT(tag.getCompound("Aspects"));
        }
    }

    // Client sync for visible state (heat, water level, aspects summary)
    @Override
    public net.minecraft.network.play.server.SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        save(tag);
        return new net.minecraft.network.play.server.SUpdateTileEntityPacket(this.worldPosition, 0, tag);
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
        this.load(getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        return save(tag);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }
}
