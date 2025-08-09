package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ScanEntity implements IScanThing {
    private String research;
    private Class entityClass;
    private boolean inherited;
    private CompoundNBT nbt;
    
    public ScanEntity(String research, Class entityClass, boolean inherited) {
        this.research = research;
        this.entityClass = entityClass;
        this.inherited = inherited;
    }
    
    public ScanEntity(String research, Class entityClass, boolean inherited, CompoundNBT nbt) {
        this.research = research;
        this.entityClass = entityClass;
        this.inherited = inherited;
        this.nbt = nbt;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        return false;
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return null;
    }
}

