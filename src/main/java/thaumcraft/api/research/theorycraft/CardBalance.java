package thaumcraft.api.research.theorycraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class CardBalance extends TheorycraftCard {
    @Override
    public CompoundNBT serialize() {
        return null;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        return false;
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        return false;
    }
    
    @Override
    public String getResearchCategory() {
        return null;
    }
    
    @Override
    public int getInspirationCost() {
        return 0;
    }
    
    @Override
    public String getLocalizedName() {
        return null;
    }
    
    @Override
    public String getLocalizedText() {
        return null;
    }
    
    @Override
    public TheorycraftCard[] getMutations() {
        return null;
    }
    
    @Override
    public boolean isAidOnly() {
        return false;
    }
}


