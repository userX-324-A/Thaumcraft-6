package thaumcraft.common.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;

public class EssentiaTransportCapability {

    @CapabilityInject(IEssentiaTransport.class)
    public static Capability<IEssentiaTransport> ESSENTIA_TRANSPORT = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IEssentiaTransport.class, new Storage(), BasicEssentiaTransport::new);
    }

    public static class Storage implements Capability.IStorage<IEssentiaTransport> {
        @Override
        public INBT writeNBT(Capability<IEssentiaTransport> capability, IEssentiaTransport instance, Direction side) {
            return null; // no-op for now
        }

        @Override
        public void readNBT(Capability<IEssentiaTransport> capability, IEssentiaTransport instance, Direction side, INBT nbt) {
            // no-op for now
        }
    }

    public static class BasicEssentiaTransport implements IEssentiaTransport {
        private int suction;
        private Aspect suctionType;
        private Aspect storedType;
        private int storedAmount;
        private final int capacity;
        private java.util.EnumSet<Direction> inputSides = java.util.EnumSet.allOf(Direction.class);
        private java.util.EnumSet<Direction> outputSides = java.util.EnumSet.allOf(Direction.class);

        public BasicEssentiaTransport() {
            this(64, 0);
        }

        public BasicEssentiaTransport(int capacity, int initialSuction) {
            this.capacity = capacity;
            this.suction = initialSuction;
        }

        @Override
        public boolean isConnectable(Direction face) { return true; }

        @Override
        public boolean canInputFrom(Direction face) { return inputSides.contains(face); }

        @Override
        public boolean canOutputTo(Direction face) { return outputSides.contains(face); }

        @Override
        public void setSuction(Aspect aspect, int amount) { this.suctionType = aspect; this.suction = amount; }

        @Override
        public Aspect getSuctionType(Direction face) { return suctionType; }

        @Override
        public int getSuctionAmount(Direction face) { return suction; }

        @Override
        public int takeEssentia(Aspect aspect, int amount, Direction face) {
            if (storedType == null || storedAmount <= 0) return 0;
            if (aspect != null && storedType != aspect) return 0;
            int toExtract = Math.min(amount, storedAmount);
            storedAmount -= toExtract;
            if (storedAmount == 0) storedType = null;
            return toExtract;
        }

        @Override
        public int addEssentia(Aspect aspect, int amount, Direction face) {
            if (aspect == null || amount <= 0) return 0;
            if (storedType != null && storedType != aspect) return 0;
            int canAccept = capacity - storedAmount;
            if (canAccept <= 0) return 0;
            int accepted = Math.min(canAccept, amount);
            storedType = aspect;
            storedAmount += accepted;
            return accepted;
        }

        @Override
        public Aspect getEssentiaType(Direction face) { return storedType; }

        @Override
        public int getEssentiaAmount(Direction face) { return storedAmount; }

        @Override
        public int getMinimumSuction() { return 32; }

        @Override
        public int getSuction() { return suction; }

        @Override
        public void setSuction(int amount) { this.suction = amount; }

        // Helpers to configure sided IO
        public BasicEssentiaTransport allowInput(java.util.Set<Direction> faces) {
            this.inputSides = java.util.EnumSet.copyOf(faces);
            return this;
        }

        public BasicEssentiaTransport allowOutput(java.util.Set<Direction> faces) {
            this.outputSides = java.util.EnumSet.copyOf(faces);
            return this;
        }

        // State accessors for BE sync
        public Aspect getStoredType() { return storedType; }
        public int getStoredAmount() { return storedAmount; }
        public void setStored(Aspect type, int amount) {
            this.storedType = type;
            this.storedAmount = Math.max(0, Math.min(capacity, amount));
        }
    }
}




