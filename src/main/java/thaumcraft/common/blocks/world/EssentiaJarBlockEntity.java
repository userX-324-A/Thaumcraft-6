package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;
import net.minecraft.block.BlockState;
import thaumcraft.common.capabilities.EssentiaTransportCapability;
import thaumcraft.common.capabilities.EssentiaTransportCapability.BasicEssentiaTransport;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaJarBlockEntity extends TileEntity {
    private LazyOptional<IEssentiaTransport> essentiaOptional = LazyOptional.of(() -> new EssentiaTransportCapability.BasicEssentiaTransport(64, 20));
    private boolean hasBrace;

    public EssentiaJarBlockEntity() { super(ModBlockEntities.ESSENTIA_JAR.get()); }
    protected EssentiaJarBlockEntity(net.minecraft.tileentity.TileEntityType<?> type) { super(type); }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == EssentiaTransportCapability.ESSENTIA_TRANSPORT) return essentiaOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        essentiaOptional.invalidate();
    }

    public boolean hasBrace() { return hasBrace; }
    public void setBrace(boolean brace) {
        if (hasBrace == brace) return;
        hasBrace = brace;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            BlockState bs = getBlockState();
            if (bs.getBlock() instanceof EssentiaJarBlock) {
                level.setBlock(worldPosition, bs.setValue(EssentiaJarBlock.BRACED, hasBrace), 3);
            }
        }
    }

    @Override
    public void load(net.minecraft.block.BlockState state, net.minecraft.nbt.CompoundNBT tag) {
        super.load(state, tag);
        if (tag.contains("HasBrace")) hasBrace = tag.getBoolean("HasBrace");
        if (tag.contains("Aspect") && tag.contains("Amount")) {
            IEssentiaTransport et = essentiaOptional.orElse(null);
            if (et instanceof BasicEssentiaTransport) {
                Aspect aspect = Aspect.getAspect(tag.getString("Aspect"));
                int amt = tag.getInt("Amount");
                ((BasicEssentiaTransport) et).setStored(aspect, amt);
            }
        }
    }

    @Override
    public net.minecraft.nbt.CompoundNBT save(net.minecraft.nbt.CompoundNBT tag) {
        tag = super.save(tag);
        tag.putBoolean("HasBrace", hasBrace);
        IEssentiaTransport et = essentiaOptional.orElse(null);
        if (et instanceof BasicEssentiaTransport) {
            Aspect a = ((BasicEssentiaTransport) et).getStoredType();
            int amt = ((BasicEssentiaTransport) et).getStoredAmount();
            if (a != null && amt > 0) {
                tag.putString("Aspect", a.getTag());
                tag.putInt("Amount", amt);
            }
        }
        return tag;
    }
}

