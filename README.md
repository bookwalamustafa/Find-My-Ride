# Find My Ride – Kotlin Multiplatform Ride-Sharing App
### Drexel University – CS 461 Final Project
### Built by: 
- Mustafa Bookwala
- Samii Shabuse
- Kennan Lu

---

## Overview

**Find My Ride** is a full-stack **Kotlin Multiplatform (KMP)** mobile application that simulates a Drexel-focused ride-sharing service.  
The app allows students to:

- Create an account & log in
- View their profile
- Browse available ride offers
- Publish a ride offer as a driver
- Send messages to other users
- Store all data in a pre-loaded SQLite database

The purpose of the project is to demonstrate **end-to-end database integration**, clean architecture, multiplatform UI, and CRUD operations learned in CS461.

---

## Tech Stack

### **Frontend / App UI**
- **Kotlin Multiplatform (KMP)**
- **JetBrains Compose Multiplatform**
- **Material3 Components**
- **Navigation built manually via sealed classes**

### **Database Layer**
- **SQLite** (preloaded DB file in `assets/findmyride.db`)
- **Custom repository layer** for all DB tables
- **Android SQLiteOpenHelper** for managing the database (Android-only)
- **Raw SQL queries** (no ORM) for maximum clarity

### **Platforms Supported**
- **Android** (Primary target)
- **Compose Desktop** (Compilation supported, no DB)
- KMP structure prepared for iOS/JS, but **DB only implemented on Android** for this assignment.

---

## Project Structure
```SQL
composeApp/
├── src/
│ ├── commonMain/
│ │ ├── feature/
│ │ │ ├── auth/ # Login, Registration
│ │ │ ├── profile/ # User profile screen
│ │ │ ├── rides/ # Find Ride & Offer Ride
│ │ │ ├── messages/ # Messaging system
│ │ │ └── db/ # Repository interfaces & shared data models
│ │ ├── App.kt # Root-level navigation deciding Login/Main
│ │ └── MainRoute.kt # In-app navigation (Dashboard → Screens)
│ │
│ ├── androidMain/
│ │ ├── db/
│ │ │ ├── FindMyRideDbProvider.kt # Loads SQLite DB from assets
│ │ │ ├── AndroidAuthRepository.kt # USER table
│ │ │ ├── AndroidProfileRepository.kt # USER + VEHICLE
│ │ │ ├── AndroidMessagesRepository.kt # MESSAGE table
│ │ │ └── AndroidRideRepository.kt # RIDE_OFFER + RIDE_REQUEST
│ │ └── MainActivity.kt # Android entry point
│ │
│ └── iosMain/ # (Not used)
│
└── assets/
└── findmyride.db # Preloaded SQLite database
```


---

## Database Schema Overview

The app uses a **pre-populated SQLite database** stored in `assets/findmyride.db`.  
This database contains all tables required for the ride-sharing scenario:

### **USER**
| Column | Type | Description |
|--------|------|-------------|
| user_id | INTEGER PK | Unique user ID |
| email | TEXT | Login credential |
| password | TEXT | User password |
| display_name | TEXT | Display name |
| phone | TEXT | User contact number |

### **VEHICLE**
| Column | Type |
|--------|------|
| vehicle_id | INTEGER PK |
| owner_user_id | INTEGER FK → USER |
| make | TEXT |
| model | TEXT |
| color | TEXT |

### **LOCATION**
Preset Drexel locations:
- University Crossings
- Korman Center
- Main Building
- etc.

### **RIDE_OFFER**
Driver-created ride offers  
(Read by AvailableRidesScreen, written by AvailableOfferScreen)

| Column | Type |
|--------|------|
| offer_id | INTEGER PK |
| driver_id | INTEGER FK → USER |
| vehicle_id | INTEGER FK → VEHICLE |
| original_location_id | INTEGER FK → LOCATION |
| dest_location_id | INTEGER FK → LOCATION |
| depart_at | TEXT |
| seats_available | INTEGER |
| price_base | REAL |
| price_per_mile | REAL |
| status | TEXT |

### **RIDE_REQUEST**
Stores ride requests created by riders  
(Used by repository, not fully exposed in UI)

### **MESSAGE**
Messaging between users  
| Column | Type |
|--------|------|
| message_id | INTEGER PK |
| sender_id | INTEGER FK → USER |
| receiver_id | INTEGER |
| content | TEXT |
| timestamp | TEXT |

---

## Database Architecture & Access Layer

This project uses a clean Repository Pattern to keep all SQLite logic isolated to the Android layer while allowing the UI layer (commonMain) to stay fully multiplatform.

