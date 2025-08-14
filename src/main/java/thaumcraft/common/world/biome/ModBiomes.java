package thaumcraft.common.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.Thaumcraft;

/**
 * Biome registration and optional injection into the Overworld biome map.
 * Registration is light-weight; actual injection is gated by config flags.
 */
public final class ModBiomes {

	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Thaumcraft.MODID);

	// Minimal Magical Forest stub. Registered so other systems can reference it; injection is gated.
	public static final RegistryObject<Biome> MAGICAL_FOREST = BIOMES.register(
			"magical_forest",
			thaumcraft.common.world.biome.biomes.MagicalForestBiome::create
	);

	private ModBiomes() {}

	public static void register(IEventBus eventBus) {
		BIOMES.register(eventBus);
	}

	@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBusHandlers {
		@SubscribeEvent
		public static void onCommonSetup(FMLCommonSetupEvent event) {
			event.enqueueWork(() -> {
				// Biome injection is deferred; distribution will be wired when magical biomes are fully implemented.
			});
		}
	}
}



