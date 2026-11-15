# Architecture

## Current structure

- `app/src/main/java/com/bedrocklauncherx/MainActivity.kt`: Compose navigation and presentation. It observes world records and routes create/delete actions to the DAO.
- `data/World.kt`: Room entity representing launcher metadata only.
- `data/LauncherDatabase.kt`: Room database and data access interface.
- `app/src/test`: local JVM unit tests.

The UI does not start a server process. No service, Java runtime, Geyser/Floodgate distribution, Minecraft intent integration, or cross-app world file access is present in Phase 1.

## Target boundaries

As functionality grows, keep these interfaces separate:

1. **UI/presentation:** Compose screens and state holders; no process or filesystem work in composables.
2. **World management:** repository and user-mediated import/export, with safeguards against overwriting saves.
3. **Server management:** engine abstraction, lifecycle state machine, readiness probes, logs, players and configuration.
4. **Java runtime management:** ARM64 runtime provisioning, integrity/version metadata, license notices, and sandboxed process invocation.
5. **Crossplay:** Paper setup plus Geyser/Floodgate configuration and their own licenses/keys.
6. **Background service:** foreground service for an explicitly user-started server; notification actions; narrowly scoped wake lock only where justified.
7. **Minecraft integration:** package availability check and documented intents only; otherwise clear manual connection instructions.
8. **Persistence/diagnostics:** Room and app-private files, structured logs with secrets redacted.

Before implementing each boundary, add tests for state transitions, failure recovery, version compatibility, and data-loss protection.
