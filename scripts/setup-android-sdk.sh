#!/bin/bash

# Android SDK Setup Script for Linux
# This script downloads and configures Android SDK command line tools

set -e

echo "========================================="
echo "Android SDK Setup Script"
echo "========================================="

# Configuration
SDK_DIR="${ANDROID_SDK_ROOT:-$HOME/android-sdk}"
CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip"
PLATFORM_VERSION="34"
BUILD_TOOLS_VERSION="34.0.0"

echo "Installing Android SDK to: $SDK_DIR"

# Create directories
mkdir -p "$SDK_DIR/cmdline-tools"
cd "$SDK_DIR/cmdline-tools"

# Download command line tools
if [ ! -f "cmdline-tools.zip" ]; then
    echo "Downloading Android SDK command line tools..."
    wget "$CMDLINE_TOOLS_URL" -O cmdline-tools.zip
else
    echo "Command line tools already downloaded"
fi

# Extract if not already extracted
if [ ! -d "latest" ]; then
    echo "Extracting command line tools..."
    unzip -q cmdline-tools.zip
    mkdir -p latest
    mv cmdline/bin cmdline/lib cmdline/NOTICE.txt cmdline/source.properties latest/ 2>/dev/null || true
    rm -rf cmdline cmdline-tools.zip
fi

# Set up environment variables
echo ""
echo "Setting up environment variables..."
export ANDROID_SDK_ROOT="$SDK_DIR"
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH"
export PATH="$ANDROID_SDK_ROOT/platform-tools:$PATH"

# Add to shell profile if not already present
SHELL_RC="${HOME}/.bashrc"
if [ -f "$HOME/.zshrc" ]; then
    SHELL_RC="${HOME}/.zshrc"
fi

if ! grep -q "ANDROID_SDK_ROOT" "$SHELL_RC"; then
    echo "" >> "$SHELL_RC"
    echo "# Android SDK" >> "$SHELL_RC"
    echo "export ANDROID_SDK_ROOT=\"$SDK_DIR\"" >> "$SHELL_RC"
    echo "export PATH=\"\$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:\$PATH\"" >> "$SHELL_RC"
    echo "export PATH=\"\$ANDROID_SDK_ROOT/platform-tools:\$PATH\"" >> "$SHELL_RC"
    echo "Environment variables added to $SHELL_RC"
fi

# Install SDK packages
echo ""
echo "Installing SDK packages..."
yes | sdkmanager --update
yes | sdkmanager "platform-tools"
yes | sdkmanager "platforms;android-${PLATFORM_VERSION}"
yes | sdkmanager "build-tools;${BUILD_TOOLS_VERSION}"

# Accept licenses
echo ""
echo "Accepting SDK licenses..."
yes | sdkmanager --licenses

echo ""
echo "========================================="
echo "Android SDK Setup Complete!"
echo "========================================="
echo ""
echo "SDK Location: $SDK_DIR"
echo "Platform: Android $PLATFORM_VERSION"
echo "Build Tools: $BUILD_TOOLS_VERSION"
echo ""
echo "To use the SDK in new terminal sessions, run:"
echo "  source $SHELL_RC"
echo ""
echo "To build the app, navigate to the project directory and run:"
echo "  ./gradlew assembleDebug"
echo ""
