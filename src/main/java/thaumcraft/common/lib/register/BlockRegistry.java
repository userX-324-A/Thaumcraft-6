package thaumcraft.common.lib.register;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.basic.BlockCandle;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (DyeColor dye : DyeColor.values()) {
            Block block = new BlockCandle("candle_" + dye.getName(), dye);
            event.getRegistry().register(block);
            BlocksTC.candles.put(dye, block);
        }
    }
}


