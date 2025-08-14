package thaumcraft.common.golems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.golems.parts.GolemAddon;
import thaumcraft.api.golems.parts.GolemArm;
import thaumcraft.api.golems.parts.GolemHead;
import thaumcraft.api.golems.parts.GolemLeg;
import thaumcraft.api.golems.parts.GolemMaterial;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Backing implementation of IGolemProperties with compact long serialization and simple registries.
 * Phase 1: static maps; Phase 2: migrate to custom Forge registries per part.
 */
public class GolemProperties implements IGolemProperties {
    private static final Map<ResourceLocation, GolemMaterial> MATERIALS = new HashMap<>();
    private static final Map<ResourceLocation, GolemHead> HEADS = new HashMap<>();
    private static final Map<ResourceLocation, GolemArm> ARMS = new HashMap<>();
    private static final Map<ResourceLocation, GolemLeg> LEGS = new HashMap<>();
    private static final Map<ResourceLocation, GolemAddon> ADDONS = new HashMap<>();

    // Trait contributions per part id (Phase 1 lightweight approach)
    private static final Map<ResourceLocation, Set<EnumGolemTrait>> MATERIAL_TRAITS = new HashMap<>();
    private static final Map<ResourceLocation, Set<EnumGolemTrait>> HEAD_TRAITS = new HashMap<>();
    private static final Map<ResourceLocation, Set<EnumGolemTrait>> ARMS_TRAITS = new HashMap<>();
    private static final Map<ResourceLocation, Set<EnumGolemTrait>> LEGS_TRAITS = new HashMap<>();
    private static final Map<ResourceLocation, Set<EnumGolemTrait>> ADDON_TRAITS = new HashMap<>();

    private final EnumSet<EnumGolemTrait> traits = EnumSet.noneOf(EnumGolemTrait.class);
    private GolemMaterial material;
    private GolemHead head;
    private GolemArm arms;
    private GolemLeg legs;
    private GolemAddon addon;
    private int rank;

    public GolemProperties() {}

    public static void registerMaterial(ResourceLocation id, GolemMaterial m) { registerMaterial(id, m, Collections.emptySet()); }
    public static void registerMaterial(ResourceLocation id, GolemMaterial m, Set<EnumGolemTrait> traits) {
        MATERIALS.put(id, m);
        MATERIAL_TRAITS.put(id, EnumSet.copyOf(traits));
    }
    public static void registerHead(ResourceLocation id, GolemHead h) { registerHead(id, h, Collections.emptySet()); }
    public static void registerHead(ResourceLocation id, GolemHead h, Set<EnumGolemTrait> traits) {
        HEADS.put(id, h);
        HEAD_TRAITS.put(id, EnumSet.copyOf(traits));
    }
    public static void registerArms(ResourceLocation id, GolemArm a) { registerArms(id, a, Collections.emptySet()); }
    public static void registerArms(ResourceLocation id, GolemArm a, Set<EnumGolemTrait> traits) {
        ARMS.put(id, a);
        ARMS_TRAITS.put(id, EnumSet.copyOf(traits));
    }
    public static void registerLegs(ResourceLocation id, GolemLeg l) { registerLegs(id, l, Collections.emptySet()); }
    public static void registerLegs(ResourceLocation id, GolemLeg l, Set<EnumGolemTrait> traits) {
        LEGS.put(id, l);
        LEGS_TRAITS.put(id, EnumSet.copyOf(traits));
    }
    public static void registerAddon(ResourceLocation id, GolemAddon a) { registerAddon(id, a, Collections.emptySet()); }
    public static void registerAddon(ResourceLocation id, GolemAddon a, Set<EnumGolemTrait> traits) {
        ADDONS.put(id, a);
        ADDON_TRAITS.put(id, EnumSet.copyOf(traits));
    }

    public static GolemProperties fromLong(long bits) {
        GolemProperties gp = new GolemProperties();
        // Very simple placeholder packing:
        // low 8 bits: rank, next 24 bits: trait bitset (supports first 24 traits), remainder unused here
        int rank = (int) (bits & 0xFF);
        int traitMask = (int) ((bits >>> 8) & 0xFFFFFF);
        gp.rank = rank;
        EnumGolemTrait[] values = EnumGolemTrait.values();
        for (int i = 0; i < Math.min(values.length, 24); i++) {
            if (((traitMask >>> i) & 1) != 0) gp.traits.add(values[i]);
        }
        return gp;
    }

