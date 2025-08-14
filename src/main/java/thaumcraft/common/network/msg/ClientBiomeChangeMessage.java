package thaumcraft.common.network.msg;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * Clientbound: indicates a biome change at a specific position. Applying the change client-side is TODO.
 */
public class ClientBiomeChangeMessage {
    private BlockPos blockPos;
    private ResourceLocation biomeKey;

    public ClientBiomeChangeMessage() {}

    public ClientBiomeChangeMessage(BlockPos pos, ResourceLocation biomeKey) {
        this.blockPos = pos;
        this.biomeKey = biomeKey;
    }

    public static void encode(ClientBiomeChangeMessage msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.blockPos);
        buf.writeResourceLocation(msg.biomeKey);
    }

    public static ClientBiomeChangeMessage decode(PacketBuffer buf) {
        ClientBiomeChangeMessage msg = new ClientBiomeChangeMessage();
        msg.blockPos = buf.readBlockPos();
        msg.biomeKey = buf.readResourceLocation();
        return msg;
    }

    public static void handle(ClientBiomeChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            Biome biome = ForgeRegistries.BIOMES.getValue(msg.biomeKey);
            if (biome == null) return;
            // TODO: Apply the biome to the local chunk at msg.blockPos on client if still needed in 1.16.5
        });
        ctx.get().setPacketHandled(true);
    }
}



