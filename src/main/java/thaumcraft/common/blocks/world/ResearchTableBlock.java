package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.fml.network.NetworkHooks;
import thaumcraft.common.menu.ResearchTableMenu;
import javax.annotation.Nullable;

public class ResearchTableBlock extends Block implements IForgeBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public ResearchTableBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private static final VoxelShape TABLETOP = Block.box(0, 12, 0, 16, 16, 16);
    private static final VoxelShape LEG_NW = Block.box(1, 0, 1, 3, 12, 3);
    private static final VoxelShape LEG_NE = Block.box(13, 0, 1, 15, 12, 3);
    private static final VoxelShape LEG_SW = Block.box(1, 0, 13, 3, 12, 15);
    private static final VoxelShape LEG_SE = Block.box(13, 0, 13, 15, 12, 15);
    private static final VoxelShape SHAPE = VoxelShapes.or(TABLETOP, LEG_NW, LEG_NE, LEG_SW, LEG_SE);

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
        return thaumcraft.common.registers.ModBlockEntities.RESEARCH_TABLE.get().create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;
        NetworkHooks.openGui((ServerPlayerEntity) player,
                new net.minecraft.inventory.container.SimpleNamedContainerProvider((id, inv, p) ->
                        new ResearchTableMenu(id, inv, IWorldPosCallable.create(world, pos)),
                        new net.minecraft.util.text.TranslationTextComponent("container.research_table")),
                buf -> buf.writeBlockPos(pos));
        return ActionResultType.CONSUME;
    }
}

