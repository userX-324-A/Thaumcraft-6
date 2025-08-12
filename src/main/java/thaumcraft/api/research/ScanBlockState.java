package thaumcraft.api.research;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

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
        if (!(obj instanceof BlockPos)) return false;
        BlockPos pos = (BlockPos) obj;
        BlockState current = player.level.getBlockState(pos);
        if (current == null) return false;
        if (!current.getBlock().equals(blockState.getBlock())) return false;
        ItemStack is = ScanningManager.getItemFromParms(player, obj);
        return is != null && !is.isEmpty();
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object object) {
        return research;
    }
}

