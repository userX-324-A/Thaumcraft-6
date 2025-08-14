package thaumcraft.common.world.biome.biomes;

import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;

/**
 * Minimal Magical Forest biome stub suitable for registration. Generation is left mostly vanilla forest-like.
 */
public final class MagicalForestBiome {

	private MagicalForestBiome() {}

	public static Biome create() {
		BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder()
			.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);

		DefaultBiomeFeatures.addDefaultOverworldLandStructures(generation);
		DefaultBiomeFeatures.addDefaultCarvers(generation);
		DefaultBiomeFeatures.addDefaultLakes(generation);
		DefaultBiomeFeatures.addDefaultMonsterRoom(generation);
		DefaultBiomeFeatures.addDefaultUndergroundVariety(generation);
		DefaultBiomeFeatures.addDefaultOres(generation);
		DefaultBiomeFeatures.addDefaultSoftDisks(generation);
		DefaultBiomeFeatures.addDefaultMushrooms(generation);
		DefaultBiomeFeatures.addDefaultFlowers(generation);
		DefaultBiomeFeatures.addDefaultGrass(generation);
		DefaultBiomeFeatures.addForestGrass(generation);
		generation.addStructureStart(StructureFeatures.STRONGHOLD);

		MobSpawnInfo.Builder spawns = new MobSpawnInfo.Builder();
		DefaultBiomeFeatures.farmAnimals(spawns);
		DefaultBiomeFeatures.commonSpawns(spawns);

		BiomeAmbience effects = new BiomeAmbience.Builder()
			.fogColor(12638463)
			.waterColor(4159204)
			.waterFogColor(329011)
			.skyColor(7907327)
			.grassColorOverride(0x7bd18b)
			.foliageColorOverride(0x6fcf89)
			.build();

		return new Biome.Builder()
			.precipitation(Biome.RainType.RAIN)
			.biomeCategory(Biome.Category.FOREST)
			.depth(0.125F)
			.scale(0.05F)
			.temperature(0.7F)
			.downfall(0.8F)
			.specialEffects(effects)
			.mobSpawnSettings(spawns.build())
			.generationSettings(generation.build())
			.build();
	}
}



