package thaumcraft.common.golems.seals;

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
 * Minimal placeholder seals to unblock engine and persistence wiring.
 */
public final class CoreSeals {
    private CoreSeals() {}

    public static void register() {
        SealRegistry.register(new PlaceholderSeal("thaumcraft:fetch"));
        SealRegistry.register(new PlaceholderSeal("thaumcraft:guard"));
        SealRegistry.register(new PlaceholderSeal("thaumcraft:harvest"));
        SealRegistry.register(new PlaceholderSeal("thaumcraft:empty"));
    }

    private static final class PlaceholderSeal implements ISeal {
        private final String key;
        PlaceholderSeal(String key) { this.key = key; }
        @Override public String getKey() { return key; }
        @Override public boolean canPlaceAt(World world, BlockPos pos, net.minecraft.util.Direction side) { return true; }
        @Override public void tickSeal(World world, ISealEntity seal) { /* no-op */ }
        @Override public void onTaskStarted(World world, IGolemAPI golem, Task task) { }
        @Override public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) { return true; }
        @Override public void onTaskSuspension(World world, Task task) { }
        @Override public boolean canGolemPerformTask(IGolemAPI golem, Task task) { return true; }
        @Override public void readCustomNBT(net.minecraft.nbt.CompoundNBT nbt) { }
        @Override public void writeCustomNBT(net.minecraft.nbt.CompoundNBT nbt) { }
        @Override public ResourceLocation getSealIcon() { return new ResourceLocation(Thaumcraft.MODID, "item/seal_placeholder"); }
        @Override public void onRemoval(World world, BlockPos pos, net.minecraft.util.Direction side) { }
        @Override public Object returnContainer(World world, net.minecraft.entity.player.PlayerEntity player, BlockPos pos, net.minecraft.util.Direction side, ISealEntity seal) { return null; }
        @Override public Object returnGui(World world, net.minecraft.entity.player.PlayerEntity player, BlockPos pos, net.minecraft.util.Direction side, ISealEntity seal) { return null; }
        @Override public EnumGolemTrait[] getRequiredTags() { return new EnumGolemTrait[0]; }
        @Override public EnumGolemTrait[] getForbiddenTags() { return new EnumGolemTrait[0]; }
    }
}


