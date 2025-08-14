package thaumcraft.common.lib.utils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.api.crafting.IngredientNBTTC;
import thaumcraft.api.items.ItemsTC;


public class InventoryUtils
{
    public static ItemStack copyMaxedStack(ItemStack stack) {
        return copyLimitedStack(stack, stack.getMaxStackSize());
    }
    
    public static ItemStack copyLimitedStack(ItemStack stack, int limit) {
        if (stack == null) {
            return ItemStack.EMPTY;
        }
        ItemStack s = stack.copy();
        if (s.getCount() > limit) {
            s.setCount(limit);
        }
        return s;
    }
    
    public static boolean consumeItemsFromAdjacentInventoryOrPlayer(World world, BlockPos pos, PlayerEntity player, boolean sim, ItemStack... items) {
        for (ItemStack stack : items) {
            boolean b = checkAdjacentChests(world, pos, stack);
            if (!b) {
                b = isPlayerCarryingAmount(player, stack, true);
            }
            if (!b) {
                return false;
            }
        }
        if (!sim) {
            for (ItemStack stack : items) {
                if (!consumeFromAdjacentChests(world, pos, stack.copy())) {
                    consumePlayerItem(player, stack, true, true);
                }
            }
        }
        return true;
    }
    
    public static boolean checkAdjacentChests(World world, BlockPos pos, ItemStack itemStack) {
        int c = itemStack.getCount();
        for (Direction face : Direction.values()) {
            if (face != Direction.UP) {
                c -= ThaumcraftInvHelper.countTotalItemsIn(world, pos.relative(face), face.getOpposite(), itemStack.copy(), ThaumcraftInvHelper.InvFilter.BASEORE);
                if (c <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean consumeFromAdjacentChests(World world, BlockPos pos, ItemStack itemStack) {
        for (Direction face : Direction.values()) {
            if (face != Direction.UP) {
                if (!itemStack.isEmpty()) {
                    ItemStack os = removeStackFrom(world, pos.relative(face), face.getOpposite(), itemStack, ThaumcraftInvHelper.InvFilter.BASEORE, false);
                    itemStack.setCount(itemStack.getCount() - os.getCount());
                    if (itemStack.isEmpty()) {
                        break;
                    }
                }
            }
        }
        return itemStack.isEmpty();
    }
    
    @Deprecated
    public static ItemStack insertStackAt(World world, BlockPos pos, Direction side, ItemStack stack, boolean simulate) {
        return ThaumcraftInvHelper.insertStackAt(world, pos, side, stack, simulate);
    }
    
    public static void ejectStackAt(World world, BlockPos pos, Direction side, ItemStack out) {
        ejectStackAt(world, pos, side, out, false);
    }
    
    public static ItemStack ejectStackAt(World world, BlockPos pos, Direction side, ItemStack out, boolean smart) {
        out = ThaumcraftInvHelper.insertStackAt(world, pos.relative(side), side.getOpposite(), out, false);
        if (smart && ThaumcraftInvHelper.getItemHandlerAt(world, pos.relative(side), side.getOpposite()) != null) {
            return out;
        }
        if (!out.isEmpty()) {
            if (world.getBlockState(pos.relative(side)).isSolidRender(world, pos.relative(side))) {
                pos = pos.relative(side.getOpposite());
            }
            ItemEntity entityitem2 = new ItemEntity(world, pos.getX() + 0.5 + side.getStepX(), pos.getY() + side.getStepY(), pos.getZ() + 0.5 + side.getStepZ(), out);
            entityitem2.setDeltaMovement(0.3 * side.getStepX(), 0.3 * side.getStepY(), 0.3 * side.getStepZ());
            world.addFreshEntity(entityitem2);
        }
        return ItemStack.EMPTY;
    }
    
    public static ItemStack removeStackFrom(World world, BlockPos pos, Direction side, ItemStack stack, ThaumcraftInvHelper.InvFilter filter, boolean simulate) {
        return removeStackFrom(ThaumcraftInvHelper.getItemHandlerAt(world, pos, side), stack, filter, simulate);
    }
    
    public static ItemStack removeStackFrom(IItemHandler inventory, ItemStack stack, ThaumcraftInvHelper.InvFilter filter, boolean simulate) {
        int amount = stack.getCount();
        int removed = 0;
        if (inventory != null) {
            for (int a = 0; a < inventory.getSlots(); ++a) {
                if (areItemStacksEqual(stack, inventory.getStackInSlot(a), filter)) {
                    int s = Math.min(amount - removed, inventory.getStackInSlot(a).getCount());
                    ItemStack es = inventory.extractItem(a, s, simulate);
                    if (es != null && !es.isEmpty()) {
                        removed += es.getCount();
                    }
                }
                if (removed >= amount) {
                    break;
                }
            }
        }
        if (removed == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack s2 = stack.copy();
        s2.setCount(removed);
        return s2;
    }
    
    public static int countStackInWorld(World world, BlockPos pos, ItemStack stack, double range, ThaumcraftInvHelper.InvFilter filter) {
        int count = 0;
        net.minecraft.util.math.AxisAlignedBB box = new net.minecraft.util.math.AxisAlignedBB(
                pos.getX() + 0.5 - range, pos.getY() + 0.5 - range, pos.getZ() + 0.5 - range,
                pos.getX() + 0.5 + range, pos.getY() + 0.5 + range, pos.getZ() + 0.5 + range
        );
        List<ItemEntity> l = world.getEntitiesOfClass(ItemEntity.class, box);
        for (ItemEntity ei : l) {
            if (ei.getItem() != null && !ei.getItem().isEmpty() && areItemStacksEqual(stack, ei.getItem(), filter)) {
                count += ei.getItem().getCount();
            }
        }
        return count;
    }
    
    public static void dropItems(World world, BlockPos pos) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory)tileEntity;
        InventoryHelper.dropContents(world, pos, inventory);
    }
    
    public static boolean consumePlayerItem(PlayerEntity player, ItemStack item, boolean nocheck, boolean ore) {
        if (!nocheck && !isPlayerCarryingAmount(player, item, ore)) {
            return false;
        }
        int count = item.getCount();
        for (int var2 = 0; var2 < player.inventory.items.size(); ++var2) {
            if (checkEnchantedPlaceholder(item, player.inventory.items.get(var2)) || areItemStacksEqual(player.inventory.items.get(var2), item, new ThaumcraftInvHelper.InvFilter(false, item.getTag() == null, ore, false).setRelaxedNBT())) {
                if (player.inventory.items.get(var2).getCount() > count) {
                    player.inventory.items.get(var2).shrink(count);
                    count = 0;
                }
                else {
                    count -= player.inventory.items.get(var2).getCount();
                    player.inventory.items.set(var2, ItemStack.EMPTY);
                }
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean consumePlayerItem(PlayerEntity player, Item item, int md, int amt) {
        if (!isPlayerCarryingAmount(player, new ItemStack(item, amt), false)) {
            return false;
        }
        int count = amt;
        for (int var2 = 0; var2 < player.inventory.items.size(); ++var2) {
            if (player.inventory.items.get(var2).getItem() == item) {
                if (player.inventory.items.get(var2).getCount() > count) {
                    player.inventory.items.get(var2).shrink(count);
                    count = 0;
                }
                else {
                    count -= player.inventory.items.get(var2).getCount();
                    player.inventory.items.set(var2, ItemStack.EMPTY);
                }
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean consumePlayerItem(PlayerEntity player, Item item, int md) {
        for (int var2 = 0; var2 < player.inventory.items.size(); ++var2) {
            if (player.inventory.items.get(var2).getItem() == item) {
                player.inventory.items.get(var2).shrink(1);
                if (player.inventory.items.get(var2).getCount() <= 0) {
                    player.inventory.items.set(var2, ItemStack.EMPTY);
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPlayerCarryingAmount(PlayerEntity player, ItemStack stack, boolean ore) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        int count = stack.getCount();
        for (int var2 = 0; var2 < player.inventory.items.size(); ++var2) {
            if (checkEnchantedPlaceholder(stack, player.inventory.items.get(var2)) || areItemStacksEqual(player.inventory.items.get(var2), stack, new ThaumcraftInvHelper.InvFilter(false, stack.getTag() == null, ore, false).setRelaxedNBT())) {
                count -= player.inventory.items.get(var2).getCount();
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean checkEnchantedPlaceholder(ItemStack stack, ItemStack stack2) {
        // Placeholder fallback: enchantedPlaceholder may not exist in 1.16 port; treat as false
        if (stack.getItem() == null) {
            return false;
        }
        Map<Enchantment, Integer> en = EnchantmentHelper.getEnchantments(stack);
        boolean b = !en.isEmpty();
    Label_0181:
        for (Enchantment e : en.keySet()) {
            Map<Enchantment, Integer> en2 = EnchantmentHelper.getEnchantments(stack2);
            if (en2.isEmpty()) {
                return false;
            }
            b = false;
            for (Enchantment e2 : en2.keySet()) {
                if (!e2.equals(e)) {
                    continue;
                }
                b = true;
                if (en2.get(e2) < en.get(e)) {
                    b = false;
                    break Label_0181;
                }
            }
        }
        return b;
    }
    
    public static EquipmentSlotType isHoldingItem(PlayerEntity player, Item item) {
        if (player == null || item == null) {
            return null;
        }
        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() == item) {
            return EquipmentSlotType.MAINHAND;
        }
        if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() == item) {
            return EquipmentSlotType.OFFHAND;
        }
        return null;
    }
    
    public static EquipmentSlotType isHoldingItem(PlayerEntity player, Class item) {
        if (player == null || item == null) {
            return null;
        }
        if (!player.getMainHandItem().isEmpty() && item.isAssignableFrom(player.getMainHandItem().getItem().getClass())) {
            return EquipmentSlotType.MAINHAND;
        }
        if (!player.getOffhandItem().isEmpty() && item.isAssignableFrom(player.getOffhandItem().getItem().getClass())) {
            return EquipmentSlotType.OFFHAND;
        }
        return null;
    }
    
    public static int getPlayerSlotFor(PlayerEntity player, ItemStack stack) {
        for (int i = 0; i < player.inventory.items.size(); ++i) {
            if (!player.inventory.items.get(i).isEmpty() && stackEqualExact(stack, player.inventory.items.get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }
    
    public static boolean areItemStacksEqualStrict(ItemStack stack0, ItemStack stack1) {
        return areItemStacksEqual(stack0, stack1, ThaumcraftInvHelper.InvFilter.STRICT);
    }
    
    public static ItemStack findFirstMatchFromFilter(NonNullList<ItemStack> filterStacks, boolean blacklist, IItemHandler inv, Direction face, ThaumcraftInvHelper.InvFilter filter) {
        return findFirstMatchFromFilter(filterStacks, blacklist, inv, face, filter, false);
    }
    
    public static ItemStack findFirstMatchFromFilter(NonNullList<ItemStack> filterStacks, boolean blacklist, IItemHandler inv, Direction face, ThaumcraftInvHelper.InvFilter filter, boolean leaveOne) {
    Label_0181:
        for (int a = 0; a < inv.getSlots(); ++a) {
            ItemStack is = inv.getStackInSlot(a);
            if (is != null && !is.isEmpty()) {
                if (is.getCount() > 0) {
                    if (!leaveOne || ThaumcraftInvHelper.countTotalItemsIn(inv, is, filter) >= 2) {
                        boolean allow = false;
                        boolean allEmpty = true;
                        for (ItemStack fs : filterStacks) {
                            if (fs != null) {
                                if (fs.isEmpty()) {
                                    continue;
                                }
                                allEmpty = false;
                                boolean r = areItemStacksEqual(fs.copy(), is.copy(), filter);
                                if (blacklist) {
                                    if (r) {
                                        continue Label_0181;
                                    }
                                    allow = true;
                                }
                                else {
                                    if (r) {
                                        return is;
                                    }
                                    continue;
                                }
                            }
                        }
                        if (blacklist && (allow || allEmpty)) {
                            return is;
                        }
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }
    
    public static boolean matchesFilters(NonNullList<ItemStack> nonNullList, boolean blacklist, ItemStack is, ThaumcraftInvHelper.InvFilter filter) {
        if (is == null || is.isEmpty() || is.getCount() <= 0) {
            return false;
        }
        boolean allow = false;
        boolean allEmpty = true;
        for (ItemStack fs : nonNullList) {
            if (fs != null) {
                if (fs.isEmpty()) {
                    continue;
                }
                allEmpty = false;
                boolean r = areItemStacksEqual(fs.copy(), is.copy(), filter);
                if (blacklist) {
                    if (r) {
                        return false;
                    }
                    allow = true;
                }
                else {
                    if (r) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return blacklist && (allow || allEmpty);
    }
    
    public static ItemStack findFirstMatchFromFilter(NonNullList<ItemStack> filterStacks, NonNullList<Integer> filterStacksSizes, boolean blacklist, NonNullList<ItemStack> itemStacks, ThaumcraftInvHelper.InvFilter filter) {
        Tuple<ItemStack, Integer> t = findFirstMatchFromFilterTuple(filterStacks, filterStacksSizes, blacklist, itemStacks, filter);
        return t == null ? ItemStack.EMPTY : t.getA();
    }
    
    public static Tuple<ItemStack, Integer> findFirstMatchFromFilterTuple(NonNullList<ItemStack> filterStacks, NonNullList<Integer> filterStacksSizes, boolean blacklist, NonNullList<ItemStack> stacks, ThaumcraftInvHelper.InvFilter filter) {
    Label_0006:
        for (ItemStack is : stacks) {
            if (is != null && !is.isEmpty()) {
                if (is.getCount() <= 0) {
                    continue;
                }
                boolean allow = false;
                boolean allEmpty = true;
                for (int idx = 0; idx < filterStacks.size(); ++idx) {
                    ItemStack fs = filterStacks.get(idx);
                    if (fs != null) {
                        if (!fs.isEmpty()) {
                            allEmpty = false;
                            boolean r = areItemStacksEqual(fs.copy(), is.copy(), filter);
                            if (blacklist) {
                                if (r) {
                                    continue Label_0006;
                                }
                                allow = true;
                            }
                            else if (r) {
                                return (Tuple<ItemStack, Integer>)new Tuple(is, filterStacksSizes.get(idx));
                            }
                        }
                    }
                }
                if (blacklist && (allow || allEmpty)) {
                    return (Tuple<ItemStack, Integer>)new Tuple(is, 0);
                }
                continue;
            }
        }
        return (Tuple<ItemStack, Integer>)new Tuple(ItemStack.EMPTY, 0);
    }
    
    public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, ThaumcraftInvHelper.InvFilter filter) {
        if (stack0 == null && stack1 != null) {
            return false;
        }
        if (stack0 != null && stack1 == null) {
            return false;
        }
        if (stack0 == null && stack1 == null) {
            return true;
        }
        if (stack0.isEmpty() && !stack1.isEmpty()) {
            return false;
        }
        if (!stack0.isEmpty() && stack1.isEmpty()) {
            return false;
        }
        if (stack0.isEmpty() && stack1.isEmpty()) {
            return true;
        }
        if (filter.useMod) {
            String m1 = "A";
            String m2 = "B";
            String a = stack0.getItem().getRegistryName() != null ? stack0.getItem().getRegistryName().getNamespace() : null;
            if (a != null) {
                m1 = a;
            }
            String b = stack1.getItem().getRegistryName() != null ? stack1.getItem().getRegistryName().getNamespace() : null;
            if (b != null) {
                m2 = b;
            }
            return m1.equals(m2);
        }
        if (filter.useOre && !stack0.isEmpty()) {
            if (TagUtils.itemsShareAnyForgeTag(stack0, stack1)) {
                return true;
            }
        }
        boolean t1 = true;
        if (!filter.igNBT) {
            t1 = (filter.relaxedNBT ? ThaumcraftInvHelper.areItemStackTagsEqualRelaxed(stack0, stack1) : ItemStack.tagMatches(stack0, stack1));
        }
        return stack0.getItem() == stack1.getItem() && t1;
    }
    
    public static void dropHarvestsAtPos(World worldIn, BlockPos pos, List<ItemStack> list) {
        dropHarvestsAtPos(worldIn, pos, list, false, 0, null);
    }
    
    public static void dropHarvestsAtPos(World worldIn, BlockPos pos, List<ItemStack> list, boolean followItem, int color, Entity target) {
        for (ItemStack item : list) {
            if (!worldIn.isClientSide && worldIn.getGameRules().getBoolean(net.minecraft.world.GameRules.RULE_DOBLOCKDROPS)) {
                float f = 0.5f;
                double d0 = worldIn.random.nextFloat() * f + (1.0f - f) * 0.5;
                double d2 = worldIn.random.nextFloat() * f + (1.0f - f) * 0.5;
                double d3 = worldIn.random.nextFloat() * f + (1.0f - f) * 0.5;
                ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d2, pos.getZ() + d3, item);
                entityitem.setDefaultPickUpDelay();
                worldIn.addFreshEntity(entityitem);
            }
        }
    }
    
    public static void dropItemAtPos(World world, ItemStack item, BlockPos pos) {
        if (!world.isClientSide && item != null && !item.isEmpty() && item.getCount() > 0) {
            ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item.copy());
            world.addFreshEntity(entityItem);
        }
    }
    
    public static void dropItemAtEntity(World world, ItemStack item, Entity entity) {
        if (!world.isClientSide && item != null && !item.isEmpty() && item.getCount() > 0) {
            ItemEntity entityItem = new ItemEntity(world, entity.getX(), entity.getY() + entity.getEyeHeight() / 2.0f, entity.getZ(), item.copy());
            world.addFreshEntity(entityItem);
        }
    }
    
    public static void dropItemsAtEntity(World world, BlockPos pos, Entity entity) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof IInventory) || world.isClientSide) {
            return;
        }
        IInventory inventory = (IInventory)tileEntity;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            if (!item.isEmpty() && item.getCount() > 0) {
                ItemEntity entityItem = new ItemEntity(world, entity.getX(), entity.getY() + entity.getEyeHeight() / 2.0f, entity.getZ(), item.copy());
                world.addFreshEntity(entityItem);
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
    }
    
    public static ItemStack cycleItemStack(Object input) {
        return cycleItemStack(input, 0);
    }
    
    public static ItemStack cycleItemStack(Object input, int counter) {
        ItemStack it = ItemStack.EMPTY;
        if (input instanceof Ingredient) {
            boolean b = !((Ingredient)input).isSimple() && !(input instanceof IngredientNBTTC);
            input = ((Ingredient)input).getItems();
            if (b) {
                ItemStack[] q = (ItemStack[])input;
                ItemStack[] r = new ItemStack[q.length];
                for (int a = 0; a < q.length; ++a) {
                    (r[a] = q[a].copy()).setDamageValue(32767);
                }
                input = r;
            }
        }
        if (input instanceof ItemStack[]) {
            ItemStack[] q2 = (ItemStack[])input;
            if (q2 != null && q2.length > 0) {
                int idx = (int)((counter + System.currentTimeMillis() / 1000L) % q2.length);
                it = cycleItemStack(q2[idx], counter++);
            }
        }
        else if (input instanceof ItemStack) {
            it = (ItemStack)input;
            if (it != null && !it.isEmpty() && it.getItem() != null && it.isDamageableItem() && it.getDamageValue() == 32767) {
                int q3 = 5000 / it.getMaxDamage();
                int md = (int)((counter + System.currentTimeMillis() / q3) % it.getMaxDamage());
                ItemStack it2 = new ItemStack(it.getItem(), 1);
                it2.setDamageValue(md);
                it2.setTag(it.getTag());
                it = it2;
            }
        }
        else if (input instanceof List) {
            List<ItemStack> q4 = (List<ItemStack>)input;
            if (q4 != null && q4.size() > 0) {
                int idx = (int)((counter + System.currentTimeMillis() / 1000L) % q4.size());
                it = cycleItemStack(q4.get(idx), counter++);
            }
        }
        else if (input instanceof String) {
            NonNullList<ItemStack> q4 = TagUtils.getItemsForOreName((String)input);
            if (q4 != null && q4.size() > 0) {
                int idx = (int)((counter + System.currentTimeMillis() / 1000L) % q4.size());
                it = cycleItemStack(q4.get(idx), counter++);
            }
        }
        return it;
    }
}


