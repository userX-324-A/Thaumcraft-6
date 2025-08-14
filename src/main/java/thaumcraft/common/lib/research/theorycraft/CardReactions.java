package thaumcraft.common.lib.research.theorycraft;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardReactions extends TheorycraftCard
{
    Aspect aspect1;
    Aspect aspect2;
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = super.serialize();
        nbt.putString("aspect1", aspect1.getTag());
        nbt.putString("aspect2", aspect2.getTag());
        return nbt;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        super.deserialize(nbt);
        aspect1 = Aspect.getAspect(nbt.getString("aspect1"));
        aspect2 = Aspect.getAspect(nbt.getString("aspect2"));
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        Random r = new Random(getSeed());
        int num = MathHelper.nextInt(r, 0, Aspect.getCompoundAspects().size() - 1);
        aspect1 = Aspect.getCompoundAspects().get(num);
        int num2;
        for (num2 = num; num2 == num; num2 = MathHelper.nextInt(r, 0, Aspect.getCompoundAspects().size() - 1)) {}
        aspect2 = Aspect.getCompoundAspects().get(num2);
        return true;
    }
    
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getResearchCategory() {
        return "ALCHEMY";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.reactions.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent(
                "card.reactions.text",
                TextFormatting.BOLD + aspect1.getName() + TextFormatting.RESET,
                TextFormatting.BOLD + aspect2.getName() + TextFormatting.RESET
        ).getString();
    }
    
    @Override
    public ItemStack[] getRequiredItems() { return null; }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal(getResearchCategory(), 25);
        if (player.getRandom().nextFloat() < 0.33) {
            data.inspiration += 1;
        }
        return true;
    }
}


