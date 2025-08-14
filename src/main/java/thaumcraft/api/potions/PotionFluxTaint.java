package thaumcraft.api.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PotionFluxTaint extends Effect {
    public PotionFluxTaint(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }
    
    @Override
    public void applyEffectTick(LivingEntity target, int strength) {
        if (target instanceof PlayerEntity) {
            target.hurt(DamageSource.MAGIC, 1.0f);
        }
    }
}


