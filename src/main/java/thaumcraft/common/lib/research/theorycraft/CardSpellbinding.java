package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardSpellbinding extends TheorycraftCard
{
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getResearchCategory() {
        return "AUROMANCY";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.spellbinding.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.spellbinding.text").getString();
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        return player.experienceLevel > 0;
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        if (player.experienceLevel <= 0) {
            return false;
        }
        int l = Math.min(5, player.experienceLevel);
        data.addTotal(getResearchCategory(), l * 5);
        player.giveExperienceLevels(-l);
        return true;
    }
}


