package thaumcraft.common.golems.seals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.ClientSealPropsMessage;

/**
 * Server-side helper for editing seal properties with permission checks and client sync.
 */
public final class SealService {
    private SealService() {}

    private static boolean canEdit(PlayerEntity actor, ISealEntity ent) {
        if (actor == null) return false;
        if (actor.abilities.instabuild) return true;
        String owner = ent.getOwner();
        return owner != null && owner.equals(actor.getUUID().toString());
    }

    public static boolean setPriority(ServerWorld world, BlockPos pos, Direction face, byte priority, PlayerEntity actor) {
        ISealEntity ent = SealWorldData.get(world).get(pos, face);
        if (ent == null || !canEdit(actor, ent)) return false;
        ent.setPriority(priority);
        SealWorldData.get(world).put(ent);
        syncProps(world, pos, face, ent, null);
        return true;
    }

    public static boolean setColor(ServerWorld world, BlockPos pos, Direction face, byte color, PlayerEntity actor) {
        ISealEntity ent = SealWorldData.get(world).get(pos, face);
        if (ent == null || !canEdit(actor, ent)) return false;
        ent.setColor(color);
        SealWorldData.get(world).put(ent);
        syncProps(world, pos, face, ent, null);
        return true;
    }

    public static boolean setLocked(ServerWorld world, BlockPos pos, Direction face, boolean locked, PlayerEntity actor) {
        ISealEntity ent = SealWorldData.get(world).get(pos, face);
        if (ent == null || !canEdit(actor, ent)) return false;
        ent.setLocked(locked);
        SealWorldData.get(world).put(ent);
        syncProps(world, pos, face, ent, null);
        return true;
    }

    public static boolean setRedstone(ServerWorld world, BlockPos pos, Direction face, boolean redstone, PlayerEntity actor) {
        ISealEntity ent = SealWorldData.get(world).get(pos, face);
        if (ent == null || !canEdit(actor, ent)) return false;
        ent.setRedstoneSensitive(redstone);
        SealWorldData.get(world).put(ent);
        syncProps(world, pos, face, ent, null);
        return true;
    }

    public static boolean setArea(ServerWorld world, BlockPos pos, Direction face, BlockPos area, PlayerEntity actor) {
        ISealEntity ent = SealWorldData.get(world).get(pos, face);
        if (ent == null || !canEdit(actor, ent)) return false;
        ent.setArea(area);
        SealWorldData.get(world).put(ent);
        syncProps(world, pos, face, ent, area.asLong());
        return true;
    }

    private static void syncProps(ServerWorld world, BlockPos pos, Direction face, ISealEntity ent, Long areaOpt) {
        ClientSealPropsMessage msg = new ClientSealPropsMessage(pos, face, ent.getPriority(), ent.getColor(), ent.isLocked(), ent.isRedstoneSensitive(), areaOpt);
        // If the seal supports toggles, include them
        if (ent.getSeal() instanceof thaumcraft.api.golems.seals.ISealConfigToggles) {
            thaumcraft.api.golems.seals.ISealConfigToggles t = (thaumcraft.api.golems.seals.ISealConfigToggles) ent.getSeal();
            thaumcraft.api.golems.seals.ISealConfigToggles.SealToggle[] arr = t.getToggles();
            boolean[] vals = new boolean[arr.length];
            for (int i = 0; i < arr.length; i++) vals[i] = arr[i].getValue();
            // Attach toggles using package-private setter
            try {
                ClientSealPropsMessage.class.getDeclaredMethod("setToggles", boolean[].class).invoke(msg, (Object) vals);
            } catch (Exception ignored) {}
        }
        NetworkHandler.sendToAllAround(msg, world, pos, 64);
    }

    public static boolean setToggle(ServerWorld world, BlockPos pos, Direction face, int index, boolean value, PlayerEntity actor) {
        ISealEntity ent = SealWorldData.get(world).get(pos, face);
        if (ent == null || !canEdit(actor, ent)) return false;
        if (!(ent.getSeal() instanceof thaumcraft.api.golems.seals.ISealConfigToggles)) return false;
        thaumcraft.api.golems.seals.ISealConfigToggles t = (thaumcraft.api.golems.seals.ISealConfigToggles) ent.getSeal();
        if (index < 0 || index >= t.getToggles().length) return false;
        t.setToggle(index, value);
        SealWorldData.get(world).put(ent);
        // Reuse props sync for now; a future ClientSealTogglesMessage could be added
        syncProps(world, pos, face, ent, ent.getArea() == null ? null : ent.getArea().asLong());
        return true;
    }
}


