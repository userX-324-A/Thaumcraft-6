package thaumcraft.api.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class PotionVisExhaust extends Effect {
    public PotionVisExhaust(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }
    
    @Override
    public void applyEffectTick(LivingEntity target, int par2) {
    }
}


