package thaumcraft.common.lib.network.misc;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.lib.SoundsTC;

import java.util.function.Supplier;


public class PacketMiscEvent
{
    private final byte type;
    private final int value;
    public static final byte WARP_EVENT = 0;
    public static final byte MIST_EVENT = 1;
    public static final byte MIST_EVENT_SHORT = 2;
    
    public PacketMiscEvent(byte type) {
        this(type, 0);
    }
    
    public PacketMiscEvent(byte type, int value) {
        this.type = type;
        this.value = value;
    }
    
    public static void encode(PacketMiscEvent message, PacketBuffer buffer) {
        buffer.writeByte(message.type);
        buffer.writeInt(message.value);
    }
    
    public static PacketMiscEvent decode(PacketBuffer buffer) {
        byte type = buffer.readByte();
        int value = buffer.readInt();
        return new PacketMiscEvent(type, value);
    }
    
    public static void handle(PacketMiscEvent message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Dist dist = Dist.CLIENT;
            processMessage(message, dist);
        });
        context.setPacketHandled(true);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void processMessage(PacketMiscEvent message, Dist dist) {
        if (dist == Dist.CLIENT) {
            PlayerEntity p = Minecraft.getInstance().player;
            if (p == null) return;

            switch (message.type) {
                case WARP_EVENT: {
                    if (!ModConfig.CONFIG_GRAPHICS.nostress) {
                        p.level.playSound(p, p.getX(), p.getY(), p.getZ(), SoundsTC.heartbeat, SoundCategory.AMBIENT, 1.0f, 1.0f);
                        break;
                    }
                    break;
                }
                case MIST_EVENT: {
                    RenderEventHandler.fogFiddled = true;
                    RenderEventHandler.fogDuration = 2400;
                    break;
                }
                case MIST_EVENT_SHORT: {
                    RenderEventHandler.fogFiddled = true;
                    if (RenderEventHandler.fogDuration < 200) {
                        RenderEventHandler.fogDuration = 200;
                        break;
                    }
                    break;
                }
            }
        }
    }
}
