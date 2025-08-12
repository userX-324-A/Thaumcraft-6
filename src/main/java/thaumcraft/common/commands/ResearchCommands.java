package thaumcraft.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class ResearchCommands {
    private ResearchCommands() {}

    @SubscribeEvent
    public static void onRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> d = event.getDispatcher();

        LiteralArgumentBuilder<CommandSource> root = Commands.literal("research")
            .requires(src -> src.hasPermission(2))
            .then(Commands.literal("grant")
                .then(Commands.argument("key", StringArgumentType.string())
                    .executes(ctx -> {
                        CommandSource src = ctx.getSource();
                        ServerPlayerEntity player = src.getPlayerOrException();
                        String key = StringArgumentType.getString(ctx, "key");
                        // supports key@stage via progressResearch called multiple times if needed
                        boolean before = ThaumcraftCapabilities.knowsResearch(player, key);
                        ResearchManager.completeResearch(player, key, true);
                        int changed = ThaumcraftCapabilities.knowsResearch(player, key) && !before ? 1 : 0;
                        src.sendSuccess(new StringTextComponent("Granted research: " + key), true);
                        return changed;
                    })))
            .then(Commands.literal("reset")
                .executes(ctx -> {
                    CommandSource src = ctx.getSource();
                    ServerPlayerEntity player = src.getPlayerOrException();
                    thaumcraft.api.capabilities.IPlayerKnowledge know = ThaumcraftCapabilities.getKnowledge(player);
                    if (know != null) know.clear();
                    src.sendSuccess(new StringTextComponent("Thaumcraft research reset for " + player.getName().getString()), true);
                    return 1;
                }))
            .then(Commands.literal("reload")
                .executes(ctx -> {
                    CommandSource src = ctx.getSource();
                    net.minecraft.server.MinecraftServer server = src.getServer();
                    if (server != null) {
                        // Trigger datapack reload which will also refresh research via ResearchReloadListener
                        server.reloadResources(server.getPackRepository().getSelectedIds());
                        src.sendSuccess(new StringTextComponent("Reloading research datapacks..."), true);
                        return 1;
                    }
                    return 0;
                }))
            .then(Commands.literal("open_book")
                .executes(ctx -> {
                    CommandSource src = ctx.getSource();
                    if (src.getEntity() instanceof ServerPlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
                        thaumcraft.common.network.NetworkHandler.sendTo(player, new thaumcraft.common.network.msg.ClientOpenThaumonomiconMessage());
                        src.sendSuccess(new StringTextComponent("Opened Thaumonomicon"), false);
                        return 1;
                    }
                    return 0;
                }));

        d.register(root);
    }
}


