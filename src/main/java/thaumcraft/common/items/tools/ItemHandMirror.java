package thaumcraft.common.items.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.aura.AuraHelper;

public class ItemHandMirror extends Item {
    public ItemHandMirror(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        net.minecraft.nbt.CompoundNBT tag = stack.getOrCreateTag();
        if (player.isCrouching()) {
            // Link to current position
            tag.putInt("linkX", player.blockPosition().getX());
            tag.putInt("linkY", player.blockPosition().getY());
            tag.putInt("linkZ", player.blockPosition().getZ());
            tag.putString("linkDim", level.dimension().location().toString());
            if (!level.isClientSide) player.displayClientMessage(new TranslationTextComponent("tooltip.thaumcraft.hand_mirror.linked"), true);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        if (!level.isClientSide) {
            if (tag.contains("linkX")) {
                int x = tag.getInt("linkX");
                int y = tag.getInt("linkY");
                int z = tag.getInt("linkZ");
                String dim = tag.contains("linkDim") ? tag.getString("linkDim") : level.dimension().location().toString();
                // Allow cross-dimension teleport if same dimension or if server can find target world
                if (player instanceof net.minecraft.entity.player.ServerPlayerEntity) {
                    // Vis cost via config
                    float cost = thaumcraft.common.config.ModConfig.COMMON.mirrorVisCost.get().floatValue();
                    float drained = AuraHelper.drainVis(level, player.blockPosition(), cost);
                    if (drained + 0.001f < cost) {
                        player.displayClientMessage(new TranslationTextComponent("tooltip.thaumcraft.hand_mirror.no_vis"), true);
                    } else {
                        net.minecraft.util.ResourceLocation rl = new net.minecraft.util.ResourceLocation(dim);
                        net.minecraft.util.RegistryKey<net.minecraft.world.World> key = net.minecraft.util.RegistryKey.create(net.minecraft.util.registry.Registry.DIMENSION_REGISTRY, rl);
                        net.minecraft.server.MinecraftServer server = ((net.minecraft.entity.player.ServerPlayerEntity) player).getServer();
                        net.minecraft.world.server.ServerWorld targetWorld = server != null ? server.getLevel(key) : null;
                        if (targetWorld == null) {
                            // Fallback to current world
                            BlockPos tp = findSafeTeleportPos(level, new BlockPos(x, y, z));
                            ((net.minecraft.entity.player.ServerPlayerEntity) player).teleportTo(tp.getX() + 0.5, tp.getY(), tp.getZ() + 0.5);
                        } else if (targetWorld == level) {
                            BlockPos tp = findSafeTeleportPos(level, new BlockPos(x, y, z));
                            ((net.minecraft.entity.player.ServerPlayerEntity) player).teleportTo(tp.getX() + 0.5, tp.getY(), tp.getZ() + 0.5);
                        } else {
                            // Cross-dimension move
                            BlockPos tp = findSafeTeleportPos(targetWorld, new BlockPos(x, y, z));
                            player.changeDimension(targetWorld);
                            ((net.minecraft.entity.player.ServerPlayerEntity) player).teleportTo(tp.getX() + 0.5, tp.getY(), tp.getZ() + 0.5);
                        }
                        int cooldown = Math.max(0, thaumcraft.common.config.ModConfig.COMMON.mirrorCooldownSeconds.get());
                        player.getCooldowns().addCooldown(this, 20 * cooldown);
                    }
                }
            } else {
                player.displayClientMessage(new TranslationTextComponent("tooltip.thaumcraft.hand_mirror.unlinked"), true);
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    private static BlockPos findSafeTeleportPos(World level, BlockPos base) {
        BlockPos.Mutable m = new BlockPos.Mutable(base.getX(), Math.max(1, base.getY()), base.getZ());
        for (int i = 0; i < 16; i++) {
            if (level.isEmptyBlock(m) && level.isEmptyBlock(m.above())) {
                return m.immutable();
            }
            m.move(0, 1, 0);
        }
        return base;
    }
}



