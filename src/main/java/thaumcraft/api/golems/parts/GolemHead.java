package thaumcraft.api.golems.parts;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GolemHead {
	public byte id;
	public String key;
	public String[] research;
	public ITextComponent localizedName;
	public IHeadFunction function;
	
	public GolemHead(byte id, String key, String[] research, IHeadFunction function) {
		this.id = id;
		this.key = key;
		this.research = research;
		this.localizedName = new TranslationTextComponent("golem.head." + this.key);
		this.function = function;
	}
	
	public String getLocalizedName() {
		return this.localizedName.getString();
	}
	
	public static GolemHead[] heads = new GolemHead[256];
	
	public static void register(GolemHead head) {
		if (heads[head.id] == null) {
			heads[head.id] = head;
		}
	}
	
	public interface IHeadFunction {}
}


