# Build & Publish Checklist ✅

Use this checklist to track your progress from building the APK to publishing on Play Store.

## Phase 1: Building Your First APK 📱

- [ ] **Install Android Studio**
  - Download from: https://developer.android.com/studio
  - Run the installer
  - Complete the setup wizard

- [ ] **Open the Project**
  - Open Android Studio
  - Click "Open" and select this project folder
  - Wait for Gradle sync to complete (5-10 minutes first time)

- [ ] **Build Debug APK**
  - Go to Build → Build Bundle(s) / APK(s) → Build APK(s)
  - Wait for build to complete (1-5 minutes)
  - Note: APK location at `app/build/outputs/apk/debug/app-debug.apk`

- [ ] **Test on Your Phone**
  - Copy APK to phone
  - Enable "Install from Unknown Sources" if needed
  - Install and open the app
  - Test opening a PDF file
  - Test opening a PPT/PPTX file
  - Test dark mode toggle
  - Test light mode

- [ ] **Get Feedback**
  - Share with 2-3 friends or family
  - Ask them to test all features
  - Note any bugs or issues
  - Fix issues if found

## Phase 2: Preparing for Play Store 🏪

- [ ] **Understand the Difference**
  - Debug APK: For testing only (what you built above)
  - Release APK/Bundle: Signed, optimized for Play Store
  - You need a release version for Play Store

- [ ] **Create Signing Key** ⚠️ CRITICAL
  - In Android Studio: Build → Generate Signed Bundle/APK
  - Click "Create new..." under Key store path
  - Fill in all details carefully
  - Use a strong password
  - Save it in a secure location
  - **BACKUP THIS FILE!** If you lose it, you can never update your app!
  - Store password in a password manager or secure note

- [ ] **Build Release Bundle**
  - In Android Studio: Build → Generate Signed Bundle/APK
  - Select "Android App Bundle" (recommended)
  - Select your keystore
  - Enter passwords
  - Select "release" build variant
  - Check V1 and V2 signature
  - Click Finish
  - Note: Bundle location at `app/release/app-release.aab`

- [ ] **Test Release Build**
  - Install release APK on your phone (if you built APK instead of bundle)
  - OR use internal testing in Play Console (if you built bundle)
  - Verify it works exactly like debug version

## Phase 3: Play Store Setup 🌐

- [ ] **Create Google Play Developer Account**
  - Go to: https://play.google.com/console
  - Sign in with Google Account
  - Pay $25 one-time registration fee
  - Complete account setup
  - Verify your email

- [ ] **Create App in Play Console**
  - Click "Create app"
  - Enter app name: "NovaReader" (or your choice)
  - Select language
  - Choose "App" type
  - Select "Free"
  - Accept declarations
  - Click "Create app"

## Phase 4: Store Listing 🎨

- [ ] **Prepare App Icon**
  - Size: 512x512 pixels
  - Format: PNG
  - No transparency
  - Square shape

- [ ] **Prepare Feature Graphic**
  - Size: 1024x500 pixels
  - Format: PNG or JPG
  - Showcases your app

- [ ] **Take Screenshots**
  - Minimum 2, maximum 8 screenshots
  - From actual app running on phone
  - Show key features:
    - Main file picker screen
    - PDF viewing screen
    - PPT viewing screen
    - Dark mode (optional)

- [ ] **Write Store Listing**
  - App name (30 chars)
  - Short description (80 chars)
  - Full description (up to 4000 chars)
  - Choose app category: Productivity
  - Add contact email
  - Add privacy policy URL (optional but recommended)

- [ ] **Upload Assets**
  - Upload app icon
  - Upload feature graphic
  - Upload screenshots
  - Preview how it looks

## Phase 5: App Configuration ⚙️

- [ ] **Set Content Rating**
  - Go to Content Rating
  - Start questionnaire
  - Answer questions honestly
  - Submit for rating
  - Review your rating

- [ ] **Choose Countries**
  - Select countries/regions
  - For worldwide: "Add all countries"
  - Or select specific countries

- [ ] **Pricing & Distribution**
  - Confirm "Free" pricing
  - Review distribution settings
  - Accept program terms

## Phase 6: Upload & Publish 🚀

- [ ] **Upload App Bundle**
  - Go to Production → Create new release
  - Upload your .aab file
  - Fill in release name: "1.0.0"
  - Add release notes:
    ```
    Initial release
    • PDF viewer with page navigation
    • PowerPoint viewer (PPT & PPTX)
    • Dark and light themes
    • Offline functionality
    ```

- [ ] **Review Everything**
  - Check all sections have green checkmarks
  - Review store listing preview
  - Verify screenshots look good
  - Double-check app bundle uploaded correctly

- [ ] **Submit for Review**
  - Click "Review release"
  - Read summary carefully
  - Click "Start rollout to Production"
  - Confirm submission

## Phase 7: After Submission 🎯

- [ ] **Wait for Review**
  - Check email for updates
  - Typical wait: 1-3 days
  - May need to address issues if rejected

- [ ] **App Goes Live**
  - Receive approval email
  - App appears on Play Store within hours
  - Share Play Store link with users

- [ ] **Post-Launch**
  - Monitor reviews and ratings
  - Respond to user feedback
  - Track downloads in Play Console
  - Plan for updates and improvements

## Important Reminders 🔴

⚠️ **NEVER lose your keystore file and password!** Without it, you can't update your app.

✅ **Test thoroughly** before publishing. First impressions matter!

📱 **Keep a backup** of all your signing credentials in a secure location.

📧 **Check your email** regularly for Play Console notifications.

⭐ **Engage with users** - respond to reviews and feedback.

## Resources 📚

- **For beginners**: [QUICKSTART.md](QUICKSTART.md)
- **Complete guide**: [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)
- **Technical docs**: [BUILDING.md](BUILDING.md)
- **Build script**: Run `./build-apk.sh`

## Need Help?

If you get stuck on any step:
1. Check the relevant guide (links above)
2. Search for the specific error message
3. Check Android Developer documentation
4. Ask in Android developer communities

## Estimated Time

- Phase 1 (Building): 30 minutes - 1 hour
- Phase 2 (Release build): 30 minutes
- Phase 3-5 (Play Store setup): 1-2 hours
- Phase 6 (Upload): 30 minutes
- Phase 7 (Review): 1-3 days

**Total active time**: 3-5 hours spread over a few days.

Good luck! You've got this! 🎉
