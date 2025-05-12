package thaumcraft.common.lib.network.misc;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.items.tools.ItemElementalShovel;

import java.util.function.Supplier;


public class PacketItemKeyToServer
{
    private final byte key;
    private final byte mod;
    
    public PacketItemKeyToServer(int key) {
        this.key = (byte)key;
        this.mod = 0;
    }
    
    public PacketItemKeyToServer(int key, int mod) {
        this.key = (byte)key;
        this.mod = (byte)mod;
    }
    
    private PacketItemKeyToServer(byte key, byte mod) {
        this.key = key;
        this.mod = mod;
    }
    
    public void encode(PacketBuffer buffer) {
        buffer.writeByte(key);
        buffer.writeByte(mod);
    }
    
    public static PacketItemKeyToServer decode(PacketBuffer buffer) {
        return new PacketItemKeyToServer(buffer.readByte(), buffer.readByte());
    }
    
    public static void handle(PacketItemKeyToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;

            World world = player.world;

            ItemStack mainHandStack = player.getHeldItemMainhand();
            ItemStack offHandStack = player.getHeldItemOffhand();

            if (message.key == 1) {
                boolean handled = false;
                if (!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof ICaster) {
                    CasterManager.toggleMisc(mainHandStack, world, player, message.mod);
                    handled = true;
                }
                if (!handled && !offHandStack.isEmpty() && offHandStack.getItem() instanceof ICaster) {
                    CasterManager.toggleMisc(offHandStack, world, player, message.mod);
                    handled = true;
                }
                if (!handled && !mainHandStack.isEmpty() && mainHandStack.getItem() instanceof ItemElementalShovel) {
                    ItemElementalShovel.setOrientation(mainHandStack, (byte) (ItemElementalShovel.getOrientation(mainHandStack) + 1));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
