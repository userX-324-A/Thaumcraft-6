package thaumcraft.common.items.baubles;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.List;

public class CurioItemBase extends Item {
    public CurioItemBase(Properties properties) {
        super(properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
        if (!CuriosCompat.isLoaded()) {
            return super.initCapabilities(stack, nbt);
        }
        try {
            Class<?> curiosApi = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            Method getHelper = curiosApi.getMethod("getCuriosHelper");
            Object helper = getHelper.invoke(null);
            Method getCurio = helper.getClass().getMethod("getCurio", ItemStack.class, Class.forName("top.theillusivec4.curios.api.type.capability.ICurioItem"));
            Object curioImpl = java.lang.reflect.Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class[]{Class.forName("top.theillusivec4.curios.api.type.capability.ICurioItem")},
                    (proxy, method, args) -> null
            );
            Object provider = getCurio.invoke(helper, stack, curioImpl);
            if (provider instanceof ICapabilityProvider) {
                return (ICapabilityProvider) provider;
            }
        } catch (Throwable ignored) {}
        return super.initCapabilities(stack, nbt);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> lines, @Nonnull ITooltipFlag flag) {
        super.appendHoverText(stack, level, lines, flag);
        if (CuriosCompat.isLoaded()) {
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.curios_compat"));
        } else {
            lines.add(new TranslationTextComponent("tooltip.thaumcraft.curios_hint"));
        }
    }
}



