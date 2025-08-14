package thaumcraft.common.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import thaumcraft.common.blocks.world.ThaumatoriumBlockEntity;
import thaumcraft.common.registers.ModMenus;

public class ThaumatoriumMenu extends Container {
    private final IWorldPosCallable access;
    private final World level;
    private final ThaumatoriumBlockEntity blockEntity;

    private IItemHandler inv;

    public ThaumatoriumMenu(int id, PlayerInventory playerInv, IWorldPosCallable access) {
        super(ModMenus.THAUMATORIUM.get(), id);
        this.access = access;
        this.level = playerInv.player.level;

        BlockPos pos = access.evaluate((lvl, p) -> p).orElse(BlockPos.ZERO);
        TileEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ThaumatoriumBlockEntity)) throw new IllegalStateException("Missing Thaumatorium at " + pos);
        this.blockEntity = (ThaumatoriumBlockEntity) be;
        this.inv = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElseThrow(IllegalStateException::new);

        // Input slot 0
        addSlot(new SlotItemHandler(inv, 0, 56, 35));
        // Output slot 1 (no insert)
        addSlot(new SlotItemHandler(inv, 1, 116, 35) {
            @Override public boolean mayPlace(net.minecraft.item.ItemStack stack) { return false; }
        });

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(access, player, level.getBlockState(access.evaluate((lvl, p) -> p).orElse(BlockPos.ZERO)).getBlock());
    }

    @Override
    public net.minecraft.item.ItemStack quickMoveStack(PlayerEntity player, int index) {
        return net.minecraft.item.ItemStack.EMPTY;
    }

    public int getCookTime() { return blockEntity.getCookTime(); }
    public int getCookTimeTotal() { return blockEntity.getCookTimeTotal(); }
    public int getCookProgressScaled(int width) {
        int total = getCookTimeTotal();
        return total > 0 ? (getCookTime() * width) / total : 0;
    }
}




