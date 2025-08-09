package thaumcraft.api.damagesource;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

public class DamageSourceThaumcraft extends DamageSource {
    public static DamageSource taint;
    public static DamageSource swarm;
    public static DamageSource tentacle;
    
    public DamageSourceThaumcraft(String par1Str) {
        super(par1Str);
    }
    
    public static DamageSource causeSwarmDamage(LivingEntity par0EntityLiving) {
        return new DamageSource("swarm");
    }
    
    public static DamageSource causeTentacleDamage(LivingEntity par0EntityLiving) {
        return new DamageSource("tentacle");
    }
    
    static {
        DamageSourceThaumcraft.taint = new DamageSourceThaumcraft("taint").setDamageBypassesArmor().setMagicDamage();
        DamageSourceThaumcraft.swarm = new DamageSource("swarm");
        DamageSourceThaumcraft.tentacle = new DamageSource("tentacle");
    }
}

