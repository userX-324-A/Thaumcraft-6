package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardBeacon extends TheorycraftCard
{
    @Override
    public int getInspirationCost() {
        return -2;
    }
    
    @Override
    public boolean isAidOnly() {
        return true;
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.beacon.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.beacon.text").getString();
    }
    
    @Override
    public String getResearchCategory() {
        return "BASICS";
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        ++data.bonusDraws;
        return true;
    }
}


