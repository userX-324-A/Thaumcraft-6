package thaumcraft.integration.jade.providers;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import snownee.jade.api.IComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.BlockAccessor;
import thaumcraft.common.blocks.world.InfusionMatrixBlockEntity;

public class InfusionMatrixTooltipProvider implements IComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof InfusionMatrixBlockEntity) {
            InfusionMatrixBlockEntity be = (InfusionMatrixBlockEntity) accessor.getTileEntity();
            tooltip.add(new StringTextComponent("Infusion: active? " + isActive(be)));
        }
    }

    private boolean isActive(InfusionMatrixBlockEntity be) {
        try {
            java.lang.reflect.Field f = InfusionMatrixBlockEntity.class.getDeclaredField("crafting");
            f.setAccessible(true);
            return (boolean) f.get(be);
        } catch (Throwable t) {
            return false;
        }
    }
}



