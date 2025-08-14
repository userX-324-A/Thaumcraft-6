package thaumcraft.common.blocks.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import thaumcraft.common.registers.ModBlockEntities;
import thaumcraft.api.aura.AuraHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VisGeneratorBlockEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private int energy;
    private final int capacity = 1000;
    private final int maxExtract = 20;
    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> this);

    public VisGeneratorBlockEntity() {
        super(ModBlockEntities.VIS_GENERATOR.get());
    }

    @Override
    public void tick() {
        World level = this.level;
        if (level == null || level.isClientSide) return;
        BlockPos pos = this.worldPosition;
        // Recharge from aura when empty
        if (energy == 0) {
            float drained = AuraHelper.drainVis(level, pos, 1.0f);
            energy = (int) (drained * 1000.0f);
            if (energy > 0) setChanged();
        }

        // Try to push to adjacent FE receiver in facing direction (simplify: push all directions if any can receive)
        for (Direction dir : Direction.values()) {
            if (energy <= 0) break;
            TileEntity neighbor = level.getBlockEntity(pos.relative(dir));
            if (neighbor == null) continue;
            LazyOptional<IEnergyStorage> opt = neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
            opt.ifPresent(target -> {
                if (target.canReceive()) {
                    int toSend = Math.min(energy, maxExtract);
                    int sent = target.receiveEnergy(toSend, false);
                    if (sent > 0) {
                        energy -= sent;
                        setChanged();
                    }
                }
            });
        }
    }

    // IEnergyStorage
    @Override public int receiveEnergy(int maxReceive, boolean simulate) { return 0; }
    @Override public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = Math.min(maxExtract, Math.min(this.maxExtract, energy));
        if (!simulate) {
            energy -= extracted;
            if (extracted > 0) setChanged();
        }
        return extracted;
    }
    @Override public int getEnergyStored() { return energy; }
    @Override public int getMaxEnergyStored() { return capacity; }
    @Override public boolean canExtract() { return true; }
    @Override public boolean canReceive() { return false; }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energyCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyCap.invalidate();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        energy = tag.getInt("Energy");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putInt("Energy", energy);
        return super.save(tag);
    }

	@Override
	public net.minecraft.nbt.CompoundNBT getUpdateTag() {
		net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
		tag.putInt("Energy", energy);
		return tag;
	}

	@Override
	public void handleUpdateTag(BlockState state, net.minecraft.nbt.CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		if (tag.contains("Energy")) energy = tag.getInt("Energy");
	}

	@Override
	public net.minecraft.network.play.server.SUpdateTileEntityPacket getUpdatePacket() {
		net.minecraft.nbt.CompoundNBT tag = new net.minecraft.nbt.CompoundNBT();
		save(tag);
		return new net.minecraft.network.play.server.SUpdateTileEntityPacket(this.worldPosition, 0, tag);
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
		this.load(getBlockState(), pkt.getTag());
	}
}



