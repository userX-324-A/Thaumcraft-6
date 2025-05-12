package thaumcraft.api.casters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class FocusPackage implements IFocusElement {
	
	@Override
	public String getResearch() {
		return null;
	}
	
	public World world;
	private LivingEntity caster;
	private UUID casterUUID;
	
	private float power = 1;
	private int complexity = 0;
	
	int index;
	UUID uid;
	
	public List<IFocusElement> nodes =  Collections.synchronizedList(new ArrayList<>());	
	
	public FocusPackage() {	}

	public FocusPackage(LivingEntity caster) {
		super();
		world = caster.level;
		this.caster = caster;
		casterUUID = caster.getUUID();
	}	
		
	@Override
	public String getKey() {
		return "thaumcraft.PACKAGE";
	}

	@Override
	public EnumUnitType getType() {
		return EnumUnitType.PACKAGE;
	}
	
	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	public UUID getUniqueID() {
		return uid;
	}

	public void setUniqueID(UUID id) {
		uid = id;
	}
	
	public int getExecutionIndex() {
		return index;
	}

	public void setExecutionIndex(int idx) {
		index = idx;
	}
	
	public void addNode(IFocusElement e) {
		nodes.add(e);
	}
	
	public UUID getCasterUUID() {
		if (caster!=null) casterUUID = caster.getUUID();
		return casterUUID;
	}

	public void setCasterUUID(UUID casterUUID) {
		this.casterUUID = casterUUID;
	}	
	
	public LivingEntity getCaster() {
		try {
			if (caster==null && world != null && casterUUID != null) {
				caster = (LivingEntity) world.getPlayerByUUID(getCasterUUID());
			}
			if (caster==null && world != null && casterUUID != null) {
				for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class,caster.getBoundingBox().inflate(0.5D), EntitySelectors.ENTITY_STILL_ALIVE)) {
					if (getCasterUUID().equals(e.getUUID())) {
						caster = e;
						break;
					}
				}
			}
		} catch (Exception e) {}
		return caster;
	}
	
	public FocusEffect[] getFocusEffects() {		
		return getFocusEffectsPackage(this);
	}
	
	private FocusEffect[] getFocusEffectsPackage(FocusPackage fp) {
		ArrayList<FocusEffect> out = new ArrayList<>();
		for (IFocusElement el:fp.nodes) {
			if (el instanceof FocusEffect) out.add((FocusEffect) el);
			else
			if (el instanceof FocusPackage) {
				for (FocusEffect fep:getFocusEffectsPackage((FocusPackage) el))
					out.add(fep);
			} else 
			if (el instanceof FocusModSplit) {
				for (FocusPackage fsp:((FocusModSplit)el).getSplitPackages())
					for (FocusEffect fep:getFocusEffectsPackage(fsp))
						out.add(fep);
			}
		}
		return out.toArray(new FocusEffect[]{});
	}

	public void deserialize(CompoundNBT nbt) {
		if (nbt.hasUUID("uid")) uid = nbt.getUUID("uid");
		index = nbt.getInt("index");
		if (nbt.hasUUID("casterUUID")) setCasterUUID(nbt.getUUID("casterUUID"));
		power = nbt.getFloat("power");
		complexity = nbt.getInt("complexity");
				
		ListNBT nodelist = nbt.getList("nodes", Constants.NBT.TAG_COMPOUND);
		nodes.clear();
		for (int x=0;x<nodelist.size();x++) {
			CompoundNBT nodenbt = nodelist.getCompound(x);
			EnumUnitType ut = EnumUnitType.valueOf(nodenbt.getString("type"));
			if (ut!=null) {
				if (ut==EnumUnitType.PACKAGE) {
					FocusPackage fp = new FocusPackage();
					fp.deserialize(nodenbt.getCompound("package"));
					nodes.add(fp);
					break;
				} else {
					IFocusElement fn = FocusEngine.getElement(nodenbt.getString("key")); 
					if (fn!=null) {						
						if (fn instanceof FocusNode) {
							((FocusNode)fn).initialize();
							if (((FocusNode)fn).getSettingList()!=null)
								for (String ns : ((FocusNode)fn).getSettingList()) {
									if (nodenbt.contains("setting." + ns, Constants.NBT.TAG_INT)) {
										((FocusNode)fn).getSetting(ns).setValue(nodenbt.getInt("setting." + ns));
									}
								}
							
							if (fn instanceof FocusModSplit) {								
								((FocusModSplit)fn).deserialize(nodenbt.getCompound("packages"));		
							}
						}
						addNode(fn);
					}
				}
			}
		}
		this.world = null;
	}

	public CompoundNBT serialize() {
		CompoundNBT nbt = new CompoundNBT();
		if (uid!=null) nbt.putUUID("uid", uid);
		nbt.putInt("index", index);
		if (getCasterUUID() != null) nbt.putUUID("casterUUID", getCasterUUID());
		if (world!=null) {
			nbt.putString("dim", world.dimension().location().toString());
		}
		nbt.putFloat("power", power);
		nbt.putInt("complexity", complexity);
		
		ListNBT nodelist = new ListNBT();
		synchronized (nodes) {
			for (IFocusElement node:nodes) {
				if (node==null || node.getType()==null) continue;
				CompoundNBT nodenbt = new CompoundNBT();
				nodenbt.putString("type", node.getType().name());
				nodenbt.putString("key", node.getKey());
				if (node.getType()==EnumUnitType.PACKAGE) {
					nodenbt.put("package", ((FocusPackage)node).serialize());
					nodelist.add(nodenbt);
					break;
				} else {				
					if (node instanceof FocusNode && ((FocusNode)node).getSettingList()!=null)
						for (String ns : ((FocusNode)node).getSettingList()) {
							nodenbt.putInt("setting."+ns, ((FocusNode)node).getSettingValue(ns));
						}
					if (node instanceof FocusModSplit) {	
						nodenbt.put("packages", ((FocusModSplit)node).serialize());	
					}
					nodelist.add(nodenbt);
				}			
			}
		}
		nbt.put("nodes", nodelist);					
		
		return nbt;
	}

	public float getPower() {
		return power;
	}

	public void multiplyPower(float pow) {
		power *= pow;
	}

	public FocusPackage copy(LivingEntity caster) {
		FocusPackage fp = new FocusPackage(caster);
		fp.deserialize(serialize());
		return fp;
	}
	
	public void initialize(LivingEntity caster) {
		this.world = caster.level;
		this.caster = caster;
		this.casterUUID = caster.getUUID();

		if (nodes != null && !nodes.isEmpty()) {
			IFocusElement node = nodes.get(0);
			if (node instanceof FocusMediumRoot) {
				((FocusMediumRoot)node).setupFromCaster(caster);
			}
		}
	}

	public int getSortingHelper() {
		String s="";
		for (IFocusElement k: nodes) {
			s+=k.getKey();
			if (k instanceof FocusNode && ((FocusNode)k).getSettingList()!=null)
				for (String ns : ((FocusNode)k).getSettingList()) {
					s += ""+((FocusNode)k).getSettingValue(ns);
				}
		}		
		return s.hashCode();
	}
}
