# ResortGuestApp

ResortGuestApp is a Kotlin Multiplatform app with a shared Compose UI and native launchers for Android and iOS.

## Project structure

- `androidApp/` Android entry point and app packaging.
- `iosApp/` Xcode project and SwiftUI entry point.
- `shared/` Shared Kotlin code and Compose UI used by both platforms.

## Requirements

- JDK 11+
- Android Studio (latest stable)
- Xcode 15+ (for iOS builds)

## Run the app

### Android

Build debug APK:

`./gradlew :androidApp:assembleDebug`

On Windows PowerShell:

`./gradlew.bat :androidApp:assembleDebug`

### iOS

Open `iosApp/` in Xcode, select a simulator/device, then Run.

## Run tests

- Shared Android host tests: `./gradlew :shared:testAndroidHostTest`
- Shared iOS simulator tests (macOS only): `./gradlew :shared:iosSimulatorArm64Test`

## Notes

- The shared UI starts in `shared/src/commonMain/kotlin/org/resortguestapp/project/App.kt`.
- Android launches shared UI from `androidApp/src/main/kotlin/org/resortguestapp/project/MainActivity.kt`.
- iOS launches shared UI from `iosApp/iosApp/ContentView.swift` via `MainViewController()`.
