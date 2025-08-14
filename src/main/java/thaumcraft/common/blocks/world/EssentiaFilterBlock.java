package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EssentiaFilterBlock extends Block {
    public EssentiaFilterBlock(Properties properties) { super(properties); }

    private static final VoxelShape SHAPE = Block.box(4, 4, 4, 12, 12, 12);

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

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.ESSENTIA_FILTER.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;
        TileEntity be = world.getBlockEntity(pos);
        if (be instanceof EssentiaFilterBlockEntity) {
            ItemStack held = player.getItemInHand(hand);
            thaumcraft.api.aspects.Aspect chosen = null;
            if (!held.isEmpty() && held.getItem() instanceof thaumcraft.api.aspects.IEssentiaContainerItem) {
                thaumcraft.api.aspects.AspectList list = ((thaumcraft.api.aspects.IEssentiaContainerItem) held.getItem()).getAspects(held);
                if (list != null && list.size() > 0) chosen = list.getAspects()[0];
            }
            ((EssentiaFilterBlockEntity) be).setFilter(chosen);
            world.sendBlockUpdated(pos, state, state, 3);
            return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }
}




