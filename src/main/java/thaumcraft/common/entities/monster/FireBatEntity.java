package thaumcraft.common.entities.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;

import java.util.Random;

public class FireBatEntity extends MonsterEntity {

    // Optional state: hanging like vanilla bat
    private static final DataParameter<Boolean> HANGING = EntityDataManager.defineId(FireBatEntity.class, DataSerializers.BOOLEAN);

    public FireBatEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        this.moveControl = new FlyingMovementController(this, 10, true);
        this.setNoGravity(true);
        this.xpReward = 3;
    }

    // Base attributes
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
        // .add(Attributes.FLYING_SPEED, 0.6D); // add later if needed
    }

    @Override
    protected void registerGoals() {
        // Movement/utility
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(6, new FlyWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

        // Combat and avoidance
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0D, 1.3D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.addGoal(1, new SwoopAttackGoal(this, 1.6D));
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        FlyingPathNavigator nav = new FlyingPathNavigator(this, world);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    /**
     * Picks random nearby air positions and flies there.
     */
    static class FlyWanderGoal extends Goal {
        private final FireBatEntity fireBat;
        private final double speedModifier;
        private double targetX;
        private double targetY;
        private double targetZ;

        FlyWanderGoal(FireBatEntity fireBat, double speed) {
            this.fireBat = fireBat;
            this.speedModifier = speed;
            this.setFlags(java.util.EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (fireBat.getTarget() != null) return false; // let attack goal drive movement
            if (fireBat.isVehicle()) return false;
            if (fireBat.random.nextInt(10) != 0) return false;
            return findAirTarget();
        }

        @Override
        public boolean canContinueToUse() {
            return fireBat.getNavigation().isInProgress() && fireBat.getTarget() == null;
        }

        @Override
        public void start() {
            fireBat.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speedModifier);
        }

        private boolean findAirTarget() {
            World world = fireBat.level;
            BlockPos origin = fireBat.blockPosition().above(1);
            java.util.Random rand = fireBat.getRandom();
            for (int i = 0; i < 8; i++) {
                double dx = origin.getX() + (rand.nextInt(9) - 4);
                double dy = origin.getY() + (rand.nextInt(7) - 3);
                double dz = origin.getZ() + (rand.nextInt(9) - 4);
                BlockPos pos = new BlockPos(dx, dy, dz);
                if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above())) {
                    this.targetX = dx + 0.5D;
                    this.targetY = dy + 0.5D;
                    this.targetZ = dz + 0.5D;
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Flies towards the target in a swooping motion and performs melee when in range.
     */
    static class SwoopAttackGoal extends Goal {
        private final FireBatEntity fireBat;
        private final double speedModifier;
        private int cooldown;

        SwoopAttackGoal(FireBatEntity fireBat, double speed) {
            this.fireBat = fireBat;
            this.speedModifier = speed;
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (cooldown > 0) cooldown--;
            LivingEntity target = fireBat.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = fireBat.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void tick() {
            LivingEntity target = fireBat.getTarget();
            if (target == null) return;
            fireBat.getLookControl().setLookAt(target, 30.0F, 30.0F);
            // Aim a bit above the target to simulate a swoop towards the head
            double tx = target.getX();
            double ty = target.getY() + target.getBbHeight() * 0.5D;
            double tz = target.getZ();
            if (!fireBat.getNavigation().isInProgress()) {
                fireBat.getNavigation().moveTo(tx, ty, tz, speedModifier);
            }
            double distSq = fireBat.distanceToSqr(target);
            if (distSq < 2.0D) {
                if (cooldown == 0) {
                    cooldown = 10;
                    fireBat.doHurtTarget(target);
                }
            }
        }

        @Override
        public boolean isInterruptable() { return false; }
    }

    // Data manager for 'hanging'
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HANGING, false);
    }

    public boolean isHanging() { return this.entityData.get(HANGING); }
    public void setHanging(boolean value) { this.entityData.set(HANGING, value); }

    // Persistence
    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hanging", this.isHanging());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Hanging")) this.setHanging(tag.getBoolean("Hanging"));
    }

    // Spawning predicate
    public static boolean canSpawn(EntityType<FireBatEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY() > 0;
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.BAT_AMBIENT; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.BAT_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
}



