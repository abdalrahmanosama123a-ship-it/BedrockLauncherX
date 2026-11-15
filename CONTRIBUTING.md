# Contributing

Thanks for helping improve BedrockLauncherX. Please open an issue before a large change and keep pull requests focused.

## Development

1. Install Android Studio with JDK 17 and Android SDK Platform 35.
2. Clone the repository and open it in Android Studio, or run `./gradlew test lint assembleDebug`.
3. Add tests for behavior changes and update relevant documentation.
4. Never commit signing keys, passwords, user world data, or private credentials.

## Scope and safety

Do not bundle Minecraft, proprietary game assets, or unlicensed server binaries. Use documented Android and upstream project interfaces. Be explicit about unsupported features and never show fabricated server/player/performance data.
