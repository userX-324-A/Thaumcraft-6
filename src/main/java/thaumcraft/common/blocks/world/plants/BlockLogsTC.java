package thaumcraft.common.blocks.world.plants;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockLogsTC extends RotatedPillarBlock {

    private final boolean isSilverwood;

    public BlockLogsTC(boolean isSilverwood, MaterialColor topColor, MaterialColor barkColor) {
        super(AbstractBlock.Properties.of(Material.WOOD, state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor)
                .strength(2.0F, 5.0F)
                .sound(SoundType.WOOD)
                .harvestTool(ToolType.AXE)
                .harvestLevel(0)
                .flammable(5, 5) // fireSpreadSpeed, flammability
        );
        this.isSilverwood = isSilverwood;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return this.isSilverwood ? 5 : super.getLightValue(state, world, pos);
    }

    @Override
    public boolean canSustainLeaves(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) { // Only proceed if the block is actually changing to something else
            int i = 4; // Search radius for leaves
            int j = i + 1;
            if (worldIn.isAreaLoaded(pos.offset(-j, -j, -j), pos.offset(j, j, j))) {
                for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-i, -i, -i), pos.offset(i, i, i))) {
                    BlockState iblockstate = worldIn.getBlockState(blockpos);
                    if (iblockstate.isSuffocating(worldIn, blockpos)) { // A simple check, might need refinement for specific leaf blocks
                        // In 1.16.5, leaf decay is typically handled by the leaf blocks themselves
                        // when they receive a block update and find no logs nearby.
                        // However, forcing an update can sometimes be useful.
                        worldIn.neighborChanged(blockpos, this, pos);
                    }
                }
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
