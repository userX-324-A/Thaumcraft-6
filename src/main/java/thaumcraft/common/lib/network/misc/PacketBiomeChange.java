package thaumcraft.common.lib.network.misc;

// import io.netty.buffer.ByteBuf; // Removed
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer; // Added (FriendlyByteBuf)
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.RegistryKey; // Added
import net.minecraft.util.registry.Registry; // Added
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist; // Added
import net.minecraftforge.api.distmarker.OnlyIn; // Added
// import net.minecraftforge.fml.common.network.simpleimpl.IMessage; // Removed
// import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler; // Removed
// import net.minecraftforge.fml.common.network.simpleimpl.MessageContext; // Removed
import net.minecraftforge.fml.network.NetworkEvent; // Added
// import net.minecraftforge.fml.relauncher.Side; // Removed
// import net.minecraftforge.fml.relauncher.SideOnly; // Removed
// import thaumcraft.Thaumcraft; // Removed, client world obtained differently
import thaumcraft.common.lib.utils.Utils;

import java.util.function.Supplier; // Added

// public class PacketBiomeChange implements IMessage, IMessageHandler<PacketBiomeChange, IMessage> // Removed implements
public class PacketBiomeChange {
    private final int x; // Made final
    private final int z; // Made final
    private final short biomeId; // Made final and renamed for clarity

    public PacketBiomeChange(int x, int z, short biomeId) {
        this.x = x;
        this.z = z;
        this.biomeId = biomeId;
    }

    // New static encode method
    public static void encode(PacketBiomeChange message, PacketBuffer buffer) {
        buffer.writeInt(message.x);
        buffer.writeInt(message.z);
        buffer.writeShort(message.biomeId);
    }

    // New static decode method
    public static PacketBiomeChange decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int z = buffer.readInt();
        short biomeId = buffer.readShort();
        return new PacketBiomeChange(x, z, biomeId);
    }

    // New static handle method
    public static void handle(PacketBiomeChange message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Ensure client-side execution
            processMessage(message);
        });
        context.setPacketHandled(true);
    }

    // Logic moved to a static private method, annotated for client side
    @OnlyIn(Dist.CLIENT)
    private static void processMessage(PacketBiomeChange message) {
        // Get Biome from ID - this is a bit more complex in 1.16+ as Biome.getBiome(int) is gone.
        // We'll assume the ID is a raw ID that needs to be mapped to a Biome instance.
        // This might require a Biome registry lookup. For now, let's try a direct approach.
        // This part might need adjustment depending on how biome IDs are handled elsewhere in the mod.
        Biome biome = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(message.biomeId);

        if (biome != null && Minecraft.getInstance().level != null) {
            Utils.setBiomeAt(Minecraft.getInstance().level, new BlockPos(message.x, 0, message.z), biome);
        } else {
            // Log an error or handle missing biome
            System.err.println("Could not find biome for ID: " + message.biomeId + " or client world is null.");
        }
    }

    // Removed old toBytes, fromBytes, and onMessage methods
    /*
    public void toBytes(ByteBuf buffer) { ... }
    public void fromBytes(ByteBuf buffer) { ... }
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketBiomeChange message, MessageContext ctx) { ... }
    */
}
