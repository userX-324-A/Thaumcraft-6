package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;

public class ScanBlock implements IScanThing {
    private String research;
    
    public ScanBlock(String research) {
        this.research = research;
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

