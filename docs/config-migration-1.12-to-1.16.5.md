Thaumcraft 1.12 → 1.16.5: Config migration quick notes

- Worldgen flags are now grouped under `worldgen` in the common config file (`config/thaumcraft-common.toml`).
  - New master toggles: `enableCrystals`, `enableAuraSeed`, `enableTrees`, `enablePlants`, `enableOres`.
  - Structures/biomes are gated and off by default while the port is in progress:
    - `enableStructures`, `enableObelisks`, `enableMounds`, `enableDungeons`.
    - `enableMagicalBiomes`, `enableMagicalForest`, `enableEerie`, `enableOuterLands`.

- Trees now support either classic 1-in-N chunk chance or per-chunk counts:
  - `greatwoodChanceChunks` (default 25) and `silverwoodChanceChunks` (default 80).
  - If a chance value > 0, it is used; otherwise `greatwoodPerChunk` / `silverwoodPerChunk` are used.
  - Silverwood prefers the `thaumcraft:magical_forest` biome (when enabled) and is slightly rarer in other forests to better match 1.12.

- Crystal clusters use attempt/cap settings rather than raw per-chunk counts:
  - `crystals.attemptsPerChunk`, `crystals.maxPerChunk`, and `crystals.netherAttemptsDivisor`.

- Ores use standard 1.16 settings (`veinSize`, `veinsPerChunk`, `maxY`). Values approximate 1.12 defaults.

- Client/HUD and item tuning moved to dedicated sections (see `client.hud`, `items.*`).

Notes
- Existing 1.12 configs are not auto-migrated. Review the TOML entries and adjust to your server preferences.
- Structures and magical biomes are registered as stubs and will be fleshed out in later builds. Keep their toggles disabled unless testing.


