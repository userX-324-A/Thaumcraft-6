package thaumcraft.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;

public class ThaumcraftApiHelper {
    public static boolean areItemStackTagsEqualRelaxed(ItemStack stack0, ItemStack stack1) {
        return false;
    }
    
    public static AspectList getObjectAspects(ItemStack is) {
        return null;
    }
    
    public static AspectList getEntityAspects(Entity entity) {
        return null;
    }
    
    public static RayTraceResult rayTraceIgnoringSource(World world, Vector3d v1, Vector3d v2, boolean bool1, boolean bool2, boolean bool3) {
        return null;
    }
    
    public static boolean isResearchComplete(String username, String researchkey) {
        return false;
    }
    
    public static boolean isResearchKnown(String username, String researchkey) {
        return false;
    }
    
    public static ItemStack getStackInRowAndColumn(Object instance, int row, int col) {
        return null;
    }

    // ---------------------------------------------------------------------
    // Tag-based helpers replacing legacy OreDictionary lookups (1.16.5)
    // ---------------------------------------------------------------------

    private static final Pattern OREDICT_PATTERN = Pattern.compile("^(ingot|nugget|gem|dust|ore|plate|gear|rod|block)([A-Z].*)$");

    /**
     * Convert a legacy OreDictionary name to a Forge item tag id, e.g. ingotCopper -> forge:ingots/copper.
     */
    public static ResourceLocation convertLegacyOreNameToItemTag(String oreName) {
        if (oreName == null || oreName.isEmpty()) return null;
        if ("plankWood".equals(oreName)) return new ResourceLocation("minecraft", "planks");
        if ("blockGlass".equals(oreName)) return new ResourceLocation("forge", "glass");
        if ("leather".equals(oreName)) return new ResourceLocation("forge", "leather");

        Matcher matcher = OREDICT_PATTERN.matcher(oreName);
        if (!matcher.matches()) {
            if (oreName.contains(":")) {
                return new ResourceLocation(oreName);
            }
            return null;
        }

        String kind = matcher.group(1).toLowerCase(Locale.ROOT);
        String material = matcher.group(2);
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

    /**
     * Return all items belonging to the tag derived from the legacy ore name. Empty if tag missing.
     */
    public static List<ItemStack> getOresWithWildCards(String oreName) {
        ResourceLocation tagId = convertLegacyOreNameToItemTag(oreName);
        List<ItemStack> out = new ArrayList<>();
        if (tagId == null) return out;
        ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(tagId);
        if (tag == null) return out;
        for (Item item : tag.getValues()) {
            out.add(new ItemStack(item));
        }
        return out;
    }
}

