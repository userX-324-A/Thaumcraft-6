package thaumcraft.api.golems.tasks;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.ProvisionRequest;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;



public class Task {

	private UUID golemUUID;
	private int id;	
	private byte type;
	private SealPos sealPos;	
	private BlockPos pos;	
	private Entity entity; 
    private RegistryKey<World> dimensionKey; // Preferred dimension identifier in 1.16.5+
    private long reservationTimestampMs;
	private boolean reserved;
	private boolean suspended;
	private boolean completed;
	private int data;
	private ProvisionRequest linkedProvision;
	/**
	 * Lifespan in seconds. Default 300 seconds
	 */
	private short lifespan;
	private byte priority=0;
	
	private Task() {}

	public Task(SealPos sealPos, BlockPos pos) {
		this.sealPos = sealPos;
		this.pos = pos;
		if (sealPos==null) {
			id = (System.currentTimeMillis()+"/BNPOS/"+pos.toString()).hashCode();
		} else
			id = (System.currentTimeMillis()+"/B/"+sealPos.face.toString()+"/"+sealPos.pos.toString()+"/"+pos.toString()).hashCode();
		type = 0;
		lifespan = 300;
	}
	
	public Task(SealPos sealPos, Entity entity) {
		this.sealPos = sealPos;
		this.entity = entity;
        if (sealPos==null) {
            id = (System.currentTimeMillis()+"/ENPOS/"+entity.getId()).hashCode();
        } else
            id = (System.currentTimeMillis()+"/E/"+sealPos.face.toString()+"/"+sealPos.pos.toString()+"/"+entity.getId()).hashCode();
		type = 1;
		lifespan = 300;
	}	

    public Task(RegistryKey<World> dimensionKey, SealPos sealPos, BlockPos pos) {
        this(sealPos, pos);
        this.dimensionKey = dimensionKey;
    }

    public Task(RegistryKey<World> dimensionKey, SealPos sealPos, Entity entity) {
        this(sealPos, entity);
        this.dimensionKey = dimensionKey;
    }

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompletion(boolean fulfilled) {
		completed = fulfilled;
		lifespan += 1;
	}

	public UUID getGolemUUID() {
		return golemUUID;
	}

	public void setGolemUUID(UUID golemUUID) {
		this.golemUUID = golemUUID;
	}

	public BlockPos getPos() {
        return type==1?entity.blockPosition():pos;
	}	
	
	public byte getType() {
		return type;
	}

	public Entity getEntity() {
		return entity;
	}

