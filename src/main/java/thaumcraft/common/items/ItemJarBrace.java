package thaumcraft.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.PlayerEntity;
import thaumcraft.common.blocks.world.EssentiaJarBlockEntity;

public class ItemJarBrace extends Item {
    public ItemJarBrace(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        World level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        PlayerEntity player = ctx.getPlayer();
        if (level.isClientSide) return ActionResultType.SUCCESS;
        TileEntity te = level.getBlockEntity(pos);
        if (te instanceof EssentiaJarBlockEntity) {
            EssentiaJarBlockEntity jar = (EssentiaJarBlockEntity) te;
            if (!jar.hasBrace()) {
                jar.setBrace(true);
                if (player == null || !player.isCreative()) {
                    ctx.getItemInHand().shrink(1);
                }
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }
}


