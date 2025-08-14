package thaumcraft.common.entities.misc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class GrappleProjectileEntity extends ThrowableEntity {
    public GrappleProjectileEntity(EntityType<? extends GrappleProjectileEntity> type, World world) { super(type, world); }
    public GrappleProjectileEntity(World world, net.minecraft.entity.LivingEntity owner) { super(thaumcraft.common.registers.ModEntities.GRAPPLE_PROJECTILE.get(), owner, world); }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!level.isClientSide) {
            if (this.getOwner() instanceof net.minecraft.entity.player.PlayerEntity) {
                ((net.minecraft.entity.player.PlayerEntity) this.getOwner()).playSound(thaumcraft.common.registers.SoundsTC.GRAPPLE_IMPACT.get(), 0.9f, 1.0f);
            }
            this.remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        super.onHitEntity(result);
        if (!level.isClientSide) {
            if (this.getOwner() instanceof net.minecraft.entity.player.PlayerEntity) {
                ((net.minecraft.entity.player.PlayerEntity) this.getOwner()).playSound(thaumcraft.common.registers.SoundsTC.GRAPPLE_IMPACT.get(), 0.9f, 1.0f);
            }
            this.remove();
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        // Simple rope constraint: apply pull when beyond max length; allow reel-in/out via sneak/jump
        if (!level.isClientSide && this.getOwner() instanceof net.minecraft.entity.LivingEntity) {
            net.minecraft.entity.LivingEntity owner = (net.minecraft.entity.LivingEntity) this.getOwner();
            double dx = this.getX() - owner.getX();
            double dy = this.getY() - owner.getEyeY();
            double dz = this.getZ() - owner.getZ();
            double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
            double maxLength = thaumcraft.common.config.ModConfig.COMMON.grappleMaxLength.get();
            double reelSpeed = thaumcraft.common.config.ModConfig.COMMON.grappleReelSpeedPerTick.get();
            boolean mitigateFall = thaumcraft.common.config.ModConfig.COMMON.grappleMitigateFallDamage.get();

            // Basic reel controls: crouch to reel in, jump to reel out up to configured max
            if (owner.isCrouching()) maxLength = Math.max(2.0, maxLength - reelSpeed);
            if (owner.isOnGround()) {
                // reset any accumulated fall distance when grounded
                owner.fallDistance = 0;
            }
            if (dist > maxLength) {
                double excess = dist - maxLength;
                double pull = Math.min(0.08, 0.02 + excess * 0.01);
                owner.setDeltaMovement(owner.getDeltaMovement().add(dx / dist * pull, dy / dist * pull, dz / dist * pull));
                owner.hurtMarked = true;
                if (mitigateFall && owner.getDeltaMovement().y < 0) {
                    owner.fallDistance = 0;
                }
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}