    // Phase 1: minimal NBT helpers to store part IDs for persistence
    public void setPartsByIds(ResourceLocation materialId, ResourceLocation headId, ResourceLocation armId,
                              ResourceLocation legId, ResourceLocation addonId) {
        if (materialId != null) this.material = MATERIALS.get(materialId);
        if (headId != null) this.head = HEADS.get(headId);
        if (armId != null) this.arms = ARMS.get(armId);
        if (legId != null) this.legs = LEGS.get(legId);
        if (addonId != null) this.addon = ADDONS.get(addonId);
    }

    public ResourceLocation getMaterialId() { return reverseLookup(MATERIALS, material); }
    public ResourceLocation getHeadId() { return reverseLookup(HEADS, head); }
    public ResourceLocation getArmsId() { return reverseLookup(ARMS, arms); }
    public ResourceLocation getLegsId() { return reverseLookup(LEGS, legs); }
    public ResourceLocation getAddonId() { return reverseLookup(ADDONS, addon); }

    private static <T> ResourceLocation reverseLookup(Map<ResourceLocation, T> map, T value) {
        if (value == null) return null;
        for (Map.Entry<ResourceLocation, T> e : map.entrySet()) {
            if (e.getValue() == value) return e.getKey();
        }
        return null;
    }

    @Override
    public Set<EnumGolemTrait> getTraits() { return Collections.unmodifiableSet(traits); }

    @Override
    public boolean hasTrait(EnumGolemTrait tag) { return traits.contains(tag); }

    @Override
    public long toLong() {
        long bits = 0L;
        // rank in low 8
        bits |= (rank & 0xFF);
        // traits in next 24
        long mask = 0L;
        EnumGolemTrait[] values = EnumGolemTrait.values();
        for (int i = 0; i < Math.min(values.length, 24); i++) {
            if (traits.contains(values[i])) mask |= (1L << i);
        }
        bits |= (mask << 8);
        return bits;
    }

    @Override
    public ItemStack[] generateComponents() { return new ItemStack[0]; }

    @Override
    public void setMaterial(GolemMaterial mat) { this.material = mat; refreshTraits(); }

    @Override
    public GolemMaterial getMaterial() { return material; }

    @Override
    public void setHead(GolemHead mat) { this.head = mat; refreshTraits(); }

    @Override
    public GolemHead getHead() { return head; }

    @Override
    public void setArms(GolemArm mat) { this.arms = mat; refreshTraits(); }

    @Override
    public GolemArm getArms() { return arms; }

    @Override
    public void setLegs(GolemLeg mat) { this.legs = mat; refreshTraits(); }

    @Override
    public GolemLeg getLegs() { return legs; }

    @Override
    public void setAddon(GolemAddon mat) { this.addon = mat; refreshTraits(); }

    @Override
    public GolemAddon getAddon() { return addon; }

    @Override
    public void setRank(int r) { this.rank = Math.max(0, r); }

    @Override
    public int getRank() { return rank; }

    private void refreshTraits() {
        traits.clear();
        // Accumulate from each selected part id
        ResourceLocation id;
        if ((id = getMaterialId()) != null) traits.addAll(MATERIAL_TRAITS.getOrDefault(id, Collections.emptySet()));
        if ((id = getHeadId()) != null) traits.addAll(HEAD_TRAITS.getOrDefault(id, Collections.emptySet()));
        if ((id = getArmsId()) != null) traits.addAll(ARMS_TRAITS.getOrDefault(id, Collections.emptySet()));
        if ((id = getLegsId()) != null) traits.addAll(LEGS_TRAITS.getOrDefault(id, Collections.emptySet()));
        if ((id = getAddonId()) != null) traits.addAll(ADDON_TRAITS.getOrDefault(id, Collections.emptySet()));
        // Resolve simple opposites like legacy did
        for (EnumGolemTrait t : EnumGolemTrait.values()) {
            if (t.opposite != null && traits.contains(t) && traits.contains(t.opposite)) {
                traits.remove(t.opposite);
            }
        }
    }
}



