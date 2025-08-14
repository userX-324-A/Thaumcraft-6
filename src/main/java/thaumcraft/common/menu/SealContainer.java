package thaumcraft.common.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import thaumcraft.Thaumcraft;

/**
 * Minimal seal container carrying position and face. No slots yet.
 */
public class SealContainer extends Container {
    @ObjectHolder(Thaumcraft.MODID + ":seal")
    public static ContainerType<SealContainer> TYPE;

    private final BlockPos pos;
    private final Direction face;

    public SealContainer(int id, PlayerInventory inv, BlockPos pos, Direction face) {
        super(TYPE, id);
        this.pos = pos;
        this.face = face;
    }

    public static SealContainer fromNetwork(int id, PlayerInventory inv, PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        Direction face = Direction.from3DDataValue(buf.readByte());
        return new SealContainer(id, inv, pos, face);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public BlockPos getPos() { return pos; }
    public Direction getFace() { return face; }
}



