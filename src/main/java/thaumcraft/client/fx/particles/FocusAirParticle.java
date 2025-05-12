package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class FocusAirParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite sprites;
    private final float randomRotationSpeed;
    private final float initialScale;

    protected FocusAirParticle(ClientWorld world, double x, double y, double z, double xd, double yd, double zd, IAnimatedSprite sprites) {
        super(world, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.lifetime = 20 + this.random.nextInt(10);
        this.gravity = -0.1F;
        this.motionX = xd;
        this.motionY = yd;
        this.motionZ = zd;
        // Alpha starts at 0.5 and fades to 0
        this.alpha = 0.5F;

        this.randomRotationSpeed = (this.random.nextFloat() - 0.5F) * 0.5F; // Similar to gaussian / 2
        this.initialScale = (float)(2.0 + this.random.nextGaussian() * 0.5);
        this.particleScale = initialScale;

        this.setSpriteFromAge(sprites); // Initial sprite
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.lifetime) {
            this.setExpired();
            return;
        }

        // Sprite animation (5 frames, index 337 to 341 on a 32xN sheet)
        // In 1.16.5, IAnimatedSprite handles this if the particle JSON is set up correctly.
        // We just need to call setSpriteFromAge.
        this.setSpriteFromAge(sprites);

        this.motionY -= 0.04D * (double)this.gravity;
        this.move(this.motionX, this.motionY, this.motionZ);

        // Slowdown
        this.motionX *= 0.75D;
        this.motionY *= 0.75D;
        this.motionZ *= 0.75D;

        // Alpha fade: 0.5 to 0.0 over lifetime
        this.alpha = 0.5F * (1.0F - ((float)this.age / (float)this.lifetime));

        // Scale: from initialScale to initialScale * 2.0 over lifetime
        this.particleScale = this.initialScale * (1.0F + ((float)this.age / (float)this.lifetime));
        
        // Rotation
        this.prevParticleAngle = this.particleAngle;
        this.particleAngle += this.randomRotationSpeed;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FocusAirParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
} 