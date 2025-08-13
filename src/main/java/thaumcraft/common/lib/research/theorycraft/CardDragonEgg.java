package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardDragonEgg extends TheorycraftCard
{
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public boolean isAidOnly() {
        return true;
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.dragonegg.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.dragonegg.text").getString();
    }
    
    @Override
    public String getResearchCategory() {
        return "BASICS";
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        String[] s = ResearchCategories.researchCategories.keySet().toArray(new String[0]);
        for (int a = 0; a < 10; ++a) {
            String cat = s[player.getRandom().nextInt(s.length)];
            data.addTotal(cat, MathHelper.nextInt(player.getRandom(), 2, 5));
        }
        return true;
    }
}

