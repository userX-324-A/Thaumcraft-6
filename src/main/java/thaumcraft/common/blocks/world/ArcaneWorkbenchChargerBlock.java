package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import thaumcraft.common.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.registers.ModBlocks;

public class ArcaneWorkbenchChargerBlock extends Block {
    public ArcaneWorkbenchChargerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.below());
        return below.getBlock() == ModBlocks.ARCANE_WORKBENCH.get();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!canSurvive(state, world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;
        BlockPos workbenchPos = pos.below();
        if (world.getBlockState(workbenchPos).getBlock() == ModBlocks.ARCANE_WORKBENCH.get()) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider((id, inv, p) ->
                            new ArcaneWorkbenchMenu(id, inv, IWorldPosCallable.create(world, workbenchPos)),
                            new TranslationTextComponent("container.arcaneworkbench")),
                    buf -> buf.writeBlockPos(workbenchPos));
            return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }
}


