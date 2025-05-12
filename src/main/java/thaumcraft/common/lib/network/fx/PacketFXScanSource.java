package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;


public class PacketFXScanSource
{
    private long loc;
    private int size;
    int C_QUARTZ = 15064789;
    int C_IRON = 14200723;
    int C_LAPIS = 1328572;
    int C_GOLD = 16576075;
    int C_DIAMOND = 6155509;
    int C_EMERALD = 1564002;
    int C_REDSTONE = 16711680;
    int C_COAL = 1052688;
    int C_SILVER = 14342653;
    int C_TIN = 15724539;
    int C_COPPER = 16620629;
    int C_AMBER = 16626469;
    int C_CINNABAR = 10159368;
    
    public PacketFXScanSource() {
    }
    
    public PacketFXScanSource(BlockPos pos, int size) {
        loc = pos.toLong();
        this.size = size;
    }
    
    public void encode(PacketBuffer buffer) {
        buffer.writeLong(loc);
        buffer.writeByte(size);
    }
    
    public PacketFXScanSource(PacketBuffer buffer) {
        loc = buffer.readLong();
        size = buffer.readByte();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void handle(PacketFXScanSource message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                startScan(mc.player.world, BlockPos.fromLong(message.loc), message.size);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void startScan(World world, BlockPos pos, int r) {
        int range = 4 + r * 4;
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (int xx = -range; xx <= range; ++xx) {
            for (int yy = -range; yy <= range; ++yy) {
                for (int zz = -range; zz <= range; ++zz) {
                    BlockPos p = pos.add(xx, yy, zz);
                    if (Utils.isOreBlock(world, p)) {
                        positions.add(p);
                    }
                }
            }
        }
        while (!positions.isEmpty()) {
            BlockPos start = positions.get(0);
            ArrayList<BlockPos> coll = new ArrayList<BlockPos>();
            coll.add(start);
            positions.remove(0);
            calcGroup(world, start, coll, positions);
            if (!coll.isEmpty()) {
                int c = getOreColor(world, start);
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                for (BlockPos p2 : coll) {
                    x += p2.getX() + 0.5;
                    y += p2.getY() + 0.5;
                    z += p2.getZ() + 0.5;
                }
                x /= coll.size();
                y /= coll.size();
                z /= coll.size();
                double dis = Math.sqrt(pos.distanceSqToCenter(x, y, z));
                FXGeneric fb = new FXGeneric(world, x, y, z, 0.0, 0.0, 0.0);
                fb.setMaxAge(44);
                Color cc = new Color(c);
                fb.setRBGColorF(cc.getRed() / 255.0f, cc.getGreen() / 255.0f, cc.getBlue() / 255.0f);
                float q = (cc.getRed() / 255.0f + cc.getGreen() / 255.0f + cc.getBlue() / 255.0f) / 3.0f;
                fb.setAlphaF(0.0f, 1.0f, 0.8f, 0.0f);
                fb.setParticles(240, 15, 1);
                fb.setGridSize(16);
                fb.setLoop(true);
                fb.setScale(9.0f);
                fb.setLayer((q < 0.25f) ? 3 : 2);
                fb.setRotationSpeed(0.0f);
                ParticleEngine.addEffectWithDelay(world, fb, (int)(dis * 3.0));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void calcGroup(World world, BlockPos start, ArrayList<BlockPos> coll, ArrayList<BlockPos> positions) {
        IBlockState bs = world.getBlockState(start);
    Label_0132:
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    BlockPos t = new BlockPos(start).add(x, y, z);
                    IBlockState ts = world.getBlockState(t);
                    if (ts.equals(bs) && positions.contains(t)) {
                        positions.remove(t);
                        coll.add(t);
                        if (positions.isEmpty()) {
                            break Label_0132;
                        }
                        calcGroup(world, t, coll, positions);
                    }
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static int getOreColor(World world, BlockPos pos) {
        IBlockState bi = world.getBlockState(pos);
        if (bi.getBlock() != Blocks.AIR && bi.getBlock() != Blocks.BEDROCK) {
            ItemStack is = BlockUtils.getSilkTouchDrop(bi);
            if (is == null || is.isEmpty()) {
                int md = bi.getBlock().getMetaFromState(bi);
                is = new ItemStack(bi.getBlock(), 1, md);
            }
            if (is == null || is.isEmpty() || is.getItem() == null) {
                return 12632256;
            }
            int[] od = OreDictionary.getOreIDs(is);
            if (od != null && od.length > 0) {
                for (int id : od) {
                    if (OreDictionary.getOreName(id) != null) {
                        if (OreDictionary.getOreName(id).toUpperCase().contains("IRON")) {
                            return 14200723;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("COAL")) {
                            return 1052688;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("REDSTONE")) {
                            return 16711680;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("GOLD")) {
                            return 16576075;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("LAPIS")) {
                            return 1328572;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("DIAMOND")) {
                            return 6155509;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("EMERALD")) {
                            return 1564002;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("QUARTZ")) {
                            return 15064789;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("SILVER")) {
                            return 14342653;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("TIN")) {
                            return 15724539;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("COPPER")) {
                            return 16620629;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("AMBER")) {
                            return 16626469;
                        }
                        if (OreDictionary.getOreName(id).toUpperCase().contains("CINNABAR")) {
                            return 10159368;
                        }
                    }
                }
            }
        }
        return 12632256;
    }
}
