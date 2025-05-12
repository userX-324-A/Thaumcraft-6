package thaumcraft.common.blocks.world.plants;

import java.util.List;
import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;

public class BlockLeavesTC extends LeavesBlock {
    private final boolean isSilverwood;

    public BlockLeavesTC(boolean isSilverwood, AbstractBlock.Properties properties) {
        super(properties);
        this.isSilverwood = isSilverwood;
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(DISTANCE, 7)
            .setValue(PERSISTENT, Boolean.FALSE));
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 30;
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, World world, BlockPos pos1, BlockPos pos2, PlayerEntity player) {
        return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, player)
            .setValue(PERSISTENT, Boolean.TRUE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);
        if (this.isSilverwood && !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        Random random = builder.getLevel().random;

        if (!state.getValue(PERSISTENT) && random.nextInt(20) == 0) {
        }

        if (this.isSilverwood && !state.getValue(PERSISTENT) && random.nextInt( (int)(20 * 0.75) ) == 0) {
        }
        return drops;
    }

    @Override
    protected boolean decaying(BlockState state) {
        return !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(DISTANCE) == 7 && !state.getValue(PERSISTENT);
    }
}
