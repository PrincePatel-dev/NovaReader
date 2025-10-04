# NovaReader - PDF and PowerPoint Viewer

A simple, efficient, and privacy-focused Android app for viewing PDF and PowerPoint files offline.

![Platform](https://img.shields.io/badge/platform-Android-green)
![License](https://img.shields.io/badge/license-MIT-blue)
![Min SDK](https://img.shields.io/badge/min%20SDK-21-orange)
![Target SDK](https://img.shields.io/badge/target%20SDK-34-orange)

## Features

### 📄 PDF Support
- Native Android PdfRenderer for high-quality rendering
- Page-by-page navigation
- Fast and efficient rendering

### 📊 PowerPoint Support  
- View both PPT (legacy) and PPTX (modern) files
- Slide-by-slide navigation
- Text content extraction using Apache POI

### 🌓 Theme Support
- **Light Mode**: Comfortable reading during daytime
- **Dark Mode**: Easy on the eyes for night reading
- Automatic system theme integration
- Reduces eye strain

### 🔒 Privacy & Offline
- **100% Offline**: No internet connection needed
- **No Data Collection**: Your files stay on your device
- **No Background Services**: Battery efficient
- **Local Processing**: All file processing done on device

### ⚡ Performance
- Lightweight and fast
- Minimal resource usage
- No background processes
- Battery optimized

## System Requirements

- **Minimum**: Android 5.0 (API 21)
- **Target**: Android 14 (API 34)
- **Permissions**: Storage access for reading files

## Quick Start

### For Beginners: Build APK using Android Studio

This is the easiest way to build and install the app on your phone.

1. **Download Android Studio** from https://developer.android.com/studio
2. **Open this project** in Android Studio
3. **Build APK**: Click `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
4. **Find APK**: Click "locate" in the notification or find it at `app/build/outputs/apk/debug/app-debug.apk`
5. **Install on Phone**: Transfer the APK to your phone and install it

For detailed step-by-step instructions with screenshots and troubleshooting:
📖 **[Complete APK Build & Play Store Guide](BUILD_APK_GUIDE.md)**

### For Developers: Command Line Build

If you prefer command line:

```bash
# Make the build script executable
chmod +x build-apk.sh

# Run the build helper script
./build-apk.sh
```

Or build manually:

```bash
# Make sure you have JDK 17+ and Android SDK installed
chmod +x gradlew
./gradlew assembleDebug

# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

For detailed developer setup instructions:
📖 **[Building Documentation](BUILDING.md)**

## Installing on Your Phone

### Method 1: Direct File Transfer (Easiest)
1. Copy the APK file to your Android phone (via USB, email, or cloud storage)
2. Open the APK file on your phone
3. If prompted, enable "Install from Unknown Sources"
4. Tap "Install"

### Method 2: Using ADB (If you have USB cable)
```bash
# Enable USB Debugging on your phone first
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Publishing to Google Play Store

Want to publish your app to the Play Store? Follow our comprehensive guide:

📖 **[Complete Play Store Publishing Guide](BUILD_APK_GUIDE.md#part-3-publishing-to-google-play-store)**

The guide covers:
- Creating a Google Play Developer account
- Building a signed release version
- Creating store listings
- Uploading your app
- Getting approved
- Best practices

## Project Structure

```
NovaReader/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/pdfpptviewer/
│   │       │   ├── MainActivity.kt          # Main file picker
│   │       │   ├── PdfViewerActivity.kt     # PDF viewing
│   │       │   └── PptViewerActivity.kt     # PPT viewing
│   │       ├── res/                         # Resources & themes
│   │       └── AndroidManifest.xml          # App configuration
│   └── build.gradle                         # App dependencies
├── gradle/                                  # Gradle wrapper
├── build.gradle                             # Project configuration
├── settings.gradle                          # Gradle settings
├── BUILD_APK_GUIDE.md                       # Complete build & publish guide
├── BUILDING.md                              # Developer build instructions
├── build-apk.sh                             # Helper script
└── README.md                                # This file
```

## Technologies Used

- **Language**: Kotlin
- **Build System**: Gradle
- **PDF Rendering**: Android PdfRenderer (native)
- **PowerPoint Parsing**: Apache POI 5.2.3
- **UI Framework**: AndroidX, Material Design
- **Async Operations**: Kotlin Coroutines

## Documentation

- 📖 **[BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)** - Complete guide for beginners (building APK + publishing to Play Store)
- 📖 **[BUILDING.md](BUILDING.md)** - Technical build instructions for developers
- 📖 **[build-apk.sh](build-apk.sh)** - Automated build helper script

## Troubleshooting

### Build Issues

**Problem**: "SDK not found" error
- **Solution**: Set Android SDK path in Android Studio or create `local.properties` with your SDK path

**Problem**: "Out of memory" during build
- **Solution**: Add `org.gradle.jvmargs=-Xmx4096m` to `gradle.properties`

**Problem**: Dependencies fail to download
- **Solution**: Check your internet connection (needed for first build only)

**Problem**: Gradle wrapper issues
- **Solution**: Run `./gradlew --stop` then `./gradlew clean` then try again

### Installation Issues

**Problem**: "App not installed" on phone
- **Solution**: Enable "Install from Unknown Sources" in phone settings

**Problem**: Can't find the APK file
- **Solution**: Look in `app/build/outputs/apk/debug/app-debug.apk`

For more troubleshooting, see [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md) or [BUILDING.md](BUILDING.md).

## Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests
- Improve documentation

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

If you encounter any issues:
1. Check the documentation ([BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md) or [BUILDING.md](BUILDING.md))
2. Search for the error message online
3. Open an issue on GitHub
4. Check Android Developer documentation at https://developer.android.com/docs

## Screenshots

*Coming soon - screenshots of the app in action*

## Roadmap

Future features we're considering:
- [ ] Zoom and pan for PDF pages
- [ ] Search within documents
- [ ] Bookmarks and favorites
- [ ] Recent files list
- [ ] Support for more file formats
- [ ] Annotation support

## Acknowledgments

- Android PdfRenderer for PDF viewing capabilities
- Apache POI for PowerPoint file parsing
- Android community for libraries and tools

---

**Ready to build your APK?** Start with [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)! 🚀
