package thaumcraft.api.items;

import net.minecraft.util.math.vector.Vector3d;

public interface IGogglesDisplayExtended {
    default Vector3d getIGogglesTextOffset() {
        return new Vector3d(0.0, 0.0, 0.0);
    }
}


