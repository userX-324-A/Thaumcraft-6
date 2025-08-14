package thaumcraft.api.internal;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class WorldCoordinates implements Comparable<WorldCoordinates> {
    public BlockPos pos;
    public int dim;
    
    public WorldCoordinates() {
    }
    
    public WorldCoordinates(BlockPos pos, int d) {
        this.pos = pos;
        this.dim = d;
    }
    
    public WorldCoordinates(WorldCoordinates par1) {
        this.pos = par1.pos;
        this.dim = par1.dim;
    }
    
    @Override
    public int hashCode() {
        return this.pos.hashCode() + this.dim;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WorldCoordinates) {
            WorldCoordinates p = (WorldCoordinates)obj;
            return this.pos.equals(p.pos) && this.dim == p.dim;
        }
        return false;
    }
    
    @Override
    public int compareTo(WorldCoordinates o) {
        if (this.pos.equals(o.pos)) {
            return this.dim - o.dim;
        }
        return this.pos.compareTo(o.pos);
    }
    
    public void readNBT(CompoundNBT nbt) {
        int x = nbt.getInt("x");
        int y = nbt.getInt("y");
        int z = nbt.getInt("z");
        this.pos = new BlockPos(x, y, z);
        this.dim = nbt.getInt("d");
    }
    
    public void writeNBT(CompoundNBT nbt) {
        nbt.putInt("x", this.pos.getX());
        nbt.putInt("y", this.pos.getY());
        nbt.putInt("z", this.pos.getZ());
        nbt.putInt("d", this.dim);
    }
}


