package thaumcraft.api.golems.parts;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GolemLeg {
	public byte id;
	public String key;
	public String[] research;
	public ITextComponent localizedName;
	public ILegFunction function;
	
	public GolemLeg(byte id, String key, String[] research, ILegFunction function) {
		this.id = id;
		this.key = key;
		this.research = research;
		this.localizedName = new TranslationTextComponent("golem.leg." + this.key);
		this.function = function;
	}
	
	public String getLocalizedName() {
		return this.localizedName.getString();
	}
	
	public static GolemLeg[] legs = new GolemLeg[256];
	
	public static void register(GolemLeg leg) {
		if (legs[leg.id] == null) {
			legs[leg.id] = leg;
		}
	}
	
	public interface ILegFunction {}
}

