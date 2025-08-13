package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
    public ResearchTableBlock(Properties properties) { super(properties); }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.RESEARCH_TABLE.get().create();
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
