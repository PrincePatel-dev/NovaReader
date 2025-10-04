# Complete Guide to Build APK and Publish to Play Store

This guide will walk you through the entire process from building your first APK to publishing it on Google Play Store.

## Part 1: Building Your APK

### Option 1: Using Android Studio (Easiest - Recommended for Beginners)

Android Studio will automatically download all required components for you.

#### Step 1: Install Android Studio
1. Download Android Studio from: https://developer.android.com/studio
2. Install it on your computer
3. Open Android Studio
4. Follow the setup wizard to install the Android SDK

#### Step 2: Open the Project
1. In Android Studio, click "Open an existing project"
2. Navigate to this project folder and open it
3. Wait for Gradle to sync (this will download dependencies automatically)
4. If prompted to update Gradle or plugins, click "Update"

#### Step 3: Build the APK
1. Click on "Build" menu at the top
2. Select "Build Bundle(s) / APK(s)"
3. Click "Build APK(s)"
4. Wait for the build to complete (you'll see a notification)
5. Click "locate" in the notification to find your APK

Your APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

#### Step 4: Install APK on Your Phone
**Method A - Direct Transfer:**
1. Copy the `app-debug.apk` file to your phone (via USB, email, or cloud storage)
2. On your phone, open the APK file
3. You may need to enable "Install from Unknown Sources" in Settings
4. Tap "Install"

**Method B - Using ADB (if you have a USB cable):**
1. Enable "Developer Options" on your phone:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
2. Enable "USB Debugging" in Developer Options
3. Connect your phone to computer via USB
4. In Android Studio, go to Terminal and run:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Option 2: Command Line Build (For Advanced Users)

If you prefer using the command line, follow the instructions in `BUILDING.md`.

## Part 2: Preparing for Play Store Release

### Understanding APK Types

- **Debug APK**: What you just built. Good for testing but NOT for Play Store.
- **Release APK**: Signed and optimized. Required for Play Store.
- **App Bundle (AAB)**: Google's preferred format for Play Store (recommended).

### Step 1: Create a Signing Key

You need to sign your app before publishing. Here's how:

#### In Android Studio:
1. Go to "Build" → "Generate Signed Bundle / APK"
2. Select "Android App Bundle" (recommended) or "APK"
3. Click "Next"
4. Click "Create new..." under Key store path
5. Fill in the details:
   - **Key store path**: Choose a secure location (IMPORTANT: Keep this file safe!)
   - **Password**: Use a strong password (IMPORTANT: Don't forget this!)
   - **Alias**: Your app name or identifier
   - **Key password**: Can be same as keystore password
   - **Validity**: 25 years (minimum required)
   - **First and Last Name**: Your name or company name
   - **Organization**: Your company/organization name (or leave as "N/A")
6. Click "OK"
7. Click "Next"
8. Select "release" build variant
9. Check both signature versions (V1 and V2)
10. Click "Finish"

**⚠️ CRITICAL: Backup Your Keystore!**
- Store the `.jks` file and password in a safe place
- If you lose this, you can NEVER update your app on Play Store!
- Consider keeping a copy in a secure cloud storage

### Step 2: Build Release APK/Bundle

After creating your signing key, Android Studio will build and sign your release version.

The signed file will be at:
- Bundle: `app/release/app-release.aab`
- APK: `app/release/app-release.apk`

## Part 3: Publishing to Google Play Store

### Prerequisites
1. A Google Account
2. One-time registration fee: $25 USD
3. Your signed app bundle (.aab file)

### Step 1: Create a Developer Account
1. Go to: https://play.google.com/console
2. Sign in with your Google Account
3. Click "Create Account"
4. Accept the Developer Agreement
5. Pay the $25 registration fee
6. Complete your account details

### Step 2: Create Your App
1. In Play Console, click "Create app"
2. Fill in the required information:
   - **App name**: NovaReader (or your preferred name)
   - **Default language**: English (or your language)
   - **App or game**: Select "App"
   - **Free or paid**: Select "Free" (you can always change this later)
3. Accept the declarations
4. Click "Create app"

### Step 3: Complete Store Listing
You'll need to provide:

#### Main store listing:
- **App name**: NovaReader
- **Short description** (80 characters): "View PDF and PowerPoint files offline with dark mode support"
- **Full description** (4000 characters): Describe your app's features
  ```
  NovaReader is a simple, efficient PDF and PowerPoint viewer for Android.
  
  Features:
  • View PDF files with native Android renderer
  • View PPT and PPTX presentations
  • Dark mode for comfortable reading at night
  • Light mode for daytime use
  • Completely offline - no internet required
  • No background services - battery efficient
  • Privacy-focused - no data collection
  • Simple and easy to use interface
  
  System Requirements:
  • Android 5.0 or higher
  • Storage permission for accessing files
  ```

- **App icon**: 512x512 PNG (must be square)
- **Feature graphic**: 1024x500 PNG
- **Screenshots**: At least 2, up to 8 screenshots (phone: 320px-3840px on shortest side)
- **App category**: Productivity or Business
- **Contact details**: Your email address
- **Privacy policy**: URL to your privacy policy (optional but recommended)

### Step 4: Set Up Content Rating
1. Click "Content rating" in left menu
2. Start questionnaire
3. Select your app category
4. Answer questions honestly (for a document viewer, most answers will be "No")
5. Submit to get rating

### Step 5: Select Countries
1. Click "Countries/regions" in left menu
2. Select "Add countries/regions"
3. Choose where you want your app available
4. For worldwide: Select "Add all countries"

### Step 6: Upload Your App Bundle
1. Click "Production" in left menu
2. Click "Create new release"
3. Upload your `.aab` file (from Step 2 of Part 2)
4. Fill in "Release name": 1.0.0
5. Add "Release notes" (what's new):
   ```
   Initial release
   • PDF viewer with page navigation
   • PowerPoint viewer (PPT & PPTX)
   • Dark and light themes
   • Offline functionality
   ```
6. Review the release
7. Click "Save" (don't publish yet!)

### Step 7: Review and Publish
1. Go through the dashboard and complete all required sections
2. Everything should have green checkmarks
3. Click "Review release" in Production
4. Review everything one last time
5. Click "Start rollout to Production"

### Step 8: Wait for Review
- Google will review your app (usually takes 1-3 days)
- You'll receive an email when it's approved
- Once approved, it will be live on Play Store

## Troubleshooting

### Build Issues
- **Out of memory**: Add `org.gradle.jvmargs=-Xmx4096m` to `gradle.properties`
- **SDK not found**: Set up Android SDK path in Android Studio
- **Dependencies fail**: Check your internet connection

### Play Store Issues
- **App rejected**: Read the rejection email carefully and fix the issues
- **Missing content**: Complete all required sections in Play Console
- **Wrong format**: Make sure you're uploading .aab (not .apk) for Production

## Important Notes

1. **Debug vs Release**: Never publish debug builds to Play Store
2. **Keystore**: NEVER lose your keystore file and password
3. **Version Code**: Increase version code for each update
4. **Testing**: Test your app thoroughly before publishing
5. **Reviews**: Respond to user reviews professionally
6. **Updates**: Keep your app updated with bug fixes and features

## Next Steps After Publishing

1. **Monitor**: Check Play Console regularly for crashes and reviews
2. **Update**: Release updates to fix bugs and add features
3. **Promote**: Share your app link with users
4. **Engage**: Respond to user feedback
5. **Analyze**: Use Play Console analytics to understand user behavior

## App Screenshots Tips

To get good screenshots of your app:
1. Install app on your phone
2. Open the app and navigate to key features
3. Take screenshots (Power + Volume Down on most Android phones)
4. Use a screenshot editor to add borders or annotations if desired
5. Upload to Play Console

## Support

If you encounter any issues:
1. Check `BUILDING.md` for build instructions
2. Review this guide again
3. Search for the error message online
4. Check Android Developer documentation: https://developer.android.com/docs

## Useful Links

- Android Studio: https://developer.android.com/studio
- Play Console: https://play.google.com/console
- Android Developer Docs: https://developer.android.com/docs
- Play Console Help: https://support.google.com/googleplay/android-developer

Good luck with your app! 🚀
