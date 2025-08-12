package thaumcraft.api.golems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;

public class GolemHelper {
    public static HashMap<Integer, ArrayList<ProvisionRequest>> provisionRequests;
    
    public static void registerSeal(ISeal seal) {
        thaumcraft.common.golems.seals.SealRegistry.register(seal);
    }
    
    public static ISeal getSeal(String key) {
        return thaumcraft.common.golems.seals.SealRegistry.get(key);
    }
    
    public static ISealEntity getSealEntity(int dim, BlockPos pos) {
        // Not implemented yet: dim-based lookup. SealEngine/SealWorldData provide per-world access.
        return null;
    }

    public static ISealEntity getSealEntity(int dim, thaumcraft.api.golems.seals.SealPos pos) {
        // Overload required by Task; not implemented yet
        return null;
    }
    
    public static void addGolem(IGolemAPI golem) {
    }
    
    public static void removeGolem(IGolemAPI golem) {
    }
    
    public static void requestProvisioning(World world, ISealEntity seal, ItemStack stack) {
    }
    
    public static void requestProvisioning(World world, BlockPos pos, Direction side, ItemStack stack) {
    }
    
    public static void requestProvisioning(World world, IGolemAPI golem, ItemStack stack) {
    }
    
    public static List<ProvisionRequest> getProvisionRequests(World world) {
        return null;
    }
    
    public static void addProvisionRequest(World world, ProvisionRequest request) {
    }
    
    public static void removeProvisionRequest(World world, ProvisionRequest request) {
    }
    
    public static void updateProvisionRequest(World world, ProvisionRequest request) {
    }
    
    public static void requestProvisioning(World world, BlockPos pos, Direction side, ItemStack stack, int ui) {
    }
    
    static {
        GolemHelper.provisionRequests = new HashMap<Integer, ArrayList<ProvisionRequest>>();
    }
}

