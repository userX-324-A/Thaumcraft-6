package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class FocusModSplit extends FocusMod {
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

    @Override
    public float getPowerMultiplier() {
        return 0.75f;
    }

    @Override
    public boolean execute() {
        // Execution is handled by FocusEngine which iterates split packages
        return true;
    }

    @Override
    public EnumSupplyType[] mustBeSupplied() {
        return null;
    }

    @Override
    public EnumSupplyType[] willSupply() {
        return null;
    }

    @Override
    public int getComplexity() {
        return 0;
    }

    @Override
    public String getKey() { return "SPLIT"; }

    @Override
    public String getResearch() { return "BASEAUROMANCY"; }
}

