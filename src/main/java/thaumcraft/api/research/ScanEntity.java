package thaumcraft.api.research;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ScanEntity implements IScanThing {
    private String research;
    private Class<? extends Entity> entityClass;
    private boolean inherited;
    private CompoundNBT nbt;
    
    public ScanEntity(String research, Class<? extends Entity> entityClass, boolean inherited) {
        this.research = research;
        this.entityClass = entityClass;
        this.inherited = inherited;
    }
    
    public ScanEntity(String research, Class<? extends Entity> entityClass, boolean inherited, CompoundNBT nbt) {
        this.research = research;
        this.entityClass = entityClass;
        this.inherited = inherited;
        this.nbt = nbt;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        if (!(obj instanceof Entity)) return false;
        Entity e = (Entity) obj;
        if (inherited) {
            if (!entityClass.isAssignableFrom(e.getClass())) return false;
        } else {
            if (e.getClass() != entityClass) return false;
        }
        if (nbt != null) {
            CompoundNBT tag = e.saveWithoutId(new CompoundNBT());
            for (String k : nbt.getAllKeys()) {
                if (!tag.contains(k)) return false;
                if (!tag.get(k).equals(nbt.get(k))) return false;
            }
        }
        return true;
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return research;
    }
}

