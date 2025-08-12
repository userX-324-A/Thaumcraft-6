package thaumcraft.common.research;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.common.lib.research.ResearchManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Datapack-backed research loader. Scans data namespaces under research folder (e.g., data/<ns>/research/*.json) and loads entries.
 */
public class ResearchReloadListener implements IFutureReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setLenient().create();

    @Override
    public CompletableFuture<Void> reload(IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            // Read and parse on background thread
            Map<ResourceLocation, JsonObject> prepared = new java.util.HashMap<>();
            for (ResourceLocation rl : resourceManager.listResources("research", (path) -> path.endsWith(".json"))) {
                try (net.minecraft.resources.IResource res = resourceManager.getResource(rl);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
                    JsonElement rootEl = JSONUtils.fromJson(GSON, reader, JsonElement.class);
                    if (rootEl != null && rootEl.isJsonObject()) {
                        prepared.put(rl, rootEl.getAsJsonObject());
                    } else {
                        Thaumcraft.LOGGER.warn("Research file {} root is not an object", rl);
                    }
                } catch (Exception e) {
                    Thaumcraft.LOGGER.warn("Failed reading research file {}: {}", rl, e.getMessage());
                }
            }
            return prepared;
        }, backgroundExecutor).thenComposeAsync(prepared -> {
            // Hand off to game thread to mutate global state
            return CompletableFuture.runAsync(() -> apply(prepared, resourceManager), gameExecutor);
        }, gameExecutor);
    }

    private void apply(Map<ResourceLocation, JsonObject> prepared, IResourceManager resourceManager) {
        // Clear existing entries for a clean rebuild, but keep categories (bounds will recalc)
        ResearchCategories.researchCategories.values().forEach(ResearchCategory::clear);

        int totalFiles = 0;
        int totalEntries = 0;
        for (Map.Entry<ResourceLocation, JsonObject> entry : prepared.entrySet()) {
            ResourceLocation rl = entry.getKey();
            JsonObject root = entry.getValue();
            if (!root.has("entries") || !root.get("entries").isJsonArray()) {
                Thaumcraft.LOGGER.warn("Research file {} missing 'entries' array", rl);
                continue;
            }
            int loadedHere = 0;
            for (JsonElement el : root.getAsJsonArray("entries")) {
                if (!el.isJsonObject()) continue;
                try {
                    ResearchEntry re = ResearchManager.parseResearchJson(el.getAsJsonObject());
                    // Validation: category exists
                    if (re.getCategory() == null || ResearchCategories.getResearchCategory(re.getCategory()) == null) {
                        Thaumcraft.LOGGER.warn("Research {} has invalid/missing category '{}' in {}", re.getKey(), re.getCategory(), rl);
                        continue;
                    }
                    // Validate references: parents, siblings, stage.required_research keys point somewhere
                    validateReferences(re, rl);
                    ResearchManager.addResearchToCategoryPublic(re);
                    loadedHere++;
                    totalEntries++;
                } catch (Exception ex) {
                    Thaumcraft.LOGGER.warn("Invalid research entry in {}: {}", rl, ex.getMessage());
                }
            }
            totalFiles++;
            Thaumcraft.LOGGER.info("Loaded {} research entries from {}", loadedHere, rl);
        }
        Thaumcraft.LOGGER.info("Research reload complete: {} files, {} entries", totalFiles, totalEntries);

        // After applying, run a best-effort localization check for en_us
        try {
            validateLocalization(resourceManager);
        } catch (Exception e) {
            Thaumcraft.LOGGER.debug("Localization validation skipped: {}", e.getMessage());
        }
    }

    private static void validateReferences(ResearchEntry re, ResourceLocation sourceFile) {
        // Parents and siblings exist (if present). Overlaps are logged inside addResearchToCategory.
        if (re.getParents() != null) {
            for (String p : re.getParents()) {
                String key = p == null ? null : (p.startsWith("~") ? p.substring(1) : p);
                if (key != null && key.contains("@")) key = key.substring(0, key.indexOf('@'));
                if (key != null && thaumcraft.api.research.ResearchCategories.getResearch(key) == null) {
                    Thaumcraft.LOGGER.warn("Research {} references missing parent '{}' in {}", re.getKey(), key, sourceFile);
                }
            }
        }
        if (re.getSiblings() != null) {
            for (String s : re.getSiblings()) {
                String key = s;
                if (key != null && key.contains("@")) key = key.substring(0, key.indexOf('@'));
                if (key != null && thaumcraft.api.research.ResearchCategories.getResearch(key) == null) {
                    Thaumcraft.LOGGER.warn("Research {} references missing sibling '{}' in {}", re.getKey(), key, sourceFile);
                }
            }
        }
        if (re.getStages() != null) {
            for (thaumcraft.api.research.ResearchStage stage : re.getStages()) {
                String[] req = stage.getResearch();
                if (req == null) continue;
                for (String r : req) {
                    String key = r;
                    if (key != null && key.contains(";")) key = key.substring(0, key.indexOf(';'));
                    if (key != null && thaumcraft.api.research.ResearchCategories.getResearch(key) == null) {
                        Thaumcraft.LOGGER.warn("Research {} stage requires missing research '{}' in {}", re.getKey(), key, sourceFile);
                    }
                }
            }
        }
    }

    private static void validateLocalization(IResourceManager resourceManager) {
        // Load language keys from en_us.json or en_us.lang if present
        java.util.Set<String> langKeys = new java.util.HashSet<>();
        try {
            net.minecraft.resources.IResource jsonRes = resourceManager.getResource(new ResourceLocation(Thaumcraft.MODID, "lang/en_us.json"));
            try (java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(jsonRes.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                com.google.gson.JsonObject obj = JSONUtils.fromJson(GSON, r, com.google.gson.JsonObject.class);
                if (obj != null) {
                    for (java.util.Map.Entry<String, com.google.gson.JsonElement> e : obj.entrySet()) {
                        langKeys.add(e.getKey());
                    }
                }
            }
        } catch (Exception ignored) {}
        if (langKeys.isEmpty()) {
            try {
                net.minecraft.resources.IResource langRes = resourceManager.getResource(new ResourceLocation(Thaumcraft.MODID, "lang/en_us.lang"));
                try (java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(langRes.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = r.readLine()) != null) {
                        int eq = line.indexOf('=');
                        if (eq > 0) {
                            String key = line.substring(0, eq).trim();
                            if (!key.isEmpty() && !key.startsWith("#")) langKeys.add(key);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        if (langKeys.isEmpty()) {
            Thaumcraft.LOGGER.warn("No en_us language file found for localization validation");
            return;
        }

        // Categories
        for (String catKey : thaumcraft.api.research.ResearchCategories.researchCategories.keySet()) {
            String key = "tc.research_category." + catKey;
            if (!langKeys.contains(key)) {
                Thaumcraft.LOGGER.warn("Missing lang key: {}", key);
            }
        }
        // Entries and stage/addendum text
        for (thaumcraft.api.research.ResearchCategory cat : thaumcraft.api.research.ResearchCategories.researchCategories.values()) {
            for (ResearchEntry re : cat.research.values()) {
                String nameKey = re.getName();
                if (nameKey != null && !nameKey.isEmpty() && !langKeys.contains(nameKey)) {
                    Thaumcraft.LOGGER.warn("Missing lang key for research name ({}): {}", re.getKey(), nameKey);
                }
                if (re.getStages() != null) {
                    for (thaumcraft.api.research.ResearchStage st : re.getStages()) {
                        String textKey = st.getText();
                        if (textKey != null && !textKey.isEmpty() && !langKeys.contains(textKey)) {
                            Thaumcraft.LOGGER.warn("Missing lang key for research stage ({}): {}", re.getKey(), textKey);
                        }
                    }
                }
                if (re.getAddenda() != null) {
                    for (thaumcraft.api.research.ResearchAddendum ad : re.getAddenda()) {
                        String textKey = ad.getText();
                        if (textKey != null && !textKey.isEmpty() && !langKeys.contains(textKey)) {
                            Thaumcraft.LOGGER.warn("Missing lang key for research addendum ({}): {}", re.getKey(), textKey);
                        }
                    }
                }
            }
        }
    }
}


