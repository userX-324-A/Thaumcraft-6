package thaumcraft.common.blocks.world.ore;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
// import thaumcraft.api.items.ItemsTC; // We'll need to resolve this
// import thaumcraft.common.items.ModItems; // Or this

import java.util.List;
import java.util.Random;

public class BlockOreAmber extends OreBlock {

    public BlockOreAmber() {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(1.5f, 5.0f)
                .sound(SoundType.STONE)
                .harvestLevel(1) // 0: wood, 1: stone, 2: iron, 3: diamond
                .harvestTool(ToolType.PICKAXE)
                .requiresTool(),
            MathHelper.nextInt(new Random(), 0, 2)); // Experience drops 0-2
    }

    // Note: In 1.16.5, drops are primarily handled by loot tables.
    // This override is a simpler way to achieve custom drop logic for now.
    // We might want to migrate this to a loot table later.
    @Override
    public List<ItemStack> getDrops(BlockState state, ServerWorld world, BlockPos pos, int fortune) {
        List<ItemStack> drops = super.getDrops(state, world, pos, fortune); // Handles base drops + experience orb

        // Clear default drops if OreBlock drops itself by default (which it does)
        // and we want to replace it with specific items.
        // However, OreBlock by default drops itself, we need to replace this.
        // A better way would be to have a loot table that drops the correct item.
        // For now, we'll clear and add. This is NOT how OreBlock is intended to be used
        // if it drops something other than itself without a loot table.
        // OreBlock's constructor does not specify what item it drops; it's assumed to be itself
        // or handled by a loot table.

        // The getDrops in Block includes fortune, but OreBlock itself doesn't increase count by default with fortune for its own drop.
        // It usually drops 1 of itself. Experience is handled separately.

        // Let's first get what super.getDrops would give and then modify it if needed,
        // or just create a new list.
        // The drop for OreBlock *is* itself. We don't want that.
        drops.clear(); // Remove the default ore block drop

        Random rand = world.getRandom();
        
        // Drop 1-2 Amber
        // int quantityAmber = 1 + rand.nextInt(2); // 1 or 2
        // For now, let's assume ItemsTC.AMBER is the RegistryObject or Item instance
        // drops.add(new ItemStack(ItemsTC.AMBER, quantityAmber)); // Placeholder

        // Chance to drop a curio (e.g., Knowledge Fragment)
        // if (rand.nextFloat() < 0.066f) {
            // drops.add(new ItemStack(ItemsTC.CURIOS.get("knowledge_fragment"))); // Placeholder for curio
        // }
        
        // For now, until ItemsTC is sorted, let's make it drop a placeholder or nothing specific from here.
        // The proper way is via loot tables.
        // To make it compile, I'll comment out the custom drops that rely on ItemsTC for now.
        // We need to decide how ItemsTC.amber and ItemsTC.curio will be referenced.
        // If we make this block drop nothing here, the loot table will be essential.
        // Or, we can assume ItemsTC.AMBER exists and use it.

        // Let's assume for now, to make progress, we can refer to ItemsTC.AMBER.
        // The curio part is more complex as it was a specific metadata item.
        
        // We should really be defining a loot table.
        // An OreBlock without a specific loot table will drop itself.
        // To make it drop amber, the simplest way without a loot table is to override getItemDropped.
        // However, getItemDropped is gone. It's all loot tables or overriding getDrops.

        // Let's stick to getDrops for now, and assume ItemsTC.AMBER will be available.
        // The curio is a secondary concern.
        
        // To correctly get variable quantity with fortune for the primary drop:
        // Item amberItem = ItemsTC.AMBER.get(); // Assuming RegistryObject
        // drops.add(new ItemStack(amberItem, quantityDroppedWithBonus(state, fortune, rand))); // quantityDroppedWithBonus is protected in Block.

        // OreBlock itself doesn't use quantityDroppedWithBonus or similar for its own drop.
        // It just drops one of itself.
        // We need to implement the quantity logic.
        
        // int quantity = 1 + rand.nextInt(2); // Drops 1 or 2 amber
        // if (fortune > 0) {
        //    quantity += rand.nextInt(fortune + 1); // Basic fortune effect
        // }
        // drops.add(new ItemStack(ModItems.AMBER.get(), quantity)); // Replace ModItems.AMBER with actual item

        // The old code had: public int quantityDropped(Random random) { return (this == BlocksTC.oreAmber) ? (1 + random.nextInt(2)) : 1; }
        // And then getDrops handled the curio. Fortune was handled by super.getDrops.

        // For 1.16.5, if we override getDrops, we are responsible for everything, including fortune.
        // The experience orbs are handled by OreBlock's spawnAdditionalDrops.
        
        // Let's assume we have an AMBER item in ModItems
        // For the quantity: 1 + random.nextInt(2). Fortune needs to be applied.
        // A simple way:
        int numAmber = 1 + world.getRandom().nextInt(2); // Base 1-2
        // Vanilla ores apply fortune like this:
        // For coal, diamond, emerald, lapis, nether_quartz:
        // drops * (random.nextInt(fortune + 2) -1), capped at 0.
        // This is complex.
        // Let's use a simpler fortune model for now: each level of fortune adds a chance to drop one more.
        for (int i = 0; i < fortune + 1; ++i) {
            if (world.getRandom().nextFloat() < 0.5F) { // Example: 50% chance per fortune level to add one
                numAmber++;
            }
        }
        // This is just a placeholder for a proper fortune handling or loot table.

        // ItemStack amberStack = new ItemStack(ModItems.AMBER.get(), numAmber); // Placeholder
        // drops.add(amberStack);

        // if (world.getRandom().nextFloat() < 0.066f) {
            // drops.add(new ItemStack(ModItems.KNOWLEDGE_FRAGMENT.get(), 1)); // Placeholder
        // }

        // To make this compile and work without ModItems ready,
        // the best approach for now is to NOT override getDrops and plan to create a loot table.
        // An OreBlock by default drops itself.
        // We will create a loot table: src/main/resources/data/thaumcraft/loot_tables/blocks/ore_amber.json
        // This loot table will define the drops of amber and the curio.

        // So, I will remove the getDrops override for now.
        // The harvest level and tool are correctly set in the properties.
        // The experience is also handled by OreBlock.

        return super.getDrops(state, world, pos, fortune); // This will drop the ore block itself. We'll fix with a loot table.
    }

    // We will need a loot table for this block to drop amber and curios.
    // Location: resources/data/thaumcraft/loot_tables/blocks/ore_amber.json
    // Content would be something like:
    /*
    {
      "type": "minecraft:block",
      "pools": [
        {
          "rolls": 1,
          "entries": [
            {
              "type": "minecraft:item",
              "name": "thaumcraft:amber", // Assuming ItemsTC.AMBER becomes thaumcraft:amber
              "functions": [
                {
                  "function": "minecraft:set_count",
                  "count": {
                    "type": "minecraft:uniform",
                    "min": 1,
                    "max": 2
                  }
                },
                {
                  "function": "minecraft:apply_bonus",
                  "enchantment": "minecraft:fortune",
                  "formula": "minecraft:ore_drops"
                }
              ]
            }
          ]
        },
        {
          "rolls": 1,
          "entries": [
            {
              "type": "minecraft:item",
              "name": "thaumcraft:knowledge_fragment", // Assuming the curio is this item
              "conditions": [
                {
                  "condition": "minecraft:random_chance_with_looting",
                  "chance": 0.066,
                  "looting_multiplier": 0.01 // Small increase with looting, adjust as needed
                }
              ]
            }
          ],
          "conditions": [
            {
              "condition": "minecraft:survives_explosion" // Or remove if not needed
            }
          ]
        }
      ]
    }
    */
} 