package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardScripting extends TheorycraftCard
{
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getResearchCategory() {
        return "GOLEMANCY";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.scripting.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.scripting.text").getString();
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        // Research table tile not yet ported; make this a simple bonus card for now
        data.addTotal(getResearchCategory(), 10);
        return true;
    }
}


