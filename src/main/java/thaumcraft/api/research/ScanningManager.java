package thaumcraft.api.research;

import java.util.ArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScanningManager {
    private static ArrayList<IScanThing> things;
    
    public static void addScannableThing(IScanThing obj) {
        things.add(obj);
    }
    
    public static void scanTheThing(PlayerEntity player, Object object) {
    }
    
    public static boolean isThingStillScannable(PlayerEntity player, Object object) {
        return false;
    }
    
    public static ItemStack getItemFromParms(PlayerEntity player, Object obj) {
        return null;
    }
    
    private static RayTraceResult rayTrace(PlayerEntity player) {
        return null;
    }
    
    static {
        things = new ArrayList<IScanThing>();
    }
}

