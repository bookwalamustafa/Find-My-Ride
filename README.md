# Find My Ride – Kotlin Multiplatform Ride-Sharing App

### Drexel University – CS 461 Final Project

### Built by:
- Mustafa Bookwala
- Samii Shabuse
- Kennan Lu

---

## 📋 Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites & Setup](#prerequisites--setup)
- [How To Run the Project](#how-to-run-the-project)
- [Database Setup](#database-setup)
- [CRUD Implementation Details](#crud-implementation-details)
- [Key Implementation Notes](#key-implementation-notes)
- [Documentation & Demo](#documentation--demo)
- [Future Improvements](#future-improvements)

---

## Overview

**Find My Ride** is a full-stack **Kotlin Multiplatform (KMP)** mobile application that simulates a Drexel-focused ride-sharing service. The app allows students to:

- ✅ Create an account & log in
- ✅ View and edit their profile
- ✅ Browse available ride offers
- ✅ Publish a ride offer as a driver
- ✅ Send and receive messages from other users
- ✅ Store all data in a pre-loaded SQLite database

The purpose of the project is to demonstrate **end-to-end database integration**, clean architecture, multiplatform UI, and **CRUD (Create, Read, Update, Delete) operations** learned in CS461.

---

## Tech Stack

### **Frontend / App UI**
- **Kotlin Multiplatform (KMP)** – Write once, run on multiple platforms
- **JetBrains Compose Multiplatform** – Modern declarative UI framework
- **Material3 Components** – Google's latest Material Design system
- **Navigation** – Custom implementation using sealed classes for type-safe routing

### **Database Layer**
- **SQLite** – Lightweight, embedded relational database
- **Pre-loaded Database** – `findmyride.db` stored in `assets/` folder
- **Custom Repository Pattern** – Clean separation of concerns for each database table
- **Android SQLiteOpenHelper** – Native Android database management
- **Raw SQL Queries** – Direct SQL for maximum control and learning (no ORM framework)

### **Platforms Supported**
- ✅ **Android** (Primary target – fully functional)
- ⚠️ **Compose Desktop** (Compilation supported, database layer not implemented)
- 🔄 **iOS/JS** – KMP structure prepared, but database implementation is Android-only for this assignment

---

## Project Structure
```
FindMyRide/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/               # Shared code across platforms
│   │   │   ├── kotlin/
│   │   │   │   ├── feature/
│   │   │   │   │   ├── auth/         # Login & Registration screens
│   │   │   │   │   ├── profile/      # User profile screen & logic
│   │   │   │   │   ├── rides/        # Find Ride & Offer Ride features
│   │   │   │   │   ├── messages/     # Messaging system UI & logic
│   │   │   │   │   └── db/           # Repository interfaces & data models
│   │   │   │   ├── App.kt            # Root navigation (Login vs Main)
│   │   │   │   └── MainRoute.kt      # In-app navigation hub
│   │   │   └── resources/            # Shared resources
│   │   │
│   │   ├── androidMain/              # Android-specific implementations
│   │   │   ├── kotlin/
│   │   │   │   ├── db/
│   │   │   │   │   ├── FindMyRideDbProvider.kt      # Database initialization
│   │   │   │   │   ├── AndroidAuthRepository.kt     # USER table CRUD
│   │   │   │   │   ├── AndroidProfileRepository.kt  # USER + VEHICLE CRUD
│   │   │   │   │   ├── AndroidMessagesRepository.kt # MESSAGE table CRUD
│   │   │   │   │   └── AndroidRideRepository.kt     # RIDE_OFFER + RIDE_REQUEST CRUD
│   │   │   │   └── MainActivity.kt   # Android app entry point
│   │   │   └── assets/
│   │   │       └── findmyride.db     # Pre-populated SQLite database
│   │   │
│   │   └── iosMain/                  # iOS implementations (not used)
│   │
│   └── build.gradle.kts              # App-level build configuration
│
├── database/
│   └── schema.sql                    # SQL schema for database creation
│
├── docs/
│   └── Deliverable_4/                # **📹 Contains live demo & CRUD documentation**
│
├── gradle/                           # Gradle wrapper files
├── build.gradle.kts                  # Project-level build configuration
├── settings.gradle.kts               # Project settings
└── README.md                         # This file
```

---

## Prerequisites & Setup

### **System Requirements**

1. **Operating System**: Windows 10/11, macOS 10.15+, or Linux
2. **RAM**: Minimum 8GB (16GB recommended for smooth emulator performance)
3. **Disk Space**: At least 10GB free space

### **Required Software**

#### 1. **Install Java Development Kit (JDK)**
- **Required Version**: JDK 17 or higher
- **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- **Verify Installation**:
```bash
  java -version
  # Should show version 17+
```

#### 2. **Install Android Studio**
- **Required Version**: Android Studio Ladybug (2024.2.1) or newer
- **Download**: [Android Studio Official Site](https://developer.android.com/studio)
- **Important**: During installation, make sure to install:
  - Android SDK
  - Android SDK Platform
  - Android Virtual Device (AVD)

#### 3. **Install Kotlin Multiplatform Plugin**
After installing Android Studio:
1. Open Android Studio
2. Go to **File → Settings** (or **Android Studio → Preferences** on macOS)
3. Navigate to **Plugins**
4. Search for "**Kotlin Multiplatform**"
5. Click **Install** and restart Android Studio

#### 4. **Configure Android SDK**
1. In Android Studio, go to **File → Settings → Appearance & Behavior → System Settings → Android SDK**
2. Ensure these are installed:
   - **Android SDK Platform 34** (or higher)
   - **Android SDK Build-Tools 34.0.0** (or higher)
   - **Android Emulator**
   - **Android SDK Platform-Tools**

#### 5. **Set Up Android Emulator**
1. In Android Studio, go to **Tools → Device Manager**
2. Click **Create Device**
3. Select a device definition (e.g., **Pixel 5**)
4. Download and select a system image:
   - Recommended: **API Level 34** (Android 14.0)
   - Select **x86_64** or **ARM64** based on your system
5. Click **Finish** to create the AVD

#### 6. **Install SQLite (Optional - for database development)**
- **Windows**: Download from [SQLite Download Page](https://www.sqlite.org/download.html)
- **macOS**: Pre-installed, or install via Homebrew:
```bash
  brew install sqlite3
```
- **Linux**:
```bash
  sudo apt-get install sqlite3  # Ubuntu/Debian
  sudo yum install sqlite       # Fedora/RHEL
```
- **Verify Installation**:
```bash
  sqlite3 --version
  # Should show version 3.x.x
```

---

## How To Run the Project

### **Step 1: Clone the Repository**
```bash
git clone https://github.com/bookwalamustafa/Find-My-Ride.git
cd FindMyRide
```

### **Step 2: Open Project in Android Studio**
1. Launch **Android Studio**
2. Select **File → Open**
3. Navigate to the `FindMyRide` folder and click **OK**
4. Wait for **Gradle sync** to complete (this may take 3-5 minutes on first run)
   - Watch the bottom status bar for "Gradle Build" progress
   - If prompted, accept any SDK licenses

### **Step 3: Verify Kotlin Multiplatform Setup**
1. Check that no errors appear in the **Build** output window
2. Verify the project structure shows `commonMain`, `androidMain`, and `iosMain` folders
3. If you see any errors related to KMP, ensure the Kotlin Multiplatform plugin is installed

### **Step 4: Select Run Configuration**
1. In the top toolbar, find the **Run Configuration** dropdown
2. Select **composeApp:androidApp** (or just **composeApp**)
3. If you don't see this option, try **File → Sync Project with Gradle Files**

### **Step 5: Launch the App**
**Option A: Using Android Emulator (Recommended)**
1. In the device dropdown (next to Run Configuration), select your created AVD
2. Click the **Run** button (green play icon) or press **Shift + F10**
3. Wait for the emulator to boot (30-60 seconds on first launch)
4. The app will automatically install and launch

**Option B: Using Physical Android Device**
1. Enable **Developer Options** on your Android device:
   - Go to **Settings → About Phone**
   - Tap **Build Number** 7 times
2. Enable **USB Debugging**:
   - Go to **Settings → Developer Options**
   - Enable **USB Debugging**
3. Connect your device via USB
4. Select your device from the device dropdown
5. Click **Run**

### **Step 6: First Launch**
- On first launch, the app will automatically copy the SQLite database from `assets/` to the device storage
- No additional configuration is needed
- The database comes pre-populated with sample data for testing

### **Troubleshooting Common Issues**

| Issue | Solution |
|-------|----------|
| "SDK not found" error | Install Android SDK Platform 34 via SDK Manager |
| Gradle sync fails | Check internet connection, clear Gradle cache (**File → Invalidate Caches**) |
| Emulator won't start | Ensure hardware acceleration (Intel HAXM/AMD WHPX) is enabled in BIOS |
| "Module not specified" error | Select **composeApp** run configuration |
| Database not loading | Check that `findmyride.db` exists in `composeApp/src/androidMain/assets/` |

---

## Database Setup

### **Using the Pre-loaded Database**
The project comes with a ready-to-use SQLite database (`findmyride.db`) that includes:
- Sample user accounts
- Pre-populated ride offers
- Example messages
- Vehicle information

**No manual database setup is required for normal use.**

### **Creating a Fresh Database (Advanced)**

If you want to create a new database from scratch:

#### **Step 1: Navigate to Project Root**
```bash
cd /path/to/FindMyRide
```

#### **Step 2: Generate Database from Schema**
```bash
sqlite3 findmyride.db < database/schema.sql
```

This command:
- Creates a new `findmyride.db` file
- Executes all SQL commands in `schema.sql`
- Sets up all tables, indexes, and constraints

#### **Step 3: Verify Database Creation**
```bash
sqlite3 findmyride.db
```

Inside the SQLite shell:
```sql
.tables                    -- List all tables
.schema USER              -- View USER table structure
SELECT * FROM USER;       -- View user data
.exit                     -- Exit SQLite shell
```

#### **Step 4: Replace Old Database**
```bash
# Backup old database (optional)
mv composeApp/src/androidMain/assets/findmyride.db composeApp/src/androidMain/assets/findmyride.db.backup

# Move new database to assets
mv findmyride.db composeApp/src/androidMain/assets/
```

#### **Step 5: Rebuild and Run**
- In Android Studio: **Build → Rebuild Project**
- Run the app – it will now use your new database

---

## CRUD Implementation Details

Our application implements comprehensive **CRUD (Create, Read, Update, Delete)** operations across all major database tables. Below is a detailed breakdown of how each operation is implemented.

### **Architecture Overview**
```
UI Layer (Compose)
      ↓
Repository Interface (commonMain)
      ↓
Repository Implementation (androidMain)
      ↓
SQLite Database (findmyride.db)
```

### **1. USER Table CRUD** (`AndroidAuthRepository.kt`)

#### **Create (Registration)**
[FILL IN: Explain how user registration works]
- Example: "When a user registers, the `registerUser()` function..."
- SQL query used
- Password hashing approach
- Validation checks

#### **Read (Login & Profile Loading)**
[FILL IN: Explain how user authentication and profile retrieval works]
- Example: "Login validates credentials using..."
- How user sessions are managed
- Profile data retrieval process

#### **Update (Profile Editing)**
[FILL IN: Explain how profile updates work]
- Example: "Users can update their profile information through..."
- Fields that can be updated
- Validation logic

#### **Delete (Account Deletion)**
[FILL IN: Explain account deletion]
- Example: "Account deletion is handled by..."
- Cascade behavior with related data
- Soft delete vs hard delete approach

---

### **2. VEHICLE Table CRUD** (`AndroidProfileRepository.kt`)

#### **Create (Add Vehicle)**
[FILL IN: Vehicle creation details]

#### **Read (View Vehicles)**
[FILL IN: How vehicles are displayed]

#### **Update (Edit Vehicle)**
[FILL IN: Vehicle editing process]

#### **Delete (Remove Vehicle)**
[FILL IN: Vehicle removal logic]

---

### **3. RIDE_OFFER Table CRUD** (`AndroidRideRepository.kt`)

#### **Create (Publish Ride)**
[FILL IN: How drivers create ride offers]

#### **Read (Browse Rides)**
[FILL IN: How rides are listed and filtered]

#### **Update (Modify Ride Details)**
[FILL IN: Editing existing ride offers]

#### **Delete (Cancel Ride)**
[FILL IN: Ride cancellation process]

---

### **4. RIDE_REQUEST Table CRUD** (`AndroidRideRepository.kt`)

#### **Create (Request a Ride)**
[FILL IN: How passengers request rides]

#### **Read (View Requests)**
[FILL IN: Displaying ride requests]

#### **Update (Update Request Status)**
[FILL IN: Accepting/rejecting requests]

#### **Delete (Cancel Request)**
[FILL IN: Request cancellation]

---

### **5. MESSAGE Table CRUD** (`AndroidMessagesRepository.kt`)

#### **Create (Send Message)**
[FILL IN: Message sending implementation]

#### **Read (View Messages)**
[FILL IN: Message retrieval and display]

#### **Update (Edit Message)**
[FILL IN: If supported, how messages can be edited]

#### **Delete (Delete Message)**
[FILL IN: Message deletion process]

---

### **Database Transaction Handling**

[FILL IN: Explain how you handle database transactions]
- Example: "All CRUD operations use database transactions to ensure..."
- Error handling approach
- Rollback strategy for failed operations

### **Data Validation**

[FILL IN: Describe validation layers]
- Example: "Input validation occurs at three levels..."
- UI-level validation
- Repository-level validation
- Database constraints

---

## Key Implementation Notes

### **State Management**
- **UI State**: Uses Jetpack Compose's `remember { mutableStateOf() }` for reactive UI updates
- **State Hoisting**: State is lifted to appropriate composable parents for sharing
- **Side Effects**: `LaunchedEffect` and `DisposableEffect` for lifecycle-aware operations

### **Navigation System**
- **Sealed Classes**: Type-safe navigation using `sealed class RootScreen` and `sealed class HomePage`
- **Manual Implementation**: No NavHost – custom navigation logic for learning purposes
- **State Preservation**: Navigation state maintained across configuration changes

### **Repository Pattern**
- **Interface Definition**: All repositories defined as interfaces in `commonMain`
- **Platform Implementation**: Concrete implementations in `androidMain` using SQLite
- **Dependency Injection**: Repositories passed down through composition
- **Single Source of Truth**: Database is the authoritative data source

### **Database Management**
- **Database Provider**: `FindMyRideDbProvider` handles initialization and copying from assets
- **Connection Pooling**: Single database instance reused across app lifecycle
- **Raw SQL Queries**: Direct SQL for transparency and learning (no ORM like Room or SQLDelight)
- **Query Optimization**: Indexed columns for frequently queried fields

### **Code Organization**
- **Feature-Based Structure**: Each feature (auth, profile, rides, messages) is self-contained
- **Separation of Concerns**: UI, business logic, and data access are clearly separated
- **Shared Code**: Maximum code sharing through `commonMain` for cross-platform readiness

### **Android-Specific Considerations**
- **SQLiteOpenHelper**: Native Android database management for direct control
- **Context Handling**: Database requires Android `Context` for file operations
- **Assets Access**: Database file copied from `assets/` to internal storage on first run

---

## Documentation & Demo

### **📹 Live Demo & CRUD Operations**

For a comprehensive demonstration of the application and detailed CRUD operation walkthroughs, please refer to:

📁 **`docs/Deliverable_4/`**

This folder contains:
- **Live Demo Video**: Complete walkthrough of all application features
- **CRUD Operation Examples**: Step-by-step demonstration of Create, Read, Update, and Delete operations for each database table
- **Database Schema Documentation**: Detailed table structures and relationships
- **Testing Scenarios**: Various use cases and edge cases tested during development

**Recommended Viewing Order:**
1. Start with the live demo video for an overview
2. Review CRUD operation examples for technical details
3. Reference database schema for understanding data relationships

---

## Future Improvements

### **Feature Enhancements**
- **Advanced Filtering**: Filter rides by time, price, available seats, and route
- **Vehicle Picker**: Allow drivers to select from their registered vehicles when offering rides
- **Enhanced Messaging**: Full chat UI with real-time updates per ride
- **Notifications**: Push notifications for new ride offers and messages
- **Matching Algorithm**: Intelligent ride request matching based on routes and preferences
- **Rating System**: Allow users to rate drivers and passengers
- **Payment Integration**: Add payment processing for ride fares

### **Technical Improvements**
- **iOS Support**: Implement database layer using SQLDelight or KMP-SQLite for iOS compatibility
- **Backend Server**: Move to a centralized server with REST API for real-time data sync
- **Enhanced Security**: Implement JWT authentication and encrypted data storage
- **Testing**: Add unit tests, integration tests, and UI tests
- **Theming**: Dark mode support and customizable themes
- **Maps Integration**: Visual route display using Google Maps or Mapbox
- **Analytics**: Track app usage and user behavior for insights

### **Platform Expansion**
- **iOS Native App**: Full iOS implementation with native database layer
- **Web Version**: Browser-based version using Kotlin/JS
- **Desktop Support**: Complete desktop app with database support

---

## Contact & Support

For questions, issues, or contributions:

- **Mustafa Bookwala**: [mmb479@drexel.edu/GitHub]
- **Samii Shabuse**: [sus24@drexel.edu/GitHub]
- **Kennan Lu**: [kjl354@drexel.edu/GitHub]

**Course**: CS 461 - Database Systems  
**Institution**: Drexel University  
**Term**: Fall 2025