1. FindMyRideDbProvider (Android-only)

- Loads the SQLite database file (findmyride.db) from the assets folder.
- Copies it into Android internal storage on first launch.
- Exposes functions for obtaining readable and writable database instances.
- Ensures all repositories use the same initialized database.

Located at: androidMain/db/FindMyRideDbProvider.kt


2. Repository Interfaces (commonMain)

Each feature defines an interface that represents its database API. These interfaces are platform-agnostic.

Example: RideRepository defines functions like:
- getOpenRideOffers()
- createRideOffer(...)

The UI talks ONLY to these interfaces, never SQLite directly.


3. Android Implementations (androidMain)

Each repository interface has a real implementation in androidMain that uses:
- SQLiteDatabase
- rawQuery()
- insert()
- update()
- delete()

Examples:
- AndroidRideRepository reads/writes RIDE_OFFER and RIDE_REQUEST
- AndroidAuthRepository reads USER for login
- AndroidProfileRepository reads USER + VEHICLE
- AndroidMessagesRepository reads/writes MESSAGE

This keeps SQL isolated to Android while UI stays pure KMP.


4. UI Layer (commonMain) Consumes Repositories

The UI never touches SQLite.

Examples:
- Reading from DB: rideRepository.getOpenRideOffers()
- Writing to DB: rideRepository.createRideOffer(...)

This separation ensures portability and clean architecture.


-------------------------------------------------------------

## Feature-by-Feature Explanation

1. Login Screen
- Reads from USER through AuthRepository.
- Validates email + password.
- Stores currentUserId inside MainRoute.
- On success -> navigates to Dashboard.


2. Dashboard
   Acts as the main navigation hub for:
- Profile
- Find Ride
- Offer Ride
- Messages

No DB calls here.


3. AvailableRidesScreen — Database READ
- Uses rideRepository.getOpenRideOffers().
- Shows list of all open rides from RIDE_OFFER table.
- Displays origin, destination, seats, price, driver info.
- Splits results into “Best Matches” and “Other Matches”.

Backend: AndroidRideRepository using SELECT queries.


4. AvailableOfferScreen — Database WRITE
   This screen lets a driver publish a ride.

When user taps Publish:
- createRideOffer() is called with driverId, vehicleId, locations, pricing, etc.
- This performs INSERT INTO RIDE_OFFER.
- App returns to Dashboard afterward.


5. Profile Screen — Database READ
   Reads from USER and VEHICLE:
- display_name
- email
- phone
- car make/model/color

Used to show account info.


6. Messages Screen — Database READ & WRITE
- Reads conversations via SELECT on MESSAGE table.
- Displays messages in a thread-style list.
- Sends messages using INSERT INTO MESSAGE.
- Powered by MessagesRepository and AndroidMessagesRepository.


-------------------------------------------------------------

## How To Run the Project

Requirements:
- Android Studio (Ladybug / Koala / modern KMP version)
- Kotlin Multiplatform plugin enabled
- Android SDK 34 or higher

Steps:
1. Open the project in Android Studio.
2. Wait for Gradle sync to complete.
3. Select run configuration: composeApp:androidApp
4. Run on emulator or Android device.
5. App starts and automatically copies SQLite database from assets.

No server required.
No external API required.
Database is bundled inside the APK.

-------------------------------------------------------------

## How To Create A New Database

1. Install SQLite if you don’t have it (`sqlite3 --version` to check).
    - Using Version: 3.50.4 2025-07-30 19:33:53 4d8adfb30e03f9cf27f800a2c1ba3c48fb4ca1b08b0f5ed59a4d5ecbf45e20a3 (64-bit)
2. In the root project folder, run:
```bash
sqlite3 findmyride.db < database/schema.sql
```
3. You can view the tables with:
```bash
sqlite3 findmyride.db
sqlite> .tables
```

-------------------------------------------------------------

## Key Implementation Notes

- UI state uses Jetpack Compose’s remember { mutableStateOf() }.
- Navigation uses sealed classes (RootScreen, HomePage).
- UI + Repository Interfaces live in commonMain.
- SQLite logic lives exclusively in androidMain.
- Database access uses raw SQL for transparency (no ORM).
- Database lifecycle & copying handled by FindMyRideDbProvider.


-------------------------------------------------------------

## Future Improvements 

- Add ride filtering by time, price, seats.
- Add vehicle picker tied to VEHICLE table.
- Add a full chat UI per ride.
- Support for iOS using SQLDelight or KMP-SQLite.
- Add push-style notifications for new rides.
- Implement ride request matching algorithm.


-------------------------------------------------------------


