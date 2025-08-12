package thaumcraft.common.lib.research;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import thaumcraft.api.research.IScanThing;
import thaumcraft.api.research.ScanningManager;


public class ScanEnchantment implements IScanThing
{
    Enchantment enchantment;
    
    public ScanEnchantment(Enchantment ench) {
        enchantment = ench;
    }
    
    @Override
    public boolean checkThing(PlayerEntity player, Object obj) {
        return getEnchantment(player, obj) != null;
    }
    
    private Enchantment getEnchantment(PlayerEntity player, Object obj) {
        if (obj == null) {
            return null;
        }
        ItemStack is = ScanningManager.getItemFromParms(player, obj);
        if (is != null && !is.isEmpty()) {
            Map<Enchantment, Integer> e = EnchantmentHelper.getEnchantments(is);
            for (Enchantment ench : e.keySet()) {
                if (ench == enchantment) {
                    return ench;
                }
            }
        }
        return null;
    }
    
    @Override
    public String getResearchKey(PlayerEntity player, Object obj) {
        return "!" + enchantment.getRegistryName();
    }
}

