package thaumcraft.api.research;

import java.util.ArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.internal.IInternalMethodHandler;
import thaumcraft.api.ThaumcraftApi;

public class ScanningManager {
    private static final ArrayList<IScanThing> things = new ArrayList<>();

    public static void addScannableThing(IScanThing obj) {
        things.add(obj);
    }

    public static void scanTheThing(PlayerEntity player, Object object) {
        boolean found = false;
        boolean suppress = false;
        for (IScanThing thing : things) {
            try {
                if (!thing.checkThing(player, object)) continue;
                String key = thing.getResearchKey(player, object);
                IInternalMethodHandler ih = ThaumcraftApi.internalMethods;
                if (key == null || key.isEmpty() || ih.progressResearch(player, key)) {
                    if (key == null || key.isEmpty()) suppress = true;
                    found = true;
                    thing.onSuccess(player, object);
                }
            } catch (Exception ignored) {}
        }
        if (!suppress) {
            // Prefer action-bar style on the client, otherwise skip chat spam for now
            ITextComponent msg = new TranslationTextComponent(found ? "tc.knownobject" : "tc.unknownobject");
            if (player.level.isClientSide) {
                try {
                    // Only present on client distribution
                    player.displayClientMessage(msg, true);
                } catch (Throwable ignored) {}
            }
        }

        // Basic: if scanning a position, also try the block item at that location
        if (object instanceof BlockPos) {
            ItemStack stack = getItemFromParms(player, object);
            if (!stack.isEmpty()) {
                scanTheThing(player, stack);
            }
        }
    }

    public static boolean isThingStillScannable(PlayerEntity player, Object object) {
        for (IScanThing thing : things) {
            try {
                if (!thing.checkThing(player, object)) continue;
                String key = thing.getResearchKey(player, object);
                if (key == null || key.isEmpty()) return true;
                if (!ThaumcraftCapabilities.knowsResearchStrict(player, key)) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public static ItemStack getItemFromParms(PlayerEntity player, Object obj) {
        if (obj == null) return ItemStack.EMPTY;
        if (obj instanceof ItemStack) return ((ItemStack) obj).copy();
        if (obj instanceof ItemEntity) {
            ItemStack fromEntity = ((ItemEntity) obj).getItem();
            return fromEntity == null ? ItemStack.EMPTY : fromEntity.copy();
        }
        if (obj instanceof BlockPos) {
            BlockPos pos = (BlockPos) obj;
            BlockState state = player.level.getBlockState(pos);
            if (state == null) return ItemStack.EMPTY;
            try {
                // Best-effort clone; fall back to simple block item
                ItemStack clone = state.getBlock().getCloneItemStack(player.level, pos, state);
                if (clone != null && !clone.isEmpty()) return clone;
            } catch (Throwable ignored) {}
            try {
                return new ItemStack(state.getBlock());
            } catch (Throwable ignored) {}
            return ItemStack.EMPTY;
        }
        if (obj instanceof Entity) {
            // No item representation for generic entities here
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    private static RayTraceResult rayTrace(PlayerEntity player, double reach) {
        Vector3d eye = player.getEyePosition(1.0f);
        Vector3d look = player.getLookAngle();
        Vector3d end = eye.add(look.x * reach, look.y * reach, look.z * reach);
        RayTraceContext ctx = new RayTraceContext(eye, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player);
        return player.level.clip(ctx);
    }
}

