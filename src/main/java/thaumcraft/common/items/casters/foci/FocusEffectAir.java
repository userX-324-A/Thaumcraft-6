package thaumcraft.common.items.casters.foci;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.particles.ModParticles;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;


public class FocusEffectAir extends FocusEffect
{
    @Override
    public String getResearch() {
        return "FOCUSELEMENTAL";
    }
    
    @Override
    public String getKey() {
        return "thaumcraft.AIR";
    }
    
    @Override
    public Aspect getAspect() {
        return Aspect.AIR;
    }
    
    @Override
    public int getComplexity() {
        return getSettingValue("power") * 2;
    }
    
    @Override
    public float getDamageForDisplay(float finalPower) {
        return (1 + getSettingValue("power")) * finalPower;
    }
    
    @Override
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        Vector3d hitVec = target.getLocation();
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> 
            new PacketDistributor.TargetPoint(hitVec.x(), hitVec.y(), hitVec.z(), 64.0, getPackage().world.dimension())), 
            new PacketFXFocusPartImpact(hitVec.x(), hitVec.y(), hitVec.z(), new String[] { getKey() })
        );

        getPackage().world.playSound(null, hitVec.x(), hitVec.y(), hitVec.z(), SoundEvents.ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.5f, 0.66f);

        if (target instanceof EntityRayTraceResult) {
            Entity entityHit = ((EntityRayTraceResult)target).getEntity();
            if (entityHit != null) {
                float damage = getDamageForDisplay(finalPower);
                entityHit.attackEntityFrom(DamageSource.thrown(entityHit, getPackage().getCaster()), damage);
                if (entityHit instanceof LivingEntity) {
                    LivingEntity livingEntityHit = (LivingEntity)entityHit;
                    if (trajectory != null) {
                        livingEntityHit.knockBack(getPackage().getCaster(), damage * 0.25f, -trajectory.direction.x(), -trajectory.direction.z());
                    }
                    else {
                        livingEntityHit.knockBack(getPackage().getCaster(), damage * 0.25f, 
                            -MathHelper.sin(entityHit.yRot * ((float)Math.PI / 180.0F)), 
                            MathHelper.cos(entityHit.yRot * ((float)Math.PI / 180.0F)));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public NodeSetting[] createSettings() {
        return new NodeSetting[] { new NodeSetting("power", "focus.common.power", new NodeSetting.NodeSettingIntRange(1, 5)) };
    }
    
    @Override
    public void renderParticleFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
        if (world.isClientSide) {
            world.addParticle(ModParticles.FOCUS_AIR.get(), posX, posY, posZ, motionX, motionY, motionZ);
        }
    }
    
    @Override
    public void onCast(Entity caster) {
        caster.level.playSound(null, caster.blockPosition().above(), SoundsTC.wind, SoundCategory.PLAYERS, 0.125f, 2.0f);
    }
}
