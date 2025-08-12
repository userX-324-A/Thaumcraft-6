package thaumcraft.common.capabilities;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.ClientSyncKnowledgeMessage;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Stores and syncs a player's Thaumcraft research and knowledge points.
 */
public class PlayerKnowledge implements IPlayerKnowledge {

    // research key -> stage (0 means known without stages, -1 unknown)
    private final Map<String, Integer> researchStages = new HashMap<>();
    // research key -> flags set
    private final Map<String, EnumSet<EnumResearchFlag>> researchFlags = new HashMap<>();
    // knowledge type -> (categoryKey or "NONE") -> raw points
    private final EnumMap<EnumKnowledgeType, Map<String, Integer>> knowledgePoints = new EnumMap<>(EnumKnowledgeType.class);

    public PlayerKnowledge() {
        for (EnumKnowledgeType type : EnumKnowledgeType.values()) {
            knowledgePoints.put(type, new HashMap<>());
        }
    }

    @Override
    public void clear() {
        researchStages.clear();
        researchFlags.clear();
        for (Map<String, Integer> map : knowledgePoints.values()) map.clear();
    }

    @Override
    public EnumResearchStatus getResearchStatus(@Nonnull String research) {
        if (!isResearchKnown(research)) return EnumResearchStatus.UNKNOWN;
        return isResearchComplete(research) ? EnumResearchStatus.COMPLETE : EnumResearchStatus.IN_PROGRESS;
    }

    @Override
    public boolean isResearchComplete(String research) {
        int stage = getResearchStageInternal(research);
        if (stage < 0) return false;
        ResearchEntry entry = ResearchCategories.getResearch(research);
        if (entry == null || entry.getStages() == null || entry.getStages().length == 0) {
            // known without stages counts as complete when stage > 0
            return stage > 0;
        }
        return stage > entry.getStages().length;
    }

    @Override
    public boolean isResearchKnown(String res) {
        return getResearchStageInternal(res) >= 0;
    }

    @Override
    public int getResearchStage(@Nonnull String research) {
        // supports key@stage check
        String key = research;
        int requiredStage = -1;
        int at = research.indexOf('@');
        if (at > 0) {
            key = research.substring(0, at);
            try { requiredStage = Integer.parseInt(research.substring(at + 1)); } catch (Exception ignored) {}
        }
        int stage = getResearchStageInternal(key);
        if (requiredStage >= 0) return stage >= requiredStage ? requiredStage : -1;
        return stage;
    }

    private int getResearchStageInternal(String key) {
        return researchStages.getOrDefault(key, -1);
    }

    @Override
    public boolean addResearch(@Nonnull String research) {
        if (isResearchKnown(research)) return false;
        researchStages.put(research, 0);
        return true;
    }

    @Override
    public boolean setResearchStage(@Nonnull String research, int stage) {
        if (!isResearchKnown(research)) researchStages.put(research, 0);
        researchStages.put(research, Math.max(stage, 0));
        return true;
    }

    @Override
    public boolean removeResearch(@Nonnull String research) {
        researchStages.remove(research);
        researchFlags.remove(research);
        return true;
    }

    @Nonnull
    @Override
    public Set<String> getResearchList() {
        return Collections.unmodifiableSet(researchStages.keySet());
    }

    @Override
    public boolean setResearchFlag(@Nonnull String research, @Nonnull EnumResearchFlag flag) {
        EnumSet<EnumResearchFlag> set = researchFlags.computeIfAbsent(research, k -> EnumSet.noneOf(EnumResearchFlag.class));
        return set.add(flag);
    }

    @Override
    public boolean clearResearchFlag(@Nonnull String research, @Nonnull EnumResearchFlag flag) {
        EnumSet<EnumResearchFlag> set = researchFlags.get(research);
        return set != null && set.remove(flag);
    }

    @Override
    public boolean hasResearchFlag(@Nonnull String research, @Nonnull EnumResearchFlag flag) {
        EnumSet<EnumResearchFlag> set = researchFlags.get(research);
        return set != null && set.contains(flag);
    }

    @Override
    public boolean addKnowledge(@Nonnull EnumKnowledgeType type, ResearchCategory category, int amount) {
        String key = category == null ? "NONE" : category.key;
        Map<String, Integer> map = knowledgePoints.get(type);
        int current = map.getOrDefault(key, 0);
        map.put(key, current + amount);
        return true;
    }

    @Override
    public int getKnowledge(@Nonnull EnumKnowledgeType type, ResearchCategory category) {
        int raw = getKnowledgeRaw(type, category);
        return raw / Math.max(1, type.getProgression());
    }

    @Override
    public int getKnowledgeRaw(@Nonnull EnumKnowledgeType type, ResearchCategory category) {
        String key = category == null ? "NONE" : category.key;
        return knowledgePoints.get(type).getOrDefault(key, 0);
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        NetworkHandler.sendTo(player, new ClientSyncKnowledgeMessage(this));
    }

    // INBTSerializable
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT root = new CompoundNBT();

        // research stages
        ListNBT researchList = new ListNBT();
        for (Map.Entry<String, Integer> e : researchStages.entrySet()) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("key", e.getKey());
            tag.putInt("stage", e.getValue());
            // flags
            EnumSet<EnumResearchFlag> set = researchFlags.get(e.getKey());
            ListNBT flags = new ListNBT();
            if (set != null) for (EnumResearchFlag f : set) flags.add(StringNBT.valueOf(f.name()));
            tag.put("flags", flags);
            researchList.add(tag);
        }
        root.put("research", researchList);

        // knowledge
        CompoundNBT know = new CompoundNBT();
        for (EnumKnowledgeType type : EnumKnowledgeType.values()) {
            CompoundNBT typeTag = new CompoundNBT();
            for (Map.Entry<String, Integer> e : knowledgePoints.get(type).entrySet()) {
                typeTag.putInt(e.getKey(), e.getValue());
            }
            know.put(type.name(), typeTag);
        }
        root.put("knowledge", know);

        return root;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        clear();
        // research
        ListNBT researchList = nbt.getList("research", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < researchList.size(); i++) {
            CompoundNBT tag = researchList.getCompound(i);
            String key = tag.getString("key");
            int stage = tag.getInt("stage");
            researchStages.put(key, stage);
            EnumSet<EnumResearchFlag> set = EnumSet.noneOf(EnumResearchFlag.class);
            ListNBT flags = tag.getList("flags", Constants.NBT.TAG_STRING);
            for (int j = 0; j < flags.size(); j++) {
                try { set.add(EnumResearchFlag.valueOf(flags.getString(j))); } catch (Exception ignored) {}
            }
            if (!set.isEmpty()) researchFlags.put(key, set);
        }
        // knowledge
        CompoundNBT know = nbt.getCompound("knowledge");
        for (EnumKnowledgeType type : EnumKnowledgeType.values()) {
            CompoundNBT typeTag = know.getCompound(type.name());
            for (String k : typeTag.getAllKeys()) {
                knowledgePoints.get(type).put(k, typeTag.getInt(k));
            }
        }
    }
}


