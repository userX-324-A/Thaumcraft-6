package thaumcraft.common.blocks.basic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliserExt;
import thaumcraft.common.blocks.BlockTC;

import java.util.Random;

public class BlockCandle extends BlockTC implements IInfusionStabiliserExt {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D);
    public final DyeColor dye;

    public BlockCandle(String name, DyeColor dye) {
        super(Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0.1F).sound(SoundType.CLOTH).lightValue(15));
        this.setRegistryName(name);
        this.dye = dye;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean canStabaliseInfusion(World world, BlockPos pos) {
        return true;
    }

    @Override
    public float getStabilizationAmount(World world, BlockPos pos) {
        return 0.1F;
    }

    @Override
    public boolean hasSymmetryPenalty(World world, BlockPos pos1, BlockPos pos2) {
        return false;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        return 0.0F;
    }
}

