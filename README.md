# The Light of Akwa Ibom — BRIGHTER WE SHINE! 🌟

[![Android Build](https://github.com/newpage0038-blip/The_Light_Of_Akwa_Ibom_Apk/actions/workflows/build-apk.yml/badge.svg)](https://github.com/newpage0038-blip/The_Light_Of_Akwa_Ibom_Apk/actions)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg)](https://developer.android.com/jetpack/compose)

An official, modern Android application celebrating the heritage, culture, unity, creativity, and opportunities of Akwa Ibom State, Nigeria.

---

## 🌟 App Creator & Credits
- **App Name:** The Light of Akwa Ibom
- **Official Slogan:** BRIGHTER WE SHINE!
- **Creator / Developer:** Daniel Ekpo
- **Artist Name:** A.K.A Newpage
- **Purpose:** A digital platform celebrating Akwa Ibom people, culture, community, creativity, and opportunities.
- **Application ID:** `com.aistudio.thelightofakwaibom.mxyrtv`
- **Current Version:** 1.0 (API 24 to API 36)

---

## 🚀 Key Features

### 1. 🏠 Home Dashboard
- Akwa Ibom state greeting chant (*"Leader: THE LIGHT! 🌟 / Members: BRIGHTER WE SHINE!"*).
- Dakkada Philosophy of greatness card.
- Comprehensive 14-section navigation hub.

### 2. 🎵 Entertainment Centre
- **Music Zone:** Afrobeats, Gospel, Highlife, and Akwa Ibom musicians with streaming platform integration and talent submission.
- **Comedy Corner:** Local comedy skits and video links.
- **Dance Challenge:** "Brighter We Shine" dance challenges and voting.
- **Light TV:** Documentaries, musical performances, and cultural interviews.

### 3. ⚽ Football & Sports Centre
- English Premier League (EPL) and UEFA Champions League (UCL) fixtures.
- Match kickoffs in West Africa Time (WAT).
- Live match status, scores, completed results, and standings table.
- Official broadcaster viewing guide (SuperSport, DStv Stream, Showmax, TNT Sports).

### 4. 🏛 Akwa Ibom Culture & Language
- Language learning modules for **Ibibio**, **Annang**, and **Oron**.
- Everyday phrases, greetings, pronunciation guides, and proverbs.
- Local delicacies (Afang, Edikang Ikong, Ekpang Nkukwo).
- Cultural quizzes with instant scoring and badges.

### 5. 💬 Community & Member Communication
- Real-time community group chat powered by Google Cloud Firestore.
- Member Directory across all 31 Local Government Areas (LGAs).
- Direct 1-on-1 messaging and voice/video calling signaling.
- Google Sign-In with Jetpack Credential Manager.

### 6. 💼 Opportunities & MSME Directory
- Verified job openings, scholarships, training programmes, and entrepreneurship grants.
- Akwa Ibom business directory with direct phone and WhatsApp contact.

### 7. 📱 Navigation & Offline Architecture
- Full in-app `← Back` and `Home` navigation buttons.
- Android system back and gesture navigation with double-back to exit on dashboard.
- Local Room Database caching for offline access to cultural content, news, and quizzes.

---

## 🛠 Tech Stack & Architecture
- **Language:** Kotlin 100%
- **UI Toolkit:** Jetpack Compose with Material Design 3 (M3)
- **Local Persistence:** Room Database with Kotlin Coroutines & Flow
- **Cloud Backend:** Google Cloud Firestore & Firebase Authentication
- **CI/CD:** GitHub Actions workflow (`.github/workflows/build-apk.yml`) building signed APKs & Google Play App Bundles (AAB)

---

## 📦 Building the APK Locally
```bash
# Clone the repository
git clone https://github.com/newpage0038-blip/The_Light_Of_Akwa_Ibom_Apk.git
cd The_Light_Of_Akwa_Ibom_Apk

# Run Unit Tests
gradle :app:testDebugUnitTest

# Build Debug / Signed APK
gradle assembleDebug

# Build Google Play App Bundle (AAB)
gradle bundleDebug
```
Output files will be located in:
- APK: `app/build/outputs/apk/debug/app-debug.apk`
- AAB: `app/build/outputs/bundle/debug/app-debug.aab`
