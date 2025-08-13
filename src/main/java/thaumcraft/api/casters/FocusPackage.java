package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.internal.CommonInternals;

public class FocusPackage implements IFocusPackage, IFocusElement {
    private World world;
    private LivingEntity caster;
    private Entity target;
    private UUID casterUUID;
    private UUID uid;
    private float power = 1.0f;
    private int complexity = 0;
    private int nodeIndex;
    private final List<IFocusElement> nodes;

    public FocusPackage() {
        this.nodes = Collections.synchronizedList(new ArrayList<>());
        this.nodeIndex = -1;
    }

    public FocusPackage(LivingEntity caster) {
        this();
        this.world = caster.level;
        this.caster = caster;
        this.casterUUID = caster.getUUID();
    }

    // IFocusElement
    @Override
    public String getKey() { return "thaumcraft.PACKAGE"; }
    @Override
    public String getResearch() { return null; }
    @Override
    public IFocusElement.EnumUnitType getType() { return IFocusElement.EnumUnitType.PACKAGE; }

    // IFocusPackage
    @Override
    public World getWorld() { return this.world; }
    @Override
    public LivingEntity getCaster() { return this.caster; }
    @Override
    public Entity getTarget() { return this.target != null && this.target.isAlive() ? this.target : null; }
    @Override
    public void setTarget(Entity target) { this.target = target; }
    @Override
    public List<IFocusElement> getNodes() { return this.nodes; }
    @Override
    public int getNodeIndex() { return this.nodeIndex; }
    @Override
    public void setNodeIndex(int nodeIndex) { this.nodeIndex = nodeIndex; }
    @Override
    public float getPower() { return this.power; }
    @Override
    public void multiplyPower(float pow) { this.power *= pow; }
    @Override
    public UUID getUniqueID() { return this.uid; }
    @Override
    public void setUniqueID(UUID id) { this.uid = id; }
    @Override
    public UUID getCasterUUID() { return this.casterUUID != null ? this.casterUUID : (this.caster != null ? this.caster.getUUID() : null); }
    @Override
    public void setCasterUUID(UUID id) { this.casterUUID = id; }

    @Override
    public void deserialize(CompoundNBT nbt) {
        this.uid = nbt.hasUUID("uid") ? nbt.getUUID("uid") : null;
        this.nodeIndex = nbt.getInt("index");
        this.casterUUID = nbt.hasUUID("casterUUID") ? nbt.getUUID("casterUUID") : null;
        this.power = nbt.getFloat("power");
        this.complexity = nbt.getInt("complexity");
        this.nodes.clear();
        ListNBT list = nbt.getList("nodes", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT nodeNBT = list.getCompound(i);
            IFocusElement unit;
            IFocusElement.EnumUnitType type = IFocusElement.EnumUnitType.valueOf(nodeNBT.getString("type"));
            if (type == IFocusElement.EnumUnitType.PACKAGE) {
                FocusPackage fp = new FocusPackage();
                fp.deserialize(nodeNBT.getCompound("package"));
                unit = fp;
                this.nodes.add(unit);
                break;
            } else {
                unit = FocusEngine.getElement(nodeNBT.getString("key"));
                if (unit != null) {
                    if (unit instanceof FocusNode) {
                        ((FocusNode) unit).initialize();
                        if (((FocusNode) unit).getSettingList() != null) {
                            for (String ns : ((FocusNode) unit).getSettingList()) {
                                ((FocusNode) unit).getSetting(ns).setValue(nodeNBT.getInt("setting." + ns));
                            }
                        }
                        if (unit instanceof FocusModSplit) {
                            ((FocusModSplit) unit).deserialize(nodeNBT.getCompound("packages"));
                        }
                    }
                    this.nodes.add(unit);
                }
            }
        }
    }

    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.uid != null) nbt.putUUID("uid", this.uid);
        nbt.putInt("index", this.nodeIndex);
        if (getCasterUUID() != null) nbt.putUUID("casterUUID", getCasterUUID());
        nbt.putFloat("power", this.power);
        nbt.putInt("complexity", this.complexity);
        ListNBT nodelist = new ListNBT();
        synchronized (nodes) {
            for (IFocusElement node : nodes) {
                if (node == null || node.getType() == null) continue;
                CompoundNBT nodenbt = new CompoundNBT();
                nodenbt.putString("type", node.getType().name());
                nodenbt.putString("key", node.getKey());
                if (node.getType() == IFocusElement.EnumUnitType.PACKAGE) {
                    nodenbt.put("package", ((FocusPackage) node).serialize());
                    nodelist.add(nodenbt);
                    break;
                } else {
                    if (node instanceof FocusNode && ((FocusNode) node).getSettingList() != null) {
                        for (String ns : ((FocusNode) node).getSettingList()) {
                            nodenbt.putInt("setting." + ns, ((FocusNode) node).getSettingValue(ns));
                        }
                    }
                    if (node instanceof FocusModSplit) {
                        nodenbt.put("packages", ((FocusModSplit) node).serialize());
                    }
                    nodelist.add(nodenbt);
                }
            }
        }
        nbt.put("nodes", nodelist);
        return nbt;
    }

    @Override
    public FocusPackage copy(LivingEntity caster) {
        FocusPackage fp = new FocusPackage(caster);
        fp.deserialize(this.serialize());
        return fp;
    }

    @Override
    public void initialize(LivingEntity caster) {
        this.world = caster.level;
        this.caster = caster;
        if (!this.nodes.isEmpty()) {
            IFocusElement node = nodes.get(0);
            if (node instanceof FocusMediumRoot && ((FocusMediumRoot) node).supplyTargets() == null) {
                ((FocusMediumRoot) node).setupFromCaster(caster);
            }
        }
    }
}

