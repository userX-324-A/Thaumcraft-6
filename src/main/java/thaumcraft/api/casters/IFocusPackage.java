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
    
    List<FocusEffect> getNodes();
    
    int getNodeIndex();
    
    void setNodeIndex(int nodeIndex);
    
    void deserialize(CompoundNBT nbt);
    
    CompoundNBT serialize();
    
    FocusPackage copy(LivingEntity caster);
    
    void initialize(LivingEntity caster);
} 
