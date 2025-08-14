package thaumcraft.common.lib.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.items.ItemsTC;



public class BlockUtils {
    static BlockPos lastPos;
    static int lasty;
    static int lastz;
    static double lastdistance;
    public static ArrayList<String> portableHoleBlackList;
    
    private static boolean removeBlock(PlayerEntity player, BlockPos pos) {
        return removeBlock(player, pos, false);
    }
    
    private static boolean removeBlock(PlayerEntity player, BlockPos pos, boolean canHarvest) {
        return player.level.removeBlock(pos, false);
    }
    
    public static boolean harvestBlockSkipCheck(World world, PlayerEntity player, BlockPos pos) {
        return harvestBlock(world, player, pos, false, false, 0, true);
    }
    
    public static boolean harvestBlock(World world, PlayerEntity player, BlockPos pos) {
        return harvestBlock(world, player, pos, false, false, 0, false);
    }
    
    public static boolean harvestBlock(World world, PlayerEntity p, BlockPos pos, boolean alwaysDrop, boolean silkOverride, int fortuneOverride, boolean skipEvent) {
        if (world.isClientSide || !(p instanceof ServerPlayerEntity)) {
            return false;
        }
        ServerPlayerEntity player = (ServerPlayerEntity)p;
        int exp = skipEvent ? 0 : ForgeHooks.onBlockBreakEvent(world, player.gameMode.getGameModeForPlayer(), player, pos);
        if (exp == -1) {
            return false;
        }
        net.minecraft.block.BlockState iblockstate = world.getBlockState(pos);
        TileEntity tileentity = world.getBlockEntity(pos);
        Block block = iblockstate.getBlock();
        if ((block == Blocks.COMMAND_BLOCK || block == Blocks.STRUCTURE_BLOCK) && !player.canUseGameMasterBlocks()) {
            world.sendBlockUpdated(pos, iblockstate, iblockstate, 3);
            return false;
        }
        world.levelEvent(null, 2001, pos, Block.getId(iblockstate));
        boolean flag1 = false;
        if (player.gameMode.isCreative()) {
            flag1 = removeBlock(player, pos);
            player.connection.send(new SChangeBlockPacket(world, pos));
        } else {
            ItemStack itemstack1 = player.getMainHandItem();
            boolean flag2 = alwaysDrop || net.minecraftforge.common.ForgeHooks.canHarvestBlock(iblockstate, player, world, pos);
            if (flag2) {
                ItemStack fakeStack = itemstack1.copy();
                Map<Enchantment, Integer> enchMap = EnchantmentHelper.getEnchantments(itemstack1);
                if (silkOverride) enchMap.put(Enchantments.SILK_TOUCH, 1);
                int fort = Math.max(fortuneOverride, enchMap.getOrDefault(Enchantments.BLOCK_FORTUNE, 0));
                if (fort > 0) enchMap.put(Enchantments.BLOCK_FORTUNE, fort);
                EnchantmentHelper.setEnchantments(enchMap, fakeStack);
                iblockstate.getBlock().playerDestroy(world, player, pos, iblockstate, tileentity, fakeStack);
            }
            flag1 = world.removeBlock(pos, false);
        }
        if (!player.gameMode.isCreative() && flag1 && exp > 0) {
            iblockstate.getBlock().popExperience((ServerWorld) world, pos, exp);
        }
        return flag1;
    }
    
    public static void destroyBlockPartially(World world, int breakerId, BlockPos pos, int progress) {}
    
