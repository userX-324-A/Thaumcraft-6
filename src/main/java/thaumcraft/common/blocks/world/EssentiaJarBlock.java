package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EssentiaJarBlock extends Block {
    public static final BooleanProperty BRACED = BooleanProperty.create("braced");
    public EssentiaJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(BRACED, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.ESSENTIA_JAR.get().create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BRACED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        net.minecraft.item.ItemStack stack = ctx.getItemInHand();
        boolean braced = false;
        if (stack.hasTag()) {
            net.minecraft.nbt.CompoundNBT tag = stack.getTag();
            if (tag != null) {
                net.minecraft.nbt.CompoundNBT bst = tag.getCompound("BlockStateTag");
                if (bst.contains("braced")) braced = "true".equalsIgnoreCase(bst.getString("braced"));
            }
        }
        return defaultBlockState().setValue(BRACED, braced);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, net.minecraft.util.math.BlockRayTraceResult hit) {
        if (level.isClientSide) return ActionResultType.SUCCESS;
        TileEntity te = level.getBlockEntity(pos);
        if (te instanceof EssentiaJarBlockEntity) {
            EssentiaJarBlockEntity jar = (EssentiaJarBlockEntity) te;
            if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty() && jar.hasBrace()) {
                // Remove brace and drop an item
                jar.setBrace(false);
                net.minecraft.item.ItemStack drop = new net.minecraft.item.ItemStack(thaumcraft.common.registers.ModItems.JAR_BRACE.get());
                net.minecraft.entity.item.ItemEntity ent = new net.minecraft.entity.item.ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, drop);
                level.addFreshEntity(ent);
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = level.getBlockEntity(pos);
            if (te instanceof EssentiaJarBlockEntity) {
                EssentiaJarBlockEntity jar = (EssentiaJarBlockEntity) te;
                if (jar.hasBrace()) {
                    net.minecraft.item.ItemStack drop = new net.minecraft.item.ItemStack(thaumcraft.common.registers.ModItems.JAR_BRACE.get());
                    net.minecraft.entity.item.ItemEntity ent = new net.minecraft.entity.item.ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, drop);
                    level.addFreshEntity(ent);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
