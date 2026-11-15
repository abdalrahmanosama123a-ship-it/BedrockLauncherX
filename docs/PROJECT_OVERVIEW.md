# Project Overview

BedrockLauncherX is an Android companion app intended to organize user-created worlds and, in later phases, manage a local multiplayer server while the official Minecraft Bedrock app is used separately.

The current Mods page now searches Modrinth's public catalog for Java Edition mods, offers Minecraft game-version filtering, and opens each project on Modrinth. This catalog browser does not install game files or make Java mods compatible with Bedrock.

## Phase 1 delivered in this repository

The native application currently stores world metadata (name, optional seed, game mode, difficulty) in a Room database and exposes a bilingual Arabic/English interface with RTL layout. Users can create, edit and delete these metadata records. They are not Minecraft world saves. Server controls, crossplay and one-tap play are intentionally not represented as working features.

## Feasibility notes

- Android's scoped-storage model restricts an app from accessing another app's private data directories. Minecraft's documented External storage option can expose files to the user, but Android's restrictions around `Android/data` mean broad direct access is not a safe assumption. An explicit user-mediated document import/export flow may be viable for eligible locations and OS versions; test it on real devices.
- A foreground service is the platform mechanism for user-noticeable ongoing work, with a persistent notification and applicable service-type/permission declarations. It cannot promise survival after force-stop or every device vendor's power-management policy.
- Paper is a Java server, and the required Java version follows the Minecraft/Paper version. Android is not an officially supported Paper server host platform. A compatible ARM64 Java runtime, process permissions, memory headroom, thermal behavior, and UDP networking all require device-level validation before promising local hosting.
- Geyser is a protocol translator connecting Bedrock clients to a Java server. Floodgate supports Bedrock authentication integrations. Correct configuration, ports, secure handling of keys, and version compatibility must be verified from upstream documentation before bundling or downloading components.
- Android's documented generic intents launch activities that advertise matching intent filters. No official Minecraft Android direct-join API is assumed here. LAN discovery and saved server/manual connection fallback require testing against current Minecraft releases.

## References

- Android foreground services: https://developer.android.com/develop/background-work/services/foreground-services
- Android storage updates: https://developer.android.com/about/versions/11/privacy/storage
- Minecraft Android storage options: https://help.minecraft.net/hc/en-us/articles/4414144725389-How-Minecraft-Stores-World-Files-on-Your-Android-Device
- Paper setup: https://docs.papermc.io/paper/getting-started/
- Geyser setup: https://geysermc.org/wiki/geyser/setup/
- Android common intents: https://developer.android.com/guide/components/intents-common
