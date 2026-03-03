# Swifty Protein (Android)

Android implementation of **Swifty Protein**: browse ligands and visualize them in interactive 3D using data from the RCSB Protein Data Bank.

## About This Project

This app was built for the Swifty Protein assignment and covers:

- mobile 3D rendering
- biometric authentication with password fallback
- searchable ligand catalog
- robust loading/error flows
- model sharing from the 3D screen

## Assignment Coverage (Mandatory + Bonus)

### Mandatory

- ✅ Themed app icon
- ✅ Launch/splash screen with visible duration
- ✅ Login view shown when app is launched and when returning from background
- ✅ Local account creation and password login
- ✅ Fingerprint login (when biometric hardware is available)
- ✅ Auth failed popup
- ✅ Password fallback when biometric is unavailable
- ✅ Ligand list loaded from `ligands.txt`
- ✅ Search in ligand list
- ✅ Warning popup when ligand cannot be loaded
- ✅ Loading indicator while fetching ligand data
- ✅ 3D ligand rendering
- ✅ CPK atom coloring
- ✅ Ball-and-Stick representation
- ✅ Atom tooltip/popup on tap, dismissed by tapping elsewhere
- ✅ Model sharing via Share button
- ✅ User can rotate/zoom the molecule

### Bonus

- ✅ Additional visualization modes:
  - Space Fill
  - Sticks Only

## Tech Stack

- **Language/UI:** Kotlin, Jetpack Compose, Material 3
- **Architecture:** MVVM + Repository
- **DI:** Hilt
- **Local storage:** Room
- **Networking:** Retrofit + OkHttp
- **Biometrics:** AndroidX Biometric
- **3D engine:** SceneView (Filament backend)

## Architecture Overview

- `ui/login`  
  Login/register screen, biometric entry point, auth error handling.
- `ui/proteinlist`  
  Ligand list, search filtering, loading overlay, fetch error dialog.
- `ui/proteinview`  
  Scene construction, atom picking, tooltip display, share flow.
- `data/remote`  
  RCSB API contract for `.cif` download.
- `data/parser`  
  mmCIF parser for atoms and bonds.
- `data/local`  
  Room entities/DAO for user accounts.
- `data/repository`  
  Auth and ligand use-cases.

## Requirements

- Android Studio (latest stable recommended)
- Android SDK 35
- Min SDK 26
- JDK 17
- Internet connection (for ligand download)

## Run Locally

1. Open the project in Android Studio.
2. Wait for Gradle sync to complete.
3. Run the `app` configuration on emulator/device.
4. Register a user account.
5. Log in (password or fingerprint if available).
6. Select a ligand from the list to open the 3D view.

## How To Test Core Flows

### Authentication

1. Launch app -> Login screen is displayed.
2. Try wrong password -> "Authentication Failed" dialog appears.
3. Register valid account -> login succeeds.
4. Press Home, open app again -> Login screen appears again.
5. On biometric-capable device, use fingerprint login.

### Ligand List

1. Verify list is populated from `ligands.txt`.
2. Type in search bar -> list filters by query.
3. Tap ligand -> loading indicator appears.
4. Disable internet and tap ligand -> error dialog appears.

### 3D Viewer

1. Molecule is rendered in 3D.
2. Colors match CPK rules by atom element.
3. Switch visualization modes (Ball & Stick / Space Fill / Sticks).
4. Tap atom -> tooltip appears with element info.
5. Tap empty area -> tooltip disappears.
6. Pinch/drag -> zoom and rotate work.
7. Tap Share -> model screenshot is shared.

## Data Source

- RCSB ligand CIF endpoint:  
  `https://files.rcsb.org/ligands/download/{ID}.cif`
