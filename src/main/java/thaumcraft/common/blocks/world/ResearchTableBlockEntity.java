package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;

public class ResearchTableBlockEntity extends TileEntity {
    public ResearchTableBlockEntity() {
        super(ModBlockEntities.RESEARCH_TABLE.get());
    }
} 
