package thaumcraft.common.lib.research.theorycraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardAwareness extends TheorycraftCard
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
        return new TranslationTextComponent("card.awareness.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.awareness.text").getString();
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal(getResearchCategory(), 20);
        if (player.getRandom().nextFloat() < 0.33) {
            data.addTotal("ELDRITCH", MathHelper.nextInt(player.getRandom(), 1, 5));
            ThaumcraftApi.internalMethods.addWarpToPlayer(player, 1, IPlayerWarp.EnumWarpType.NORMAL);
        }
        return true;
    }
}


