package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ScanBlock implements IScanThing {
    private String research;
    
    public ScanBlock(String research) {
        this.research = research;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        if (!(obj instanceof BlockPos)) return false;
        ItemStack is = ScanningManager.getItemFromParms(player, obj);
        return is != null && !is.isEmpty();
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return research;
    }
}


