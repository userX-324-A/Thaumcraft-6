package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class EssentiaPumpBlock extends Block implements IForgeBlock {
    public EssentiaPumpBlock(Properties properties) { super(properties); }

    private static final VoxelShape BODY = Block.box(3, 0, 3, 13, 10, 13);
    private static final VoxelShape PIPE = Block.box(6, 10, 6, 10, 16, 10);
    private static final VoxelShape SHAPE = VoxelShapes.or(BODY, PIPE);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Override
    public @Nullable TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.ESSENTIA_PUMP.get().create();
    }
}





