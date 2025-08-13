package thaumcraft.api.casters;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class FocusMediumRoot extends FocusMedium {
    private Trajectory[] trajectories;
    private RayTraceResult[] targets;

    public FocusMediumRoot() {
        super();
    }

    public FocusMediumRoot(Trajectory[] trajectories, RayTraceResult[] targets) {
        super();
        this.trajectories = trajectories;
        this.targets = targets;
    }

    @Override
    public String getKey() { return "ROOT"; }

    @Override
    public String getResearch() { return "BASEAUROMANCY"; }

    @Override
    public int getComplexity() { return 0; }

    @Override
    public EnumSupplyType[] willSupply() {
        return new EnumSupplyType[] { EnumSupplyType.TARGET, EnumSupplyType.TRAJECTORY };
    }

    @Override
    public RayTraceResult[] supplyTargets() {
        return targets;
    }

    @Override
    public Trajectory[] supplyTrajectories() {
        return trajectories;
    }

    public void setupFromCaster(LivingEntity caster) {
        Vector3d sv = caster.position().add(0.0, caster.getEyeHeight() - 0.1, 0.0);
        Vector3d look = caster.getLookAngle();
        this.trajectories = new Trajectory[] { new Trajectory(sv, look) };
        // In 1.16 there is no direct RayTraceResult-from-entity ctor. Provide null targets for effects that don't need it.
        this.targets = null;
    }

    public Vector3d getTarget() {
        return trajectories != null && trajectories.length > 0 ? trajectories[0].direction : null;
    }
}

