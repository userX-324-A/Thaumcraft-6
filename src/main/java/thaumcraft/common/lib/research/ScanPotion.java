package thaumcraft.common.lib.research;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import thaumcraft.api.research.IScanThing;
import thaumcraft.api.research.ScanningManager;


public class ScanPotion implements IScanThing
{
    Effect effect;
    
    public ScanPotion(Effect effect) {
        this.effect = effect;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        return getEffectInstance(player, obj) != null;
    }
    
    private EffectInstance getEffectInstance(PlayerEntity player, Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LivingEntity) {
            LivingEntity e = (LivingEntity)obj;
            for (EffectInstance effectInstance : e.getActiveEffects()) {
                if (effectInstance.getEffect() == effect) {
                    return effectInstance;
                }
            }
        }
        else {
            ItemStack is = ScanningManager.getItemFromParms(player, obj);
            if (is != null && !is.isEmpty()) {
                for (EffectInstance effectInstance : PotionUtils.getMobEffects(is)) {
                    if (effectInstance.getEffect() == effect) {
                        return effectInstance;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object obj) {
        return "!" + effect.getRegistryName();
    }
}


