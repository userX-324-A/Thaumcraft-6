package thaumcraft.common.registers;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
 // Attribute registration handled via EntityAttributeCreationEvent
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.Thaumcraft;
import thaumcraft.common.entities.monster.FireBatEntity;

/**
 * Entity type registry (1.16.5).
 */
public final class ModEntities {
    private ModEntities() {}
    public static void init() {}

    public static final RegistryObject<EntityType<FireBatEntity>> FIRE_BAT = RegistryManager.ENTITIES.register("fire_bat",
            () -> EntityType.Builder.of(FireBatEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.5f, 0.9f)
                    .build(Thaumcraft.MODID + ":fire_bat"));

    // Attributes will be registered once FireBat attributes are finalized

}


