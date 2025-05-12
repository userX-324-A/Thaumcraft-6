package thaumcraft.common.blocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.lib.utils.InventoryUtils;


public abstract class BlockTCTile extends BlockTC
{
    protected final Class<? extends TileEntity> tileClass;
    protected static boolean keepInventory;
    protected static boolean spillEssentia;
    
    public BlockTCTile(AbstractBlock.Properties properties, Class<? extends TileEntity> tc) {
        super(properties);
        this.tileClass = tc;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (tileClass == null) {
            return null;
        }
        try {
            return tileClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Thaumcraft.LOGGER.error("Failed to create TileEntity of type " + tileClass.getName(), e);
            return null;
        }
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            InventoryUtils.dropItems(worldIn, pos);
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof IEssentiaTransport && BlockTCTile.spillEssentia && !worldIn.isClientSide) {
                int ess = ((IEssentiaTransport) tileentity).getEssentiaAmount(Direction.UP);
                if (ess > 0) {
                    AuraHelper.polluteAura(worldIn, pos, (float) ess, true);
                }
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
            worldIn.removeBlockEntity(pos);
        } else {
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
    
    @Override
    public boolean triggerEvent(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(id, param);
    }
    
    static {
        BlockTCTile.keepInventory = false;
        BlockTCTile.spillEssentia = true;
    }
}
