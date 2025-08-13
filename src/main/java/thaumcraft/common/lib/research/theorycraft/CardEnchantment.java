package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardEnchantment extends TheorycraftCard
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
        return new TranslationTextComponent("card.enchantment.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.enchantment.text").getString();
    }
    
    @Override
    public String getResearchCategory() {
        return "INFUSION";
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        if (player.experienceLevel >= 5) {
            player.giveExperienceLevels(-5);
            data.addTotal("INFUSION", MathHelper.nextInt(player.getRandom(), 15, 20));
            data.addTotal("AUROMANCY", MathHelper.nextInt(player.getRandom(), 15, 20));
            return true;
        }
        return false;
    }
}

