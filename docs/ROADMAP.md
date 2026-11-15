# Roadmap

- [x] Phase 1 foundation: Android project, Compose navigation, Arabic/English RTL switch, Room-backed world metadata create/edit/delete, seed, game mode, difficulty, basic tests and CI definition.
- [x] Mod discovery: public Modrinth search, all listed Minecraft versions, loader filters for Fabric/Forge/NeoForge/Quilt, and project-page links.
- [ ] Licensed Java Edition launcher: official Microsoft/Xbox/Minecraft account flow, Mojang version manifest, Java runtime provisioning, libraries/assets, and tested game launch. Requires a registered OAuth client and device-level compatibility validation.
- [ ] Mod installation: resolve Modrinth project versions/dependencies, verify downloaded hashes, install into an isolated per-version profile, and handle loader/runtime compatibility.
- [ ] Phase 1 completion: thumbnails, richer seed validation, backup/restore and user-mediated world-file import/export; device/storage compatibility tests.
- [ ] Phase 2: evaluate and implement an ARM64 Java runtime and server installation/process lifecycle only after a compatibility and license review; server logs, real status and readiness checks.
- [ ] Phase 3: foreground service, persistent notification actions, graceful shutdown/recovery and battery guidance; validate on multiple Android versions and vendors.
- [ ] Phase 4: Paper + Geyser + Floodgate optional stack, version matrix, secure key handling and resource limits.
- [ ] Phase 5: test official Minecraft launch, LAN discovery and any documented direct-join pathway. If direct join is unsupported, provide a tested manual saved-server fallback.
- [ ] Phase 6: assess Fabric/NeoForge server compatibility; do not claim Java client mods run on Bedrock.
- [ ] Phase 7: performance, accessibility, end-to-end tests, release signing instructions and publishing readiness.

A roadmap item is not a delivered feature. Release notes must distinguish tested behavior from planned work.
