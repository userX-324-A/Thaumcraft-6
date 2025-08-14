package thaumcraft.common.entities.golem.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.golems.tasks.TaskEngine;

import java.util.EnumSet;

/**
 * Minimal goal that reserves and completes one task as a proof-of-wiring.
 */
public class GolemConsumeTaskGoal extends Goal {
    private final IGolemAPI golem;
    private Task current;
    private int cooldown;

    public GolemConsumeTaskGoal(IGolemAPI golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!(golem.getGolemWorld() instanceof ServerWorld)) return false;
        if (cooldown > 0) { cooldown--; return false; }
        return true;
    }

    @Override
    public void start() {
        ServerWorld world = (ServerWorld) golem.getGolemWorld();
        current = TaskEngine.reserveNext(world, golem.getGolemEntity().getUUID());
        if (current == null) {
            cooldown = 20; // try again in 1s
        }
    }

    @Override
    public boolean canContinueToUse() {
        return current != null;
    }

    @Override
    public void tick() {
        if (current == null) return;
        // Placeholder behavior: instantly complete
        ServerWorld world = (ServerWorld) golem.getGolemWorld();
        TaskEngine.complete(world, current);
        current = null;
        cooldown = 20;
    }
}



