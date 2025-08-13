package thaumcraft.common.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.api.aspects.IEssentiaTransport;
import net.minecraft.util.math.BlockPos;

public class ItemResonator extends Item {
    public ItemResonator(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        // Scan aura/vis/flux and targeted BE resonance; show basic HUD text for now
        Vector3d eye = player.getEyePosition(1.0f);
        Vector3d look = player.getLookAngle();
        Vector3d reach = eye.add(look.scale(8.0));
        BlockRayTraceResult rtr = level.clip(new RayTraceContext(eye, reach, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
        String target = (rtr != null ? level.getBlockState(rtr.getBlockPos()).getBlock().getRegistryName().toString() : "none");
        if (!level.isClientSide) {
            BlockPos pos = player.blockPosition();
            float vis = thaumcraft.api.aura.AuraHelper.getVis(level, pos);
            float flux = thaumcraft.api.aura.AuraHelper.getFlux(level, pos);

            String beInfo = "";
            if (rtr != null) {
                BlockPos hit = rtr.getBlockPos();
                TileEntity te = level.getBlockEntity(hit);
                if (te != null) {
                    LazyOptional<IEssentiaTransport> opt = te.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, rtr.getDirection());
                    opt.ifPresent(et -> {
                        int amt = et.getEssentiaAmount(rtr.getDirection());
                        thaumcraft.api.aspects.Aspect a = et.getEssentiaType(rtr.getDirection());
                        String aName = (a != null ? a.getName() : "none");
                        String msg = String.format("Resonator → Target: %s | Vis: %.0f | Flux: %.0f | Essentia: %d %s", target, vis, flux, amt, aName);
                        player.displayClientMessage(new StringTextComponent(msg), true);
                    });
                }
            }
            if (beInfo.isEmpty()) {
                String msg = String.format("Resonator → Target: %s | Vis: %.0f | Flux: %.0f", target, vis, flux);
                player.displayClientMessage(new StringTextComponent(msg), true);
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
    }

    // Optional: hold to sample nearby BEs and show aggregate count (basic pass)
    @Override
    public void inventoryTick(ItemStack stack, World level, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        int cadence = thaumcraft.common.config.ModConfig.COMMON.resonatorHudCadenceTicks.get();
        if (!(entity instanceof PlayerEntity) || level.isClientSide || !selected || ((PlayerEntity) entity).tickCount % Math.max(1, cadence) != 0) return;
        PlayerEntity player = (PlayerEntity) entity;
        BlockPos center = player.blockPosition();
        int radius = Math.max(1, thaumcraft.common.config.ModConfig.COMMON.resonatorScanRadius.get());
        int found = 0;
        for (BlockPos p : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
            TileEntity te = level.getBlockEntity(p);
            if (te == null) continue;
            LazyOptional<IEssentiaTransport> opt = te.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, null);
            if (opt.isPresent()) found++;
            if (found >= 20) break;
        }
        // Find nearest essentia transport with type/amount/direction
        String nearestTxt = "none";
        int nearestAmt = 0;
        String nearestAspect = "";
        net.minecraft.util.Direction nearestDir = null;
        int bestDist2 = Integer.MAX_VALUE;
        for (BlockPos p : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
            TileEntity te = level.getBlockEntity(p);
            if (te == null) continue;
            for (net.minecraft.util.Direction d : net.minecraft.util.Direction.values()) {
                LazyOptional<IEssentiaTransport> opt = te.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, d);
                if (!opt.isPresent()) continue;
                IEssentiaTransport et = opt.orElse(null);
                if (et == null) continue;
                int amt = et.getEssentiaAmount(d);
                thaumcraft.api.aspects.Aspect a = et.getEssentiaType(d);
                int dx = p.getX() - center.getX();
                int dy = p.getY() - center.getY();
                int dz = p.getZ() - center.getZ();
                int dist2 = dx*dx + dy*dy + dz*dz;
                if (amt > 0 && dist2 < bestDist2) {
                    bestDist2 = dist2;
                    nearestAmt = amt;
                    nearestAspect = (a != null ? a.getName() : "");
                    nearestDir = d;
                    nearestTxt = p.getX()+","+p.getY()+","+p.getZ();
                }
            }
        }
        String msg = "Resonator → Base: " + thaumcraft.client.hud.HudHandler.auraBase +
                " | Vis: " + Math.round(thaumcraft.client.hud.HudHandler.auraVis) +
                " | Flux: " + Math.round(thaumcraft.client.hud.HudHandler.auraFlux) +
                " | Essentia BEs: " + found +
                (nearestDir != null ? (" | Nearest: " + nearestAmt + " " + nearestAspect + " @ " + nearestTxt + " facing " + nearestDir.getName()) : "");
        player.displayClientMessage(new StringTextComponent(msg), true);
    }
}


