package thaumcraft.api.research.theorycraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Base class for theorycraft cards. Provides default serialization for shared fields
 * and a deterministic seed used by cards to randomize choices in a stable way.
 */
public abstract class TheorycraftCard {
    protected long seed;

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * Default serialization stores the seed so cards can restore deterministic state.
     */
    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putLong("seed", seed);
        return tag;
    }

    /**
     * Default deserialization restores the seed.
     */
    public void deserialize(CompoundNBT nbt) {
        if (nbt == null) return;
        this.seed = nbt.getLong("seed");
    }

    /**
     * Optional initialization hook before the card is shown/used.
     * Default returns true and does nothing.
     */
    public boolean initialize(PlayerEntity player, ResearchTableData data) {
        return true;
    }

    public abstract boolean activate(PlayerEntity player, ResearchTableData data);

    public abstract String getResearchCategory();

    public abstract int getInspirationCost();

    public abstract String getLocalizedName();

    public abstract String getLocalizedText();

    /**
     * Default: no mutations.
     */
    public TheorycraftCard[] getMutations() {
        return null;
    }

    /**
     * Default: not aid-only.
     */
    public boolean isAidOnly() {
        return false;
    }

    /**
     * Optional: items required to play this card.
     */
    public ItemStack[] getRequiredItems() {
        return null;
    }

    /**
     * Optional: whether required items are consumed.
     */
    public boolean[] getRequiredItemsConsumed() {
        return null;
    }
}

