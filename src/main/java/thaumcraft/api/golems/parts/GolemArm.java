package thaumcraft.api.golems.parts;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.golems.IGolemAPI;

public class GolemArm {
	public byte id;
	public String key;
	public String[] research;
	public ITextComponent localizedName;
	public IArmFunction function;
	
	public GolemArm(byte id, String key, String[] research, IArmFunction function) {
		this.id = id;
		this.key = key;
		this.research = research;
		this.localizedName = new TranslationTextComponent("golem.arm." + this.key);
		this.function = function;
	}
	
	public String getLocalizedName() {
		return this.localizedName.getString();
	}
	
	public static GolemArm[] arms = new GolemArm[256];
	
	public static void register(GolemArm arm) {
		if (arms[arm.id] == null) {
			arms[arm.id] = arm;
		}
	}
	
	public interface IArmFunction {
		boolean canAttack();
		
		void onMeleeAttack(IGolemAPI p0);
		
		void onRangedAttack(IGolemAPI p0, LivingEntity p1, float p2);
		
		RangedAttackGoal getRangedAttackAI(IRangedAttackMob p0);
	}
}


