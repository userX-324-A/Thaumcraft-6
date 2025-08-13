package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardSynergy extends TheorycraftCard
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
        return new TranslationTextComponent("card.synergy.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.synergy.text").getString();
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        int tot = 0;
        tot += data.totalsByCategory.getOrDefault("ARTIFICE", 0);
        tot += data.totalsByCategory.getOrDefault("ALCHEMY", 0);
        tot += data.totalsByCategory.getOrDefault("INFUSION", 0);
        return tot >= 15;
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        int tot = 0;
        tot += data.totalsByCategory.getOrDefault("ARTIFICE", 0);
        tot += data.totalsByCategory.getOrDefault("ALCHEMY", 0);
        tot += data.totalsByCategory.getOrDefault("INFUSION", 0);
        if (tot >= 15) {
            tot = 15;
            String[] cats = { "ARTIFICE", "ALCHEMY", "INFUSION" };
            int tries = 0;
            while (tot > 0 && tries < 1000) {
                ++tries;
                for (String category : cats) {
                    int cur = data.totalsByCategory.getOrDefault(category, 0);
                    if (cur > 0) {
                        data.totalsByCategory.put(category, cur - 1);
                        if (--tot <= 0) {
                            break;
                        }
                    }
                }
            }
            data.addTotal("GOLEMANCY", 30);
            return true;
        }
        return false;
    }
}

