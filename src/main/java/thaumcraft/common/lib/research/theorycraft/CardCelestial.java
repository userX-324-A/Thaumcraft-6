package thaumcraft.common.lib.research.theorycraft;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardCelestial extends TheorycraftCard
{
    int md1;
    int md2;
    String cat;
    
    public CardCelestial() {
        cat = "BASICS";
    }
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = super.serialize();
        nbt.putInt("md1", md1);
        nbt.putInt("md2", md2);
        nbt.putString("cat", cat);
        return nbt;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        super.deserialize(nbt);
        md1 = nbt.getInt("md1");
        md2 = nbt.getInt("md2");
        cat = nbt.getString("cat");
    }
    
    @Override
    public String getResearchCategory() {
        return cat;
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        if (data.totalsByCategory.isEmpty() || !ThaumcraftCapabilities.knowsResearch(player, "CELESTIALSCANNING")) {
            return false;
        }
        Random r = new Random(getSeed());
        md1 = MathHelper.nextInt(r, 0, 12);
        md2 = md1;
        while (md1 == md2) {
            md2 = MathHelper.nextInt(r, 0, 12);
        }
        int hVal = 0;
        String hKey = "";
        for (String category : data.totalsByCategory.keySet()) {
            int q = data.totalsByCategory.getOrDefault(category, 0);
            if (q > hVal) {
                hVal = q;
                hKey = category;
            }
        }
        cat = hKey;
        return cat != null;
    }
    
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.celestial.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent(
                "card.celestial.text",
                TextFormatting.BOLD + new TranslationTextComponent("tc.research_category." + cat).getString() + TextFormatting.RESET
        ).getString();
    }
    
    @Override
    public ItemStack[] getRequiredItems() {
        ItemStack a = new ItemStack(ItemsTC.celestialNotes);
        a.getOrCreateTag().putInt("md", md1);
        ItemStack b = new ItemStack(ItemsTC.celestialNotes);
        b.getOrCreateTag().putInt("md", md2);
        return new ItemStack[] { a, b };
    }
    
    @Override
    public boolean[] getRequiredItemsConsumed() {
        return new boolean[] { true, true };
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal(getResearchCategory(), MathHelper.nextInt(player.getRandom(), 25, 50));
        boolean sun = md1 == 0 || md2 == 0;
        boolean moon = md1 > 4 || md2 > 4;
        boolean stars = (md1 > 0 && md1 < 5) || (md2 > 0 && md2 < 5);
        if (stars) {
            int amt = MathHelper.nextInt(player.getRandom(), 0, 5);
            data.addTotal("ELDRITCH", amt * 2);
            ThaumcraftApi.internalMethods.addWarpToPlayer(player, amt, IPlayerWarp.EnumWarpType.TEMPORARY);
        }
        if (moon) {
            ++data.bonusDraws;
        }
        return true;
    }
}

