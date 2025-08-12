package thaumcraft.api.research.theorycraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class ResearchTableData {
    public PlayerEntity player;
    public String research;
    public int inspiration;
    public int inspirationStart;
    public int bonusDraws;
    public Set<String> aids;
    public List<String> savedCards;
    public List<CardChoice> cardChoices;
    public List<TheorycraftCard> completeCards;

    public ResearchTableData() {
        this.savedCards = new ArrayList<String>();
        this.cardChoices = new ArrayList<CardChoice>();
        this.completeCards = new ArrayList<TheorycraftCard>();
    }

    public ResearchTableData(PlayerEntity player, TileEntity tileResearchTable) {
        this();
        this.player = player;
    }

    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("inspiration", inspiration);
        tag.putInt("inspirationStart", inspirationStart);
        tag.putInt("bonusDraws", bonusDraws);
        return tag;
    }

    public CompoundNBT serializeCardChoice(CardChoice mc) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("key", mc.key == null ? "" : mc.key);
        tag.putBoolean("selected", mc.selected);
        return tag;
    }

    public void deserialize(CompoundNBT nbt) {
        if (nbt == null) return;
        inspiration = nbt.getInt("inspiration");
        inspirationStart = nbt.getInt("inspirationStart");
        bonusDraws = nbt.getInt("bonusDraws");
    }

    public CardChoice deserializeCardChoice(CompoundNBT nbt) {
        if (nbt == null) return null;
        CardChoice c = new CardChoice();
        c.key = nbt.getString("key");
        c.selected = nbt.getBoolean("selected");
        c.card = null;
        c.seed = 0L;
        return c;
    }

    public void drawCards(int draw, PlayerEntity pe) {
        this.cardChoices.clear();
    }

    private TheorycraftCard generateCard(String key, long seed, PlayerEntity pe) {
        return null;
    }

    private TheorycraftCard generateCardWithNBT(String key, CompoundNBT nbt) {
        return null;
    }

    public void initialize(PlayerEntity player1, Set<String> aids) {
        this.player = player1;
        this.aids = aids;
        this.inspirationStart = getAvailableInspiration(player1);
        this.inspiration = this.inspirationStart;
    }

    public ArrayList<String> getAvailableCategories(PlayerEntity player) {
        return new ArrayList<>();
    }

    public static int getAvailableInspiration(PlayerEntity player) {
        return 5;
    }

    public class CardChoice {
        public TheorycraftCard card;
        public String key;
        public long seed;
        public boolean selected;
    }
}


