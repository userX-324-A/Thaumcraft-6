package thaumcraft.common.lib.network.misc;

// import io.netty.buffer.ByteBuf; // Removed
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity; // Updated import
import net.minecraft.network.PacketBuffer; // Added import
import net.minecraft.util.SoundCategory;
// import net.minecraftforge.fml.common.network.ByteBufUtils; // Removed, use PacketBuffer methods
// import net.minecraftforge.fml.common.network.simpleimpl.IMessage; // Removed
// import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler; // Removed
// import net.minecraftforge.fml.common.network.simpleimpl.MessageContext; // Removed
import net.minecraftforge.api.distmarker.Dist; // Added import
import net.minecraftforge.api.distmarker.OnlyIn; // Added import
import net.minecraftforge.fml.network.NetworkEvent; // Added import
// import net.minecraftforge.fml.relauncher.Side; // Removed
// import net.minecraftforge.fml.relauncher.SideOnly; // Removed
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.client.lib.events.HudHandler;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.lib.SoundsTC;

import java.util.function.Supplier; // Added import

// public class PacketKnowledgeGain implements IMessage, IMessageHandler<PacketKnowledgeGain, IMessage> // Removed implements
public class PacketKnowledgeGain {
    private final byte type; // Made final
    private final String cat; // Made final

    // Constructor remains largely the same
    public PacketKnowledgeGain(byte type, String value) {
        this.type = type;
        this.cat = (value == null) ? "" : value;
    }

    // New static encode method
    public static void encode(PacketKnowledgeGain message, PacketBuffer buffer) {
        buffer.writeByte(message.type);
        buffer.writeUtf(message.cat); // Use PacketBuffer.writeUtf
    }

    // New static decode method
    public static PacketKnowledgeGain decode(PacketBuffer buffer) {
        byte type = buffer.readByte();
        String cat = buffer.readUtf(32767); // Use PacketBuffer.readUtf
        return new PacketKnowledgeGain(type, cat);
    }

    // New static handle method
    public static void handle(PacketKnowledgeGain message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Ensure client-side execution
            processMessage(message);
        });
        context.setPacketHandled(true);
    }

    // Logic moved to a static private method, annotated for client side
    @OnlyIn(Dist.CLIENT)
    private static void processMessage(PacketKnowledgeGain message) {
        PlayerEntity p = Minecraft.getInstance().player; // Use getInstance() and PlayerEntity
        if (p == null) return; // Add null check

        IPlayerKnowledge.EnumKnowledgeType type = IPlayerKnowledge.EnumKnowledgeType.values()[message.type];
        ResearchCategory cat = (message.cat.length() > 0) ? ResearchCategories.getResearchCategory(message.cat) : null;

        // HudHandler access might need checking if RenderEventHandler.INSTANCE is still valid
        // Assuming HudHandler is accessible statically or via an instance from Minecraft.getInstance()
        // For now, assuming direct access via RenderEventHandler is okay, but this might need revision.
        RenderEventHandler eventHandler = RenderEventHandler.INSTANCE; // Assuming this is still valid
        if (eventHandler != null) { // Check if INSTANCE is valid
             eventHandler.hudHandler.knowledgeGainTrackers.add(new HudHandler.KnowledgeGainTracker(type, cat, 40 + p.level.random.nextInt(20), p.level.random.nextLong())); // Use p.level.random
        }

        // Updated playSound call
        p.level.playSound(p, p.getX(), p.getY(), p.getZ(), SoundsTC.learn, SoundCategory.AMBIENT, 1.0f, 1.0f);
    }
    
    // Removed old IMessageHandler interface, toBytes, fromBytes, onMessage methods
}
