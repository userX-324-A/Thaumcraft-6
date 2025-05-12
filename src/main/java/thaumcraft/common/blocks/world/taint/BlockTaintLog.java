package thaumcraft.common.blocks.world.taint;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.registration.ModBlocks;

public class BlockTaintLog extends RotatedPillarBlock implements ITaintBlock
{
    public BlockTaintLog(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
    }
    
    public static AbstractBlock.Properties TaintLogProperties() {
        return AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_PURPLE)
                .strength(3.0f, 10.0f)
                .sound(SoundType.WOOD)
                .randomTicks();
    }
    
    @Override
    public SoundType getSoundType(BlockState state, IBlockReader world, BlockPos pos, net.minecraft.entity.Entity entity) {
        return super.getSoundType(state, world, pos, entity);
    }
    
    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 4;
    }
    
    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 4;
    }
    
    @Override
    public void die(World world, BlockPos pos, BlockState blockState) {
        if (ModBlocks.FLUX_GOO != null && ModBlocks.FLUX_GOO.get() != null) {
             world.setBlock(pos, ModBlocks.FLUX_GOO.get().defaultBlockState(), 3);
        } else {
            // Fallback or comment out if FLUX_GOO is not yet available
            // world.setBlock(pos, net.minecraft.block.Blocks.AIR.defaultBlockState(), 3);
        }
    }
    
    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClientSide) {
            if (!TaintHelper.isNearTaintSeed(world, pos)) {
                die(world, pos, state);
            } else {
                TaintHelper.spreadFibres(world, pos);
            }
        }
    }
    
    @Override
    protected ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }
    
    @Override
    public boolean canSustainLeaves(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }
    
    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            byte b0 = 4;
            int i = b0 + 1;
            if (worldIn.isAreaLoaded(pos.offset(-i, -i, -i), pos.offset(i, i, i))) {
                for (BlockPos blockpos1 : BlockPos.betweenClosed(pos.offset(-b0, -b0, -b0), pos.offset(b0, b0, b0))) {
                    BlockState iblockstate1 = worldIn.getBlockState(blockpos1);
                    if (iblockstate1.is(net.minecraftforge.common.Tags.Blocks.LEAVES)) {
                        iblockstate1.getBlock().randomTick(iblockstate1, (ServerWorld) worldIn, blockpos1, worldIn.random);
                    }
                }
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
    
    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}
