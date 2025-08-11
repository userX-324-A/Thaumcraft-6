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

    public ClientSealFilterMessage() {}

    public static void encode(ClientSealFilterMessage m, PacketBuffer buf) {
        buf.writeBlockPos(m.pos);
        buf.writeByte(m.face.get3DDataValue());
        buf.writeByte(m.filtersize);
        for (int a = 0; a < m.filtersize; ++a) {
            buf.writeItem(m.filter.get(a));
            buf.writeShort(m.filterStackSize.get(a));
        }
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
        return m;
    }

    public static void handle(ClientSealFilterMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // TODO: integrate with golem seals when ported
        });
        ctx.get().setPacketHandled(true);
    }
}


