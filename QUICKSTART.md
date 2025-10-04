# Quick Start - Build Your APK in 5 Minutes! 🚀

## The Absolute Easiest Way (Recommended for Beginners)

### Step 1: Download Android Studio
Go to: https://developer.android.com/studio

Click the big green "Download" button and install it.

### Step 2: Open This Project
1. Open Android Studio
2. Click "Open" or "Open an existing project"
3. Navigate to this folder and select it
4. Click "OK"

### Step 3: Wait for Setup
Android Studio will automatically:
- Download everything needed
- Set up the project
- Show you a "Sync successful" message

This takes 5-10 minutes the first time. Just wait! ☕

### Step 4: Build Your APK
1. Click the "Build" menu at the top
2. Click "Build Bundle(s) / APK(s)"
3. Click "Build APK(s)"
4. Wait for the build (1-5 minutes)
5. You'll see a notification saying "APK(s) generated successfully"

### Step 5: Find Your APK
Click "locate" in the notification, or find it here:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Step 6: Install on Your Phone

**Option A - Transfer File (Easiest):**
1. Copy `app-debug.apk` to your phone (USB, email, or cloud)
2. Open the file on your phone
3. If it asks, allow "Install from Unknown Sources"
4. Tap "Install"
5. Done! Your app is installed! 🎉

**Option B - Using USB Cable:**
1. Connect phone to computer with USB
2. On your phone:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times (enables Developer Mode)
   - Go to Settings → Developer Options
   - Enable "USB Debugging"
3. In Android Studio Terminal, type:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## That's It!

You now have the app on your phone! 📱

## What About Play Store?

The APK you just built is for testing. To publish to Play Store, you need a **signed release version**.

📖 See [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md) for the complete Play Store process.

## Need Help?

- **For detailed instructions**: Read [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)
- **For technical details**: Read [BUILDING.md](BUILDING.md)
- **For command line build**: Run `./build-apk.sh`

## Common Questions

**Q: Why Android Studio and not just a command?**
A: Android Studio automatically downloads and configures everything you need. It's much easier for beginners!

**Q: Do I need to pay for Android Studio?**
A: No! Android Studio is completely free.

**Q: Can I use this APK on Play Store?**
A: Not yet. This is a debug version for testing. For Play Store, you need a release version (see [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)).

**Q: Is it safe to allow "Unknown Sources"?**
A: Yes, for your own APK. Just be careful not to install APKs from untrusted sources.

**Q: How big is the APK?**
A: Around 10-20 MB depending on the build.

**Q: Does it work offline?**
A: Yes! Once installed, the app works completely offline.

## Next Steps

1. ✅ Install app on your phone
2. ✅ Test all features (open PDF, open PPT, try dark mode)
3. ✅ Get feedback from friends
4. 📖 Read [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md) to publish on Play Store

Good luck! 🎉
