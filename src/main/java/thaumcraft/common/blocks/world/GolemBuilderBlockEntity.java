package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;

public class GolemBuilderBlockEntity extends TileEntity {
    public GolemBuilderBlockEntity() {
        super(ModBlockEntities.GOLEM_BUILDER.get());
    }
}

