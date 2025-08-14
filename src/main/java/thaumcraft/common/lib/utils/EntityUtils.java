package thaumcraft.common.lib.utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
// import net.minecraftforge.fml.common.FMLCommonHandler;
// import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
// import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss; // pending entity port
// import thaumcraft.common.entities.monster.mods.ChampionModifier; // pending port


public class EntityUtils
{
    public static AttributeModifier CHAMPION_HEALTH;
    public static AttributeModifier CHAMPION_DAMAGE;
    public static AttributeModifier BOLDBUFF;
    public static AttributeModifier MIGHTYBUFF;
    public static AttributeModifier[] HPBUFF;
    public static AttributeModifier[] DMGBUFF;
    
    public static boolean isFriendly(Entity source, Entity target) {
        if (source == null || target == null) {
            return false;
        }
        if (source.getId() == target.getId()) {
            return true;
        }
        if (source.isPassengerOfSameVehicle(target)) {
            return true;
        }
        if (source.isAlliedTo(target)) {
            return true;
        }
        try {
            if (!target.level.isClientSide && target instanceof PlayerEntity && target.getServer() != null && !target.getServer().isPvpAllowed()) {
                return true;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public static Vector3d posToHand(Entity e, Hand hand) {
        double px = e.getX();
        double py = e.getBoundingBox().minY + e.getBbHeight() / 2.0f + 0.25;
        double pz = e.getZ();
        float m = (hand == Hand.MAIN_HAND) ? 0.0f : 180.0f;
        px += -MathHelper.cos((e.yRot + m) / 180.0f * 3.141593f) * 0.3f;
        pz += -MathHelper.sin((e.yRot + m) / 180.0f * 3.141593f) * 0.3f;
        Vector3d vec3d = e.getLookAngle();
        px += vec3d.x * 0.3;
        py += vec3d.y * 0.3;
        pz += vec3d.z * 0.3;
        return new Vector3d(px, py, pz);
    }
    
    public static boolean hasGoggles(Entity e) {
        if (!(e instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity viewer = (PlayerEntity)e;
        if (viewer.getMainHandItem().getItem() instanceof IGoggles && showPopups(viewer.getMainHandItem(), viewer)) {
            return true;
        }
        // Armor slots
        for (ItemStack armor : viewer.inventory.armor) {
            if (armor.getItem() instanceof IGoggles && showPopups(armor, viewer)) return true;
        }
        // TODO: Optional Curios compatibility layer (capability) can be added here
        return false;
    }
    
    private static boolean showPopups(ItemStack stack, PlayerEntity player) {
        return ((IGoggles)stack.getItem()).showIngamePopups(stack, player);
    }
    
    public static boolean hasRevealer(Entity e) {
        if (!(e instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity viewer = (PlayerEntity)e;
        if (viewer.getMainHandItem().getItem() instanceof IRevealer && reveals(viewer.getMainHandItem(), viewer)) {
            return true;
        }
        if (viewer.getOffhandItem().getItem() instanceof IRevealer && reveals(viewer.getOffhandItem(), viewer)) {
            return true;
        }
        for (ItemStack armor : viewer.inventory.armor) {
            if (armor.getItem() instanceof IRevealer && reveals(armor, viewer)) return true;
        }
        return false;
    }
    
    private static boolean reveals(ItemStack stack, PlayerEntity player) {
        return ((IRevealer)stack.getItem()).showNodes(stack, player);
    }
    
    public static Entity getPointedEntity(World world, Entity entity, double minrange, double range, float padding, boolean nonCollide) {
        return getPointedEntity(world, net.minecraft.util.math.BlockRayTraceResult.miss(new Vector3d(entity.getX(), entity.getEyeY(), entity.getZ()), net.minecraft.util.Direction.getRandom(world.random), new BlockPos(entity.blockPosition())), entity.getLookAngle(), minrange, range, padding, nonCollide);
    }
    
    public static Entity getPointedEntity(World world, Entity entity, Vector3d lookVec, double minrange, double range, float padding) {
        return getPointedEntity(world, net.minecraft.util.math.BlockRayTraceResult.miss(new Vector3d(entity.getX(), entity.getEyeY(), entity.getZ()), net.minecraft.util.Direction.getRandom(world.random), new BlockPos(entity.blockPosition())), lookVec, minrange, range, padding, false);
    }
    
    public static Entity getPointedEntity(World world, RayTraceResult ray, Vector3d lookVec, double minrange, double range, float padding) {
        return getPointedEntity(world, ray, lookVec, minrange, range, padding, false);
    }
    
    public static Entity getPointedEntity(World world, RayTraceResult ray, Vector3d lookVec, double minrange, double range, float padding, boolean nonCollide) {
        Entity pointedEntity = null;
        double d = range;
        Vector3d entityVec = ray.getLocation();
        Vector3d vec3d2 = entityVec.add(lookVec.x * d, lookVec.y * d, lookVec.z * d);
        float f1 = padding;
        AxisAlignedBB bb = new AxisAlignedBB(entityVec.x, entityVec.y, entityVec.z, entityVec.x, entityVec.y, entityVec.z).inflate(0.5);
        List<Entity> list = world.getEntities((Entity) null, bb.expandTowards(lookVec.x * d, lookVec.y * d, lookVec.z * d).inflate(f1, f1, f1));
        double d2 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);
            if (entityVec.distanceTo(new Vector3d(entity.getX(), entity.getY(), entity.getZ())) >= minrange) {
                if (entity.isPickable() || nonCollide) {
                    if (world.clip(new net.minecraft.util.math.RayTraceContext(entityVec, new Vector3d(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ()), net.minecraft.util.math.RayTraceContext.BlockMode.COLLIDER, net.minecraft.util.math.RayTraceContext.FluidMode.ANY, entity)).getType() == RayTraceResult.Type.MISS) {
                        float f2 = Math.max(0.8f, entity.getPickRadius());
                        AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(f2, f2, f2);
                        Vector3d intercept = axisalignedbb.clip(entityVec, vec3d2).orElse(null);
                        if (axisalignedbb.contains(entityVec)) {
                            if (0.0 < d2 || d2 == 0.0) {
                                pointedEntity = entity;
                                d2 = 0.0;
                            }
                        }
                        else if (intercept != null) {
                            double d3 = entityVec.distanceTo(intercept);
                            if (d3 < d2 || d2 == 0.0) {
                                pointedEntity = entity;
                                d2 = d3;
                            }
                        }
                    }
                }
            }
        }
        return pointedEntity;
    }
    
    public static RayTraceResult getPointedEntityRay(World world, Entity ignoreEntity, Vector3d startVec, Vector3d lookVec, double minrange, double range, float padding, boolean nonCollide) {
        RayTraceResult pointedEntityRay = null;
        double d = range;
        Vector3d vec3d2 = startVec.add(lookVec.x * d, lookVec.y * d, lookVec.z * d);
        float f1 = padding;
        AxisAlignedBB bb = (ignoreEntity != null) ? ignoreEntity.getBoundingBox() : new AxisAlignedBB(startVec.x, startVec.y, startVec.z, startVec.x, startVec.y, startVec.z).inflate(0.5);
        List<Entity> list = world.getEntities(ignoreEntity, bb.expandTowards(lookVec.x * d, lookVec.y * d, lookVec.z * d).inflate(f1, f1, f1));
        double d2 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);
            if (startVec.distanceTo(new Vector3d(entity.getX(), entity.getY(), entity.getZ())) >= minrange) {
                if (entity.isPickable() || nonCollide) {
                    if (world.clip(new net.minecraft.util.math.RayTraceContext(startVec, new Vector3d(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ()), net.minecraft.util.math.RayTraceContext.BlockMode.COLLIDER, net.minecraft.util.math.RayTraceContext.FluidMode.ANY, entity)).getType() == RayTraceResult.Type.MISS) {
                        float f2 = Math.max(0.8f, entity.getPickRadius());
                        AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(f2, f2, f2);
                        Vector3d intercept = axisalignedbb.clip(startVec, vec3d2).orElse(null);
                        if (axisalignedbb.contains(startVec)) {
                            if (0.0 < d2 || d2 == 0.0) {
                                pointedEntityRay = net.minecraft.util.math.BlockRayTraceResult.miss(startVec, net.minecraft.util.Direction.getRandom(entity.level.random), new BlockPos(startVec));
                                d2 = 0.0;
                            }
                        }
                        else if (intercept != null) {
                            double d3 = startVec.distanceTo(intercept);
                            if (d3 < d2 || d2 == 0.0) {
                                pointedEntityRay = net.minecraft.util.math.BlockRayTraceResult.miss(intercept, net.minecraft.util.Direction.getRandom(entity.level.random), new BlockPos(intercept));
                                d2 = d3;
                            }
                        }
                    }
                }
            }
        }
        return pointedEntityRay;
    }
    
    public static Entity getPointedEntity(World world, LivingEntity player, double range, Class<?> clazz) {
        Entity pointedEntity = null;
        double d = range;
        Vector3d vec3d = new Vector3d(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vector3d vec3d2 = player.getLookAngle();
        Vector3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        float f1 = 1.1f;
        List<Entity> list = world.getEntities(player, player.getBoundingBox().expandTowards(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d).inflate(f1, f1, f1));
        double d2 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);
            if (entity.isPickable() && world.clip(new net.minecraft.util.math.RayTraceContext(new Vector3d(player.getX(), player.getY() + player.getEyeHeight(), player.getZ()), new Vector3d(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ()), net.minecraft.util.math.RayTraceContext.BlockMode.COLLIDER, net.minecraft.util.math.RayTraceContext.FluidMode.ANY, player)).getType() == RayTraceResult.Type.MISS) {
                if (!clazz.isInstance(entity)) {
                    float f2 = Math.max(0.8f, entity.getPickRadius());
                    AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(f2, f2, f2);
                    Vector3d intercept = axisalignedbb.clip(vec3d, vec3d3).orElse(null);
                    if (axisalignedbb.contains(vec3d)) {
                        if (0.0 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = 0.0;
                        }
                    }
                    else if (intercept != null) {
                        double d3 = vec3d.distanceTo(intercept);
                        if (d3 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        return pointedEntity;
    }
    
    public static boolean canEntityBeSeen(Entity entity, TileEntity te) {
        return te.getLevel().clip(new net.minecraft.util.math.RayTraceContext(new Vector3d(te.getBlockPos().getX() + 0.5, te.getBlockPos().getY() + 1.25, te.getBlockPos().getZ() + 0.5), new Vector3d(entity.getX(), entity.getY(), entity.getZ()), net.minecraft.util.math.RayTraceContext.BlockMode.COLLIDER, net.minecraft.util.math.RayTraceContext.FluidMode.ANY, entity)).getType() == RayTraceResult.Type.MISS;
    }
    
    public static boolean canEntityBeSeen(Entity lookingEntity, double x, double y, double z) {
        return lookingEntity.level.clip(new net.minecraft.util.math.RayTraceContext(new Vector3d(x, y, z), new Vector3d(lookingEntity.getX(), lookingEntity.getY(), lookingEntity.getZ()), net.minecraft.util.math.RayTraceContext.BlockMode.COLLIDER, net.minecraft.util.math.RayTraceContext.FluidMode.ANY, lookingEntity)).getType() == RayTraceResult.Type.MISS;
    }
    
    public static boolean canEntityBeSeen(Entity lookingEntity, Entity targetEntity) {
        return lookingEntity.level.clip(new net.minecraft.util.math.RayTraceContext(new Vector3d(lookingEntity.getX(), lookingEntity.getY() + lookingEntity.getBbHeight() / 2.0f, lookingEntity.getZ()), new Vector3d(targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() / 2.0f, targetEntity.getZ()), net.minecraft.util.math.RayTraceContext.BlockMode.COLLIDER, net.minecraft.util.math.RayTraceContext.FluidMode.ANY, lookingEntity)).getType() == RayTraceResult.Type.MISS;
    }
    
    public static void resetFloatCounter(ServerPlayerEntity player) {
        // No public field; skipping float counter reset in 1.16.5
    }
    
    public static <T extends Entity> List<T> getEntitiesInRange(World world, BlockPos pos, Entity entity, Class<? extends T> classEntity, double range) {
        return getEntitiesInRange(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, entity, classEntity, range);
    }
    
    public static <T extends Entity> List<T> getEntitiesInRange(World world, double x, double y, double z, Entity entity, Class<? extends T> classEntity, double range) {
        ArrayList<T> out = new ArrayList<T>();
        List<T> list = world.getEntitiesOfClass(classEntity, new AxisAlignedBB(x, y, z, x, y, z).inflate(range, range, range));
        for (T ent : list) {
            if (entity != null && entity.getId() == ent.getId()) continue;
            out.add(ent);
        }
        return out;
    }
    
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(World world, Entity entity, Class<? extends T> classEntity, double range) {
        List<T> list = getEntitiesInRange(world, entity.getX(), entity.getY(), entity.getZ(), entity, classEntity, range);
        List<T> sl = list.stream().sorted(new EntityDistComparator(entity)).collect(Collectors.toList());
        return sl;
    }
    
    public static boolean isVisibleTo(float fov, Entity ent, Entity ent2, float range) {
        double[] x = { ent2.getX(), ent2.getBoundingBox().minY + ent2.getBbHeight() / 2.0f, ent2.getZ() };
        double[] t = { ent.getX(), ent.getBoundingBox().minY + ent.getEyeHeight(), ent.getZ() };
        Vector3d q = ent.getLookAngle();
        q = new Vector3d(q.x * range, q.y * range, q.z * range);
        Vector3d l = q.add(ent.getX(), ent.getBoundingBox().minY + ent.getEyeHeight(), ent.getZ());
        double[] b = { l.x, l.y, l.z };
        return Utils.isLyingInCone(x, t, b, fov);
    }
    
    public static boolean isVisibleTo(float fov, Entity ent, double xx, double yy, double zz, float range) {
        double[] x = { xx, yy, zz };
        double[] t = { ent.getX(), ent.getBoundingBox().minY + ent.getEyeHeight(), ent.getZ() };
        Vector3d q = ent.getLookAngle();
        q = new Vector3d(q.x * range, q.y * range, q.z * range);
        Vector3d l = q.add(ent.getX(), ent.getBoundingBox().minY + ent.getEyeHeight(), ent.getZ());
        double[] b = { l.x, l.y, l.z };
        return Utils.isLyingInCone(x, t, b, fov);
    }
    
    public static ItemEntity entityDropSpecialItem(Entity entity, ItemStack stack, float dropheight) {
        if (stack.getCount() != 0 && stack.getItem() != null) {
            ItemEntity itemEntity = new ItemEntity(entity.level, entity.getX(), entity.getY() + dropheight, entity.getZ(), stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setDeltaMovement(0.0, 0.1, 0.0);
            entity.level.addFreshEntity(itemEntity);
            return itemEntity;
        }
        return null;
    }
    
    public static void makeChampion(MobEntity entity, boolean persist) {
        // champion attribute pending port; skip early guard
        // champion selection pending port
        // TODO: champion system pending port; skip champion mod attribute for now
        if (true) {
            // placeholder branch
        }
        // if (!(entity instanceof EntityThaumcraftBoss)) {
            ModifiableAttributeInstance iattributeinstance = entity.getAttribute(net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH);
            iattributeinstance.removeModifier(EntityUtils.CHAMPION_HEALTH);
            iattributeinstance.addTransientModifier(EntityUtils.CHAMPION_HEALTH);
            ModifiableAttributeInstance iattributeinstance2 = entity.getAttribute(net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
            iattributeinstance2.removeModifier(EntityUtils.CHAMPION_DAMAGE);
            iattributeinstance2.addTransientModifier(EntityUtils.CHAMPION_DAMAGE);
            entity.heal(25.0f);
            // entity.setCustomName(new net.minecraft.util.text.StringTextComponent(ChampionModifier.mods[type].getModNameLocalized() + " " + entity.getName().getString()));
        // } else {
        //     ((EntityThaumcraftBoss)entity).generateName();
        // }
        if (persist) {
            entity.setPersistenceRequired();
        }
        switch (0) {
            case 0: {
                ModifiableAttributeInstance sai = entity.getAttribute(net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
                sai.removeModifier(EntityUtils.BOLDBUFF);
                sai.addTransientModifier(EntityUtils.BOLDBUFF);
                break;
            }
            case 3: {
                ModifiableAttributeInstance mai = entity.getAttribute(net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
                mai.removeModifier(EntityUtils.MIGHTYBUFF);
                mai.addTransientModifier(EntityUtils.MIGHTYBUFF);
                break;
            }
            case 5: {
                int bh = (int)entity.getAttribute(net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH).getBaseValue() / 2;
                entity.setAbsorptionAmount(entity.getAbsorptionAmount() + bh);
                break;
            }
        }
    }
    
    public static void makeTainted(LivingEntity target) {
        // champion attribute pending port; skip early guard
        // retained for reference
        ModifiableAttributeInstance modai = target.getAttribute(net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH);
        if (modai == null) {
            return;
        }
        // champion flag pending port
        if (true) {
            ModifiableAttributeInstance iattributeinstance = target.getAttribute(net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH);
            iattributeinstance.removeModifier(EntityUtils.HPBUFF[5]);
            iattributeinstance.addTransientModifier(EntityUtils.HPBUFF[5]);
            ModifiableAttributeInstance iattributeinstance2 = target.getAttribute(net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
            if (iattributeinstance2 == null) {
                // 1.16 attributes are registered per-entity; skip dynamic registration
                target.getAttribute(net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(2.0f, (target.getBbHeight() + target.getBbWidth()) * 2.0f));
            } else {
                iattributeinstance2.removeModifier(EntityUtils.DMGBUFF[0]);
                iattributeinstance2.addTransientModifier(EntityUtils.DMGBUFF[0]);
            }
            target.heal(25.0f);
        }
    }
    
    static {
        CHAMPION_HEALTH = new AttributeModifier(UUID.fromString("a62bef38-48cc-42a6-ac5e-ef913841c4fd"), "Champion health buff", 100.0, AttributeModifier.Operation.ADDITION);
        CHAMPION_DAMAGE = new AttributeModifier(UUID.fromString("a340d2db-d881-4c25-ac62-f0ad14cd63b0"), "Champion damage buff", 2.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        BOLDBUFF = new AttributeModifier(UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037"), "Bold speed boost", 0.3, AttributeModifier.Operation.MULTIPLY_BASE);
        MIGHTYBUFF = new AttributeModifier(UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3"), "Mighty damage boost", 2.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        HPBUFF = new AttributeModifier[] { new AttributeModifier(UUID.fromString("54d621c1-dd4d-4b43-8bd2-5531c8875797"), "HEALTH BUFF 1", 50.0, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("f51257dc-b7fa-4f7a-92d7-75d68e8592c4"), "HEALTH BUFF 2", 50.0, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("3d6b2e42-4141-4364-b76d-0e8664bbd0bb"), "HEALTH BUFF 3", 50.0, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("02c97a08-801c-4131-afa2-1427a6151934"), "HEALTH BUFF 4", 50.0, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("0f354f6a-33c5-40be-93be-81b1338567f1"), "HEALTH BUFF 5", 50.0, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("0f354f6a-33c5-40be-93be-81b1338567f1"), "HEALTH BUFF 6", 25.0, AttributeModifier.Operation.ADDITION) };
        DMGBUFF = new AttributeModifier[] { new AttributeModifier(UUID.fromString("534f8c57-929a-48cf-bbd6-0fd851030748"), "DAMAGE BUFF 1", 0.5, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("d317a76e-0e7c-4c61-acfd-9fa286053b32"), "DAMAGE BUFF 2", 0.5, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("ff462d63-26a2-4363-830e-143ed97e2a4f"), "DAMAGE BUFF 3", 0.5, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("cf1eb39e-0c67-495f-887c-0d3080828d2f"), "DAMAGE BUFF 4", 0.5, AttributeModifier.Operation.ADDITION), new AttributeModifier(UUID.fromString("3cfab9da-2701-43d8-ac07-885f16fa4117"), "DAMAGE BUFF 5", 0.5, AttributeModifier.Operation.ADDITION) };
    }
    
    public static class EntityDistComparator implements Comparator<Entity>
    {
        private final Entity source;
        
        public EntityDistComparator(Entity source) {
            this.source = source;
        }
        
        @Override
        public int compare(Entity a, Entity b) {
            if (a.equals(b)) {
                return 0;
            }
            double da = source.distanceToSqr(a);
            double db = source.distanceToSqr(b);
            return (da < db) ? -1 : ((da > db) ? 1 : 0);
        }
    }
}


