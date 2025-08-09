package thaumcraft.api.research.theorycraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class TheorycraftCard {
    public abstract CompoundNBT serialize();
    
    public abstract void deserialize(CompoundNBT nbt);
    
    public abstract boolean initialize(PlayerEntity player, ResearchTableData data);
    
    public abstract boolean activate(PlayerEntity player, ResearchTableData data);
    
    public abstract String getResearchCategory();
    
    public abstract int getInspirationCost();
    
    public abstract String getLocalizedName();
    
    public abstract String getLocalizedText();
    
    public abstract TheorycraftCard[] getMutations();
    
    public abstract boolean isAidOnly();
}