	public int getId() {
		return id;
	}
	
	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean res) {
		reserved = res;
        lifespan += 120;
        reservationTimestampMs = res ? System.currentTimeMillis() : 0L;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		setLinkedProvision(null);
		this.suspended = suspended;
	}

	public SealPos getSealPos() {
		return sealPos;
	}

    public RegistryKey<World> getDimensionKey() {
        return dimensionKey;
    }

    public void setDimensionKey(RegistryKey<World> dimensionKey) {
        this.dimensionKey = dimensionKey;
    }

    public long getReservationTimestampMs() { return reservationTimestampMs; }
    public void setReservationTimestampMs(long v) { reservationTimestampMs = v; }

    // Basic NBT IO for persistence
    public net.minecraft.nbt.CompoundNBT serializeNBT() {
        net.minecraft.nbt.CompoundNBT nbt = new net.minecraft.nbt.CompoundNBT();
        nbt.putInt("id", id);
        nbt.putByte("type", type);
        if (pos != null) nbt.putLong("pos", pos.asLong());
        if (entity != null) nbt.putUUID("EUUID", entity.getUUID());
        if (golemUUID != null) nbt.putUUID("GUUID", golemUUID);
        if (sealPos != null) {
            net.minecraft.nbt.CompoundNBT sp = new net.minecraft.nbt.CompoundNBT();
            sp.putLong("pos", sealPos.pos.asLong());
            sp.putByte("face", (byte) sealPos.face.get3DDataValue());
            nbt.put("sealpos", sp);
        }
        if (dimensionKey != null) nbt.putString("dim", dimensionKey.location().toString());
        nbt.putBoolean("reserved", reserved);
        nbt.putBoolean("suspended", suspended);
        nbt.putBoolean("completed", completed);
        nbt.putInt("data", data);
        nbt.putInt("priority", priority);
        nbt.putShort("lifespan", lifespan);
        nbt.putLong("res_ts", reservationTimestampMs);
        return nbt;
    }

    public static Task deserializeNBT(net.minecraft.nbt.CompoundNBT nbt) {
        Task t = new Task();
        t.id = nbt.getInt("id");
        t.type = nbt.getByte("type");
        if (nbt.contains("pos")) t.pos = net.minecraft.util.math.BlockPos.of(nbt.getLong("pos"));
        if (nbt.hasUUID("GUUID")) t.golemUUID = nbt.getUUID("GUUID");
        if (nbt.contains("sealpos")) {
            net.minecraft.nbt.CompoundNBT sp = nbt.getCompound("sealpos");
            t.sealPos = new thaumcraft.api.golems.seals.SealPos(net.minecraft.util.math.BlockPos.of(sp.getLong("pos")), net.minecraft.util.Direction.from3DDataValue(sp.getByte("face")));
        }
        if (nbt.contains("dim")) {
            t.dimensionKey = net.minecraft.util.RegistryKey.create(net.minecraft.util.registry.Registry.DIMENSION_REGISTRY, new net.minecraft.util.ResourceLocation(nbt.getString("dim")));
        }
        t.reserved = nbt.getBoolean("reserved");
        t.suspended = nbt.getBoolean("suspended");
        t.completed = nbt.getBoolean("completed");
        t.data = nbt.getInt("data");
        t.priority = nbt.getByte("priority");
        t.lifespan = nbt.getShort("lifespan");
        t.reservationTimestampMs = nbt.getLong("res_ts");
        return t;
    }

	public boolean equals(Object o)
    {
        if (!(o instanceof Task))
        {
            return false;
        }
        else
        {
        	Task t = (Task)o;
            return t.id == id;
        }
    }

	public long getLifespan() {
		return lifespan;
	}
	
	public void setLifespan(short ls) {
		lifespan = ls;
	}

    public boolean canGolemPerformTask(IGolemAPI golem) {
        ISealEntity se;
        if (dimensionKey != null) {
            se = GolemHelper.getSealEntity(dimensionKey, sealPos);
        } else {
            // Legacy fallback: overworld
            se = GolemHelper.getSealEntity(0, sealPos);
        }
		if (se!=null) {
			if (golem.getGolemColor()>0 && se.getColor()>0 && golem.getGolemColor() != se.getColor()) return false;
			return se.getSeal().canGolemPerformTask(golem,this);
		} else {
			return true;
		}
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public ProvisionRequest getLinkedProvision() {
		return linkedProvision;
	}

	public void setLinkedProvision(ProvisionRequest linkedProvision) {
		this.linkedProvision = linkedProvision;
	}

	
	
	
//	public static Task readNBT(NBTTagCompound nbt)
//  {		
//		Task task = new Task();
//		task.id = nbt.getInteger("id");
//		task.type = nbt.getByte("type");		
//		if (nbt.hasKey("pos", 4)) task.pos = BlockPos.fromLong(nbt.getLong("pos"));	
//		
//		if (nbt.hasKey("GUUIDMost", 4) && nbt.hasKey("GUUIDLeast", 4))
//			task.golemUUID = new UUID(nbt.getLong("GUUIDMost"), nbt.getLong("GUUIDLeast"));
//		
//		if (nbt.hasKey("EUUIDMost", 4) && nbt.hasKey("EUUIDLeast", 4))
//			task.entityUUID = new UUID(nbt.getLong("EUUIDMost"), nbt.getLong("EUUIDLeast"));
//		
//		if (task.pos==null && task.entityUUID==null) return null;
//		
//		task.reserved = nbt.getBoolean("reserved");
//		task.waitOnSuspension = nbt.getBoolean("wos");
//		task.suspended = false;
//		task.completed = nbt.getBoolean("completed");
//		task.expireTime = System.currentTimeMillis() + 300000;		
//		if (nbt.hasKey("sealpos", 10)) {
//			NBTTagCompound sealpos = nbt.getCompoundTag("sealpos");
//			SealPos sp = new SealPos(BlockPos.fromLong(nbt.getLong("pos")), EnumFacing.VALUES[nbt.getByte("face")]);
//			TaskHandler.sealTaskCrossRef.put(task.id, sp);
//		}
//		return task;
//  }
//	
//	public static NBTTagCompound writeNBT(Task task)
//  {
//		NBTTagCompound nbt = new NBTTagCompound();
//		nbt.setInteger("id", task.id);
//		nbt.setByte("type", task.type);
//		if (task.pos!=null) nbt.setLong("pos", task.pos.toLong());
//		if (task.entity!=null) {
//			nbt.setLong("EUUIDMost", task.entity.getUniqueID().getMostSignificantBits());
//          nbt.setLong("EUUIDLeast", task.entity.getUniqueID().getLeastSignificantBits());
//		}
//		if (task.golemUUID!=null) {
//			nbt.setLong("GUUIDMost", task.golemUUID.getMostSignificantBits());
//          nbt.setLong("GUUIDLeast", task.golemUUID.getLeastSignificantBits());
//		}
//		nbt.setBoolean("reserved", task.reserved);
//		nbt.setBoolean("wos", task.waitOnSuspension);
//		nbt.setBoolean("completed", task.completed);
//		
//		SealPos sp = TaskHandler.sealTaskCrossRef.get(task.getId());
//		if (sp!=null) {
//			NBTTagCompound sealpos = new NBTTagCompound();
//			sealpos.setLong("pos", sp.pos.toLong());
//			sealpos.setByte("face", (byte) sp.face.ordinal());
//			nbt.setTag("sealpos", sealpos);
//		}
//		return nbt;
//  }

}

