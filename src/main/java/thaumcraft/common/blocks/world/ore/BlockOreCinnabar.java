package thaumcraft.common.blocks.world.ore;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockOreCinnabar extends OreBlock {

    public BlockOreCinnabar() {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(2.0f, 5.0f)
                .sound(SoundType.STONE)
                .harvestLevel(2) // 0: wood, 1: stone, 2: iron, 3: diamond
                .harvestTool(ToolType.PICKAXE)
                .requiresTool(),
            MathHelper.nextInt(new Random(), 0, 2)); // Experience drops 0-2
    }

    // No getDrops override is needed if it drops itself.
    // No custom loot table is strictly needed either; it will default to dropping the block itself.
} 