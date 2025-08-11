package thaumcraft.common.network.msg;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound: seal data push. Replacement for PacketSealToClient. Client handling TBD with golems port. */
public class ClientSealMessage {
    private BlockPos pos;
    private Direction face;
    private String type;
    private long area;
    private boolean[] props;
    private boolean blacklist;
    private byte filtersize;
    private NonNullList<ItemStack> filter;
    private NonNullList<Integer> filterStackSize;
    private byte priority;
    private byte color;
    private boolean locked;
    private boolean redstone;
    private String owner;

    public ClientSealMessage() {}

    public static void encode(ClientSealMessage m, PacketBuffer buf) {
        buf.writeBlockPos(m.pos);
        buf.writeByte(m.face.get3DDataValue());
        buf.writeByte(m.priority);
        buf.writeByte(m.color);
        buf.writeBoolean(m.locked);
        buf.writeBoolean(m.redstone);
        buf.writeUtf(m.owner);
        buf.writeUtf(m.type);
        buf.writeBoolean(m.blacklist);
        buf.writeByte(m.filtersize);
        if (m.filtersize > 0) {
            for (int a = 0; a < m.filtersize; ++a) {
                buf.writeItem(m.filter.get(a));
                buf.writeShort(m.filterStackSize.get(a));
            }
        }
        if (m.area != 0L) buf.writeLong(m.area);
        if (m.props != null) {
            for (boolean b : m.props) buf.writeBoolean(b);
        }
    }

    public static ClientSealMessage decode(PacketBuffer buf) {
        ClientSealMessage m = new ClientSealMessage();
        m.pos = buf.readBlockPos();
        m.face = Direction.from3DDataValue(buf.readByte());
        m.priority = buf.readByte();
        m.color = buf.readByte();
        m.locked = buf.readBoolean();
        m.redstone = buf.readBoolean();
        m.owner = buf.readUtf(32767);
        m.type = buf.readUtf(32767);
        m.blacklist = buf.readBoolean();
        m.filtersize = buf.readByte();
        if (m.filtersize > 0) {
            m.filter = NonNullList.withSize(m.filtersize, ItemStack.EMPTY);
            m.filterStackSize = NonNullList.withSize(m.filtersize, 0);
            for (int a = 0; a < m.filtersize; ++a) {
                m.filter.set(a, buf.readItem());
                m.filterStackSize.set(a, (int) buf.readShort());
            }
        }
        // area/props optional; will require seal system port to fully reconstruct
        return m;
    }

    public static void handle(ClientSealMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // TODO: integrate with golem seal client state when ported
        });
        ctx.get().setPacketHandled(true);
    }
}


