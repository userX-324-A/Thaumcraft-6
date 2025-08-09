package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.registers.ModBlockEntities;

public class EssentiaValveBlockEntity extends TileEntity {
    private boolean open = true;

    public EssentiaValveBlockEntity() { super(ModBlockEntities.ESSENTIA_VALVE.get()); }

    @Override
    public net.minecraft.nbt.CompoundNBT getUpdateTag() {
        net.minecraft.nbt.CompoundNBT tag = super.getUpdateTag();
        tag.putBoolean("open", open);
        return tag;
    }

    @Override
    public void handleUpdateTag(net.minecraft.nbt.CompoundNBT tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("open")) this.open = tag.getBoolean("open");
    }
}



