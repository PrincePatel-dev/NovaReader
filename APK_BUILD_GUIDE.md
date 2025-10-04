# 🚀 NovaReader APK Build Guide
## Complete Step-by-Step Instructions for Future Updates

### 📋 **Before You Start**
- Keep your `release-key.keystore` file safe
- Remember your keystore password: `novareader123`
- Make sure Android SDK is installed

---

## 🔧 **Scenario 1: Adding New Features (Code Changes)**

### **Step 1: Make Your Code Changes**
```bash
# Make changes to your Kotlin files in:
# - app/src/main/java/com/pdfpptviewer/
# - app/src/main/res/ (for UI changes)
```

### **Step 2: Update Version Number**
Edit `app/build.gradle`:
```gradle
defaultConfig {
    applicationId "com.pdfpptviewer"
    minSdk 26
    targetSdk 34
    versionCode 2          // Increment this (was 1)
    versionName "1.1"      // Update this (was "1.0")
}
```

### **Step 3: Build New APK**
```bash
export ANDROID_SDK_ROOT="/home/codespace/android-sdk"
gradle --no-daemon clean assembleRelease
```

### **Step 4: Find Your New APK**
```bash
ls -lah /workspaces/NovaReader/app/build/outputs/apk/release/
# Copy and rename if desired:
cp app/build/outputs/apk/release/app-release.apk NovaReader-v1.1.apk
```

---

## 🔐 **Scenario 2: Changing Keystore Password**

### **⚠️ CRITICAL WARNING**
- **ONLY do this BEFORE publishing to Play Store**
- **NEVER change keystore after publishing**
- **Once on Play Store, ALWAYS use the same keystore**

### **Step 1: Change Keystore Password**
```bash
# Change store password
keytool -storepasswd -keystore app/release-key.keystore

# Change key password
keytool -keypasswd -alias nova-reader -keystore app/release-key.keystore
```

### **Step 2: Update build.gradle**
```gradle
signingConfigs {
    release {
        storeFile file('release-key.keystore')
        storePassword 'your-new-store-password'
        keyAlias 'nova-reader'
        keyPassword 'your-new-key-password'
    }
}
```

### **Step 3: Rebuild APK**
```bash
export ANDROID_SDK_ROOT="/home/codespace/android-sdk"
gradle --no-daemon clean assembleRelease
```

---

## 🏪 **Scenario 3: Publishing Updates to Play Store**

### **Step 1: Build APK (with updated version)**
```bash
# Always increment versionCode in build.gradle first!
export ANDROID_SDK_ROOT="/home/codespace/android-sdk"
gradle --no-daemon clean assembleRelease
```

### **Step 2: Upload to Play Console**
1. Go to Google Play Console
2. Select your app "NovaReader"
3. Go to "Release" → "Production"
4. Click "Create new release"
5. Upload your new APK
6. Add release notes describing what's new
7. Click "Review release" → "Start rollout to production"

### **Step 3: Version Requirements**
- **versionCode**: Must be higher than previous (1 → 2 → 3...)
- **versionName**: Can be anything ("1.0" → "1.1" → "2.0"...)

---

## 🔄 **Quick Reference Commands**

### **Environment Setup**
```bash
export ANDROID_SDK_ROOT="/home/codespace/android-sdk"
```

### **Clean Build**
```bash
gradle --no-daemon clean assembleRelease
```

### **Find APK**
```bash
find . -name "*.apk" -type f
```

### **Rename APK**
```bash
cp app/build/outputs/apk/release/app-release.apk NovaReader-v1.x.apk
```

---

## 📱 **Testing Your APK**

### **Before Uploading to Play Store:**
1. Install APK on your phone
2. Test all features (PDF viewing, PPT viewing)
3. Check for crashes
4. Verify app icon and name display correctly

### **Installation on Phone:**
1. Enable "Unknown Sources" in Settings
2. Transfer APK to phone
3. Tap APK file to install
4. Test thoroughly

---

## ⚠️ **Important Notes**

### **Version Management:**
- **Always increment versionCode** for updates
- Use meaningful versionName (1.0, 1.1, 2.0)
- Keep track of what changed in each version

### **Keystore Security:**
- **Backup your keystore** to multiple safe locations
- **Never lose the keystore** - you can't update your app without it
- **Use same keystore** for all future updates

### **Build Troubleshooting:**
- If build fails, check error messages
- Clean build: `gradle clean`
- Check Android SDK path is set correctly
- Ensure all dependencies are available

---

## 🎯 **Version History Template**

Keep track of your releases:
```
v1.0 (versionCode: 1) - Initial release
v1.1 (versionCode: 2) - Bug fixes and UI improvements
v1.2 (versionCode: 3) - New PDF features
v2.0 (versionCode: 4) - Major UI overhaul
```

---

## 🚀 **Quick Build Commands Summary**

```bash
# 1. Set Android SDK
export ANDROID_SDK_ROOT="/home/codespace/android-sdk"

# 2. Build APK
gradle --no-daemon clean assembleRelease

# 3. Find and rename APK
cp app/build/outputs/apk/release/app-release.apk NovaReader-v1.x.apk

# 4. Ready to install/upload!
```

Save this guide for future reference!