package thaumcraft.common.items.baubles;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

public final class CuriosCompat {
    private CuriosCompat() {}

    public static boolean isLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static boolean isEquipped(PlayerEntity player, ItemStack stack) {
        if (!isLoaded() || player == null || stack.isEmpty()) return false;
        try {
            Class<?> curiosApi = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            java.lang.reflect.Method getCuriosHelper = curiosApi.getMethod("getCuriosHelper");
            Object helper = getCuriosHelper.invoke(null);
            java.lang.reflect.Method find = helper.getClass().getMethod("findEquippedCurio", ItemStack.class, PlayerEntity.class);
            java.util.Optional<?> result = (java.util.Optional<?>) find.invoke(helper, stack, player);
            return result != null && result.isPresent();
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean hasEquipped(PlayerEntity player, Item item) {
        if (!isLoaded() || player == null || item == null) return false;
        try {
            Class<?> curiosApi = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            java.lang.reflect.Method getCuriosHelper = curiosApi.getMethod("getCuriosHelper");
            Object helper = getCuriosHelper.invoke(null);
            // getEquippedCurios(Player) -> ICuriosItemHandler, then iterate stacks
            java.lang.Class<?> handlerCls = Class.forName("top.theillusivec4.curios.api.type.inventory.ICuriosItemHandler");
            java.lang.Class<?> curioStackHandlerCls = Class.forName("top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler");
            java.lang.Class<?> slotResultCls = Class.forName("top.theillusivec4.curios.api.SlotResult");
            java.lang.reflect.Method getEquippedCurios = helper.getClass().getMethod("getEquippedCurios", PlayerEntity.class);
            java.util.Optional<?> optHandler = (java.util.Optional<?>) getEquippedCurios.invoke(helper, player);
            if (optHandler == null || !optHandler.isPresent()) return false;
            Object handler = optHandler.get();
            java.lang.reflect.Method getCurios = handlerCls.getMethod("getCurios");
            java.util.Map<?, ?> curiosMap = (java.util.Map<?, ?>) getCurios.invoke(handler);
            if (curiosMap == null) return false;
            for (Object entry : curiosMap.values()) {
                Object dyn = entry;
                if (curioStackHandlerCls.isInstance(dyn)) {
                    java.lang.reflect.Method getSlots = curioStackHandlerCls.getMethod("getSlots");
                    java.lang.reflect.Method getStackInSlot = curioStackHandlerCls.getMethod("getStackInSlot", int.class);
                    int slots = (int) getSlots.invoke(dyn);
                    for (int i = 0; i < slots; i++) {
                        ItemStack s = (ItemStack) getStackInSlot.invoke(dyn, i);
                        if (!s.isEmpty() && s.getItem() == item) return true;
                    }
                }
            }
        } catch (Throwable t) {
            return false;
        }
        return false;
    }
}


