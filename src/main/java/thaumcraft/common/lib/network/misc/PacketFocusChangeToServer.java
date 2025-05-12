package thaumcraft.common.lib.network.misc;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.items.casters.CasterManager;

import java.util.function.Supplier;


public class PacketFocusChangeToServer
{
    private final String focus;

    public PacketFocusChangeToServer(String focus) {
        this.focus = focus;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeString(focus);
    }

    public static PacketFocusChangeToServer decode(PacketBuffer buffer) {
        return new PacketFocusChangeToServer(buffer.readString(32767));
    }

    public static void handle(PacketFocusChangeToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;

            World world = player.world;

            if (player.getHeldItemMainhand().getItem() instanceof ICaster) {
                CasterManager.changeFocus(player.getHeldItemMainhand(), world, player, message.focus);
            } else if (player.getHeldItemOffhand().getItem() instanceof ICaster) {
                CasterManager.changeFocus(player.getHeldItemOffhand(), world, player, message.focus);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

