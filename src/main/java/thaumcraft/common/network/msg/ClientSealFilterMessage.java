package thaumcraft.common.network.msg;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound: seal filter update. Replacement for PacketSealFilterToClient. */
public class ClientSealFilterMessage {
    private BlockPos pos;
    private Direction face;
    private byte filtersize;
    private NonNullList<ItemStack> filter;
    private NonNullList<Integer> filterStackSize;
    private boolean blacklist;

    public ClientSealFilterMessage() {}

    public boolean isBlacklist() { return blacklist; }

    public ClientSealFilterMessage(BlockPos pos, Direction face,
                                   NonNullList<ItemStack> filter,
                                   NonNullList<Integer> filterStackSize,
                                   boolean blacklist) {
        this.pos = pos;
        this.face = face;
        this.filtersize = (byte) (filter == null ? 0 : filter.size());
        this.filter = filter == null ? NonNullList.create() : filter;
        this.filterStackSize = filterStackSize == null ? NonNullList.create() : filterStackSize;
        this.blacklist = blacklist;
    }

    public static void encode(ClientSealFilterMessage m, PacketBuffer buf) {
        buf.writeBlockPos(m.pos);
        buf.writeByte(m.face.get3DDataValue());
        buf.writeByte(m.filtersize);
        for (int a = 0; a < m.filtersize; ++a) {
            buf.writeItem(m.filter.get(a));
            buf.writeShort(m.filterStackSize.get(a));
        }
        buf.writeBoolean(m.blacklist);
    }

    public static ClientSealFilterMessage decode(PacketBuffer buf) {
        ClientSealFilterMessage m = new ClientSealFilterMessage();
        m.pos = buf.readBlockPos();
        m.face = Direction.from3DDataValue(buf.readByte());
        m.filtersize = buf.readByte();
        m.filter = NonNullList.withSize(m.filtersize, ItemStack.EMPTY);
        m.filterStackSize = NonNullList.withSize(m.filtersize, 0);
        for (int a = 0; a < m.filtersize; ++a) {
            m.filter.set(a, buf.readItem());
            m.filterStackSize.set(a, (int) buf.readShort());
        }
        m.blacklist = buf.readBoolean();
        return m;
    }

    public static void handle(ClientSealFilterMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Update client cache with filter contents; UI can read from cache
            thaumcraft.client.golems.ClientSealCache.putFilter(msg.pos, msg.face, msg.filtersize, msg.filter, msg.filterStackSize, msg.blacklist);
            // If a SealScreen is open for this seal, trigger it to refresh text/slots (best-effort)
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.screen instanceof thaumcraft.common.client.screen.SealScreen) {
                // Force re-init to pull latest cache
                mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


