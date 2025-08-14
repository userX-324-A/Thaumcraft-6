package thaumcraft.common.lib.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

/**
 * Utilities to migrate legacy OreDictionary lookups to the modern Tag system.
 */
public final class TagUtils {

    private TagUtils() {}

    private static final Pattern OREDICT_PATTERN = Pattern.compile("^(ingot|nugget|gem|dust|ore|plate|gear|rod|block)([A-Z].*)$");

    public static boolean isBlockTaggedAsOre(Block block) {
        return Tags.Blocks.ORES.contains(block);
    }

    public static boolean isItemStackInTag(ItemStack stack, ResourceLocation tagId) {
        if (stack == null || stack.isEmpty()) return false;
        ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(tagId);
        return tag != null && tag.contains(stack.getItem());
    }

    public static boolean isBlockInTag(Block block, ResourceLocation tagId) {
        ITag<Block> tag = TagCollectionManager.getInstance().getBlocks().getTag(tagId);
        return tag != null && tag.contains(block);
    }

    public static NonNullList<ItemStack> getItemsForOreName(String oreName) {
        ResourceLocation tagId = convertLegacyOreNameToItemTag(oreName);
        NonNullList<ItemStack> out = NonNullList.create();
        if (tagId == null) {
            return out;
        }
        ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(tagId);
        if (tag == null) {
            return out;
        }
        for (Item item : tag.getValues()) {
            out.add(new ItemStack(item));
        }
        return out;
    }

    /**
     * Returns true if the two stacks share at least one Forge item tag. Mirrors the
     * semantics of "ore dictionary equality" used previously.
     */
    public static boolean itemsShareAnyForgeTag(ItemStack a, ItemStack b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) return false;
        Set<ResourceLocation> aTags = getForgeOwningTags(a.getItem());
        if (aTags.isEmpty()) return false;
        Set<ResourceLocation> bTags = getForgeOwningTags(b.getItem());
        if (bTags.isEmpty()) return false;
        for (ResourceLocation rl : aTags) {
            if (bTags.contains(rl)) return true;
        }
        return false;
    }

    private static Set<ResourceLocation> getForgeOwningTags(Item item) {
        Set<ResourceLocation> out = new HashSet<>();
        // Forge exposes item.getTags() in 1.16.5 which lists all tag ids this item belongs to
        for (ResourceLocation rl : item.getTags()) {
            if ("forge".equals(rl.getNamespace())) {
                out.add(rl);
            }
        }
        return out;
    }

    /**
     * Best-effort conversion of common OreDictionary names to Forge item tag ids.
     * Examples:
     *  - ingotCopper -> forge:ingots/copper
     *  - nuggetIron -> forge:nuggets/iron
     *  - oreCinnabar -> forge:ores/cinnabar
     *  - gemAmber -> forge:gems/amber
     *  - dustGlowstone -> forge:dusts/glowstone
     *  - plateBrass -> forge:plates/brass
     *  - blockBrass -> forge:storage_blocks/brass
     *  - plankWood -> minecraft:planks (fallback to forge:planks if needed)
     *  - blockGlass -> forge:glass
     */
    public static ResourceLocation convertLegacyOreNameToItemTag(String oreName) {
        if (oreName == null || oreName.isEmpty()) return null;
        // Special well-known cases
        if ("plankWood".equals(oreName)) return new ResourceLocation("minecraft", "planks");
        if ("blockGlass".equals(oreName)) return new ResourceLocation("forge", "glass");
        if ("leather".equals(oreName)) return new ResourceLocation("forge", "leather");

        Matcher m = OREDICT_PATTERN.matcher(oreName);
        if (!m.matches()) {
            // Fallback: treat as a direct namespaced tag id if provided (e.g. "forge:XYZ")
            if (oreName.contains(":")) {
                return new ResourceLocation(oreName);
            }
            return null;
        }
        String kind = m.group(1).toLowerCase(Locale.ROOT);
        String material = m.group(2);
        String matPath = material.substring(0, 1).toLowerCase(Locale.ROOT) + material.substring(1);

        String family;
        switch (kind) {
            case "ingot": family = "ingots"; break;
            case "nugget": family = "nuggets"; break;
            case "gem": family = "gems"; break;
            case "dust": family = "dusts"; break;
            case "ore": family = "ores"; break;
            case "plate": family = "plates"; break;
            case "gear": family = "gears"; break;
            case "rod": family = "rods"; break;
            case "block": family = "storage_blocks"; break;
            default: family = kind + "s"; break;
        }

        return new ResourceLocation("forge", family + "/" + matPath);
    }
}




