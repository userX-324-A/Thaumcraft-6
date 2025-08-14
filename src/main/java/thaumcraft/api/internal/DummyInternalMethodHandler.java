package thaumcraft.api.internal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge.EnumKnowledgeType;
import thaumcraft.api.capabilities.IPlayerWarp.EnumWarpType;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.api.research.ResearchCategory;

/**
 * No-op internal methods sufficient for compiling during the port.
 */
public class DummyInternalMethodHandler implements IInternalMethodHandler {
    @Override public boolean addKnowledge(PlayerEntity player, EnumKnowledgeType type, ResearchCategory category, int amount) { return false; }
    @Override public boolean progressResearch(PlayerEntity player, String researchkey) { return false; }
    @Override public boolean completeResearch(PlayerEntity player, String researchkey) { return false; }
    @Override public boolean doesPlayerHaveRequisites(PlayerEntity player, String researchkey) { return false; }
    @Override public void addWarpToPlayer(PlayerEntity player, int amount, EnumWarpType type) {
        thaumcraft.api.capabilities.IPlayerWarp cap = thaumcraft.api.capabilities.ThaumcraftCapabilities.getWarp(player);
        if (cap == null || amount == 0) return;
        int newVal = cap.add(type, amount);
        if (player instanceof net.minecraft.entity.player.ServerPlayerEntity) {
            cap.sync((net.minecraft.entity.player.ServerPlayerEntity) player);
            thaumcraft.common.network.NetworkHandler.sendTo((net.minecraft.entity.player.ServerPlayerEntity) player,
                    new thaumcraft.common.network.msg.ClientWarpMessage((byte) (type == EnumWarpType.PERMANENT ? 1 : (type == EnumWarpType.NORMAL ? 0 : 2)), amount));
        }
    }
    @Override public int getActualWarp(PlayerEntity player) {
        thaumcraft.api.capabilities.IPlayerWarp cap = thaumcraft.api.capabilities.ThaumcraftCapabilities.getWarp(player);
        if (cap == null) return 0;
        return Math.max(0, cap.get(EnumWarpType.NORMAL) + cap.get(EnumWarpType.PERMANENT));
    }
    @Override public AspectList getObjectAspects(ItemStack is) { return new AspectList(is); }
    @Override public AspectList generateTags(ItemStack is) { return new AspectList(is); }
    @Override public float drainVis(World world, BlockPos pos, float amount, boolean simulate) { return 0; }
    @Override public float drainFlux(World world, BlockPos pos, float amount, boolean simulate) { return 0; }
    @Override public void addVis(World world, BlockPos pos, float amount) { }
    @Override public void addFlux(World world, BlockPos pos, float amount, boolean showEffect) { }
    @Override public float getTotalAura(World world, BlockPos pos) { return 0; }
    @Override public float getVis(World world, BlockPos pos) { return 0; }
    @Override public float getFlux(World world, BlockPos pos) { return 0; }
    @Override public int getAuraBase(World world, BlockPos pos) { return 0; }
    @Override public void registerSeal(ISeal seal) { }
    @Override public ISeal getSeal(String key) { return null; }
    @Override public ISealEntity getSealEntity(int dim, SealPos pos) { return null; }
    @Override public void addGolemTask(int dim, Task task) { }
    @Override public boolean shouldPreserveAura(World world, PlayerEntity player, BlockPos pos) { return false; }
    @Override public ItemStack getSealStack(String key) { return ItemStack.EMPTY; }
}

