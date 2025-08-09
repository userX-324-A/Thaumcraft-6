package thaumcraft.api.research;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;

public class ScanBlockState implements IScanThing {
    private String research;
    private BlockState blockState;
    
    public ScanBlockState(BlockState blockState) {
        this("!" + blockState.getBlock().getRegistryName().toString(), blockState);
    }
    
    public ScanBlockState(String research, BlockState blockState) {
        this.research = research;
        this.blockState = blockState;
    }
    
    public ScanBlockState(String research, BlockState blockState, boolean item) {
        this.research = research;
        this.blockState = blockState;
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

