# NovaReader

A modern, feature-rich PDF and PowerPoint viewer for Android with beautiful UI and smooth performance.

![Version](https://img.shields.io/badge/version-1.1-blue)
![Android](https://img.shields.io/badge/android-5.0%2B-green)
![Material Design](https://img.shields.io/badge/material-design%203-purple)

## ✨ Features

### 📄 PDF Viewing
- High-quality rendering with Android PdfRenderer
- **Intelligent page caching** - instant navigation for visited pages
- Smooth page transitions
- Page counter with Previous/Next controls
- **3x resolution** for sharp text and images
- Full offline support

### 📊 PowerPoint Viewing
- Support for both PPT and PPTX formats
- Text content extraction from all slides
- Slide-by-slide navigation with counter
- Enhanced text spacing for better readability
- Works completely offline

### 🌓 Dark & Light Modes
- **Full theme support** across all screens
- Beautiful Material Design 3 color palette
- **Reduces eye strain** during night reading
- Automatic text color adaptation
- Toggle switch on main screen

### 🎨 Modern UI (v1.1)
- **Material Design 3** guidelines
- Card-based layouts with elevation
- Rounded corners and smooth animations
- MaterialButtons with icons
- Professional appearance

### ⚡ Performance Optimized
- Background thread rendering (no UI lag)
- Page caching system (last 5 pages)
- Async file operations
- Efficient memory management
- Loading indicators for all operations

## 📱 Screenshots

### Light Mode
*Dashboard showing modern card layout with PDF and PowerPoint buttons*

### Dark Mode
*Professional dark theme with easy-on-eyes color scheme*

### PDF Viewer
*High-quality PDF rendering with smooth navigation*

### PowerPoint Viewer
*Clean slide display with enhanced text spacing*

## 🚀 Installation

### For Users

1. Download the APK from [Releases](../../releases)
2. Enable "Unknown Sources" in your device settings
3. Install the APK
4. Open NovaReader and enjoy!

### For Developers

#### Prerequisites
- JDK 17 or higher
- Android SDK (API 21-34)
- Gradle (included)

#### Building from Source

```bash
# Clone the repository
git clone https://github.com/PrincePatel-dev/NovaReader.git
cd NovaReader

# Set Android SDK path
export ANDROID_SDK_ROOT="/path/to/android-sdk"

# Build release APK
gradle --no-daemon clean assembleRelease

# Find APK at:
# app/build/outputs/apk/release/app-release.apk
```

See [BUILDING.md](BUILDING.md) for detailed build instructions.

## 📖 Documentation

- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Complete overview of v1.1 improvements
- **[UI_IMPROVEMENTS.md](UI_IMPROVEMENTS.md)** - Technical details of UI changes
- **[VISUAL_CHANGES.md](VISUAL_CHANGES.md)** - Visual guide to new design
- **[CHANGELOG.md](CHANGELOG.md)** - Version history
- **[BUILDING.md](BUILDING.md)** - Build instructions

## 🎯 What's New in v1.1

### Major Improvements
- ✅ **Material Design 3 UI** - Complete visual redesign
- ✅ **Performance Boost** - PDF page caching eliminates lag
- ✅ **Full Dark Mode** - Works everywhere, not just dashboard
- ✅ **Better Stability** - Improved error handling
- ✅ **Loading Indicators** - Visual feedback for all operations

### Before vs After
| Feature | v1.0 | v1.1 |
|---------|------|------|
| UI Design | Basic | Material Design 3 |
| PDF Navigation | 200-500ms lag | Instant (cached) |
| Dark Mode | Dashboard only | Full app |
| Loading Feedback | None | Progress indicators |
| Button Style | Plain | MaterialButtons with icons |
| Layout | Simple | Card-based with elevation |

## 🛠️ Technical Details

### Built With
- **Language**: Kotlin
- **UI Framework**: Android SDK with Material Components
- **PDF Rendering**: Android PdfRenderer API
- **PowerPoint**: Apache POI 5.2.3
- **Async**: Kotlin Coroutines 1.7.3
- **Design**: Material Design 3

### Architecture
- **Min SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 14 (API 34)
- **App Size**: ~13 MB
- **Permissions**: Storage access for file reading

### Performance Specs
- **PDF Cache**: Last 5 pages in memory
- **Rendering**: 3x resolution (high quality)
- **Thread Model**: Async with Coroutines
- **Memory Management**: Automatic bitmap recycling

## 🎨 Color Scheme

### Light Mode
- **Primary**: #005AC1 (Deep Blue)
- **Background**: #FDFCFF (Crisp White)
- **Surface**: #F5F5F5 (Light Gray)

### Dark Mode
- **Primary**: #ADC6FF (Soft Blue)
- **Background**: #1A1C1E (Dark Gray)
- **Surface**: #2B2D30 (Medium Gray)

## 🤝 Contributing

Contributions are welcome! Here's how:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👤 Author

**Prince Patel**
- GitHub: [@PrincePatel-dev](https://github.com/PrincePatel-dev)

## 🙏 Acknowledgments

- Material Design 3 guidelines by Google
- Android PdfRenderer API
- Apache POI for PowerPoint support
- Kotlin Coroutines for async operations

## 📧 Support

If you have any questions or issues:
- Open an [Issue](../../issues)
- Check the [Documentation](IMPLEMENTATION_SUMMARY.md)
- Review the [FAQ](#faq) below

## ❓ FAQ

### Q: Why doesn't PPT preserve formatting?
**A**: Apache POI extracts text content only. For visual formatting, we would need image-based rendering which would significantly increase app size and loading time. The current approach prioritizes speed and simplicity.

### Q: Why is PDF navigation sometimes slow on first view?
**A**: We render at 3x resolution for sharp quality. First-time rendering takes a moment, but cached pages load instantly. This is a deliberate trade-off for better quality.

### Q: Does the app work offline?
**A**: Yes! NovaReader works completely offline. All processing is done locally on your device.

### Q: What file formats are supported?
**A**: 
- PDF: All standard PDFs
- PowerPoint: PPT and PPTX

### Q: How much storage does the app use?
**A**: 
- App size: ~13 MB
- Runtime cache: ~20-50 MB (for page caching)
- Total: Less than 100 MB

## 🔮 Future Plans

- [ ] Pinch-to-zoom for PDF pages
- [ ] Search functionality
- [ ] Bookmarks and favorites
- [ ] Recent files list
- [ ] Page thumbnails
- [ ] Image-based PPT rendering (optional)
- [ ] Export to PDF from PPT
- [ ] Annotation support

## 📊 Stats

- **Lines of Code**: ~1,500 (Kotlin + XML)
- **Activities**: 3 (Main, PDF Viewer, PPT Viewer)
- **Layouts**: 3 (with Material Design 3)
- **Dependencies**: 8 libraries
- **Documentation**: ~26 KB

---

**Made with ❤️ for better document reading experience**

**Star ⭐ this repo if you find it useful!**
