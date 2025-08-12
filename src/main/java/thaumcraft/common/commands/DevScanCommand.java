package thaumcraft.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.research.ScanningManager;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class DevScanCommand {
    private DevScanCommand() {}

    @SubscribeEvent
    public static void onRegister(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSource> root = LiteralArgumentBuilder
                .<CommandSource>literal("tcdev_scan")
                .requires(src -> src.hasPermission(2))
                .executes(ctx -> {
                    CommandSource source = ctx.getSource();
                    if (source.getEntity() instanceof ServerPlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
                        ScanningManager.scanTheThing(player, null);
                        source.sendSuccess(new StringTextComponent("Thaumcraft: triggered scan."), true);
                        return Command.SINGLE_SUCCESS;
                    }
                    source.sendFailure(new StringTextComponent("Player-only command"));
                    return 0;
                });
        event.getDispatcher().register(root);
    }
}


