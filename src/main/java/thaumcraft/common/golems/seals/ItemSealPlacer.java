package thaumcraft.common.golems.seals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.SealPos;
import thaumcraft.api.golems.seals.ISealEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.inventory.container.Container;
import thaumcraft.common.menu.SealContainer;

public class ItemSealPlacer extends Item {
    private final String fixedSealKey; // if non-null, this item always places this seal type

    public ItemSealPlacer(Properties properties) { this(properties, null); }

    public ItemSealPlacer(Properties properties, String fixedSealKey) {
        super(properties);
        this.fixedSealKey = fixedSealKey;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) return ActionResultType.SUCCESS;

        ServerWorld serverWorld = (ServerWorld) world;
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.PASS;

        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        ItemStack stack = context.getItemInHand();
        String key = this.fixedSealKey;
        if (key == null || key.isEmpty()) {
            key = stack.hasTag() ? stack.getTag().getString("seal_key") : "";
        }
        if (key == null || key.isEmpty()) key = "thaumcraft:fetch";

        ISeal seal = SealRegistry.create(key);
        if (seal == null) return ActionResultType.PASS;

        // Sneak-use removes an existing seal (owner or creative only)
        if (player.isShiftKeyDown()) {
            ISealEntity existing = SealWorldData.get(serverWorld).get(pos, face);
            if (existing != null) {
                boolean canEdit = player.abilities.instabuild || player.getUUID().toString().equals(existing.getOwner());
                if (!canEdit) return ActionResultType.PASS;
                if (existing.getSeal() != null) existing.getSeal().onRemoval(world, pos, face);
                SealWorldData.get(serverWorld).remove(pos, face);
                thaumcraft.common.network.NetworkHandler.sendToAllAround(
                        new thaumcraft.common.network.msg.ClientSealMessage(pos, face, "REMOVE", (byte)0, (byte)0, false, false, ""),
                        serverWorld,
                        pos,
                        64);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }

        // If a seal already exists at this face and not sneaking, open GUI instead of placing
        ISealEntity existing = SealWorldData.get(serverWorld).get(pos, face);
        if (existing != null && !player.isShiftKeyDown()) {
            // Owner check for opening GUI
            boolean canEdit = player.abilities.instabuild || player.getUUID().toString().equals(existing.getOwner());
            if (!canEdit) return ActionResultType.PASS;

            // Prefer seal-provided container if available
            Object custom = existing.getSeal() == null ? null : existing.getSeal().returnContainer(world, player, pos, face, existing);
            if (custom instanceof INamedContainerProvider) {
                NetworkHooks.openGui((net.minecraft.entity.player.ServerPlayerEntity) player, (INamedContainerProvider) custom, (packetBuffer) -> {});
                return ActionResultType.SUCCESS;
            }
            // Client-only custom GUI support
            Object gui = existing.getSeal() == null ? null : existing.getSeal().returnGui(world, player, pos, face, existing);
            if (gui instanceof net.minecraft.client.gui.screen.Screen) {
                // Ask client to open its own screen via a simple container to carry context
                // For now, fall back to default SealContainer; a custom ClientOpen message could be added later
            }

            INamedContainerProvider provider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() { return new StringTextComponent("Seal"); }

                @Override
                public Container createMenu(int windowId, net.minecraft.entity.player.PlayerInventory inv, PlayerEntity p) {
                    return new SealContainer(windowId, inv, pos, face);
                }
            };
            NetworkHooks.openGui((net.minecraft.entity.player.ServerPlayerEntity) player, provider, (packetBuffer) -> {
                packetBuffer.writeBlockPos(pos);
                packetBuffer.writeByte((byte) face.get3DDataValue());
            });
            return ActionResultType.SUCCESS;
        }

        if (!seal.canPlaceAt(world, pos, face)) return ActionResultType.PASS;
        // Prevent duplicate placement at same position/face
        if (SealWorldData.get(serverWorld).contains(pos, face)) return ActionResultType.PASS;

        SealEntity entity = new SealEntity(world, new SealPos(pos, face), seal);
        entity.setOwner(player.getUUID());
        SealWorldData data = SealWorldData.get(serverWorld);
        data.put(entity);
        entity.syncToClient(world);

        if (!player.abilities.instabuild) {
            stack.shrink(1);
        }
        return ActionResultType.SUCCESS;
    }
}


