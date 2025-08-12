package thaumcraft.common.world.aura;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.ClientAuraMessage;

/**
 * Server tick events to periodically sync aura values to nearby players and on login.
 */
@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class AuraEvents {
    private AuraEvents() {}

    private static int tickCounter;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        tickCounter++;
        // Every 20 ticks (~1s), sync to players their current chunk aura/flux
        if (tickCounter % 20 != 0) return;

        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        for (ServerWorld world : server.getAllLevels()) {
            AuraWorldData data = AuraWorldData.get(world);
            for (ServerPlayerEntity player : world.players()) {
                BlockPos pos = player.blockPosition();
                float vis = data.getVis(pos);
                float flux = data.getFlux(pos);
                float base = data.getBase(pos);
                NetworkHandler.sendTo(player, new ClientAuraMessage((short) (int) base, vis, flux));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ServerWorld world = player.getLevel();
        AuraWorldData data = AuraWorldData.get(world);
        BlockPos pos = player.blockPosition();
        float vis = data.getVis(pos);
        float flux = data.getFlux(pos);
        float base = data.getBase(pos);
        NetworkHandler.sendTo(player, new ClientAuraMessage((short) (int) base, vis, flux));
    }
}


