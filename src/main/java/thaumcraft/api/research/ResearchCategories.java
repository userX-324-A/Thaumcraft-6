package thaumcraft.api.research;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResearchCategories {
    public static Map<String, ResearchCategory> researchCategories;
    
    public static void registerCategory(String key, ResourceLocation icon, ResourceLocation background) {
        if (getResearchCategory(key) == null) {
            ResearchCategory rl = new ResearchCategory(key, null, icon, background);
            researchCategories.put(key, rl);
        }
    }
    
    public static ResearchCategory getResearchCategory(String key) {
        return researchCategories.get(key);
    }
    
    public static ITextComponent getCategoryName(String key) {
        return new TranslationTextComponent("tc.research_category." + key);
    }
    
    static {
        researchCategories = new HashMap<String, ResearchCategory>();
    }
}



