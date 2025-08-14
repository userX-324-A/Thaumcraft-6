package thaumcraft.api;

import net.minecraft.item.IItemTier;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.util.LazyValue;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class ThaumcraftMaterials {
    public enum ToolTiers implements IItemTier {
        THAUMIUM(3, 1024, 8.0F, 3.0F, 18, () -> Ingredient.of(Items.IRON_INGOT)),
        VOID(4, 1561, 9.0F, 4.0F, 14, () -> Ingredient.of(Items.ENDER_PEARL));

        private final int harvestLevel;
        private final int durability;
        private final float speed;
        private final float attackDamageBonus;
        private final int enchantmentValue;
        private final LazyValue<Ingredient> repairIngredient;

        ToolTiers(int harvestLevel, int durability, float speed, float attackDamageBonus, int enchantmentValue, java.util.function.Supplier<Ingredient> repairIngredient) {
            this.harvestLevel = harvestLevel;
            this.durability = durability;
            this.speed = speed;
            this.attackDamageBonus = attackDamageBonus;
            this.enchantmentValue = enchantmentValue;
            this.repairIngredient = new LazyValue<>(repairIngredient);
        }

        @Override public int getUses() { return durability; }
        @Override public float getSpeed() { return speed; }
        @Override public float getAttackDamageBonus() { return attackDamageBonus; }
        @Override public int getLevel() { return harvestLevel; }
        @Override public int getEnchantmentValue() { return enchantmentValue; }
        @Override public Ingredient getRepairIngredient() { return repairIngredient.get(); }
    }

    public enum ArmorMaterials implements IArmorMaterial {
        THAUMIUM("thaumcraft:thaumium", 18, new int[]{2, 5, 6, 2}, 18, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
                () -> Ingredient.of(Items.IRON_INGOT)),
        VOID("thaumcraft:void", 25, new int[]{3, 6, 8, 3}, 14, SoundEvents.ARMOR_EQUIP_NETHERITE, 2.0F, 0.1F,
                () -> Ingredient.of(Items.ENDER_PEARL));

        private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
        private final String name;
        private final int durabilityMultiplier;
        private final int[] slotProtections;
        private final int enchantmentValue;
        private final SoundEvent equipSound;
        private final float toughness;
        private final float knockbackResistance;
        private final LazyValue<Ingredient> repairIngredient;

        ArmorMaterials(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue,
                       SoundEvent equipSound, float toughness, float knockbackResistance,
                       java.util.function.Supplier<Ingredient> repairIngredient) {
            this.name = name;
            this.durabilityMultiplier = durabilityMultiplier;
            this.slotProtections = slotProtections;
            this.enchantmentValue = enchantmentValue;
            this.equipSound = equipSound;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairIngredient = new LazyValue<>(repairIngredient);
        }

        @Override public int getDurabilityForSlot(EquipmentSlotType slot) { return BASE_DURABILITY[slot.getIndex()] * durabilityMultiplier; }
        @Override public int getDefenseForSlot(EquipmentSlotType slot) { return slotProtections[slot.getIndex()]; }
        @Override public int getEnchantmentValue() { return enchantmentValue; }
        @Override public SoundEvent getEquipSound() { return equipSound; }
        @Override public Ingredient getRepairIngredient() { return repairIngredient.get(); }
        @Override public String getName() { return name; }
        @Override public float getToughness() { return toughness; }
        @Override public float getKnockbackResistance() { return knockbackResistance; }
    }
}


