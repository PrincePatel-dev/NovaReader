#!/bin/bash

# APK Build Helper Script
# This script helps you build the Android APK for NovaReader

set -e

clear

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         NovaReader - APK Build Helper                         ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "This script will help you build an APK for your Android phone."
echo ""

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo "✅ $2"
    else
        echo "❌ $2"
    fi
}

# Check Java
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "STEP 1: Checking Prerequisites"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

JAVA_OK=0
if command_exists java; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        print_status 0 "Java $JAVA_VERSION installed"
        JAVA_OK=1
    else
        print_status 1 "Java version too old (need 17+, found $JAVA_VERSION)"
    fi
else
    print_status 1 "Java not found"
fi

# Check Android SDK
SDK_OK=0
if [ -n "$ANDROID_SDK_ROOT" ] && [ -d "$ANDROID_SDK_ROOT" ]; then
    print_status 0 "Android SDK found at: $ANDROID_SDK_ROOT"
    SDK_OK=1
elif [ -n "$ANDROID_HOME" ] && [ -d "$ANDROID_HOME" ]; then
    print_status 0 "Android SDK found at: $ANDROID_HOME"
    export ANDROID_SDK_ROOT="$ANDROID_HOME"
    SDK_OK=1
else
    print_status 1 "Android SDK not found"
fi

# Check if local.properties exists
if [ -f "local.properties" ]; then
    print_status 0 "local.properties configured"
elif [ $SDK_OK -eq 1 ]; then
    echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties
    print_status 0 "Created local.properties"
fi

echo ""

# If prerequisites not met, show instructions
if [ $JAVA_OK -eq 0 ] || [ $SDK_OK -eq 0 ]; then
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "❌ Prerequisites Not Met"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "You have two options to build your APK:"
    echo ""
    echo "Option 1: Use Android Studio (EASIEST - RECOMMENDED)"
    echo "  1. Download from: https://developer.android.com/studio"
    echo "  2. Install Android Studio (it includes everything you need)"
    echo "  3. Open this project in Android Studio"
    echo "  4. Click Build → Build Bundle(s) / APK(s) → Build APK(s)"
    echo "  5. Find your APK in app/build/outputs/apk/debug/"
    echo ""
    echo "Option 2: Install Android SDK manually"
    echo "  Run the setup script:"
    echo "    chmod +x scripts/setup-android-sdk.sh"
    echo "    ./scripts/setup-android-sdk.sh"
    echo ""
    echo "For complete step-by-step instructions, see:"
    echo "  📄 BUILD_APK_GUIDE.md"
    echo "  📄 BUILDING.md"
    echo ""
    exit 1
fi

# Check Gradle wrapper
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "STEP 2: Checking Build System"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

if [ ! -f "gradlew" ]; then
    print_status 1 "gradlew not found"
    exit 1
fi

chmod +x gradlew
print_status 0 "Gradle wrapper ready"

# Check if wrapper jar exists
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    print_status 1 "gradle-wrapper.jar missing"
    echo ""
    echo "Fixing Gradle wrapper..."
    if command_exists gradle; then
        gradle wrapper --gradle-version 8.2
        print_status 0 "Gradle wrapper fixed"
    else
        echo "❌ Cannot fix automatically. Please install Gradle or use Android Studio."
        exit 1
    fi
else
    print_status 0 "Gradle wrapper jar present"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "STEP 3: Building Debug APK"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "This may take a few minutes on first build (downloading dependencies)..."
echo "Please be patient..."
echo ""

# Build the APK
if ./gradlew assembleDebug; then
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "🎉 SUCCESS! APK Built Successfully!"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Your APK is located at:"
    echo "  📱 app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "File size: $(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Next Steps:"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "To install on your Android phone:"
    echo ""
    echo "Option 1: Transfer the APK file"
    echo "  1. Copy app/build/outputs/apk/debug/app-debug.apk to your phone"
    echo "  2. Open the file on your phone"
    echo "  3. Allow 'Install from Unknown Sources' if prompted"
    echo "  4. Tap Install"
    echo ""
    echo "Option 2: Install via USB (if phone is connected)"
    echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "To publish to Google Play Store:"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "⚠️  Note: This is a DEBUG build. For Play Store, you need a RELEASE build."
    echo ""
    echo "Follow the complete guide in:"
    echo "  📄 BUILD_APK_GUIDE.md"
    echo ""
    echo "Quick summary:"
    echo "  1. Open project in Android Studio"
    echo "  2. Build → Generate Signed Bundle/APK"
    echo "  3. Create a keystore (SAVE IT SAFELY!)"
    echo "  4. Build signed release bundle (.aab)"
    echo "  5. Upload to Play Console"
    echo ""
else
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "❌ Build Failed"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Common solutions:"
    echo "  1. Check your internet connection (needed to download dependencies)"
    echo "  2. Run: ./gradlew clean"
    echo "  3. Try again: ./gradlew assembleDebug"
    echo ""
    echo "If problems persist:"
    echo "  - Use Android Studio (it handles everything automatically)"
    echo "  - See BUILD_APK_GUIDE.md for detailed instructions"
    echo "  - See BUILDING.md for troubleshooting"
    echo ""
    exit 1
fi
