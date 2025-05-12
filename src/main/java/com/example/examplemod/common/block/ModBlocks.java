package com.example.examplemod.common.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.common.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.core.Direction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.Thaumcraft;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);

    public static final RegistryObject<Block> TAINT_FIBRE = registerBlock("taint_fibre",
            () -> new BlockTaintFibre(BlockBehaviour.Properties.of(ThaumcraftMaterials.MATERIAL_TAINT, MaterialColor.COLOR_PURPLE)
                    .strength(1.0f)
                    .sound(SoundsTC.GORE)
                    .randomTicks()), CreativeModeTab.TAB_MISC); // Using TAB_MISC for now, will change later

    public static final RegistryObject<Block> TAINT_LOG = registerBlock("taint_log",
            () -> new BlockTaintLog(BlockBehaviour.Properties.of(ThaumcraftMaterials.MATERIAL_TAINT, MaterialColor.COLOR_PURPLE)
                    .strength(3.0f, 100.0f)
                    .sound(SoundsTC.GORE)
                    .randomTicks()
                    // .harvestTool(ToolType.AXE) // Replaced by tool tiers in 1.16+
                    // .harvestLevel(0) // Replaced by tool tiers in 1.16+
            ), CreativeModeTab.TAB_BUILDING_BLOCKS); // Placeholder, will update creative tab later

    public static class BlockTaintFibre extends Block {
        public static final BooleanProperty NORTH = BooleanProperty.create("north");
        public static final BooleanProperty EAST = BooleanProperty.create("east");
        public static final BooleanProperty SOUTH = BooleanProperty.create("south");
        public static final BooleanProperty WEST = BooleanProperty.create("west");
        public static final BooleanProperty UP = BooleanProperty.create("up");
        public static final BooleanProperty DOWN = BooleanProperty.create("down");
        public static final BooleanProperty GROWTH1 = BooleanProperty.create("growth1");
        public static final BooleanProperty GROWTH2 = BooleanProperty.create("growth2");
        public static final BooleanProperty GROWTH3 = BooleanProperty.create("growth3");
        public static final BooleanProperty GROWTH4 = BooleanProperty.create("growth4");

        public BlockTaintFibre(Properties properties) {
            super(properties);
            this.registerDefaultState(this.stateDefinition.any()
                    .setValue(NORTH, Boolean.FALSE)
                    .setValue(EAST, Boolean.FALSE)
                    .setValue(SOUTH, Boolean.FALSE)
                    .setValue(WEST, Boolean.FALSE)
                    .setValue(UP, Boolean.FALSE)
                    .setValue(DOWN, Boolean.FALSE)
                    .setValue(GROWTH1, Boolean.FALSE)
                    .setValue(GROWTH2, Boolean.FALSE)
                    .setValue(GROWTH3, Boolean.FALSE)
                    .setValue(GROWTH4, Boolean.FALSE));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, GROWTH1, GROWTH2, GROWTH3, GROWTH4);
        }

        // TODO: Implement loot table, tick behavior, neighborchanged, connections, etc.
    }

    public static class BlockTaintLog extends Block {
        public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class);

        public BlockTaintLog(Properties properties) {
            super(properties);
            this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(AXIS);
        }

        // TODO: Implement die, updateTick, getStateForPlacement, canSustainLeaves, isWood, breakBlock, loot table
        // TODO: Add axe as effective tool via tags
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
} 