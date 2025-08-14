package thaumcraft.common.tiles.crafting;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import thaumcraft.common.registers.ModBlockEntities;

public class TileCrucible extends TileEntity {
    public TileCrucible(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileCrucible() {
        this(ModBlockEntities.CRUCIBLE.get());
    }
} 

