package thaumcraft.api.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.aura.AuraHelper;

// no @Nullable usage in this helper currently

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AspectSourceHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static boolean drainEssentia(TileEntity tile, Aspect aspect, Direction direction, int range) {
        return drainEssentia(tile, aspect, direction, range, false);
    }
    
    public static boolean drainEssentia(TileEntity tile, Aspect aspect, Direction direction, int range, boolean forti) {
        if (tile == null || tile.isRemoved()) {
            return false;
        }
        World world = tile.getLevel();
        for (int a = 0; a < range; ++a) {
            BlockPos pos = tile.getBlockPos().relative(direction, a + 1);
            TileEntity te = world.getBlockEntity(pos);
            if (te != null && !te.isRemoved() && te instanceof IEssentiaTransport) {
                IEssentiaTransport et = (IEssentiaTransport)te;
                if (et.getEssentiaType(direction.getOpposite()) == aspect && et.getEssentiaAmount(direction.getOpposite()) > 0 && et.getSuctionType(direction.getOpposite()) == null && et.takeEssentia(aspect, 1, direction.getOpposite()) == 1) {
                    if (forti && world.random.nextInt(10) == 0) {
                        AuraHelper.pollute(world, pos, 1, true);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean findEssentia(TileEntity tile, Aspect aspect, Direction direction, int range) {
        World world = tile.getLevel();
        for (int a = 0; a < range; ++a) {
            BlockPos pos = tile.getBlockPos().relative(direction, a + 1);
            TileEntity te = world.getBlockEntity(pos);
            if (te != null && !te.isRemoved() && te instanceof IEssentiaTransport) {
                IEssentiaTransport et = (IEssentiaTransport)te;
                if (et.getEssentiaType(direction.getOpposite()) == aspect && et.getEssentiaAmount(direction.getOpposite()) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}

