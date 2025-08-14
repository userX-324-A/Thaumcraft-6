package thaumcraft.common.worldgen.structure;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.gen.feature.structure.Structure;
import thaumcraft.Thaumcraft;

/**
 * Placeholder structure registry for Obelisks, Mounds, and Eldritch Dungeons.
 * Actual registration/injection will be implemented later and gated by config flags.
 */
public final class ModStructures {

	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Thaumcraft.MODID);

	private ModStructures() {}

	public static void register(IEventBus eventBus) {
		STRUCTURES.register(eventBus);
	}
}



