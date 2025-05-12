package com.example.examplemod.common.item;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.Thaumcraft;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
} 