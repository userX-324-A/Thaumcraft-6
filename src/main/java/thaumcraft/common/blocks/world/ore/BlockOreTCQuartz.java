package thaumcraft.common.blocks.world.ore;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockOreTCQuartz extends OreBlock {

    public BlockOreTCQuartz() {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(3.0f, 5.0f)
                .sound(SoundType.STONE)
                .harvestLevel(1) // Matches vanilla Nether Quartz Ore harvest level
                .harvestTool(ToolType.PICKAXE)
                .requiresTool(),
            MathHelper.nextInt(new Random(), 2, 5)); // Vanilla Nether Quartz Ore drops 2-5 XP
    }

    // Drops will be handled by a loot table: thaumcraft:ore_quartz
    // to drop minecraft:quartz.
} 