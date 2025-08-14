package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public class HungryChestBlock extends Block {
    public HungryChestBlock(Properties properties) { super(properties); }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.HUNGRY_CHEST.get().create();
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof HungryChestBlockEntity) {
                ((HungryChestBlockEntity) te).dropAllContents();
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}


