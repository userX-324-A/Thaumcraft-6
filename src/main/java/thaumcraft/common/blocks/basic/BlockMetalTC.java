package thaumcraft.common.blocks.basic;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import thaumcraft.common.blocks.BlockTC;

public class BlockMetalTC extends BlockTC
{
    public BlockMetalTC() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(4.0f, 10.0f)
                .sound(SoundType.METAL)
        );
    }
    
    @Override
    public boolean isBeaconBase(BlockState state, IBlockReader world, BlockPos pos, BlockPos beaconPos) {
        return true;
    }
}
