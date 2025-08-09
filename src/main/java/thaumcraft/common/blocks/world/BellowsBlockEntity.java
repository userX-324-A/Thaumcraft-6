package thaumcraft.common.blocks.world;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.registers.ModBlockEntities;

public class BellowsBlockEntity extends TileEntity implements ITickableTileEntity {
    public float inflation = 1.0f;
    private boolean direction = false;
    private boolean firstRun = true;

    public BellowsBlockEntity() {
        super(ModBlockEntities.BELLOWS.get());
    }

    @Override
    public void tick() {
        if (world == null || !world.isRemote) return;
        if (firstRun) {
            inflation = 0.35f + world.rand.nextFloat() * 0.55f;
            firstRun = false;
        }
        if (!direction && inflation > 0.35f) {
            inflation -= 0.075f;
            if (inflation <= 0.35f) direction = true;
        } else if (direction && inflation < 1.0f) {
            inflation += 0.025f;
            if (inflation >= 1.0f) direction = false;
        }
    }
}

