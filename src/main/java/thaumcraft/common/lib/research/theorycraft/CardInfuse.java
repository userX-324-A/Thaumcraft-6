package thaumcraft.common.lib.research.theorycraft;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class CardInfuse extends TheorycraftCard
{
    Aspect aspect;
    ItemStack stack;
    static ItemStack[] options;
    
    public CardInfuse() {
        stack = ItemStack.EMPTY;
    }
    
    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = super.serialize();
        nbt.putString("aspect", aspect.getTag());
        nbt.put("stack", stack.serializeNBT());
        return nbt;
    }
    
    @Override
    public void deserialize(CompoundNBT nbt) {
        super.deserialize(nbt);
        aspect = Aspect.getAspect(nbt.getString("aspect"));
        stack = ItemStack.of(nbt.getCompound("stack"));
    }
    
    @Override
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        Random r = new Random(getSeed());
        int num = r.nextInt(Aspect.getCompoundAspects().size());
        aspect = Aspect.getCompoundAspects().get(num);
        stack = CardInfuse.options[r.nextInt(CardInfuse.options.length)].copy();
        return aspect != null && stack != null;
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
        return new TranslationTextComponent("card.infuse.name").getString();
    }
    
    @Override
    public String getLocalizedText() {
        return new TranslationTextComponent(
                "card.infuse.text",
                TextFormatting.BOLD + aspect.getName() + TextFormatting.RESET,
                stack.getHoverName(),
                getVal()
        ).getString();
    }
    
    private int getVal() {
        return 10;
    }
    
    @Override
    public ItemStack[] getRequiredItems() { return null; }
    
    @Override
    public boolean[] getRequiredItemsConsumed() {
        return new boolean[] { true, true };
    }
    
    @Override
    public boolean activate(PlayerEntity player, ResearchTableData data) {
        data.addTotal(getResearchCategory(), getVal());
        return true;
    }
    
    static {
        CardInfuse.options = new ItemStack[] {
                new ItemStack(ItemsTC.alumentum),
                new ItemStack(BlocksTC.candles.getOrDefault(DyeColor.YELLOW, net.minecraft.block.Blocks.AIR)),
                new ItemStack(ItemsTC.amber),
                new ItemStack(ItemsTC.brain),
                new ItemStack(ItemsTC.fabric),
                new ItemStack(ItemsTC.salisMundus),
                new ItemStack(ItemsTC.ingots),
                new ItemStack(ItemsTC.ingots),
                new ItemStack(ItemsTC.quicksilver),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.EMERALD),
                new ItemStack(Items.BLAZE_ROD),
                new ItemStack(Items.LEATHER),
                new ItemStack(net.minecraft.block.Blocks.WHITE_WOOL),
                new ItemStack(Items.BRICK),
                new ItemStack(Items.ARROW),
                new ItemStack(Items.EGG),
                new ItemStack(Items.FEATHER),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(Items.REDSTONE),
                new ItemStack(Items.GHAST_TEAR),
                new ItemStack(Items.GUNPOWDER),
                new ItemStack(Items.BOW),
                new ItemStack(Items.GOLDEN_SWORD),
                new ItemStack(Items.IRON_SWORD),
                new ItemStack(Items.IRON_PICKAXE),
                new ItemStack(Items.GOLDEN_PICKAXE),
                new ItemStack(Items.QUARTZ),
                new ItemStack(Items.APPLE)
        };
    }
}

