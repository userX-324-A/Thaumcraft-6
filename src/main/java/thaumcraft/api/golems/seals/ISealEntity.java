package thaumcraft.api.golems.seals;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public interface ISealEntity {

	public void tickSealEntity(World world);

	public ISeal getSeal();
    // Non-API convenience setter used during NBT load
    // Implementations in common may add this, but API remains stable for mods.
    

	public SealPos getSealPos();
    // Non-API convenience setter used during placement or load
    

	public byte getPriority();

	public void setPriority(byte priority);

	public void readNBT(CompoundNBT nbt);

	public CompoundNBT writeNBT();

	public void syncToClient(World world);

	public BlockPos getArea();

	public void setArea(BlockPos v);

	boolean isLocked();

	void setLocked(boolean locked);
	
	public boolean isRedstoneSensitive();

	public void setRedstoneSensitive(boolean redstone);

	String getOwner();

	void setOwner(String owner);
	
	public byte getColor();

	public void setColor(byte color);

	public boolean isStoppedByRedstone(World world);

}
