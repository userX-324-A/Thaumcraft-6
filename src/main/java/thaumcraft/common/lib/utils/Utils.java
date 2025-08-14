package thaumcraft.common.lib.utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
// import java.io.DataInput;
// import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
// import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
// import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.entity.player.ServerPlayerEntity;
// import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
// import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
// import net.minecraft.util.Hand;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
// Vec3d replaced by Vector3d in 1.16.5
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
// import net.minecraftforge.fml.common.network.NetworkRegistry; // not used in 1.16.5 path
import net.minecraftforge.common.Tags;
import net.minecraft.tags.BlockTags;
import thaumcraft.api.internal.WeightedRandomLoot;
// import thaumcraft.api.items.ItemsTC;
// Removed CodeChickenLib AABB rotation dependency for now
// import thaumcraft.common.network.NetworkHandler; // 1.16.5 channel (not used here yet)
// import thaumcraft.common.network.msg.ClientBiomeChangeMessage;


public class Utils
{
    public static HashMap<List<Object>, ItemStack> specialMiningResult;
    public static HashMap<List<Object>, Float> specialMiningChance;
    public static String[] colorNames;
    public static int[] colors;
    public static ArrayList<List> oreDictLogs;
    
    public static boolean isChunkLoaded(World world, int x, int z) {
        return world.hasChunk(x >> 4, z >> 4);
    }
    
    public static boolean useBonemealAtLoc(World world, PlayerEntity player, BlockPos pos) {
        ItemStack stack = new ItemStack(Items.BONE_MEAL);
        return BoneMealItem.applyBonemeal(stack, world, pos, player);
    }
    
