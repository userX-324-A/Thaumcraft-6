package thaumcraft.common.golems.seals;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.golems.seals.ISealEntity;

/**
 * Optional strict validation pass on world load to prune invalid seals immediately.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class SealWorldLoadValidator {
    private SealWorldLoadValidator() {}

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (!(event.getWorld() instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) event.getWorld();
        SealWorldData data = SealWorldData.get(world);
        java.util.List<ISealEntity> toRemove = new java.util.ArrayList<>();
        for (ISealEntity se : data.all()) {
            if (se.getSealPos() == null || se.getSeal() == null) {
                toRemove.add(se);
                continue;
            }
            BlockPos pos = se.getSealPos().pos;
            if (!se.getSeal().canPlaceAt(world, pos, se.getSealPos().face)) {
                toRemove.add(se);
            }
        }
        for (ISealEntity se : toRemove) {
            BlockPos pos = se.getSealPos() == null ? null : se.getSealPos().pos;
            net.minecraft.util.Direction face = se.getSealPos() == null ? net.minecraft.util.Direction.NORTH : se.getSealPos().face;
            if (pos != null && se.getSeal() != null) se.getSeal().onRemoval(world, pos, face);
            if (pos != null) data.remove(pos, face);
            if (pos != null) thaumcraft.common.network.NetworkHandler.sendToAllAround(
                    new thaumcraft.common.network.msg.ClientSealMessage(pos, face, "REMOVE", (byte)0, (byte)0, false, false, ""),
                    world, pos, 64);
        }
    }
}



