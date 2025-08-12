package thaumcraft.common.registers;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
 // Attribute registration handled via EntityAttributeCreationEvent
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.Thaumcraft;
import thaumcraft.common.entities.monster.FireBatEntity;
import thaumcraft.common.entities.monster.EldritchCrabEntity;
import thaumcraft.common.entities.golem.ThaumcraftGolemEntity;

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

    public static final RegistryObject<EntityType<EldritchCrabEntity>> ELDRITCH_CRAB = RegistryManager.ENTITIES.register("eldritch_crab",
            () -> EntityType.Builder.of(EldritchCrabEntity::new, EntityClassification.MONSTER)
                    .sized(0.8f, 0.6f)
                    .build(Thaumcraft.MODID + ":eldritch_crab"));

    public static final RegistryObject<EntityType<ThaumcraftGolemEntity>> TC_GOLEM = RegistryManager.ENTITIES.register("tc_golem",
            () -> EntityType.Builder.of(ThaumcraftGolemEntity::new, EntityClassification.CREATURE)
                    .sized(0.6f, 1.1f)
                    .build(Thaumcraft.MODID + ":tc_golem"));

}


