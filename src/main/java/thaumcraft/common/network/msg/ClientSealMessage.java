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
    private boolean[] props; // optional per-seal toggles
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

    public ClientSealMessage(BlockPos pos, Direction face, String type, byte priority, byte color, boolean locked, boolean redstone, String owner) {
        this.pos = pos;
        this.face = face;
        this.type = type;
        this.priority = priority;
        this.color = color;
        this.locked = locked;
        this.redstone = redstone;
        this.owner = owner == null ? "" : owner;
        this.filtersize = 0;
    }

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
        if (m.props != null) for (boolean b : m.props) buf.writeBoolean(b);
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
        // Optional trailing fields. We can't know count of props; infer from remaining bytes.
        if (buf.readableBytes() >= Long.BYTES) m.area = buf.readLong();
        int remaining = buf.readableBytes();
        if (remaining > 0) {
            // Interpret remaining bytes as booleans; cap at reasonable length
            int max = Math.min(remaining, 16);
            m.props = new boolean[max];
            for (int i = 0; i < max; i++) m.props[i] = buf.readBoolean();
            // Drop any trailing bytes to avoid decode desync
            while (buf.readableBytes() > 0) buf.readBoolean();
        }
        return m;
    }

    public static void handle(ClientSealMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if ("REMOVE".equals(msg.type)) {
                thaumcraft.client.golems.ClientSealCache.remove(msg.pos, msg.face);
            } else {
                // Populate client cache for rendering/GUI. Partial fields supported.
                Long areaOpt = msg.area == 0L ? null : msg.area;
                thaumcraft.client.golems.ClientSealCache.upsert(
                        msg.pos, msg.face, msg.type, msg.priority, msg.color, msg.locked, msg.redstone, msg.owner, areaOpt);
                if (msg.props != null) {
                    thaumcraft.client.golems.ClientSealCache.putToggles(msg.pos, msg.face, msg.props);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos getPos() { return pos; }
    public Direction getFace() { return face; }
    public String getType() { return type; }
    public byte getPriority() { return priority; }
    public byte getColor() { return color; }
    public boolean isLocked() { return locked; }
    public boolean isRedstone() { return redstone; }
    public String getOwner() { return owner; }

    public ClientSealMessage setAreaLong(long area) {
        this.area = area;
        return this;
    }
}



