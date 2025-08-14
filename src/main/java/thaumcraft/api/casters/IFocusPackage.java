package thaumcraft.api.casters;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IFocusPackage {
    World getWorld();
    
    LivingEntity getCaster();
    
    Entity getTarget();
    
    void setTarget(Entity target);
    
    List<IFocusElement> getNodes();
    
    int getNodeIndex();
    
    void setNodeIndex(int nodeIndex);

    float getPower();

    void multiplyPower(float pow);

    java.util.UUID getUniqueID();

    void setUniqueID(java.util.UUID id);

    java.util.UUID getCasterUUID();

    void setCasterUUID(java.util.UUID id);
    
    void deserialize(CompoundNBT nbt);
    
    CompoundNBT serialize();
    
    FocusPackage copy(LivingEntity caster);
    
    void initialize(LivingEntity caster);
} 

