package thaumcraft.api.casters;

import net.minecraft.util.math.vector.Vector3d;

public class Trajectory {
    public Vector3d source;
    public Vector3d direction;
    
    public Trajectory(Vector3d source, Vector3d direction) {
        this.source = source;
        this.direction = direction;
    }
}

