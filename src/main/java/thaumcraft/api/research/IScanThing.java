package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;

public interface IScanThing {
    boolean checkThing(PlayerEntity player, Object obj);
    
    String getResearchKey(PlayerEntity player, Object object);
    
    default void onSuccess(PlayerEntity player, Object object) {
    }
}


