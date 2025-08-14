package thaumcraft.integration.jade.providers;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import snownee.jade.api.IComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.view.IServerDataProvider;
import snownee.jade.api.BlockAccessor;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.capabilities.EssentiaTransportCapability;

public class EssentiaTooltipProvider implements IComponentProvider, IServerDataProvider<BlockAccessor> {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        IElementHelper helper = tooltip.getElementHelper();
        Aspect aspect = null;
        int amount = 0;
        if (accessor.getServerData().contains("Aspect")) {
            aspect = Aspect.getAspect(accessor.getServerData().getString("Aspect"));
            amount = accessor.getServerData().getInt("Amount");
        }
        if (aspect != null && amount > 0) {
            tooltip.add(new StringTextComponent(aspect.getName() + ": " + amount));
        }
    }

    @Override
    public void appendServerData(net.minecraft.nbt.CompoundNBT data, BlockAccessor accessor) {
        TileEntity te = accessor.getTileEntity();
        if (te == null) return;
        te.getCapability(EssentiaTransportCapability.ESSENTIA_TRANSPORT, null).ifPresent(tr -> {
            Aspect a = tr.getSuctionType(null);
            if (a == null && tr instanceof thaumcraft.common.capabilities.EssentiaTransportCapability.BasicEssentiaTransport) {
                a = ((thaumcraft.common.capabilities.EssentiaTransportCapability.BasicEssentiaTransport) tr).getStoredType();
            }
            int amt = 0;
            if (tr instanceof thaumcraft.common.capabilities.EssentiaTransportCapability.BasicEssentiaTransport) {
                amt = ((thaumcraft.common.capabilities.EssentiaTransportCapability.BasicEssentiaTransport) tr).getStoredAmount();
            }
            if (a != null && amt > 0) {
                data.putString("Aspect", a.getTag());
                data.putInt("Amount", amt);
            }
        });
    }
}



