# Overview

This is a native Android application for viewing PDF and PowerPoint files offline. The app features dark and light mode support, works completely offline without internet access, and is optimized for battery efficiency with no background processes.

## Recent Changes

**October 3, 2025**: Complete Android app implementation with PDF and PPT/PPTX viewing capabilities
- Created full Android project structure with Gradle build system
- Implemented MainActivity with file picker supporting both PPT and PPTX files
- Built PdfViewerActivity using Android's native PdfRenderer API
- Built PptViewerActivity using Apache POI library for both legacy PPT and modern PPTX formats
- Implemented dark mode and light mode themes that work across all activities
- Configured proper MIME type detection for reliable file format identification
- Added build documentation and SDK setup scripts for local compilation

# User Preferences

Preferred communication style: Simple, everyday language.

# System Architecture

## Application Type
- **Platform**: Native Android application
- **Build System**: Gradle-based Android build system
- **Minimum Requirements**: JDK 17 or higher

## Build Architecture
The application supports two build approaches:

1. **Android Studio Build** (Recommended)
   - Full IDE integration with automatic SDK management
   - Visual build tools and debugging capabilities
   - Output: APK files in `app/build/outputs/apk/debug/`

2. **Command-Line Build**
   - Uses Android SDK Command Line Tools
   - Requires manual SDK setup and environment configuration
   - Suitable for CI/CD pipelines and headless environments

## Development Environment
- **JDK Version**: 17 or higher required for compilation
- **SDK Management**: Handled via Android SDK Manager (sdkmanager)
- **Build Output**: Standard Android APK format

## Key Architectural Decisions

### Why Native Android?
- Direct access to Android system APIs for file handling
- Better performance for PDF and PPT rendering
- Native UI/UX consistency with Android design patterns

### Build System Choice
- Gradle provides standardized Android build configuration
- Supports both GUI and CLI workflows
- Enables easy dependency management and build variants

# External Dependencies

## Core Development Tools
- **JDK**: Version 17+ (Oracle or OpenJDK)
- **Android SDK**: Required for building and packaging
  - Command Line Tools version 9477386 or later
  - Platform-specific SDK packages (managed via sdkmanager)

## Build Tools
- **Gradle**: Android build automation (included with project)
- **Android Build Tools**: For compiling and packaging APKs
- **Platform Tools**: For device interaction and debugging

## SDK Components
- Command Line Tools: Core SDK management utilities
- Platform Tools: ADB and other device communication tools
- Build Tools: Compilation and packaging utilities
- SDK Platforms: Android API level libraries (API 21-34)

## Runtime Dependencies
- **AndroidX Libraries**: Core, AppCompat, ConstraintLayout, Material Design
- **Kotlin Coroutines**: For asynchronous file loading (1.7.3)
- **Apache POI**: For PowerPoint file parsing (5.2.3)
  - poi: Core library for legacy PPT support
  - poi-ooxml: For modern PPTX support
  - poi-scratchpad: Additional PPT format support
  - xmlbeans: XML processing for OOXML formats

# Features

## PDF Viewing
- Native Android PdfRenderer for high-quality rendering
- Page-by-page navigation with previous/next controls
- Page counter showing current page and total pages
- Full offline support with no network requirements

## PowerPoint Viewing
- Support for both legacy PPT and modern PPTX formats
- Text extraction and display from all slides
- Slide-by-slide navigation
- Slide counter showing current slide and total slides
- Robust format detection using MIME types with fallback

## Theme Support
- Light mode for comfortable daytime reading
- Dark mode to reduce eye strain during night use
- Theme-aware color schemes for all UI elements
- Consistent theming across PDF and PowerPoint viewers
- Manual toggle switch in main menu

## System Integration
- Native Android file picker with proper MIME type filtering
- Storage permission handling for Android 12 and below
- No permissions required on Android 13+ (uses system picker)
- Completely offline operation
- Battery optimized with no background services