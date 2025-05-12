package thaumcraft.client.fx.particles;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Thaumcraft.MODID);

    public static final RegistryObject<BasicParticleType> FOCUS_AIR =
            PARTICLE_TYPES.register("focus_air", () -> new BasicParticleType(false));
    // false: not alwaysShowOnServer - this is a client-side visual particle

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
} 