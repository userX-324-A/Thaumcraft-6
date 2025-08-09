package thaumcraft.api.golems.parts;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GolemMaterial {
	public String key;
	public String[] research;
	public int itemColor;
	public int healthMod;
	public int armorMod;
	public int damageMod;
	public ITextComponent localizedName;
	
	public GolemMaterial(String key, String[] research, int itemColor, int healthMod, int armorMod, int damageMod) {
		this.key = key;
		this.research = research;
		this.itemColor = itemColor;
		this.localizedName = new TranslationTextComponent("golem.material." + this.key);
		this.healthMod = healthMod;
		this.armorMod = armorMod;
		this.damageMod = damageMod;
	}
	
	public String getLocalizedName() {
		return this.localizedName.getString();
	}
}

