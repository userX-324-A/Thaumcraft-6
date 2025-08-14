package thaumcraft.common.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thaumcraft.Thaumcraft;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID)
public final class CapabilityInit {

    private CapabilityInit() {}

    public static final ResourceLocation KNOWLEDGE_RL = new ResourceLocation(Thaumcraft.MODID, "knowledge");
    public static final ResourceLocation WARP_RL = new ResourceLocation(Thaumcraft.MODID, "warp");

    @CapabilityInject(IPlayerKnowledge.class)
    public static Capability<IPlayerKnowledge> PLAYER_KNOWLEDGE_CAP = null;
    @CapabilityInject(IPlayerWarp.class)
    public static Capability<IPlayerWarp> PLAYER_WARP_CAP = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerKnowledge.class, new Storage(), PlayerKnowledge::new);
        CapabilityManager.INSTANCE.register(IPlayerWarp.class, new WarpStorage(), PlayerWarp::new);
    }

    public static class Storage implements Capability.IStorage<IPlayerKnowledge> {
        @Nullable
        @Override
        public CompoundNBT writeNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, net.minecraft.util.Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, net.minecraft.util.Direction side, net.minecraft.nbt.INBT nbt) {
            if (nbt instanceof CompoundNBT) instance.deserializeNBT((CompoundNBT) nbt);
        }
    }

    public static class WarpStorage implements Capability.IStorage<IPlayerWarp> {
        @Nullable
        @Override
        public CompoundNBT writeNBT(Capability<IPlayerWarp> capability, IPlayerWarp instance, net.minecraft.util.Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IPlayerWarp> capability, IPlayerWarp instance, net.minecraft.util.Direction side, net.minecraft.nbt.INBT nbt) {
            if (nbt instanceof CompoundNBT) instance.deserializeNBT((CompoundNBT) nbt);
        }
    }

    // Provider and attachment
    public static class KnowledgeProvider implements ICapabilityProvider, net.minecraftforge.common.util.INBTSerializable<CompoundNBT> {
        private final IPlayerKnowledge backend = new PlayerKnowledge();

        @Nonnull
        @Override
        public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
            if (cap == PLAYER_KNOWLEDGE_CAP) {
                return net.minecraftforge.common.util.LazyOptional.of(() -> backend).cast();
            }
            return net.minecraftforge.common.util.LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            backend.deserializeNBT(nbt);
        }
    }

    public static class WarpProvider implements ICapabilityProvider, net.minecraftforge.common.util.INBTSerializable<CompoundNBT> {
        private final IPlayerWarp backend = new PlayerWarp();

        @Nonnull
        @Override
        public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
            if (cap == PLAYER_WARP_CAP) {
                return net.minecraftforge.common.util.LazyOptional.of(() -> backend).cast();
            }
            return net.minecraftforge.common.util.LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            backend.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(KNOWLEDGE_RL, new KnowledgeProvider());
            event.addCapability(WARP_RL, new WarpProvider());
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PLAYER_KNOWLEDGE_CAP).ifPresent(oldCap ->
                event.getPlayer().getCapability(PLAYER_KNOWLEDGE_CAP).ifPresent(newCap -> newCap.deserializeNBT(oldCap.serializeNBT()))
            );
            event.getOriginal().getCapability(PLAYER_WARP_CAP).ifPresent(oldCap ->
                event.getPlayer().getCapability(PLAYER_WARP_CAP).ifPresent(newCap -> newCap.deserializeNBT(oldCap.serializeNBT()))
            );
        }
    }
}



