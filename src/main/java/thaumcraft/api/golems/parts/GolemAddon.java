package thaumcraft.api.golems.parts;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GolemAddon {
	public byte id;
	public String key;
	public String[] research;
	public ITextComponent localizedName;
	public IAddonFunction function;
	
	public GolemAddon(byte id, String key, String[] research, IAddonFunction function) {
		this.id = id;
		this.key = key;
		this.research = research;
		this.localizedName = new TranslationTextComponent("golem.addon." + this.key);
		this.function = function;
	}
	
	public String getLocalizedName() {
		return this.localizedName.getString();
	}
	
	public static GolemAddon[] addons = new GolemAddon[256];
	
	public static void register(GolemAddon addon) {
		if (addons[addon.id] == null) {
			addons[addon.id] = addon;
		}
	}
	
	public interface IAddonFunction {}
}


