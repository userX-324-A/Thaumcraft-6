package thaumcraft.common.lib.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.common.network.msg.ClientSyncWarpMessage;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.ClientSyncKnowledgeMessage;
import thaumcraft.common.golems.seals.SealWorldData;
import thaumcraft.common.network.msg.ClientSealMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import thaumcraft.common.menu.SealContainer;

@Mod.EventBusSubscriber
public class EventHandlerNetwork {
    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) event.getPlayer();
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(sp);
        if (knowledge != null) {
            NetworkHandler.sendTo(sp, new ClientSyncKnowledgeMessage(knowledge));
        }
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(sp);
        if (warp != null) {
            NetworkHandler.sendTo(sp, new ClientSyncWarpMessage(warp.serializeNBT()));
        }

        // Initial seal sync for chunks around the player
        ServerWorld world = sp.getLevel();
        SealWorldData data = SealWorldData.get(world);
        data.all().forEach(se -> {
            if (se.getSealPos() == null || se.getSeal() == null) return;
            BlockPos pos = se.getSealPos().pos;
            // Basic proximity filter (64 blocks)
            if (pos.closerThan(sp.blockPosition(), 64.0)) {
                ClientSealMessage msg = new ClientSealMessage(
                        pos,
                        se.getSealPos().face,
                        se.getSeal().getKey(),
                        se.getPriority(),
                        se.getColor(),
                        se.isLocked(),
                        se.isRedstoneSensitive(),
                        se.getOwner()
                );
                if (se.getArea() != null) msg.setAreaLong(se.getArea().asLong());
                NetworkHandler.sendTo(sp, msg);
            }
        });
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) event.getPlayer();
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(sp);
        if (knowledge != null) {
            NetworkHandler.sendTo(sp, new ClientSyncKnowledgeMessage(knowledge));
        }
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(sp);
        if (warp != null) {
            NetworkHandler.sendTo(sp, new ClientSyncWarpMessage(warp.serializeNBT()));
        }
    }

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event) {
        ServerPlayerEntity sp = event.getPlayer();
        ServerWorld world = sp.getLevel();
        SealWorldData data = SealWorldData.get(world);
        net.minecraft.util.math.ChunkPos watched = event.getPos();
        data.all().forEach(se -> {
            if (se.getSealPos() == null || se.getSeal() == null) return;
            BlockPos pos = se.getSealPos().pos;
            if (watched.x == pos.getX() >> 4 && watched.z == pos.getZ() >> 4) {
                ClientSealMessage msg = new ClientSealMessage(
                        pos,
                        se.getSealPos().face,
                        se.getSeal().getKey(),
                        se.getPriority(),
                        se.getColor(),
                        se.isLocked(),
                        se.isRedstoneSensitive(),
                        se.getOwner()
                );
                if (se.getArea() != null) msg.setAreaLong(se.getArea().asLong());
                NetworkHandler.sendTo(sp, msg);
            }
        });
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity sp = (ServerPlayerEntity) event.getPlayer();
        IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(sp);
        if (knowledge != null) {
            NetworkHandler.sendTo(sp, new ClientSyncKnowledgeMessage(knowledge));
        }
    }

    // Flush queued knowledge syncs about once per second to avoid spam
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        // Simple 20-tick cadence using server tick count
        int tick = (int)(server.overworld().getGameTime() % 20);
        if (tick != 0) return;
        if (thaumcraft.common.lib.research.ResearchManager.syncList.isEmpty()) return;
        server.getPlayerList().getPlayers().forEach(p -> {
            String name = p.getName().getString();
            if (thaumcraft.common.lib.research.ResearchManager.syncList.remove(name) != null) {
                IPlayerKnowledge k = ThaumcraftCapabilities.getKnowledge(p);
                if (k != null) NetworkHandler.sendTo(p, new ClientSyncKnowledgeMessage(k));
            }
        });
    }

    // Empty-hand open GUI for seals
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if (world.isClientSide) return;
        ServerWorld sw = (ServerWorld) world;
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        if (!player.getMainHandItem().isEmpty()) return;
        BlockPos pos = event.getPos();
        Direction face = event.getFace();
        if (face == null) return;
        thaumcraft.api.golems.seals.ISealEntity ent = SealWorldData.get(sw).get(pos, face);
        if (ent == null) return;
        boolean canEdit = player.abilities.instabuild || (ent.getOwner() != null && ent.getOwner().equals(player.getUUID().toString()));
        if (!canEdit) return;
        NetworkHooks.openGui((ServerPlayerEntity) player,
                new net.minecraft.inventory.container.INamedContainerProvider() {
                    @Override
                    public net.minecraft.util.text.ITextComponent getDisplayName() {
                        return new net.minecraft.util.text.StringTextComponent("Seal");
                    }
                    @Override
                    public net.minecraft.inventory.container.Container createMenu(int windowId, net.minecraft.entity.player.PlayerInventory inv, PlayerEntity p) {
                        return new SealContainer(windowId, inv, pos, face);
                    }
                }, buf -> {
                    buf.writeBlockPos(pos);
                    buf.writeByte((byte) face.get3DDataValue());
                });
        event.setCanceled(true);
    }
}

