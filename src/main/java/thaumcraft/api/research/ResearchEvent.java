package thaumcraft.api.research;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;
import thaumcraft.api.capabilities.IPlayerKnowledge;

public class ResearchEvent extends Event {
    private PlayerEntity player;
    
    public ResearchEvent(PlayerEntity player) {
        this.player = player;
    }
    
    public PlayerEntity getPlayer() {
        return this.player;
    }
    
    public static class Knowledge extends ResearchEvent {
        private final IPlayerKnowledge.EnumKnowledgeType type;
        private final ResearchCategory category;
        private final int amount;

        public Knowledge(PlayerEntity player, IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category, int amount) {
            super(player);
            this.type = type;
            this.category = category;
            this.amount = amount;
        }

        public IPlayerKnowledge.EnumKnowledgeType getType() { return type; }
        public ResearchCategory getCategory() { return this.category; }
        public int getAmount() { return this.amount; }
    }
    
    public static class Research extends ResearchEvent {
        private String researchKey;
        
        public Research(PlayerEntity player, String researchKey) {
            super(player);
            this.researchKey = researchKey;
        }
        
        public String getResearchKey() {
            return this.researchKey;
        }
    }
}