    public static void findBlocks(World world, BlockPos pos, net.minecraft.block.BlockState block, int reach) {
        for (int xx = -reach; xx <= reach; ++xx) {
            for (int yy = reach; yy >= -reach; --yy) {
                for (int zz = -reach; zz <= reach; ++zz) {
                    if (Math.abs(BlockUtils.lastPos.getX() + xx - pos.getX()) > 24) {
                        return;
                    }
                    if (Math.abs(BlockUtils.lastPos.getY() + yy - pos.getY()) > 48) {
                        return;
                    }
                    if (Math.abs(BlockUtils.lastPos.getZ() + zz - pos.getZ()) > 24) {
                        return;
                    }
                    net.minecraft.block.BlockState bs = world.getBlockState(BlockUtils.lastPos.offset(xx, yy, zz));
                    boolean same = bs.getBlock() == block.getBlock();
                    if (same && bs.getDestroySpeed(world, BlockUtils.lastPos.offset(xx, yy, zz)) >= 0.0f) {
                        double xd = BlockUtils.lastPos.getX() + xx - pos.getX();
                        double yd = BlockUtils.lastPos.getY() + yy - pos.getY();
                        double zd = BlockUtils.lastPos.getZ() + zz - pos.getZ();
                        double d = xd * xd + yd * yd + zd * zd;
                        if (d > BlockUtils.lastdistance) {
                            BlockUtils.lastdistance = d;
                            BlockUtils.lastPos = BlockUtils.lastPos.offset(xx, yy, zz);
                            findBlocks(world, pos, block, reach);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    public static boolean breakFurthestBlock(World world, BlockPos pos, net.minecraft.block.BlockState block, PlayerEntity player) {
        BlockUtils.lastPos = new BlockPos(pos);
        BlockUtils.lastdistance = 0.0;
        int reach = isWoodLog(world, pos) ? 2 : 1;
        findBlocks(world, pos, block, reach);
        boolean worked = harvestBlockSkipCheck(world, player, BlockUtils.lastPos);
        world.sendBlockUpdated(pos, block, block, 3);
        return worked;
    }

    private static boolean isWoodLog(World world, BlockPos pos) {
        net.minecraft.block.BlockState state = world.getBlockState(pos);
        return state.is(BlockTags.LOGS) || state.getMaterial() == Material.WOOD;
    }
    
    public static BlockRayTraceResult getTargetBlock(World world, Entity entity, boolean par3) {
        return getTargetBlock(world, entity, par3, par3, 10.0);
    }
    
    public static BlockRayTraceResult getTargetBlock(World world, Entity entity, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, double range) {
        float var4 = 1.0f;
        float var5 = entity.xRotO + (entity.xRot - entity.xRotO) * var4;
        float var6 = entity.yRotO + (entity.yRot - entity.yRotO) * var4;
        double var7 = entity.xo + (entity.getX() - entity.xo) * var4;
        double var8 = entity.yo + (entity.getY() - entity.yo) * var4 + entity.getEyeHeight();
        double var9 = entity.zo + (entity.getZ() - entity.zo) * var4;
        Vector3d var10 = new Vector3d(var7, var8, var9);
        float var11 = MathHelper.cos(-var6 * 0.017453292f - 3.1415927f);
        float var12 = MathHelper.sin(-var6 * 0.017453292f - 3.1415927f);
        float var13 = -MathHelper.cos(-var5 * 0.017453292f);
        float var14 = MathHelper.sin(-var5 * 0.017453292f);
        float var15 = var12 * var13;
        float var16 = var11 * var13;
        Vector3d var17 = var10.add(var15 * range, var14 * range, var16 * range);
        return world.clip(new RayTraceContext(var10, var17, RayTraceContext.BlockMode.COLLIDER, stopOnLiquid ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, entity));
    }
    
    public static int countExposedSides(World world, BlockPos pos) {
        int count = 0;
        for (Direction dir : Direction.values()) {
            if (world.isEmptyBlock(pos.relative(dir))) {
                ++count;
            }
        }
        return count;
    }
    
    public static boolean isBlockExposed(World world, BlockPos pos) {
        for (Direction face : Direction.values()) {
            if (!net.minecraft.block.Block.isFaceFull(world.getBlockState(pos.relative(face)).getCollisionShape(world, pos.relative(face)), face.getOpposite())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAdjacentToSolidBlock(World world, BlockPos pos) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.relative(face)).isFaceSturdy(world, pos.relative(face), face.getOpposite())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockTouching(IBlockReader world, BlockPos pos, net.minecraft.block.BlockState bs) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.relative(face)) == bs) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockTouching(IBlockReader world, BlockPos pos, Block bs) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.relative(face)).getBlock() == bs) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockTouching(IBlockReader world, BlockPos pos, Material mat, boolean solid) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.relative(face)).getMaterial() == mat && (!solid || world.getBlockState(pos.relative(face)).isFaceSturdy(world, pos.relative(face), face.getOpposite()))) {
                return true;
            }
        }
        return false;
    }
    
