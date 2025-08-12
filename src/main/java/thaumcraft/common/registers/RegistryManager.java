package thaumcraft.common.registers;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;

public class RegistryManager {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Thaumcraft.MODID);
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Thaumcraft.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Thaumcraft.MODID);
    public static final DeferredRegister<ContainerType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Thaumcraft.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Thaumcraft.MODID);


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        ENTITIES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        MENUS.register(eventBus);
        SOUNDS.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = thaumcraft.Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusHandlers {
        @SubscribeEvent
        public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(thaumcraft.common.registers.ModEntities.FIRE_BAT.get(),
                    thaumcraft.common.entities.monster.FireBatEntity.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = thaumcraft.Thaumcraft.MODID)
    public static class ForgeBusHandlers {
        @SubscribeEvent
        public static void onBiomeLoading(BiomeLoadingEvent event) {
            // Nether spawns baseline
            if (event.getCategory() == net.minecraft.world.biome.Biome.Category.NETHER) {
                event.getSpawns().getSpawner(net.minecraft.entity.EntityClassification.MONSTER).add(
                        new net.minecraft.world.biome.MobSpawnInfo.Spawners(
                                thaumcraft.common.registers.ModEntities.FIRE_BAT.get(), 15, 1, 3));
            }
            // Seasonal overworld spawns on Oct 31
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int month = calendar.get(java.util.Calendar.MONTH) + 1; // 1..12
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
            if (month == 10 && day == 31) {
                switch (event.getCategory()) {
                    case PLAINS:
                    case FOREST:
                    case SWAMP:
                    case TAIGA:
                    case EXTREME_HILLS:
                    case SAVANNA:
                    case JUNGLE:
                    case DESERT:
                    case MUSHROOM:
                    case MESA:
                    case BEACH:
                    case RIVER:
                        event.getSpawns().getSpawner(net.minecraft.entity.EntityClassification.MONSTER).add(
                                new net.minecraft.world.biome.MobSpawnInfo.Spawners(
                                        thaumcraft.common.registers.ModEntities.FIRE_BAT.get(), 5, 1, 2));
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
