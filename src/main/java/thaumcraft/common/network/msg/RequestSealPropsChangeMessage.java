package thaumcraft.common.network.msg;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import thaumcraft.common.golems.seals.SealService;

import java.util.function.Supplier;

/** Serverbound: request to change core seal properties. */
public class RequestSealPropsChangeMessage {
    public enum Kind { PRIORITY, COLOR, LOCKED, REDSTONE, AREA, TOGGLE }
    private BlockPos pos;
    private Direction face;
    private Kind kind;
    private int intVal;
    private boolean boolVal;
    private long areaVal;

    public RequestSealPropsChangeMessage() {}

    public RequestSealPropsChangeMessage(BlockPos pos, Direction face, Kind kind, int intVal, boolean boolVal, long areaVal) {
        this.pos = pos;
        this.face = face;
        this.kind = kind;
        this.intVal = intVal;
        this.boolVal = boolVal;
        this.areaVal = areaVal;
    }

    public static void encode(RequestSealPropsChangeMessage m, PacketBuffer buf) {
        buf.writeBlockPos(m.pos);
        buf.writeByte(m.face.get3DDataValue());
        buf.writeEnum(m.kind);
        buf.writeInt(m.intVal);
        buf.writeBoolean(m.boolVal);
        buf.writeLong(m.areaVal);
    }

    public static RequestSealPropsChangeMessage decode(PacketBuffer buf) {
        RequestSealPropsChangeMessage m = new RequestSealPropsChangeMessage();
        m.pos = buf.readBlockPos();
        m.face = Direction.from3DDataValue(buf.readByte());
        m.kind = buf.readEnum(Kind.class);
        m.intVal = buf.readInt();
        m.boolVal = buf.readBoolean();
        m.areaVal = buf.readLong();
        return m;
    }

    public static void handle(RequestSealPropsChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity sp = ctx.get().getSender();
        if (sp == null) { ctx.get().setPacketHandled(true); return; }
        ctx.get().enqueueWork(() -> {
            net.minecraft.world.server.ServerWorld world = (net.minecraft.world.server.ServerWorld) sp.level;
            switch (msg.kind) {
                case PRIORITY: SealService.setPriority(world, msg.pos, msg.face, (byte) msg.intVal, sp); break;
                case COLOR: SealService.setColor(world, msg.pos, msg.face, (byte) msg.intVal, sp); break;
                case LOCKED: SealService.setLocked(world, msg.pos, msg.face, msg.boolVal, sp); break;
                case REDSTONE: SealService.setRedstone(world, msg.pos, msg.face, msg.boolVal, sp); break;
                case AREA: SealService.setArea(world, msg.pos, msg.face, BlockPos.of(msg.areaVal), sp); break;
                case TOGGLE: SealService.setToggle(world, msg.pos, msg.face, msg.intVal, msg.boolVal, sp); break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}



