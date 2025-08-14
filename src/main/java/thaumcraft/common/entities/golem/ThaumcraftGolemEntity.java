package thaumcraft.common.entities.golem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.IWorldReader;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.common.golems.GolemProperties;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Thaumcraft Golem (1.16.5): basic core entity with trait-driven goals and carrying inventory.
 */
public class ThaumcraftGolemEntity extends CreatureEntity implements IGolemAPI {

    private static final DataParameter<Byte> COLOR = EntityDataManager.defineId(ThaumcraftGolemEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> PROPS_BITS = EntityDataManager.defineId(ThaumcraftGolemEntity.class, DataSerializers.INT);

    private IGolemProperties properties;
    private NonNullList<ItemStack> carrying = NonNullList.create();
    private int rankXp;
    private UUID ownerUuid;

    public ThaumcraftGolemEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        this.xpReward = 0;
        // default properties; can be overridden by summoning item or NBT
        this.properties = new GolemProperties();
        // Apply defaults from config
        try {
            thaumcraft.common.golems.GolemBootstrap.registerDefaults(); // ensure parts available
            thaumcraft.common.golems.GolemProperties gp = (thaumcraft.common.golems.GolemProperties) this.properties;
            thaumcraft.common.config.ModConfig.Common cfg = thaumcraft.common.config.ModConfig.COMMON;
            java.util.function.Function<String, net.minecraft.util.ResourceLocation> rl = s -> new net.minecraft.util.ResourceLocation(s);
            gp.setPartsByIds(
                rl.apply(cfg.defaultGolemMaterial.get()),
                rl.apply(cfg.defaultGolemHead.get()),
                rl.apply(cfg.defaultGolemArms.get()),
                rl.apply(cfg.defaultGolemLegs.get()),
                rl.apply(cfg.defaultGolemAddon.get())
            );
        } catch (Throwable ignored) {}
        resizeInventory();
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.9D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));

        // Trait-driven combat/utility
        Set<EnumGolemTrait> traits = this.properties.getTraits();
        if (traits.contains(EnumGolemTrait.FIGHTER) || traits.contains(EnumGolemTrait.BRUTAL)) {
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1D, true));
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, net.minecraft.entity.monster.MonsterEntity.class, true));
        }

        if (traits.contains(EnumGolemTrait.HAULER) || traits.contains(EnumGolemTrait.BREAKER)) {
            // Move near interesting blocks; placeholder until full task system exists
            this.goalSelector.addGoal(5, new MoveToBlockGoal(this, 0.9D, 8) {
                @Override
                protected boolean isValidTarget(IWorldReader world, BlockPos pos) {
                    return false; // real task targeting will be wired later
                }
            });
        }

        // Minimal task consumer goal
        this.goalSelector.addGoal(3, new thaumcraft.common.entities.golem.goal.GolemConsumeTaskGoal(this));
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        GroundPathNavigator nav = new GroundPathNavigator(this, world);
        nav.setCanOpenDoors(true);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, (byte) 0);
        this.entityData.define(PROPS_BITS, 0);
    }

    // Persistence
    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("Color", this.getGolemColor());
        tag.putLong("PropsLong", this.properties.toLong());
        tag.putInt("RankXp", this.rankXp);
        if (this.ownerUuid != null) tag.putUUID("Owner", this.ownerUuid);
        // part ids (optional phase 1)
        if (this.properties instanceof thaumcraft.common.golems.GolemProperties) {
            thaumcraft.common.golems.GolemProperties gp = (thaumcraft.common.golems.GolemProperties) this.properties;
            if (gp.getMaterialId() != null) tag.putString("MatId", gp.getMaterialId().toString());
            if (gp.getHeadId() != null) tag.putString("HeadId", gp.getHeadId().toString());
            if (gp.getArmsId() != null) tag.putString("ArmsId", gp.getArmsId().toString());
            if (gp.getLegsId() != null) tag.putString("LegsId", gp.getLegsId().toString());
            if (gp.getAddonId() != null) tag.putString("AddonId", gp.getAddonId().toString());
        }
        // inventory
        ListNBT inv = new ListNBT();
        for (int i = 0; i < this.carrying.size(); i++) {
            ItemStack stack = this.carrying.get(i);
            if (!stack.isEmpty()) {
                CompoundNBT s = new CompoundNBT();
                s.putByte("Slot", (byte) i);
                stack.save(s);
                inv.add(s);
            }
        }
        tag.put("Inventory", inv);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Color")) setGolemColor(tag.getByte("Color"));
        if (tag.contains("PropsLong")) {
            long bits = tag.getLong("PropsLong");
            this.properties = GolemProperties.fromLong(bits);
            resizeInventory();
        }
        if (this.properties instanceof thaumcraft.common.golems.GolemProperties) {
            thaumcraft.common.golems.GolemProperties gp = (thaumcraft.common.golems.GolemProperties) this.properties;
            java.util.function.Function<String, net.minecraft.util.ResourceLocation> rl = s -> new net.minecraft.util.ResourceLocation(s);
            if (tag.contains("MatId")) gp.setPartsByIds(rl.apply(tag.getString("MatId")), null, null, null, null);
            if (tag.contains("HeadId")) gp.setPartsByIds(null, rl.apply(tag.getString("HeadId")), null, null, null);
            if (tag.contains("ArmsId")) gp.setPartsByIds(null, null, rl.apply(tag.getString("ArmsId")), null, null);
            if (tag.contains("LegsId")) gp.setPartsByIds(null, null, null, rl.apply(tag.getString("LegsId")), null);
            if (tag.contains("AddonId")) gp.setPartsByIds(null, null, null, null, rl.apply(tag.getString("AddonId")));
        }
        if (tag.contains("RankXp")) this.rankXp = tag.getInt("RankXp");
        if (tag.hasUUID("Owner")) this.ownerUuid = tag.getUUID("Owner");
        // inventory
        this.carrying.clear();
        resizeInventory();
        ListNBT inv = tag.getList("Inventory", 10);
        for (int i = 0; i < inv.size(); i++) {
            CompoundNBT s = inv.getCompound(i);
            int slot = s.getByte("Slot") & 255;
            if (slot >= 0 && slot < this.carrying.size()) this.carrying.set(slot, ItemStack.of(s));
        }
    }

    private void resizeInventory() {
        int desired = computeCarrySlots();
        if (desired < 1) desired = 1;
        if (this.carrying.size() == desired) return;
        NonNullList<ItemStack> newInv = NonNullList.withSize(desired, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(desired, this.carrying.size()); i++) newInv.set(i, this.carrying.get(i));
        this.carrying = newInv;
    }

    private int computeCarrySlots() {
        // very rough pass: base 1, +1 if HAULER, +1 if DEFT, +1 if ARMORED arms (simulated via traits)
        int slots = 1;
        Set<EnumGolemTrait> traits = this.properties.getTraits();
        if (traits.contains(EnumGolemTrait.HAULER)) slots++;
        if (traits.contains(EnumGolemTrait.DEFT)) slots++;
        if (traits.contains(EnumGolemTrait.ARMORED)) slots++;
        return Math.min(slots, 6);
    }

    // IGolemAPI implementation
    @Override
    public MobEntity getGolemEntity() { return this; }

    @Override
    public IGolemProperties getProperties() { return properties; }

    @Override
    public void setProperties(IGolemProperties prop) {
        this.properties = prop != null ? prop : new GolemProperties();
        this.entityData.set(PROPS_BITS, (int) this.properties.toLong());
        resizeInventory();
        // Adjust a few attributes loosely based on material mods
        double baseHp = 30.0D + Optional.ofNullable(properties.getMaterial()).map(m -> (double) m.healthMod).orElse(0.0);
        double baseArmor = 4.0D + Optional.ofNullable(properties.getMaterial()).map(m -> (double) m.armorMod).orElse(0.0);
        double baseDmg = 4.0D + Optional.ofNullable(properties.getMaterial()).map(m -> (double) m.damageMod).orElse(0.0);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(baseHp);
        this.getAttribute(Attributes.ARMOR).setBaseValue(Math.max(0.0D, baseArmor));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(1.0D, baseDmg));
    }

    @Override
    public World getGolemWorld() { return level; }

    @Override
    public ItemStack holdItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        for (int i = 0; i < carrying.size(); i++) {
            ItemStack slot = carrying.get(i);
            if (slot.isEmpty()) {
                carrying.set(i, stack.copy());
                return ItemStack.EMPTY;
            }
            if (ItemStack.isSame(slot, stack) && ItemStack.tagMatches(slot, stack)) {
                int max = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                int room = max - slot.getCount();
                if (room > 0) {
                    int toMove = Math.min(room, stack.getCount());
                    slot.grow(toMove);
                    stack.shrink(toMove);
                    if (stack.isEmpty()) return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack dropItem(ItemStack stack) {
        if (stack == null) {
            // drop first non-empty
            for (int i = 0; i < carrying.size(); i++) {
                ItemStack s = carrying.get(i);
                if (!s.isEmpty()) { carrying.set(i, ItemStack.EMPTY); return s; }
            }
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < carrying.size(); i++) {
            ItemStack s = carrying.get(i);
            if (!s.isEmpty() && ItemStack.isSame(s, stack) && ItemStack.tagMatches(s, stack)) {
                carrying.set(i, ItemStack.EMPTY);
                return s;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCarry(ItemStack stack, boolean partial) {
        if (stack == null || stack.isEmpty()) return true;
        int amount = canCarryAmount(stack);
        return partial ? amount > 0 : amount >= stack.getCount();
    }

    @Override
    public int canCarryAmount(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return carrying.size() * 64;
        int space = 0;
        for (ItemStack s : carrying) {
            if (s.isEmpty()) space += stack.getMaxStackSize();
            else if (ItemStack.isSame(s, stack) && ItemStack.tagMatches(s, stack)) {
                space += Math.max(0, Math.min(s.getMaxStackSize(), stack.getMaxStackSize()) - s.getCount());
            }
        }
        return space;
    }

    @Override
    public boolean isCarrying(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        for (ItemStack s : carrying) if (ItemStack.isSame(s, stack) && ItemStack.tagMatches(s, stack) && s.getCount() >= stack.getCount()) return true;
        return false;
    }

    @Override
    public NonNullList<ItemStack> getCarrying() { return carrying; }

    @Override
    public void addRankXp(int xp) { this.rankXp += Math.max(0, xp); }

    @Override
    public byte getGolemColor() { return this.entityData.get(COLOR); }
    public void setGolemColor(byte color) { this.entityData.set(COLOR, color); }

    @Override
    public void swingArm() { this.swing(net.minecraft.util.Hand.MAIN_HAND); }

    @Override
    public boolean isInCombat() { return this.getTarget() != null && this.getTarget().isAlive(); }

    // Ownership helpers
    public Optional<UUID> getOwnerUuid() { return Optional.ofNullable(ownerUuid); }
    public void setOwnerUuid(UUID uuid) { this.ownerUuid = uuid; }

    // Convenience: set guard target entity (temporary behavior)
    public void setGuardTarget(Entity entity) {
        if (entity instanceof PlayerEntity || entity instanceof AbstractVillagerEntity) {
            // do nothing yet, placeholder for guard tasking
        }
    }
}



