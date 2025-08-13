package thaumcraft.common.golems.seals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.ClientSealMessage;

public class SealEntity implements ISealEntity {
    private SealPos sealPos;
    private ISeal seal;
    private byte priority;
    private byte color;
    private boolean locked;
    private boolean redstone;
    private String owner;
    private java.util.UUID ownerUuid;
    private BlockPos area;

    public SealEntity() {
        this.priority = 0;
        this.color = 0;
        this.locked = false;
        this.redstone = false;
        this.owner = "";
        // stopped state currently unused; task suspension handled elsewhere
        this.area = new BlockPos(1, 1, 1);
    }

    public SealEntity(World world, SealPos sealPos, ISeal seal) {
        this();
        this.sealPos = sealPos;
        this.seal = seal;
        if (seal instanceof ISealConfigArea) {
            int x = (sealPos.face.getStepX() == 0) ? 3 : 1;
            int y = (sealPos.face.getStepY() == 0) ? 3 : 1;
            int z = (sealPos.face.getStepZ() == 0) ? 3 : 1;
            area = new BlockPos(x, y, z);
        }
    }

    @Override
    public void tickSealEntity(World world) {
        if (seal != null) {
            if (isStoppedByRedstone(world)) return;
            seal.tickSeal(world, this);
        }
    }

    @Override
    public boolean isStoppedByRedstone(World world) {
        if (!isRedstoneSensitive()) return false;
        BlockPos p = getSealPos().pos;
        Direction f = getSealPos().face;
        // In 1.16.5 vanilla world does not expose signal query directly; defer to level methods
        // Use neighbor signal OR power; direction-specific checks can be added later if needed
        return world.hasNeighborSignal(p) || world.hasNeighborSignal(p.relative(f));
    }

    @Override
    public ISeal getSeal() { return seal; }

    public void setSeal(ISeal seal) { this.seal = seal; }

    @Override
    public SealPos getSealPos() { return sealPos; }

    public void setSealPos(SealPos sealPos) { this.sealPos = sealPos; }

    @Override
    public byte getPriority() { return priority; }

    @Override
    public void setPriority(byte priority) { this.priority = priority; }

    @Override
    public byte getColor() { return color; }

    @Override
    public void setColor(byte color) { this.color = color; }

    @Override
    public String getOwner() { return ownerUuid != null ? ownerUuid.toString() : owner; }

    @Override
    public void setOwner(String owner) {
        this.owner = owner == null ? "" : owner;
        try { this.ownerUuid = java.util.UUID.fromString(this.owner); } catch (Exception ignored) { this.ownerUuid = null; }
    }
    public void setOwner(java.util.UUID owner) {
        this.ownerUuid = owner;
        this.owner = owner == null ? "" : owner.toString();
    }

    @Override
    public boolean isLocked() { return locked; }

    @Override
    public void setLocked(boolean locked) { this.locked = locked; }

    @Override
    public boolean isRedstoneSensitive() { return redstone; }

    @Override
    public void setRedstoneSensitive(boolean redstone) { this.redstone = redstone; }

    // No extra accessors beyond interface (keeps API clean)

    @Override
    public void readNBT(CompoundNBT nbt) {
        BlockPos p = BlockPos.of(nbt.getLong("pos"));
        Direction face = Direction.from3DDataValue(nbt.getByte("face"));
        this.sealPos = new SealPos(p, face);
        setPriority(nbt.getByte("priority"));
        setColor(nbt.getByte("color"));
        setLocked(nbt.getBoolean("locked"));
        setRedstoneSensitive(nbt.getBoolean("redstone"));
        if (nbt.hasUUID("owner_uuid")) {
            setOwner(nbt.getUUID("owner_uuid"));
        } else {
            setOwner(nbt.getString("owner"));
        }
        if (seal != null) {
            seal.readCustomNBT(nbt);
            if (seal instanceof ISealConfigArea) {
                area = BlockPos.of(nbt.getLong("area"));
            }
            if (seal instanceof ISealConfigToggles) {
                for (ISealConfigToggles.SealToggle prop : ((ISealConfigToggles) seal).getToggles()) {
                    if (nbt.contains(prop.getKey())) {
                        prop.setValue(nbt.getBoolean(prop.getKey()));
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT writeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("pos", sealPos.pos.asLong());
        nbt.putByte("face", (byte) sealPos.face.get3DDataValue());
        nbt.putString("type", seal.getKey());
        nbt.putByte("priority", getPriority());
        nbt.putByte("color", getColor());
        nbt.putBoolean("locked", isLocked());
        nbt.putBoolean("redstone", isRedstoneSensitive());
        if (ownerUuid != null) nbt.putUUID("owner_uuid", ownerUuid);
        nbt.putString("owner", owner == null ? "" : owner);
        if (seal != null) {
            seal.writeCustomNBT(nbt);
            if (seal instanceof ISealConfigArea) {
                nbt.putLong("area", area.asLong());
            }
            if (seal instanceof ISealConfigToggles) {
                for (ISealConfigToggles.SealToggle prop : ((ISealConfigToggles) seal).getToggles()) {
                    nbt.putBoolean(prop.getKey(), prop.getValue());
                }
            }
        }
        return nbt;
    }

    @Override
    public void syncToClient(World world) {
        if (world.isClientSide) return;
        ClientSealMessage msg = new ClientSealMessage(
                sealPos.pos,
                sealPos.face,
                seal != null ? seal.getKey() : "",
                priority,
                color,
                locked,
                redstone,
                owner
        );
        if (seal instanceof ISealConfigArea && area != null) {
            msg.setAreaLong(area.asLong());
        }
        NetworkHandler.sendToAllAround(msg, (net.minecraft.world.server.ServerWorld) world, sealPos.pos, 64);
    }

    @Override
    public BlockPos getArea() { return area; }

    @Override
    public void setArea(BlockPos v) { area = v; }
}


