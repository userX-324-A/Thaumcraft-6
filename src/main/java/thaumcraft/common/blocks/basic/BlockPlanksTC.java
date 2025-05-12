package thaumcraft.common.blocks.basic;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import thaumcraft.common.blocks.BlockTC;

public class BlockPlanksTC extends BlockTC
{
    public BlockPlanksTC() {
        super(AbstractBlock.Properties.of(Material.WOOD)
                .strength(2.0f)
                .sound(SoundType.WOOD)
                .harvestTool(ToolType.AXE).harvestLevel(0)
                .flammable(true)
                .ignitedByLava()
        );
    }
}
