package thaumcraft.common.lib.research;
import net.minecraft.entity.player.PlayerEntity;
import thaumcraft.api.research.IScanThing;
import net.minecraft.util.math.BlockPos;


public class ScanSky implements IScanThing {
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        if (obj != null) return false;
        // True if player can see sky and is outdoors enough
        BlockPos pos = player.blockPosition();
        return player.level.canSeeSky(pos);
    }

    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return "!SKY";
    }
}

