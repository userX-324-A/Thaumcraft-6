package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class FocusModSplit extends FocusNode {
    private List<FocusPackage> packages;
    
    public FocusModSplit() {
        this.packages = new ArrayList<FocusPackage>();
    }
    
    public List<FocusPackage> getSplitPackages() {
        return this.packages;
    }
    
    public void deserialize(CompoundNBT nbt) {
        ListNBT list = nbt.getList("packages", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); ++i) {
            FocusPackage fp = new FocusPackage();
            fp.deserialize(list.getCompound(i));
            this.packages.add(fp);
        }
    }
    
    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (FocusPackage fp : this.packages) {
            list.add(fp.serialize());
        }
        nbt.put("packages", list);
        return nbt;
    }
}

