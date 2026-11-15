# Building

## Requirements

- JDK 17.
- Android SDK Platform 35 and Android Build Tools installed (Android Studio is the easiest setup).
- Network access for Gradle and Maven dependencies.

Set `ANDROID_HOME` or `ANDROID_SDK_ROOT` to the SDK location. Do not commit `local.properties`.

## Commands

From the repository root:

```bash
./gradlew test
./gradlew lint
./gradlew assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`. Install it with Android Studio or `adb install -r app/build/outputs/apk/debug/app-debug.apk`. The debug APK is not a production release and is not signed with a project release key.

## Continuous integration

`.github/workflows/android-build.yml` runs on pushes to `main`, pull requests into `main`, and manual dispatch. It validates the wrapper, uses JDK 17, runs tests and lint, builds the debug APK, then uploads `BedrockLauncherX-debug-apk` for 14 days. GitHub Actions must report success before claiming a successful CI build or providing an artifact link.

## Future release signing

Use Android Gradle signing configuration with credentials supplied only via protected GitHub Actions secrets or a local ignored properties file. Never commit signing material. No signed release APK is produced by the current workflow.
