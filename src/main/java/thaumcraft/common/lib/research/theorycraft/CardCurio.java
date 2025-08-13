package thaumcraft.common.lib.research.theorycraft;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardCurio extends TheorycraftCard
{
    ItemStack curio;
    
    public CardCurio() {
        curio = ItemStack.EMPTY;
    }
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = super.serialize();
        nbt.put("stack", curio.serializeNBT());
        return nbt;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        super.deserialize(nbt);
        curio = ItemStack.of(nbt.getCompound("stack"));
    }
    
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.curio.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.curio.text").getString();
    }
    
    @Override
    public String getResearchCategory() {
        return "BASICS";
    }
    
    @Override
    public ItemStack[] getRequiredItems() { return null; }
    
    @Override
    public boolean[] getRequiredItemsConsumed() { return null; }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        Random r = new Random(getSeed());
        // Minimal port: no curio item filtering yet; just succeed
        return true;
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal("BASICS", 5);
        String[] s = ResearchCategories.researchCategories.keySet().toArray(new String[0]);
        data.addTotal(s[player.getRandom().nextInt(s.length)], 5);
        data.addTotal("BASICS", MathHelper.nextInt(player.getRandom(), 25, 35));
        if (player.getRandom().nextBoolean()) {
            ++data.bonusDraws;
        }
        if (player.getRandom().nextBoolean()) {
            ++data.bonusDraws;
        }
        return true;
    }
}

