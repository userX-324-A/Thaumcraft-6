package thaumcraft.common.lib.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.common.items.baubles.CuriosCompat;
import thaumcraft.common.registers.ModItems;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class GameplayEvents {

    private GameplayEvents() {}

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (!CuriosCompat.isLoaded()) return;

        // Charm of Undying: prevent fatal damage once, consume the charm
        if (event.getAmount() >= player.getHealth()) {
            if (CuriosCompat.hasEquipped(player, ModItems.CHARM_UNDYING.get())) {
                event.setCanceled(true);
                player.setHealth(1.0f);
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 200, 1));
                player.addEffect(new EffectInstance(Effects.REGENERATION, 200, 1));
                // Consume one charm from curios slots (best-effort via reflection helper)
                try {
                    Class<?> curiosApi = Class.forName("top.theillusivec4.curios.api.CuriosApi");
                    Object helper = curiosApi.getMethod("getCuriosHelper").invoke(null);
                    Class<?> handlerCls = Class.forName("top.theillusivec4.curios.api.type.inventory.ICuriosItemHandler");
                    Class<?> curioStackHandlerCls = Class.forName("top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler");
                    java.util.Optional<?> opt = (java.util.Optional<?>) helper.getClass().getMethod("getEquippedCurios", PlayerEntity.class).invoke(helper, player);
                    if (opt != null && opt.isPresent()) {
                        Object handler = opt.get();
                        java.util.Map<?, ?> curios = (java.util.Map<?, ?>) handlerCls.getMethod("getCurios").invoke(handler);
                        for (Object dyn : curios.values()) {
                            if (curioStackHandlerCls.isInstance(dyn)) {
                                int slots = (int) curioStackHandlerCls.getMethod("getSlots").invoke(dyn);
                                for (int i = 0; i < slots; i++) {
                                    net.minecraft.item.ItemStack s = (net.minecraft.item.ItemStack) curioStackHandlerCls.getMethod("getStackInSlot", int.class).invoke(dyn, i);
                                    if (!s.isEmpty() && s.getItem() == ModItems.CHARM_UNDYING.get()) {
                                        curioStackHandlerCls.getMethod("extractItem", int.class, int.class, boolean.class).invoke(dyn, i, 1, false);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable ignored) {}
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;
        if (!CuriosCompat.isLoaded()) return;

        // Verdant charm: steady regen while below full
        if (CuriosCompat.hasEquipped(player, ModItems.CHARM_VERDANT.get())) {
            int interval = thaumcraft.common.config.ModConfig.COMMON.verdantRegenIntervalTicks.get();
            int amt = thaumcraft.common.config.ModConfig.COMMON.verdantRegenAmount.get();
            if (interval > 0 && player.tickCount % interval == 0 && player.getHealth() < player.getMaxHealth()) {
                player.heal(Math.max(1, amt));
            }
        }

        // Voidseer charm: chance to clear bad effects (placeholder light mitigation)
        if (CuriosCompat.hasEquipped(player, ModItems.CHARM_VOIDSEER.get())) {
            int every = thaumcraft.common.config.ModConfig.COMMON.voidseerCleanseIntervalTicks.get();
            if (every > 0 && player.tickCount % every == 0) {
                player.getActiveEffects().removeIf(e -> e.getEffect() == Effects.CONFUSION || e.getEffect() == Effects.WEAKNESS);
            }
        }
    }
}



