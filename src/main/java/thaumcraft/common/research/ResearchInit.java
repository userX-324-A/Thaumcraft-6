package thaumcraft.common.research;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Effect;
import net.minecraftforge.registries.ForgeRegistries;
import thaumcraft.api.research.ScanningManager;
import thaumcraft.common.lib.research.ScanEnchantment;
import thaumcraft.common.lib.research.ScanGeneric;
import thaumcraft.common.lib.research.ScanPotion;
import thaumcraft.common.lib.research.ScanSky;

/**
 * Bootstraps scannable registrations at mod init time.
 */
public final class ResearchInit {
    private ResearchInit() {}

    public static void registerScannables() {
        // Generic aspects scan for items/entities
        ScanningManager.addScannableThing(new ScanGeneric());

        // Enchantments
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
            if (enchantment != null) {
                ScanningManager.addScannableThing(new ScanEnchantment(enchantment));
            }
        }

        // Status effects (handle name differences across mappings by checking instance types)
        for (Object obj : ForgeRegistries.POTIONS.getValues()) {
            if (obj instanceof Effect) {
                Effect effect = (Effect) obj;
                ScanningManager.addScannableThing(new ScanPotion(effect));
            }
        }

        // Sky scan
        ScanningManager.addScannableThing(new ScanSky());

        // Entity Scan* deferred until entities are fully ported
    }
}


