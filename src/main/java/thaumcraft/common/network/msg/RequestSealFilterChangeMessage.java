package thaumcraft.common.network.msg;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.api.golems.seals.ISealConfigFilter;
import thaumcraft.common.golems.seals.SealWorldData;

import java.util.function.Supplier;

/** Serverbound: request to change a seal's item filter. */
public class RequestSealFilterChangeMessage {
    private BlockPos pos;
    private Direction face;
    private byte size;
    private NonNullList<ItemStack> filter;
    private NonNullList<Integer> counts;
    private boolean blacklist;

    public RequestSealFilterChangeMessage() {}

    public RequestSealFilterChangeMessage(BlockPos pos, Direction face,
                                          NonNullList<ItemStack> filter,
                                          NonNullList<Integer> counts,
                                          boolean blacklist) {
        this.pos = pos;
        this.face = face;
        this.size = (byte) (filter == null ? 0 : filter.size());
        this.filter = filter == null ? NonNullList.create() : filter;
        this.counts = counts == null ? NonNullList.create() : counts;
        this.blacklist = blacklist;
    }

    public static void encode(RequestSealFilterChangeMessage m, PacketBuffer buf) {
        buf.writeBlockPos(m.pos);
        buf.writeByte(m.face.get3DDataValue());
        buf.writeByte(m.size);
        for (int i = 0; i < m.size; i++) {
            buf.writeItem(m.filter.get(i));
            buf.writeShort(m.counts.get(i));
        }
        buf.writeBoolean(m.blacklist);
    }

    public static RequestSealFilterChangeMessage decode(PacketBuffer buf) {
        RequestSealFilterChangeMessage m = new RequestSealFilterChangeMessage();
        m.pos = buf.readBlockPos();
        m.face = Direction.from3DDataValue(buf.readByte());
        m.size = buf.readByte();
        m.filter = NonNullList.withSize(m.size, ItemStack.EMPTY);
        m.counts = NonNullList.withSize(m.size, 0);
        for (int i = 0; i < m.size; i++) {
            m.filter.set(i, buf.readItem());
            m.counts.set(i, (int) buf.readShort());
        }
        m.blacklist = buf.readBoolean();
        return m;
    }

    public static void handle(RequestSealFilterChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity sp = ctx.get().getSender();
        if (sp == null) { ctx.get().setPacketHandled(true); return; }
        ctx.get().enqueueWork(() -> {
            net.minecraft.world.server.ServerWorld world = (net.minecraft.world.server.ServerWorld) sp.level;
            thaumcraft.api.golems.seals.ISealEntity ent = SealWorldData.get(world).get(msg.pos, msg.face);
            if (ent == null) return;
            if (!sp.abilities.instabuild && (ent.getOwner() == null || !ent.getOwner().equals(sp.getUUID().toString()))) return;
            if (ent.getSeal() instanceof ISealConfigFilter) {
                ISealConfigFilter f = (ISealConfigFilter) ent.getSeal();
                for (int i = 0; i < msg.size; i++) {
                    f.setFilterSlot(i, msg.filter.get(i));
                    f.setFilterSlotSize(i, msg.counts.get(i));
                }
                f.setBlacklist(msg.blacklist);
                // Broadcast change to nearby clients
                thaumcraft.common.network.NetworkHandler.sendToAllAround(
                        makeClientSync(world, msg), world, msg.pos, 64);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static thaumcraft.common.network.msg.ClientSealFilterMessage makeClientSync(net.minecraft.world.server.ServerWorld world, RequestSealFilterChangeMessage msg) {
        // Build fresh state from the authoritative seal so we send consistent data
        thaumcraft.api.golems.seals.ISealEntity ent = thaumcraft.common.golems.seals.SealWorldData.get(world).get(msg.pos, msg.face);
        if (ent != null && ent.getSeal() instanceof ISealConfigFilter) {
            ISealConfigFilter f = (ISealConfigFilter) ent.getSeal();
            net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> stacks = f.getInv();
            net.minecraft.util.NonNullList<java.lang.Integer> sizes = f.getSizes();
            return new thaumcraft.common.network.msg.ClientSealFilterMessage(msg.pos, msg.face, stacks, sizes, f.isBlacklist());
        }
        // Fallback to echoing the request if the entity isn't available yet
        return new thaumcraft.common.network.msg.ClientSealFilterMessage(msg.pos, msg.face, msg.filter, msg.counts, msg.blacklist);
    }
}


