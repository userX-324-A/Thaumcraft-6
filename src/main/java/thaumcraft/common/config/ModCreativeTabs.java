package thaumcraft.common.config;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.ItemsTC;

public class ModCreativeTabs {
    public static final ItemGroup TAB_THAUMCRAFT = new ItemGroup("thaumcraft") { // "thaumcraft" is the label used for the tab name lang key
        @Override
        public ItemStack createIcon() {
            // Ensure ItemsTC.thaumonomicon is initialized before this is called.
            // With DeferredRegister, this should be fine as long as the ItemGroup is referenced after ITEMS.register calls.
            // If ItemsTC.thaumonomicon itself is null here, it means the RegistryObject might not be populated yet, 
            // or we have a circular dependency in class loading. A common fix is to use a known vanilla item or a placeholder
            // if the primary icon item isn't ready, or ensure this ItemGroup is initialized lazily or after item registration.
            // For now, we assume ItemsTC.thaumonomicon will be available or we use a placeholder.
            // return new ItemStack(ItemsTC.thaumonomicon.get()); // This will be the target once ItemsTC is updated
            return new ItemStack(net.minecraft.item.Items.BOOK); // Placeholder until ItemsTC.thaumonomicon is a RegistryObject
        }
    };
} 