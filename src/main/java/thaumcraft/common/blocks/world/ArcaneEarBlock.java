package thaumcraft.common.blocks.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.RequestNoteMessage;

import javax.annotation.Nullable;

public class ArcaneEarBlock extends Block {
    public ArcaneEarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return thaumcraft.common.registers.ModBlockEntities.ARCANE_EAR.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (level.isClientSide) {
            NetworkHandler.sendToServer(new RequestNoteMessage(pos));
        }
        return ActionResultType.SUCCESS;
    }
}




