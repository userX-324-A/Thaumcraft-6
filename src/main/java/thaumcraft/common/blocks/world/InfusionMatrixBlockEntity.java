package thaumcraft.common.blocks.world;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionRecipe;
import thaumcraft.common.registers.ModBlockEntities;
import thaumcraft.common.registers.ModRecipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InfusionMatrixBlockEntity extends TileEntity implements ITickableTileEntity {
    private boolean crafting = false;
    private int tickCounter = 0;
    private int consumeDelay = 10;
    private int instabilityTicker = 0;
    private ResourceLocation activeRecipeId = null;
    private NonNullList<Ingredient> remainingComponents = NonNullList.create();

    public InfusionMatrixBlockEntity() {
        super(ModBlockEntities.INFUSION_MATRIX.get());
    }

    @Override
    public void tick() {
        World level = this.level;
        if (level == null || level.isClientSide) return;
        this.tickCounter++;

        if (!this.crafting) {
            // Throttle expensive environment scans while idle
            if ((this.tickCounter % thaumcraft.common.config.ModConfig.COMMON.infusionScanIntervalTicks.get()) != 0) return;
            // Try to locate a valid infusion setup
            PedestalBlockEntity center = getCentralPedestal(level, worldPosition);
            if (center == null) return;
            ItemStack central = center.getInventory().getStackInSlot(0);
            if (central.isEmpty()) return;
            List<PedestalBlockEntity> pedestals = getPeripheralPedestals(level, worldPosition);
            List<ItemStack> components = new ArrayList<>();
            for (PedestalBlockEntity p : pedestals) {
                ItemStack stack = p.getInventory().getStackInSlot(0);
                if (!stack.isEmpty()) components.add(stack.copy());
            }
            IInfusionRecipe recipe = findMatchingRecipe(level, central, components);
            if (recipe != null) {
                // Optional research gate before starting
                String req = recipe.getResearch();
                if (req != null && !req.isEmpty()) {
                    // gate on nearest player above the matrix; if none, allow
                    List<net.minecraft.entity.player.PlayerEntity> players = level.getEntitiesOfClass(net.minecraft.entity.player.PlayerEntity.class, new AxisAlignedBB(worldPosition).inflate(8));
                    boolean knows = players.isEmpty() || thaumcraft.api.capabilities.ThaumcraftCapabilities.knowsResearch(players.get(0), req);
                    if (!knows) return;
                }
                this.crafting = true;
                this.activeRecipeId = recipe.getId();
                this.remainingComponents = NonNullList.create();
                this.remainingComponents.addAll(recipe.getComponents());
                setChanged();
            }
            return;
        }

        // Crafting progress: periodically consume a matching component from any pedestal
        if (this.tickCounter % this.consumeDelay == 0) {
            PedestalBlockEntity center = getCentralPedestal(level, worldPosition);
            if (center == null) { reset(); return; }
            List<PedestalBlockEntity> pedestals = getPeripheralPedestals(level, worldPosition);

            // If no components left, finish crafting
            if (this.remainingComponents.isEmpty()) {
                IInfusionRecipe recipe = getActiveRecipe(level, this.activeRecipeId);
                if (recipe == null) { reset(); return; }
                ItemStack central = center.getInventory().getStackInSlot(0);
                if (central.isEmpty() || !recipe.getCentralIngredient().test(central)) { reset(); return; }
                // Consume central item and output result
                center.getInventory().extractItem(0, 1, false);
                ItemStack out = recipe.getResultItem().copy();
                center.getInventory().insertItem(0, out, false);
                triggerCompletionFX(out);
                reset();
                return;
            }

            // Try to match and consume the next ingredient from any pedestal
            for (int i = 0; i < this.remainingComponents.size(); i++) {
                Ingredient need = this.remainingComponents.get(i);
                boolean consumed = false;
                for (PedestalBlockEntity p : pedestals) {
                    ItemStack stack = p.getInventory().getStackInSlot(0);
                    if (!stack.isEmpty() && need.test(stack)) {
                        ItemStack taken = p.getInventory().extractItem(0, 1, false);
                        this.remainingComponents.remove(i);
                        consumed = true;
                        setChanged();
                        triggerConsumeFX(p.getBlockPos(), taken);
                        break;
                    }
                }
                if (consumed) break;
            }
        }

        // Between consumptions, handle aspect drain toward the matrix and instability ticks
        IInfusionRecipe recipe = getActiveRecipe(level, this.activeRecipeId);
        if (recipe != null) {
            // Drain required aspects gradually from nearby essentia transports (very simplified)
            thaumcraft.api.aspects.AspectList al = recipe.getAspects(null);
            if (al != null) {
                for (thaumcraft.api.aspects.Aspect aspect : al.getAspects()) {
                    int need = Math.max(0, al.getAmount(aspect));
                    if (need <= 0) continue;
                    int pulled = pullEssentia(level, worldPosition, aspect, need);
                    // if we fail to pull enough over time, instability increases chance to pop items
                    if (pulled < need) {
                        instabilityTicker++;
                        if (instabilityTicker % Math.max(20, 200 - (recipe.getInstability() * 10)) == 0) {
                            causeMinorInstability(level);
                            triggerInstabilityFX();
                        }
                    }
                }
            }
        }
    }

    private void reset() {
        this.crafting = false;
        this.activeRecipeId = null;
        this.remainingComponents = NonNullList.create();
        this.tickCounter = 0;
        setChanged();
    }

    private int pullEssentia(World level, BlockPos origin, thaumcraft.api.aspects.Aspect aspect, int need) {
        int radius = 6;
        int takenTotal = 0;
        for (BlockPos p : BlockPos.betweenClosed(origin.offset(-radius, -radius, -radius), origin.offset(radius, radius, radius))) {
            TileEntity be = level.getBlockEntity(p);
            if (!(be instanceof TileEntity)) continue;
            java.util.Optional<thaumcraft.api.aspects.IEssentiaTransport> cap = be.getCapability(thaumcraft.common.capabilities.EssentiaTransportCapability.ESSENTIA_TRANSPORT, null).resolve();
            if (!cap.isPresent()) continue;
            thaumcraft.api.aspects.IEssentiaTransport et = cap.get();
            int took = et.takeEssentia(aspect, Math.min(need - takenTotal, 4), net.minecraft.util.Direction.UP);
            if (took > 0) takenTotal += took;
            if (takenTotal >= need) break;
        }
        return takenTotal;
    }

    private void causeMinorInstability(World level) {
        // Very basic effect: nudge items on pedestals or drop a small flux into aura stub
        thaumcraft.api.aura.AuraHelper.addFlux(level, worldPosition, 1);
    }

    // ---- FX hooks (no-op for now; intended for client packet triggers later) ----
    protected void triggerConsumeFX(BlockPos pedestalPos, ItemStack consumedStack) { /* hook */ }
    protected void triggerInstabilityFX() { /* hook */ }
    protected void triggerCompletionFX(ItemStack result) { /* hook */ }

    @Nullable
    private static PedestalBlockEntity getCentralPedestal(World level, BlockPos matrixPos) {
        BlockPos below = matrixPos.below(2);
        TileEntity be = level.getBlockEntity(below);
        return be instanceof PedestalBlockEntity ? (PedestalBlockEntity) be : null;
    }

    private static List<PedestalBlockEntity> getPeripheralPedestals(World level, BlockPos matrixPos) {
        List<PedestalBlockEntity> list = new ArrayList<>();
        int radius = 8;
        AxisAlignedBB area = new AxisAlignedBB(matrixPos.offset(-radius, -2, -radius), matrixPos.offset(radius, -2, radius));
        for (int x = (int) area.minX; x <= area.maxX; x++) {
            for (int z = (int) area.minZ; z <= area.maxZ; z++) {
                BlockPos p = new BlockPos(x, matrixPos.getY() - 2, z);
                if (p.equals(matrixPos.below(2))) continue;
                TileEntity be = level.getBlockEntity(p);
                if (be instanceof PedestalBlockEntity) {
                    list.add((PedestalBlockEntity) be);
                }
            }
        }
        return list;
    }

    @Nullable
    private static IInfusionRecipe findMatchingRecipe(World level, ItemStack central, List<ItemStack> components) {
        List<IInfusionRecipe> recipes = new ArrayList<>();
        level.getRecipeManager().getAllRecipesFor(ModRecipes.INFUSION_RECIPE_TYPE).forEach(r -> {
            if (r instanceof IInfusionRecipe) recipes.add((IInfusionRecipe) r);
        });
        for (IInfusionRecipe r : recipes) {
            if (!r.getCentralIngredient().test(central)) continue;
            NonNullList<Ingredient> need = r.getComponents();
            boolean[] used = new boolean[components.size()];
            boolean ok = true;
            for (Ingredient ing : need) {
                boolean found = false;
                for (int i = 0; i < components.size(); i++) {
                    if (!used[i] && ing.test(components.get(i))) {
                        used[i] = true;
                        found = true;
                        break;
                    }
                }
                if (!found) { ok = false; break; }
            }
            if (ok) return r;
        }
        return null;
    }

    @Nullable
    private static IInfusionRecipe getActiveRecipe(World level, @Nullable ResourceLocation id) {
        if (id == null) return null;
        return level.getRecipeManager().byKey(id)
                .filter(r -> r.getType() == ModRecipes.INFUSION_RECIPE_TYPE)
                .map(r -> (IInfusionRecipe) r)
                .orElse(null);
    }

    @Override
    public void load(net.minecraft.block.BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.crafting = nbt.getBoolean("Crafting");
        this.tickCounter = nbt.getInt("Ticks");
        if (nbt.contains("Recipe")) this.activeRecipeId = new ResourceLocation(nbt.getString("Recipe"));
        this.consumeDelay = nbt.getInt("Delay");
        // remainingComponents persisted minimally by count; for simplicity, rebuild from recipe when resuming
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putBoolean("Crafting", crafting);
        nbt.putInt("Ticks", tickCounter);
        if (activeRecipeId != null) nbt.putString("Recipe", activeRecipeId.toString());
        nbt.putInt("Delay", consumeDelay);
        return super.save(nbt);
    }

    // Client sync for visual state
    @Override
    public net.minecraft.network.play.server.SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        save(tag);
        return new net.minecraft.network.play.server.SUpdateTileEntityPacket(this.worldPosition, 0, tag);
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
        this.load(getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        return save(tag);
    }

    @Override
    public void handleUpdateTag(net.minecraft.block.BlockState state, CompoundNBT tag) {
        load(state, tag);
    }
}
