# BedrockLauncherX

A native Android companion launcher for Minecraft Bedrock Edition. The official Minecraft app remains separately installed; this project does not include Minecraft code or assets.

> **Status:** Phase 1 foundation. The current app provides a bilingual Compose shell and persistent local world metadata management. It does not yet launch worlds inside Minecraft or host a server. Server status and performance values are not fabricated.

## Current functionality

- Native Kotlin and Jetpack Compose Android app, minimum Android 8.0 (API 26), targeting SDK 35.
- Arabic/English interface switch with right-to-left layout for Arabic.
- Room-backed world list with create/delete and stored seed, game mode, and difficulty.
- Dark theme and initial launcher navigation.
- GitHub Actions workflow for tests, lint, debug APK and 14-day artifact retention.

World entries are launcher metadata only in this phase; they are not imported into or created by Minecraft. Android limits cross-app access to private application data. Minecraft also supports Application and External world storage modes, so direct management of the game's private worlds cannot be assumed.

## Build

Requirements: Android Studio or Android SDK Platform 35, JDK 17, and network access for Gradle dependencies.

```bash
./gradlew test
./gradlew lint
./gradlew assembleDebug
```

Install the debug APK at `app/build/outputs/apk/debug/app-debug.apk` on an ARM64 Android device. Debug builds are not release-signed. See [docs/BUILDING.md](docs/BUILDING.md).

## Architecture

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md), [docs/PROJECT_OVERVIEW.md](docs/PROJECT_OVERVIEW.md), and [docs/ROADMAP.md](docs/ROADMAP.md).

## Crossplay and hosting

The planned Java server stack is Paper with Geyser and Floodgate, subject to Android runtime, memory, thermals, network, and upstream compatibility testing. Paper's Java requirement depends on the selected Minecraft version. Running an arbitrary server JAR inside Android is not yet implemented. Geyser translates Bedrock clients to a Java server; it does not make Java client mods compatible with Bedrock.

## Trademarks and licensing

Minecraft and related marks belong to their respective owners. BedrockLauncherX is an independent community project and is not affiliated with or endorsed by Mojang or Microsoft. This repository's MIT license applies only to its own original code, not to third-party dependencies or Minecraft.
