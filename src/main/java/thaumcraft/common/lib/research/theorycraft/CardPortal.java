package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardPortal extends TheorycraftCard
{
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) { return true; }
    @Override
    public boolean isAidOnly() {
        return true;
    }
    
    @Override
    public int getInspirationCost() {
        return -1;
    }
    
    @Override
    public String getResearchCategory() {
        return "ELDRITCH";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.portal.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.portal.text").getString();
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        String[] s = ResearchCategories.researchCategories.keySet().toArray(new String[0]);
        data.addTotal(s[player.getRandom().nextInt(s.length)], MathHelper.nextInt(player.getRandom(), 5, 10));
        data.addTotal(s[player.getRandom().nextInt(s.length)], MathHelper.nextInt(player.getRandom(), 5, 10));
        data.addTotal("ELDRITCH", MathHelper.nextInt(player.getRandom(), 5, 10));
        data.bonusDraws += 2;
        ThaumcraftApi.internalMethods.addWarpToPlayer(player, 5, IPlayerWarp.EnumWarpType.TEMPORARY);
        ThaumcraftApi.internalMethods.addWarpToPlayer(player, 1, IPlayerWarp.EnumWarpType.NORMAL);
        return true;
    }
}


