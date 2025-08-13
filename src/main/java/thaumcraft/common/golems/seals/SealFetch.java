package thaumcraft.common.golems.seals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;

/**
 * Minimal fetch/collect placeholder seal. Enqueues simple tasks around itself for now.
 */
public class SealFetch implements ISeal {
    private static final ResourceLocation ICON = new ResourceLocation(Thaumcraft.MODID, "items/seals/seal_pickup");

    @Override
    public String getKey() { return "thaumcraft:fetch"; }

    @Override
    public boolean canPlaceAt(World world, BlockPos pos, Direction side) { return true; }

    @Override
    public void tickSeal(World world, ISealEntity seal) {
        // Placeholder: enqueue a simple no-op block task at the seal position every few seconds to prove wiring
        if (world.getGameTime() % 100 != 0) return;
        RegistryKey<net.minecraft.world.World> key = world.dimension();
        thaumcraft.api.golems.tasks.Task task = new thaumcraft.api.golems.tasks.Task(key, seal.getSealPos(), seal.getSealPos().pos);
        thaumcraft.common.golems.tasks.TaskEngine.enqueue(task);
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
    public EnumGolemTrait[] getForbiddenTags() { return new EnumGolemTrait[0]; }
}


