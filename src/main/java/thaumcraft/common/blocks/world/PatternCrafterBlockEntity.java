package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;

public class PatternCrafterBlockEntity extends TileEntity {
    public PatternCrafterBlockEntity() {
        super(ModBlockEntities.PATTERN_CRAFTER.get());
    }
}

