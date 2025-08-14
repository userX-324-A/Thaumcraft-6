package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardDarkWhispers extends TheorycraftCard
{
    @Override
    public boolean isAidOnly() {
        return true;
    }
    
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getResearchCategory() {
        return "ELDRITCH";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.darkwhisper.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.darkwhisper.text").getString();
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        int l = player.experienceLevel;
        player.giveExperienceLevels(-(10 + l));
        if (l > 0) {
            for (String k : ResearchCategories.researchCategories.keySet()) {
                if (player.getRandom().nextBoolean()) {
                    continue;
                }
                data.addTotal(k, MathHelper.nextInt(player.getRandom(), 0, Math.max(1, (int)Math.sqrt(l))));
            }
        }
        data.addTotal("ELDRITCH", MathHelper.nextInt(player.getRandom(), Math.max(1, l / 5), Math.max(5, l / 2)));
        ThaumcraftApi.internalMethods.addWarpToPlayer(player, Math.max(1, (int)Math.sqrt(l)), IPlayerWarp.EnumWarpType.NORMAL);
        if (player.getRandom().nextBoolean()) {
            ++data.bonusDraws;
        }
        return true;
    }
}


