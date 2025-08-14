package thaumcraft.common.lib.research.theorycraft;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
 


public class CardMindOverMatter extends TheorycraftCard
{
    ItemStack stack;
    static ItemStack[] options;
    
    public CardMindOverMatter() {
        stack = ItemStack.EMPTY;
    }
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = super.serialize();
        nbt.put("stack", stack.serializeNBT());
        return nbt;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        super.deserialize(nbt);
        stack = ItemStack.of(nbt.getCompound("stack"));
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        Random r = new Random(getSeed());
        stack = CardMindOverMatter.options[r.nextInt(CardMindOverMatter.options.length)].copy();
        return stack != null;
    }
    
    @Override
    public int getInspirationCost() {
        return 1;
    }
    
    @Override
    public String getResearchCategory() {
        return "ARTIFICE";
    }
    
    @Override
    public String getLocalizedName() {
        return new TranslationTextComponent("card.mindmatter.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent("card.mindmatter.text", getVal()).getString();
    }
    
    private int getVal() { return 10; }
    
    @Override
    public ItemStack[] getRequiredItems() { return null; }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal(getResearchCategory(), getVal());
        return true;
    }
    
    static {
        CardMindOverMatter.options = new ItemStack[] {
                new ItemStack(ItemsTC.visResonator),
                new ItemStack(ItemsTC.thaumometer),
                new ItemStack(net.minecraft.block.Blocks.ANVIL),
                new ItemStack(net.minecraft.block.Blocks.ACTIVATOR_RAIL),
                new ItemStack(net.minecraft.block.Blocks.DISPENSER),
                new ItemStack(net.minecraft.block.Blocks.DROPPER),
                new ItemStack(net.minecraft.block.Blocks.ENCHANTING_TABLE),
                new ItemStack(net.minecraft.block.Blocks.ENDER_CHEST),
                new ItemStack(net.minecraft.block.Blocks.JUKEBOX),
                new ItemStack(net.minecraft.block.Blocks.DAYLIGHT_DETECTOR),
                new ItemStack(net.minecraft.block.Blocks.PISTON),
                new ItemStack(net.minecraft.block.Blocks.HOPPER),
                new ItemStack(net.minecraft.block.Blocks.STICKY_PISTON),
                new ItemStack(Items.MAP),
                new ItemStack(Items.COMPASS),
                new ItemStack(Items.TNT_MINECART),
                new ItemStack(Items.COMPARATOR),
                new ItemStack(Items.CLOCK)
        };
    }
}


