package thaumcraft.api.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.util.IndirectEntityDamageSource;

public class DamageSourceIndirectThaumcraftEntity extends IndirectEntityDamageSource {
    public DamageSourceIndirectThaumcraftEntity(String par1Str, Entity par2Entity, Entity par3Entity) {
        super(par1Str, par2Entity, par3Entity);
    }
}

