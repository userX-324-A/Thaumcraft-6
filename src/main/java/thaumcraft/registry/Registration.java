package thaumcraft.registry;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenManagerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ScreenManager;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;
import thaumcraft.world.crafting.arcane.*;

public final class Registration {
    private Registration() {}

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Thaumcraft.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Thaumcraft.MODID);

    // Arcane Workbench
    public static final RegistryObject<Block> ARCANE_WORKBENCH_BLOCK = BLOCKS.register("arcane_workbench", ArcaneWorkbenchBlock::new);
    public static final RegistryObject<Item> ARCANE_WORKBENCH_ITEM = ITEMS.register("arcane_workbench", () -> new BlockItem(ARCANE_WORKBENCH_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<TileEntityType<ArcaneWorkbenchTile>> ARCANE_WORKBENCH_TILE = TILES.register("arcane_workbench", () -> TileEntityType.Builder.of(ArcaneWorkbenchTile::new, ARCANE_WORKBENCH_BLOCK.get()).build(null));

    public static final RegistryObject<ContainerType<ArcaneWorkbenchContainer>> ARCANE_WORKBENCH_CONTAINER = CONTAINERS.register("arcane_workbench", () -> new ContainerType<>(ArcaneWorkbenchContainer::fromNetwork));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TILES.register(modBus);
        CONTAINERS.register(modBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClientOnly {
        public static void initScreens() {
            ScreenManager.register(ARCANE_WORKBENCH_CONTAINER.get(), ArcaneWorkbenchScreen::new);
        }
    }
}



