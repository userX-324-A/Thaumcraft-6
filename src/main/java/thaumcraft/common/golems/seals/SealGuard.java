package thaumcraft.common.golems.seals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;

public class SealGuard implements ISeal, ISealConfigArea {
    private static final ResourceLocation ICON = new ResourceLocation(Thaumcraft.MODID, "items/seals/seal_guard");

    @Override
    public String getKey() { return "thaumcraft:guard"; }

    @Override
    public boolean canPlaceAt(World world, BlockPos pos, Direction side) { return !world.isEmptyBlock(pos); }

    @Override
    public void tickSeal(World world, ISealEntity seal) {
        // Placeholder: no AI yet; area-aware when task system is ready
    }

    @Override
    public void onTaskStarted(World world, IGolemAPI golem, Task task) { }

    @Override
    public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) { return true; }

    @Override
    public void onTaskSuspension(World world, Task task) { }

    @Override
    public boolean canGolemPerformTask(IGolemAPI golem, Task task) { return true; }

    @Override
    public void readCustomNBT(CompoundNBT nbt) { }

    @Override
    public void writeCustomNBT(CompoundNBT nbt) { }

    @Override
    public ResourceLocation getSealIcon() { return ICON; }

    @Override
    public void onRemoval(World world, BlockPos pos, Direction side) { }

    @Override
    public Object returnContainer(World world, net.minecraft.entity.player.PlayerEntity player, BlockPos pos, Direction side, ISealEntity seal) { return null; }

    @Override
    public Object returnGui(World world, net.minecraft.entity.player.PlayerEntity player, BlockPos pos, Direction side, ISealEntity seal) { return null; }

    @Override
    public EnumGolemTrait[] getRequiredTags() { return new EnumGolemTrait[0]; }

    @Override
    public EnumGolemTrait[] getForbiddenTags() { return null; }
}


