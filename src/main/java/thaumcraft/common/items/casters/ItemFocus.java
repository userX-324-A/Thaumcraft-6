package thaumcraft.common.items.casters;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.casters.FocusMediumRoot;
import thaumcraft.api.casters.FocusModSplit;
import thaumcraft.api.casters.FocusNode;
import thaumcraft.api.casters.FocusPackage;
import thaumcraft.api.casters.IFocusElement;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.common.items.ItemTCBase;


public class ItemFocus extends ItemTCBase
{
    private int maxComplexity;
    
    public ItemFocus(String name, int complexity, Item.Properties props) {
        super(name, props);
        maxComplexity = complexity;
    }
    
    public int getFocusColor(ItemStack focusstack) {
        if (focusstack == null || focusstack.isEmpty() || focusstack.getTagCompound() == null) {
            return 16777215;
        }
        int color = 16777215;
        if (!focusstack.getTagCompound().contains("color")) {
            FocusPackage core = getPackage(focusstack);
            if (core != null) {
                FocusEffect[] fe = core.getFocusEffects();
                int r = 0;
                int g = 0;
                int b = 0;
                for (FocusEffect ef : fe) {
                    Color c = new Color(FocusEngine.getElementColor(ef.getKey()));
                    r += c.getRed();
                    g += c.getGreen();
                    b += c.getBlue();
                }
                if (fe.length > 0) {
                    r /= fe.length;
                    g /= fe.length;
                    b /= fe.length;
                }
                Color c2 = new Color(r, g, b);
                color = c2.getRGB();
                focusstack.getOrCreateTag().putInt("color", color);
            }
        }
        else {
            color = focusstack.getTagCompound().getInt("color");
        }
        return color;
    }
    
    public String getSortingHelper(ItemStack focusstack) {
        if (focusstack == null || focusstack.isEmpty() || !focusstack.hasTag()) {
            return null;
        }
        int sh = focusstack.getOrCreateTag().getInt("srt");
        if (sh == 0) {
            FocusPackage focusPackage = getPackage(focusstack);
            if (focusPackage != null) {
                sh = focusPackage.getSortingHelper();
                focusstack.getOrCreateTag().putInt("srt", sh);
            }
        }
        return focusstack.getDisplayName().getString() + sh;
    }
    
    public static void setPackage(ItemStack focusstack, FocusPackage core) {
        NBTTagCompound tag = core.serialize();
        focusstack.addTagElement("package", tag);
    }
    
    public static FocusPackage getPackage(ItemStack focusstack) {
        if (focusstack == null || focusstack.isEmpty()) {
            return null;
        }
        NBTTagCompound tag = focusstack.getShareTag() != null ? focusstack.getShareTag().getCompound("package") : null;
        if (tag != null && !tag.isEmpty()) {
            FocusPackage p = new FocusPackage();
            p.deserialize(tag);
            return p;
        }
        return null;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addFocusInformation(stack, worldIn, tooltip, flagIn);
    }
    
    public void addFocusInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        FocusPackage p = getPackage(stack);
        if (p != null) {
            float al = getVisCost(stack);
            String amount = ItemStack.DECIMALFORMAT.format(al);
            tooltip.add(new StringTextComponent(amount + " ").append(new TranslationTextComponent("item.Focus.cost1")));
            for (IFocusElement fe : p.nodes) {
                if (fe instanceof FocusNode && !(fe instanceof FocusMediumRoot)) {
                    buildInfo(tooltip, (FocusNode)fe, 0);
                }
            }
        }
    }
    
    private void buildInfo(List<ITextComponent> list, FocusNode node, int depth) {
        if (node instanceof FocusNode && !(node instanceof FocusMediumRoot)) {
            String indentation = "";
            for (int a = 0; a < depth; ++a) {
                indentation += "  ";
            }

            MutableTextComponent lineComponent = new StringTextComponent(indentation);

            lineComponent.append(new TranslationTextComponent(node.getUnlocalizedName()).withStyle(TextFormatting.DARK_PURPLE));

            if (node.getSettingList() != null && !node.getSettingList().isEmpty()) {
                lineComponent.append(new StringTextComponent(" [").withStyle(TextFormatting.DARK_AQUA));
                boolean firstSetting = true;
                for (String st : node.getSettingList()) {
                    NodeSetting ns = node.getSetting(st);
                    if (ns != null) {
                        if (!firstSetting) {
                            lineComponent.append(new StringTextComponent(", ").withStyle(TextFormatting.DARK_AQUA));
                        }
                        lineComponent.append(new StringTextComponent(ns.getLocalizedName() + " " + ns.getValueText()).withStyle(TextFormatting.DARK_AQUA));
                        firstSetting = false;
                    }
                }
                lineComponent.append(new StringTextComponent("]").withStyle(TextFormatting.DARK_AQUA));
            }
            list.add(lineComponent);

            if (node instanceof FocusModSplit) {
                FocusModSplit split = (FocusModSplit)node;
                if (split.getSplitPackages() != null) {
                    for (FocusPackage p : split.getSplitPackages()) {
                        if (p != null && p.nodes != null) {
                            for (IFocusElement fe : p.nodes) {
                                if (fe instanceof FocusNode && !(fe instanceof FocusMediumRoot)) {
                                    buildInfo(list, (FocusNode)fe, depth + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public float getVisCost(ItemStack focusstack) {
        FocusPackage p = getPackage(focusstack);
        return (p == null) ? 0.0f : (p.getComplexity() / 5.0f);
    }
    
    public int getActivationTime(ItemStack focusstack) {
        FocusPackage p = getPackage(focusstack);
        return (p == null) ? 0 : Math.max(5, p.getComplexity() / 5 * (p.getComplexity() / 4));
    }
    
    public int getMaxComplexity() {
        return maxComplexity;
    }
}
