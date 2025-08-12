package thaumcraft.common.golems.seals;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.Thaumcraft;

/**
 * Server tick hook that advances all active seals per world.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class SealEngine {
    private SealEngine() {}

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) return;
        if (!(event.world instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) event.world;
        SealWorldData data = SealWorldData.get(world);
        data.all().forEach(se -> se.tickSealEntity(world));
    }
}


