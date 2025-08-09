package thaumcraft.api.casters;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NodeSetting {
    private int min;
    private int max;
    private String key;
    private String research;
    
    public NodeSetting(String key, String research, int min, int max) {
        this.key = key;
        this.research = research;
        this.min = min;
        this.max = max;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getMax() {
        return this.max;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getResearch() {
        return this.research;
    }
    
    public ITextComponent getLocalizedName() {
        return new TranslationTextComponent("nodesetting." + this.key);
    }
    
    public ITextComponent getLocalizedText() {
        return new TranslationTextComponent("nodesetting." + this.key + ".text");
    }
}

