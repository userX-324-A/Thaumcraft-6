package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.internal.CommonInternals;

public class FocusPackage implements IFocusPackage {
    private World world;
    private LivingEntity caster;
    private Entity target;
    private List<FocusEffect> nodes;
    private int nodeIndex;
    
    public FocusPackage() {
        this.nodes = new ArrayList<FocusEffect>();
        this.nodeIndex = -1;
    }
    
    public FocusPackage(LivingEntity caster) {
        this();
        this.world = caster.getEntityWorld();
        this.caster = caster;
    }
    
    @Override
    public World getWorld() {
        return this.world;
    }
    
    @Override
    public LivingEntity getCaster() {
        return this.caster;
    }
    
    @Override
    public Entity getTarget() {
        if (this.target == null) {
            return null;
        }
        if (this.target.isAlive()) {
            return this.target;
        }
        return null;
    }
    
    @Override
    public void setTarget(Entity target) {
        this.target = target;
    }
    
    @Override
    public List<FocusEffect> getNodes() {
        return this.nodes;
    }
    
    @Override
    public int getNodeIndex() {
        return this.nodeIndex;
    }
    
    @Override
    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        this.world = null;
        if (nbt.hasUniqueId("casterUUID")) {
            // this.caster = world.getPlayerByUuid(nbt.getUniqueId("casterUUID"));
        }
        if (nbt.hasUniqueId("targetUUID")) {
            // this.target = world.getPlayerByUuid(nbt.getUniqueId("targetUUID"));
        }
        ListNBT nodeList = nbt.getList("nodes", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nodeList.size(); ++i) {
            CompoundNBT nodeNBT = nodeList.getCompound(i);
            FocusEffect node = CommonInternals.focusEffects.get(nodeNBT.getString("key"));
            if (node != null) {
                node.deserialize(nodeNBT);
                this.nodes.add(node);
            }
        }
    }
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.caster != null) {
            nbt.putUniqueId("casterUUID", this.caster.getUniqueID());
        }
        if (this.target != null) {
            nbt.putUniqueId("targetUUID", this.target.getUniqueID());
        }
        ListNBT nodeList = new ListNBT();
        for (FocusEffect node : this.nodes) {
            CompoundNBT nodeNBT = new CompoundNBT();
            nodeNBT.putString("key", node.getKey());
            node.serialize(nodeNBT);
            nodeList.add(nodeNBT);
        }
        nbt.put("nodes", nodeList);
        return nbt;
    }
    
    @Override
    public FocusPackage copy(LivingEntity caster) {
        FocusPackage newPackage = new FocusPackage(caster);
        newPackage.target = this.target;
        for (FocusEffect node : this.nodes) {
            newPackage.nodes.add(node.copy());
        }
        return newPackage;
    }
    
    @Override
    public void initialize(LivingEntity caster) {
        this.world = caster.getEntityWorld();
        this.caster = caster;
    }
}

