package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ArcaneFurnaceBlock extends Block {
    public ArcaneFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.ARCANE_FURNACE.get().create();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) { return true; }

    @Override
    public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof ArcaneFurnaceBlockEntity) {
            ArcaneFurnaceBlockEntity af = (ArcaneFurnaceBlockEntity) te;
            int progress = af.getCookTime();
            int total = Math.max(1, af.getCookTimeTotal());
            return Math.min(15, (int)Math.floor(15.0 * progress / total));
        }
        return 0;
    }
}