    public static Direction getFaceBlockTouching(IBlockReader world, BlockPos pos, Block bs) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.relative(face)).getBlock() == bs) {
                return face;
            }
        }
        return null;
    }
    
    public static boolean isPortableHoleBlackListed(net.minecraft.block.BlockState blockstate) {
        return isBlockListed(blockstate, BlockUtils.portableHoleBlackList);
    }
    
    public static boolean isBlockListed(net.minecraft.block.BlockState blockstate, List<String> list) {
        String stateString = blockstate.toString();
        for (String key : list) {
            String[] splitString = key.split(";");
            if (splitString[0].contains(":")) {
                if (!blockstate.getBlock().getRegistryName().toString().equals(splitString[0])) {
                    continue;
                }
                if (splitString.length <= 1) {
                    return true;
                }
                int matches = 0;
                for (int a = 1; a < splitString.length; ++a) {
                    if (stateString.contains(splitString[a])) {
                        ++matches;
                    }
                }
                if (matches == splitString.length - 1) {
                    return true;
                }
                continue;
            }
            else {
                ItemStack bs = new ItemStack(blockstate.getBlock().asItem());
                NonNullList<ItemStack> candidates = TagUtils.getItemsForOreName(splitString[0]);
                for (ItemStack stack : candidates) {
                    if (stack.getItem() == bs.getItem()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static double distance(BlockPos b1, BlockPos b2) {
        double d3 = b1.getX() - b2.getX();
        double d4 = b1.getY() - b2.getY();
        double d5 = b1.getZ() - b2.getZ();
        return d3 * d3 + d4 * d4 + d5 * d5;
    }
    
    public static Direction.Axis getBlockAxis(World world, BlockPos pos) {
        net.minecraft.block.BlockState state = world.getBlockState(pos);
        Direction.Axis ax = Direction.Axis.Y;
        for (net.minecraft.state.Property<?> prop : state.getProperties()) {
            if (prop.getName().equals("axis")) {
                Object val = state.getValue(prop);
                if (val instanceof Direction.Axis) {
                    ax = (Direction.Axis) val;
                    break;
                }
            }
        }
        return ax == null ? Direction.Axis.Y : ax;
    }
    
    public static boolean hasLOS(World world, BlockPos source, BlockPos target) {
        Vector3d a = new Vector3d(source.getX() + 0.5, source.getY() + 0.5, source.getZ() + 0.5);
        Vector3d b = new Vector3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
        net.minecraft.util.math.RayTraceResult mop = world.clip(new RayTraceContext(a, b, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null));
        return mop.getType() == net.minecraft.util.math.RayTraceResult.Type.MISS ||
                (mop.getType() == net.minecraft.util.math.RayTraceResult.Type.BLOCK && ((BlockRayTraceResult)mop).getBlockPos().equals(target));
    }
    
    public static ItemStack getSilkTouchDrop(net.minecraft.block.BlockState bs) {
        ItemStack dropped = ItemStack.EMPTY;
        try {
            // Not applicable in 1.16 API; return default clone of block item
            dropped = new ItemStack(bs.getBlock().asItem());
        }
        catch (Exception e) {
            Thaumcraft.LOGGER.warn("Could not invoke net.minecraft.block.Block method getSilkTouchDrop");
        }
        return dropped;
    }
    
    static {
        BlockUtils.lastPos = BlockPos.ZERO;
        BlockUtils.lasty = 0;
        BlockUtils.lastz = 0;
        BlockUtils.lastdistance = 0.0;
        BlockUtils.portableHoleBlackList = new ArrayList<String>();
    }
    
    public static class BlockPosComparator implements Comparator<BlockPos> {
        private BlockPos source;
        
        public BlockPosComparator(BlockPos source) {
            this.source = source;
        }
        
        @Override
        public int compare(BlockPos a, BlockPos b) {
            if (a.equals(b)) {
                return 0;
            }
            double da = source.distSqr(a);
            double db = source.distSqr(b);
            return (da < db) ? -1 : ((da > db) ? 1 : 0);
        }
    }
}



