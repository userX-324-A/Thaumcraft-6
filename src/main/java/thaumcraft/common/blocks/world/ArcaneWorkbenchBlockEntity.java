package thaumcraft.common.blocks.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.common.registers.ModBlockEntities;

public class ArcaneWorkbenchBlockEntity extends TileEntity {
    public static final int CRAFT_GRID_SLOTS = 9;
    public static final int CRYSTAL_SLOTS = 6;
    public static final int RESULT_SLOT = 1;

    private final ItemStackHandler craftingGrid = new ItemStackHandler(CRAFT_GRID_SLOTS);
    private final ItemStackHandler crystalInput = new ItemStackHandler(CRYSTAL_SLOTS);

    public ArcaneWorkbenchBlockEntity() {
        super(ModBlockEntities.ARCANE_WORKBENCH.get());
    }

    public ItemStackHandler getCraftingGrid() {
        return craftingGrid;
    }

    public ItemStackHandler getCrystalInput() {
        return crystalInput;
    }

    public void read(CompoundNBT tag) {
        super.read(tag);
        craftingGrid.deserializeNBT(tag.getCompound("Grid"));
        crystalInput.deserializeNBT(tag.getCompound("Crystals"));
    }

    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Grid", craftingGrid.serializeNBT());
        tag.put("Crystals", crystalInput.serializeNBT());
        return super.write(tag);
    }
}
