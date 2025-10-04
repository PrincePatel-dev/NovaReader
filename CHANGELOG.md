# NovaReader Changelog

## Version 1.1 (Current Development)

### 🎨 Major UI Redesign
- **Material Design 3**: Completely redesigned UI using Material Design 3 guidelines
- **Modern Color Palette**: Beautiful blue/gray color scheme that works in both light and dark modes
- **Card-Based Layout**: All screens now use elevated card components for better visual hierarchy
- **Enhanced Buttons**: MaterialButtons with icons and rounded corners for a polished look

### ⚡ Performance Improvements
- **PDF Caching**: Implemented intelligent page caching system
  - Last 5 pages kept in memory for instant navigation
  - Background rendering prevents UI lag
  - 3x higher resolution for sharper text and images
- **Async Operations**: All file operations now run in background threads
- **Loading Indicators**: Added progress bars during file loading and page rendering

### 🌓 Enhanced Dark Mode
- **Full Theme Support**: Dark mode now properly applies to all content
- **Adaptive Colors**: Text and backgrounds automatically adjust based on theme
- **Eye Comfort**: Reduced eye strain during night reading

### 🔧 Stability Improvements
- **Better Error Handling**: Improved exception handling to prevent crashes
- **Lifecycle Management**: Proper Coroutine lifecycle management
- **Resource Cleanup**: Better memory management with bitmap recycling

### 📱 UI Components Updated
- **Dashboard**:
  - Card-based layout with header and content sections
  - App subtitle "Document Viewer"
  - Icons on buttons (📄 PDF, 📊 PowerPoint)
  - Better spacing and visual hierarchy

- **PDF Viewer**:
  - Loading indicator during file opening
  - Elevated controls card at bottom
  - Material buttons with previous/next icons
  - Smooth page transitions

- **PPT Viewer**:
  - Content wrapped in card for better presentation
  - Enhanced text spacing (1.3x line height)
  - Material buttons with icons
  - Improved readability

### 🐛 Bug Fixes
- Fixed app returning to dashboard unexpectedly
- Fixed dark mode not applying to content properly
- Improved PDF navigation responsiveness
- Better handling of large files

---

## Version 1.0 (Initial Release)

### Features
- PDF viewing with page navigation
- PowerPoint (PPT/PPTX) text extraction and viewing
- Basic dark mode support
- File picker integration
- Offline operation
