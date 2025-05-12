package thaumcraft.common.lib.network.misc;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.client.lib.events.HudHandler;
import thaumcraft.common.world.aura.AuraChunk;

import java.util.function.Supplier;


public class PacketAuraToClient
{
    private final short base;
    private final float vis;
    private final float flux;

    public PacketAuraToClient(AuraChunk ac) {
        this.base = ac.getBase();
        this.vis = ac.getVis();
        this.flux = ac.getFlux();
    }
    
    private PacketAuraToClient(short base, float vis, float flux) {
        this.base = base;
        this.vis = vis;
        this.flux = flux;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeShort(base);
        buffer.writeFloat(vis);
        buffer.writeFloat(flux);
    }

    public static PacketAuraToClient decode(PacketBuffer buffer) {
        short base = buffer.readShort();
        float vis = buffer.readFloat();
        float flux = buffer.readFloat();
        return new PacketAuraToClient(base, vis, flux);
    }

    public static void handle(PacketAuraToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            HudHandler.currentAura = new AuraChunk(null, message.base, message.vis, message.flux);
        });
        ctx.get().setPacketHandled(true);
    }
}
