package thaumcraft.api.research;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import thaumcraft.api.aspects.Aspect;

public class ScanAspect implements IScanThing {
    private Aspect aspect;
    
    public ScanAspect(String research, Aspect aspect) {
        this.aspect = aspect;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        return false;
    }
    
    @Override
    public void onSuccess(PlayerEntity player, Object obj) {
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return null;
    }
}

