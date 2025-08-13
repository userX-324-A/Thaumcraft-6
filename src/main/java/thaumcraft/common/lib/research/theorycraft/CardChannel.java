package thaumcraft.common.lib.research.theorycraft;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
 


public class CardChannel extends TheorycraftCard
{
    Aspect aspect;
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = super.serialize();
        nbt.putString("aspect", aspect.getTag());
        return nbt;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        super.deserialize(nbt);
        aspect = Aspect.getAspect(nbt.getString("aspect"));
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        Random r = new Random(getSeed());
        int num = r.nextInt(Aspect.getCompoundAspects().size());
        aspect = Aspect.getCompoundAspects().get(num);
        return true;
    }
    
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getResearchCategory() {
        return "INFUSION";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent(
                "card.channel.name",
                TextFormatting.DARK_BLUE + aspect.getName() + TextFormatting.RESET + "" + TextFormatting.BOLD
        ).getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent(
                "card.channel.text",
                TextFormatting.BOLD + aspect.getName() + TextFormatting.RESET
        ).getString();
    }
    
    @Override
    public ItemStack[] getRequiredItems() { return null; }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal(getResearchCategory(), 25);
        return true;
    }
}

