package thaumcraft.common.registers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.Thaumcraft;

/**
 * 1.16.5 sound registry entries.
 * Ensure you have assets/thaumcraft/sounds.json providing the "learn" sound event.
 */
public final class SoundsTC {
    private SoundsTC() {}

    public static final RegistryObject<SoundEvent> LEARN = RegistryManager.SOUNDS.register("learn",
            () -> new SoundEvent(new ResourceLocation(Thaumcraft.MODID, "learn")));

    // Sounds referenced by network messages/FX
    public static final RegistryObject<SoundEvent> WHISPERS = RegistryManager.SOUNDS.register("whispers",
            () -> new SoundEvent(new ResourceLocation(Thaumcraft.MODID, "whispers")));

    public static final RegistryObject<SoundEvent> HEARTBEAT = RegistryManager.SOUNDS.register("heartbeat",
            () -> new SoundEvent(new ResourceLocation(Thaumcraft.MODID, "heartbeat")));

    // Eldritch crab
    public static final RegistryObject<SoundEvent> CRAB_TALK = RegistryManager.SOUNDS.register("crabtalk",
            () -> new SoundEvent(new ResourceLocation(Thaumcraft.MODID, "crabtalk")));
    public static final RegistryObject<SoundEvent> CRAB_DEATH = RegistryManager.SOUNDS.register("crabdeath",
            () -> new SoundEvent(new ResourceLocation(Thaumcraft.MODID, "crabdeath")));
    public static final RegistryObject<SoundEvent> CRAB_CLAW = RegistryManager.SOUNDS.register("crabclaw",
            () -> new SoundEvent(new ResourceLocation(Thaumcraft.MODID, "crabclaw")));
}


