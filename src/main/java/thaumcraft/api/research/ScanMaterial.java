package thaumcraft.api.research;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;

public class ScanMaterial implements IScanThing {
    private String research;
    private Material mat;
    
    public ScanMaterial(String research, Material mat) {
        this.research = research;
        this.mat = mat;
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

