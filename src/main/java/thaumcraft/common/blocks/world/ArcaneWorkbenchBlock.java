package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import thaumcraft.common.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.registers.ModBlockEntities;

public class ArcaneWorkbenchBlock extends Block {
    public ArcaneWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlockEntities.ARCANE_WORKBENCH.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;
        NetworkHooks.openGui((ServerPlayerEntity) player,
                new SimpleNamedContainerProvider((id, inv, p) ->
                        new ArcaneWorkbenchMenu(id, inv, IWorldPosCallable.create(world, pos)),
                        new TranslationTextComponent("container.arcaneworkbench")),
                buf -> buf.writeBlockPos(pos));
        return ActionResultType.CONSUME;
    }
}

