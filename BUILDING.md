# Building the PDF PPT Viewer Android App

This is a native Android application that must be built locally or in a CI environment with Android SDK support.

## Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
   ```bash
   java -version  # Should show version 17+
   ```

2. **Android SDK Command Line Tools**
   - Download from: https://developer.android.com/studio#command-line-tools-only
   - Or install Android Studio which includes all necessary tools

## Local Build Setup

### Option 1: Using Android Studio (Recommended for beginners)

1. Install Android Studio from https://developer.android.com/studio
2. Open this project in Android Studio
3. Let Studio download SDK components automatically
4. Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
5. Find the APK in `app/build/outputs/apk/debug/`

### Option 2: Command Line Build

1. **Install Android SDK Command Line Tools**
   ```bash
   # Download command line tools
   wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
   
   # Extract and set up
   mkdir -p ~/android-sdk/cmdline-tools
   cd ~/android-sdk/cmdline-tools
   unzip commandlinetools-linux-*.zip
   mkdir latest
   mv bin lib NOTICE.txt source.properties latest/
   ```

2. **Set Environment Variables**
   ```bash
   export ANDROID_SDK_ROOT="$HOME/android-sdk"
   export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH"
   export PATH="$ANDROID_SDK_ROOT/platform-tools:$PATH"
   ```

3. **Install Required SDK Packages**
   ```bash
   sdkmanager --update
   sdkmanager "platform-tools"
   sdkmanager "platforms;android-34"
   sdkmanager "build-tools;34.0.0"
   sdkmanager --licenses  # Accept all licenses
   ```

4. **Build the APK**
   ```bash
   # Navigate to project directory
   cd /path/to/PDFPPTViewer
   
   # Make gradlew executable
   chmod +x gradlew
   
   # Build debug APK
   ./gradlew assembleDebug
   
   # Build release APK (unsigned)
   ./gradlew assembleRelease
   ```

5. **Locate the APK**
   - Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
   - Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Installing on Android Device

### Via USB (ADB)
```bash
# Enable USB debugging on your Android device first
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Via File Transfer
1. Copy the APK file to your Android device
2. Open the file on your device
3. Allow installation from unknown sources if prompted
4. Tap "Install"

## Troubleshooting

### Out of Memory Errors
If you encounter memory errors during build, add to `gradle.properties`:
```
org.gradle.jvmargs=-Xmx4096m
```

### License Not Accepted
Run: `sdkmanager --licenses` and accept all

### Gradle Daemon Issues
```bash
./gradlew --stop
./gradlew clean
./gradlew assembleDebug
```

## Project Features

- ✅ PDF viewing with native Android PdfRenderer
- ✅ PPT/PPTX viewing with Apache POI
- ✅ Dark and light mode support
- ✅ Completely offline functionality
- ✅ No background processes
- ✅ Battery optimized

## System Requirements

- **Minimum Android Version**: Android 5.0 (API 21)
- **Target Android Version**: Android 14 (API 34)
- **Permissions**: Storage access for reading PDF/PPT files
