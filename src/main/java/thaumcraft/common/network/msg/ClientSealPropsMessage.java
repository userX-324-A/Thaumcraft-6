package thaumcraft.common.network.msg;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Clientbound: updates core seal properties (priority, color, locked, redstone, optional area). */
public class ClientSealPropsMessage {
    private BlockPos pos;
    private Direction face;
    private byte priority;
    private byte color;
    private boolean locked;
    private boolean redstone;
    private long area; // 0L if not included
    private boolean[] toggles; // optional per-seal toggles for UI sync

    public ClientSealPropsMessage() {}

    public ClientSealPropsMessage(BlockPos pos, Direction face, byte priority, byte color, boolean locked, boolean redstone, Long areaOpt) {
        this.pos = pos;
        this.face = face;
        this.priority = priority;
        this.color = color;
        this.locked = locked;
        this.redstone = redstone;
        this.area = areaOpt == null ? 0L : areaOpt;
    }

    // Package-private setter used by server helper via reflection fallback if needed
    void setToggles(boolean[] t) { this.toggles = t; }

    public static void encode(ClientSealPropsMessage m, PacketBuffer buf) {
        buf.writeBlockPos(m.pos);
        buf.writeByte(m.face.get3DDataValue());
        buf.writeByte(m.priority);
        buf.writeByte(m.color);
        buf.writeBoolean(m.locked);
        buf.writeBoolean(m.redstone);
        buf.writeLong(m.area);
        if (m.toggles != null) {
            buf.writeByte(m.toggles.length);
            for (boolean b : m.toggles) buf.writeBoolean(b);
        } else {
            buf.writeByte(0);
        }
    }

    public static ClientSealPropsMessage decode(PacketBuffer buf) {
        ClientSealPropsMessage m = new ClientSealPropsMessage();
        m.pos = buf.readBlockPos();
        m.face = Direction.from3DDataValue(buf.readByte());
        m.priority = buf.readByte();
        m.color = buf.readByte();
        m.locked = buf.readBoolean();
        m.redstone = buf.readBoolean();
        m.area = buf.readLong();
        int tlen = buf.readByte() & 0xFF;
        if (tlen > 0) {
            m.toggles = new boolean[tlen];
            for (int i = 0; i < tlen; i++) m.toggles[i] = buf.readBoolean();
        }
        return m;
    }

    public static void handle(ClientSealPropsMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Long areaOpt = msg.area == 0L ? null : msg.area;
            thaumcraft.client.golems.ClientSealCache.updateProps(msg.pos, msg.face, msg.priority, msg.color, msg.locked, msg.redstone, areaOpt);
            if (msg.toggles != null) {
                thaumcraft.client.golems.ClientSealCache.putToggles(msg.pos, msg.face, msg.toggles);
            }
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.screen instanceof thaumcraft.common.client.screen.SealScreen) {
                mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



