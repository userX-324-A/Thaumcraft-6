package thaumcraft.common.entities.monster;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Pose;
import net.minecraft.entity.EntitySize;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.ILivingEntityData;

import java.util.Random;

import thaumcraft.common.registers.SoundsTC;

/**
 * Eldritch Crab port (1.16.5). Keeps its signature behavior of leaping, riding targets, and claw attacks.
 */
public class EldritchCrabEntity extends MonsterEntity {

    private static final DataParameter<Boolean> HELM = EntityDataManager.defineId(EldritchCrabEntity.class, DataSerializers.BOOLEAN);
    private int attackCooldownTicks;

    public EldritchCrabEntity(EntityType<? extends MonsterEntity> type, World level) {
        super(type, level);
        this.xpReward = 6;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.63F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HELM, false);
    }

    public boolean hasHelm() { return this.entityData.get(HELM); }
    public void setHelm(boolean value) {
        this.entityData.set(HELM, value);
        // Slightly slower while wearing helm to mirror legacy behavior
        double speed = value ? 0.275D : 0.30D;
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        // Give some armor while helm is on
        this.getAttribute(Attributes.ARMOR).setBaseValue(value ? 5.0D : 0.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Helm", hasHelm());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Helm")) setHelm(tag.getBoolean("Helm"));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.attackCooldownTicks > 0) this.attackCooldownTicks--;

        // Early fall-damage protection after spawn, mirrors old behavior
        if (this.tickCount < 20) this.fallDistance = 0.0F;

        if (!this.level.isClientSide) {
            // Try to ride target if swooping above and conditions met
            if (!this.isPassenger() && this.getTarget() != null && !this.getTarget().isPassenger()
                    && !this.isOnGround() && !this.hasHelm() && this.getTarget().isAlive()
                    && this.getY() - this.getTarget().getY() >= this.getTarget().getBbHeight() * 0.5F
                    && this.distanceToSqr(this.getTarget()) < 4.0D) {
                this.startRiding(this.getTarget(), true);
            }

            // Attack while riding; chance to dismount
            if (this.isPassenger() && this.getVehicle() != null && this.attackCooldownTicks <= 0) {
                this.attackCooldownTicks = 10 + this.getRandom().nextInt(10);
                this.doHurtTarget(this.getVehicle());
                if (this.getRandom().nextFloat() < 0.2F) {
                    this.stopRiding();
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean result = super.doHurtTarget(target);
        if (result) {
            this.playSound(SoundsTC.CRAB_CLAW.get(), 1.0F, 0.9F + this.random.nextFloat() * 0.2F);
        }
        return result;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && this.hasHelm() && this.getHealth() / this.getMaxHealth() <= 0.5F) {
            setHelm(false);
        }
        return result;
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag) {
        ILivingEntityData result = super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
        boolean hard = difficulty.getDifficulty() == net.minecraft.world.Difficulty.HARD;
        if (hard) setHelm(true); else setHelm(this.random.nextFloat() < 0.33F);
        // 10% chance to spawn with random spider potion effect on hard (simulate old behavior)
        if (hard && this.random.nextFloat() < 0.1F) {
            // Borrow Spider group data idea: just increase speed a bit temporarily
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() + 0.05D);
        }
        return result;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundsTC.CRAB_TALK.get(); }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.GENERIC_HURT; }
    @Override
    protected SoundEvent getDeathSound() { return SoundsTC.CRAB_DEATH.get(); }

    @Override
    public CreatureAttribute getMobType() { return CreatureAttribute.ARTHROPOD; }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return 0.35F;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return entity instanceof EldritchCrabEntity || super.isAlliedTo(entity);
    }

    public static boolean canSpawn(EntityType<EldritchCrabEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        // Basic ground spawn predicate; tweak in spawning phase when adding biome spawns
        return world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY() > 0;
    }
}


