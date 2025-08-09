package thaumcraft.api.research.theorycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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
        return null;
    }
    
    public CompoundNBT serializeCardChoice(CardChoice mc) {
        return null;
    }
    
    public void deserialize(CompoundNBT nbt) {
    }
    
    public CardChoice deserializeCardChoice(CompoundNBT nbt) {
        return null;
    }
    
    public void drawCards(int draw, PlayerEntity pe) {
    }
    
    private TheorycraftCard generateCard(String key, long seed, PlayerEntity pe) {
        return null;
    }
    
    private TheorycraftCard generateCardWithNBT(String key, CompoundNBT nbt) {
        return null;
    }
    
    public void initialize(PlayerEntity player1, Set<String> aids) {
    }
    
    public ArrayList<String> getAvailableCategories(PlayerEntity player) {
        return null;
    }
    
    public static int getAvailableInspiration(PlayerEntity player) {
        return 0;
    }
    
    public class CardChoice {
        public TheorycraftCard card;
        public String key;
        public long seed;
        public boolean selected;
    }
}