    public static boolean hasColor(byte[] colors) {
        for (byte col : colors) {
            if (col >= 0) {
                return true;
            }
        }
        return false;
    }
    
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0L, source.size());
        }
        finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
    
    public static void addSpecialMiningResult(ItemStack in, ItemStack out, float chance) {
        Utils.specialMiningResult.put(Arrays.asList(in.getItem(), in.getDamageValue()), out);
        Utils.specialMiningChance.put(Arrays.asList(in.getItem(), in.getDamageValue()), chance);
    }
    
    public static ItemStack findSpecialMiningResult(ItemStack is, float chance, Random rand) {
        ItemStack dropped = is.copy();
        float r = rand.nextFloat();
        List<Object> ik = Arrays.asList(is.getItem(), is.getDamageValue());
        if (Utils.specialMiningResult.containsKey(ik) && r <= chance * Utils.specialMiningChance.get(ik)) {
            dropped = Utils.specialMiningResult.get(ik).copy();
            dropped.setCount(dropped.getCount() * is.getCount());
        }
        return dropped;
    }
    
    public static float clamp_float(float par0, float par1, float par2) {
        return (par0 < par1) ? par1 : ((par0 > par2) ? par2 : par0);
    }
    
    public static void setBiomeAt(World world, BlockPos pos, Biome biome) {
        setBiomeAt(world, pos, biome, true);
    }
    
    public static void setBiomeAt(World world, BlockPos pos, Biome biome, boolean sync) {
        if (biome == null) {
            return;
        }
        Chunk chunk = world.getChunk(pos.getX() >> 4, pos.getZ() >> 4); // reserved for future per-chunk biome ops
        if (sync && !world.isClientSide && world instanceof net.minecraft.world.server.ServerWorld) {
            net.minecraft.world.server.ServerWorld sw = (net.minecraft.world.server.ServerWorld) world;
            thaumcraft.common.network.NetworkHandler.sendToAllAround(
                    new thaumcraft.common.network.msg.ClientBiomeChangeMessage(pos, netForgeBiomeId(biome)),
                    sw,
                    pos,
                    128.0
            );
        }
    }

    private static net.minecraft.util.ResourceLocation netForgeBiomeId(Biome biome) {
        return biome.getRegistryName();
    }
    
    public static boolean resetBiomeAt(World world, BlockPos pos) {
        return resetBiomeAt(world, pos, true);
    }
    
    public static boolean resetBiomeAt(World world, BlockPos pos, boolean sync) {
        Biome biome = world.getBiome(pos);
        setBiomeAt(world, pos, biome, sync);
        return true;
    }
    
    public static boolean isWoodLog(IBlockReader world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Block bi = bs.getBlock();
        return bs.is(BlockTags.LOGS);
    }
    
    public static boolean isOreBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.is(Tags.Blocks.ORES);
    }
    
    public static int setNibble(int data, int nibble, int nibbleIndex) {
        int shift = nibbleIndex * 4;
        return (data & ~(15 << shift)) | nibble << shift;
    }
    
    public static int getNibble(int data, int nibbleIndex) {
        return data >> (nibbleIndex << 2) & 0xF;
    }
    
    public static boolean getBit(int value, int bit) {
        return (value & 1 << bit) != 0x0;
    }
    
    public static int setBit(int value, int bit) {
        return value | 1 << bit;
    }
    
    public static int clearBit(int value, int bit) {
        return value & ~(1 << bit);
    }
    
    public static int toggleBit(int value, int bit) {
        return value ^ 1 << bit;
    }
    
    public static byte pack(boolean... vals) {
        byte result = 0;
        for (boolean bit : vals) {
            result = (byte)(result << 1 | ((bit & true) ? 1 : 0));
        }
        return result;
    }
    
    public static boolean[] unpack(byte val) {
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; ++i) {
            result[i] = ((byte)(val >> 7 - i & 0x1) == 1);
        }
        return result;
    }
    
    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value };
    }
    
    public static int byteArraytoInt(byte[] bytes) {
        return bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3];
    }
    
    public static byte[] shortToByteArray(short value) {
        return new byte[] { (byte)(value >>> 8), (byte)value };
    }
    
    public static short byteArraytoShort(byte[] bytes) {
        return (short)(bytes[0] << 8 | bytes[1]);
    }
    
    public static boolean isLyingInCone(double[] x, double[] t, double[] b, float aperture) {
        double halfAperture = aperture / 2.0f;
        double[] apexToXVect = dif(t, x);
        double[] axisVect = dif(t, b);
        boolean isInInfiniteCone = dotProd(apexToXVect, axisVect) / magn(apexToXVect) / magn(axisVect) > Math.cos(halfAperture);
        if (!isInInfiniteCone) {
            return false;
        }
        boolean isUnderRoundCap = dotProd(apexToXVect, axisVect) / magn(axisVect) < magn(axisVect);
        return isUnderRoundCap;
    }
    
    public static double dotProd(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }
    
    public static double[] dif(double[] a, double[] b) {
        return new double[] { a[0] - b[0], a[1] - b[1], a[2] - b[2] };
    }
    
    public static double magn(double[] a) {
        return Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
    }
    
    public static Vector3d calculateVelocity(Vector3d from, Vector3d to, double heightGain, double gravity) {
        double endGain = to.y - from.y;
        double horizDist = Math.sqrt(distanceSquared2d(from, to));
        double gain = heightGain;
        double maxGain = (gain > endGain + gain) ? gain : (endGain + gain);
        double a = -horizDist * horizDist / (4.0 * maxGain);
        double b = horizDist;
        double c = -endGain;
        double slope = -b / (2.0 * a) - Math.sqrt(b * b - 4.0 * a * c) / (2.0 * a);
        double vy = Math.sqrt(maxGain * gravity);
        double vh = vy / slope;
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;
        double vx = vh * dirx;
        double vz = vh * dirz;
        return new Vector3d(vx, vy, vz);
    }
    
    public static double distanceSquared2d(Vector3d from, Vector3d to) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        return dx * dx + dz * dz;
    }
    
    public static double distanceSquared3d(Vector3d from, Vector3d to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public static ItemStack generateLoot(int rarity, Random rand) {
        ItemStack is = ItemStack.EMPTY;
        if (rarity > 0 && rand.nextFloat() < 0.025f * rarity) {
            is = genGear(rarity, rand);
            if (is.isEmpty()) {
                is = generateLoot(rarity, rand);
            }
        }
        else {
            switch (rarity) {
                default: {
                    is = ((WeightedRandomLoot)WeightedRandom.getRandomItem(rand, (List<WeightedRandom.Item>) (List<?>) WeightedRandomLoot.lootBagCommon)).item;
                    break;
                }
                case 1: {
                    is = ((WeightedRandomLoot)WeightedRandom.getRandomItem(rand, (List<WeightedRandom.Item>) (List<?>) WeightedRandomLoot.lootBagUncommon)).item;
                    break;
                }
                case 2: {
                    is = ((WeightedRandomLoot)WeightedRandom.getRandomItem(rand, (List<WeightedRandom.Item>) (List<?>) WeightedRandomLoot.lootBagRare)).item;
                    break;
                }
            }
        }
        if (is.getItem() == Items.BOOK) {
            net.minecraft.enchantment.EnchantmentHelper.enchantItem(rand, is, (int)(5.0f + rarity * 0.75f * rand.nextInt(18)), false);
        }
        return is.copy();
    }
    
    private static ItemStack genGear(int rarity, Random rand) {
        ItemStack is = ItemStack.EMPTY;
        int quality = rand.nextInt(2);
        if (rand.nextFloat() < 0.2f) {
            ++quality;
        }
        if (rand.nextFloat() < 0.15f) {
            ++quality;
        }
        if (rand.nextFloat() < 0.1f) {
            ++quality;
        }
        if (rand.nextFloat() < 0.095f) {
            ++quality;
        }
        if (rand.nextFloat() < 0.095f) {
            ++quality;
        }
        Item item = getGearItemForSlot(rand.nextInt(5), quality);
        if (item != null) {
            is = new ItemStack(item, 1);
            if (rand.nextInt(4) < rarity) {
                net.minecraft.enchantment.EnchantmentHelper.enchantItem(rand, is, (int)(5.0f + rarity * 0.75f * rand.nextInt(18)), false);
            }
            return is.copy();
        }
        return ItemStack.EMPTY;
    }
    
    private static Item getGearItemForSlot(int slot, int quality) {
        switch (slot) {
            case 4: {
                if (quality == 0) {
                    return Items.LEATHER_HELMET;
                }
                if (quality == 1) {
                    return Items.GOLDEN_HELMET;
                }
                if (quality == 2) {
                    return Items.CHAINMAIL_HELMET;
                }
                if (quality == 3) {
                    return Items.IRON_HELMET;
                }
                if (quality == 5) {
                    return Items.DIAMOND_HELMET;
                }
            }
            case 3: {
                if (quality == 0) {
                    return Items.LEATHER_CHESTPLATE;
                }
                if (quality == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                }
                if (quality == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                }
                if (quality == 3) {
                    return Items.IRON_CHESTPLATE;
                }
                if (quality == 5) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            }
            case 2: {
                if (quality == 0) {
                    return Items.LEATHER_LEGGINGS;
                }
                if (quality == 1) {
                    return Items.GOLDEN_LEGGINGS;
                }
                if (quality == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                }
                if (quality == 3) {
                    return Items.IRON_LEGGINGS;
                }
                if (quality == 5) {
                    return Items.DIAMOND_LEGGINGS;
                }
            }
            case 1: {
                if (quality == 0) {
                    return Items.LEATHER_BOOTS;
                }
                if (quality == 1) {
                    return Items.GOLDEN_BOOTS;
                }
                if (quality == 2) {
                    return Items.CHAINMAIL_BOOTS;
                }
                if (quality == 3) {
                    return Items.IRON_BOOTS;
                }
                if (quality == 5) {
                    return Items.DIAMOND_BOOTS;
                }
            }
            case 0: {
                if (quality == 0) {
                    return Items.IRON_AXE;
                }
                if (quality == 1) {
                    return Items.IRON_SWORD;
                }
                if (quality == 2) {
                    return Items.GOLDEN_AXE;
                }
                if (quality == 3) {
                    return Items.GOLDEN_SWORD;
                }
                if (quality == 5) {
                    return Items.DIAMOND_SWORD;
                }
                break;
            }
        }
        return null;
    }
    
    public static void writeItemStackToBuffer(ByteBuf bb, ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            bb.writeShort(-1);
        }
        else {
            bb.writeShort(net.minecraft.item.Item.getId(stack.getItem()));
            bb.writeShort(stack.getCount());
            bb.writeShort(stack.getDamageValue());
            CompoundNBT nbt = stack.getShareTag();
            writeNBTTagCompoundToBuffer(bb, nbt);
        }
    }
    
    public static ItemStack readItemStackFromBuffer(ByteBuf bb) {
        ItemStack itemstack = ItemStack.EMPTY;
        short short1 = bb.readShort();
        if (short1 >= 0) {
            short b0 = bb.readShort();
            short short2 = bb.readShort();
            itemstack = new ItemStack(net.minecraft.item.Item.byId(short1), b0);
            itemstack.setTag(readNBTTagCompoundFromBuffer(bb));
        }
        return itemstack;
    }
    
    public static void writeNBTTagCompoundToBuffer(ByteBuf bb, CompoundNBT nbt) {
        if (nbt == null) {
            bb.writeByte(0);
        }
        else {
            try {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(bb));
            }
            catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }
    
    public static CompoundNBT readNBTTagCompoundFromBuffer(ByteBuf bb) {
        int i = bb.readerIndex();
        byte b0 = bb.readByte();
        if (b0 == 0) {
            return null;
        }
        bb.readerIndex(i);
        try {
            return CompressedStreamTools.read(new ByteBufInputStream(bb));
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public static Vector3d rotateAsBlock(Vector3d vec, Direction side) {
        return rotate(vec.subtract(0.5, 0.5, 0.5), side).add(0.5, 0.5, 0.5);
    }
    
    public static Vector3d rotateAsBlockRev(Vector3d vec, Direction side) {
        return revRotate(vec.subtract(0.5, 0.5, 0.5), side).add(0.5, 0.5, 0.5);
    }
    
    public static Vector3d rotate(Vector3d vec, Direction side) {
        switch (side) {
            case DOWN: {
                return new Vector3d(vec.x, -vec.y, -vec.z);
            }
            case UP: {
                return new Vector3d(vec.x, vec.y, vec.z);
            }
            case NORTH: {
                return new Vector3d(vec.x, vec.z, -vec.y);
            }
            case SOUTH: {
                return new Vector3d(vec.x, -vec.z, vec.y);
            }
            case WEST: {
                return new Vector3d(-vec.y, vec.x, vec.z);
            }
            case EAST: {
                return new Vector3d(vec.y, -vec.x, vec.z);
            }
            default: {
                return null;
            }
        }
    }
    
    public static Vector3d revRotate(Vector3d vec, Direction side) {
        switch (side) {
            case DOWN: {
                return new Vector3d(vec.x, -vec.y, -vec.z);
            }
            case UP: {
                return new Vector3d(vec.x, vec.y, vec.z);
            }
            case NORTH: {
                return new Vector3d(vec.x, -vec.z, vec.y);
            }
            case SOUTH: {
                return new Vector3d(vec.x, vec.z, -vec.y);
            }
            case WEST: {
                return new Vector3d(vec.y, -vec.x, vec.z);
            }
            case EAST: {
                return new Vector3d(-vec.y, vec.x, vec.z);
            }
            default: {
                return null;
            }
        }
    }
    
    public static Vector3d rotateAroundX(Vector3d vec, float angle) {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = vec.x;
        double var5 = vec.y * var2 + vec.z * var3;
        double var6 = vec.z * var2 - vec.y * var3;
        return new Vector3d(var4, var5, var6);
    }
    
    public static Vector3d rotateAroundY(Vector3d vec, float angle) {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = vec.x * var2 + vec.z * var3;
        double var5 = vec.y;
        double var6 = vec.z * var2 - vec.x * var3;
        return new Vector3d(var4, var5, var6);
    }
    
    public static Vector3d rotateAroundZ(Vector3d vec, float angle) {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = vec.x * var2 + vec.y * var3;
        double var5 = vec.y * var2 - vec.x * var3;
        double var6 = vec.z;
        return new Vector3d(var4, var5, var6);
    }
    
    public static BlockRayTraceResult rayTrace(World worldIn, Entity entityIn, boolean useLiquids) {
        double d3 = 5.0;
        // 1.16: use default reach; ServerPlayer reach access is not public without mixins
        return rayTrace(worldIn, entityIn, useLiquids, d3);
    }
    
    public static BlockRayTraceResult rayTrace(World worldIn, Entity entityIn, boolean useLiquids, double range) {
        Vector3d start = new Vector3d(entityIn.getX(), entityIn.getEyeY(), entityIn.getZ());
        Vector3d look = entityIn.getLookAngle();
        Vector3d end = start.add(look.x * range, look.y * range, look.z * range);
        RayTraceContext.FluidMode fluid = useLiquids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE;
        return worldIn.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, fluid, entityIn));
    }
    
    public static BlockRayTraceResult rayTrace(World worldIn, Entity entityIn, Vector3d lookvec, boolean useLiquids, double range) {
        Vector3d start = new Vector3d(entityIn.getX(), entityIn.getEyeY(), entityIn.getZ());
        Vector3d end = start.add(lookvec.x * range, lookvec.y * range, lookvec.z * range);
        RayTraceContext.FluidMode fluid = useLiquids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE;
        return worldIn.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, fluid, entityIn));
    }
    
    public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            }
            return getField(superClass, fieldName);
        }
    }
    
    public static AxisAlignedBB rotateBlockAABB(AxisAlignedBB aabb, Direction facing) {
        // TODO: Re-implement precise AABB rotation if needed. For now return as-is to avoid CCL dep.
        return aabb;
    }
    
    static {
        Utils.specialMiningResult = new HashMap<List<Object>, ItemStack>();
        Utils.specialMiningChance = new HashMap<List<Object>, Float>();
        colorNames = new String[] { "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black" };
        colors = new int[] { 15790320, 15435844, 12801229, 6719955, 14602026, 4312372, 14188952, 4408131, 10526880, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019 };
        Utils.oreDictLogs = new ArrayList<List>();
    }
}


