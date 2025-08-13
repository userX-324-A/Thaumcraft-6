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
    
    @Deprecated
    public static ISealEntity getSealEntity(int dim, BlockPos pos) {
        return null; // Deprecated: use the SealPos overload for face-aware lookup
    }

    public static ISealEntity getSealEntity(int dim, thaumcraft.api.golems.seals.SealPos pos) {
        if (pos == null) return null;
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.LogicalSidedProvider.INSTANCE.get(net.minecraftforge.fml.LogicalSide.SERVER);
        if (server == null) return null;
        net.minecraft.world.server.ServerWorld world = resolveWorld(server, dim);
        if (world == null) return null;
        return thaumcraft.common.golems.seals.SealWorldData.get(world).get(pos.pos, pos.face);
    }

    public static ISealEntity getSealEntity(net.minecraft.util.RegistryKey<net.minecraft.world.World> dimensionKey,
                                            thaumcraft.api.golems.seals.SealPos pos) {
        if (pos == null) return null;
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.LogicalSidedProvider.INSTANCE.get(net.minecraftforge.fml.LogicalSide.SERVER);
        if (server == null) return null;
        net.minecraft.world.server.ServerWorld world = server.getLevel(dimensionKey);
        if (world == null) return null;
        return thaumcraft.common.golems.seals.SealWorldData.get(world).get(pos.pos, pos.face);
    }

    private static net.minecraft.world.server.ServerWorld resolveWorld(net.minecraft.server.MinecraftServer server, int dim) {
        // Map common legacy ids: -1 (nether), 0 (overworld), 1 (end)
        net.minecraft.world.server.ServerWorld world;
        if (dim == -1) {
            world = server.getLevel(net.minecraft.world.World.NETHER);
        } else if (dim == 0) {
            world = server.getLevel(net.minecraft.world.World.OVERWORLD);
        } else if (dim == 1) {
            world = server.getLevel(net.minecraft.world.World.END);
        } else {
            // Fallback: try to find a world whose dimension id matches stringified dim in registry name suffix
            // Since numeric ids are deprecated, mods should migrate to passing proper RegistryKey instead of int.
            world = server.overworld();
        }
        return world;
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

