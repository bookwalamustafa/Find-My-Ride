#!/usr/bin/env python3
"""
generate_database.py
1. Generates database/schema.sql and database/populate.sql.
2. Creates 'findmyride.db' from scratch in the database folder.
3. Executes the schema and population scripts against the database.
4. Copies the finished database to ./app/composeApp/src/androidMain/assets/findmyride.db so that it can be used by the application
"""

import os
import random
import datetime
import sqlite3
import shutil
from pathlib import Path

random.seed(42)

# --- CONFIGURATION ---

# SQL Output Paths
OUT_DIR = Path("database")
OUT_DIR.mkdir(parents=True, exist_ok=True)
SCHEMA_PATH = OUT_DIR / "schema.sql"
POPULATE_PATH = OUT_DIR / "populate.sql"

# DB Path
DB_FILENAME = OUT_DIR / "findmyride.db"
CURRENT_DB_PATH = Path(DB_FILENAME)

# Target Asset Path
ASSET_DIR = Path("./app/composeApp/src/androidMain/assets")
ASSET_DB_PATH = ASSET_DIR / "findmyride.db"

# --- SCHEMA DEFINITION ---
SCHEMA_SQL = """
PRAGMA foreign_keys = ON;

CREATE TABLE "USER" (
  user_id       INTEGER PRIMARY KEY,
  email         TEXT    NOT NULL UNIQUE,
  username      TEXT    NOT NULL UNIQUE,
  password_hash TEXT    NOT NULL,
  phone_number  TEXT,
  role          TEXT    NOT NULL CHECK (role IN ('rider','driver','both')),
  rating_avg    REAL    DEFAULT 2.5 CHECK (rating_avg BETWEEN 0 AND 5),
  created_at    TEXT    NOT NULL DEFAULT (datetime('now'))
);

CREATE TABLE "VEHICLE" (
  vehicle_id     INTEGER PRIMARY KEY,
  owner_user_id  INTEGER NOT NULL,
  make           TEXT    NOT NULL,
  model          TEXT    NOT NULL,
  color          TEXT,
  plate          TEXT    NOT NULL UNIQUE,
  seats_total    INTEGER NOT NULL CHECK (seats_total BETWEEN 1 AND 8),
  year           INTEGER CHECK (year BETWEEN 1900 AND 2100),
  fun_fact       TEXT,
  FOREIGN KEY (owner_user_id) REFERENCES "USER"(user_id)
);

CREATE TABLE "LOCATION" (
  location_id INTEGER PRIMARY KEY,
  name        TEXT NOT NULL,
  address     TEXT NOT NULL
);

CREATE TABLE "RIDE_OFFER" (
  offer_id             INTEGER PRIMARY KEY,
  driver_id            INTEGER NOT NULL,
  vehicle_id           INTEGER NOT NULL,
  original_location_id INTEGER NOT NULL,
  dest_location_id     INTEGER NOT NULL,
  depart_at            TEXT    NOT NULL,
  seats_available      INTEGER NOT NULL CHECK (seats_available >= 0),
  price_base           NUMERIC(10,2) NOT NULL CHECK (price_base >= 0),
  price_per_mile       NUMERIC(10,2) NOT NULL CHECK (price_per_mile >= 0),
  status               TEXT NOT NULL CHECK (status IN ('open','closed','cancelled','full')),
  created_at           TEXT NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (driver_id)            REFERENCES "USER"(user_id),
  FOREIGN KEY (vehicle_id)           REFERENCES "VEHICLE"(vehicle_id),
  FOREIGN KEY (original_location_id) REFERENCES "LOCATION"(location_id),
  FOREIGN KEY (dest_location_id)     REFERENCES "LOCATION"(location_id)
);

CREATE TABLE "RIDE_REQUEST" (
  request_id          INTEGER PRIMARY KEY,
  rider_id            INTEGER NOT NULL,
  pickup_location_id  INTEGER NOT NULL,
  dropoff_location_id INTEGER NOT NULL,
  earliest_pickup     TEXT    NOT NULL,
  latest_pickup       TEXT,
  seats_needed        INTEGER NOT NULL CHECK (seats_needed >= 1),
  status              TEXT    NOT NULL CHECK (status IN ('open','matched','cancelled','expired')),
  created_at          TEXT    NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (rider_id)            REFERENCES "USER"(user_id),
  FOREIGN KEY (pickup_location_id)  REFERENCES "LOCATION"(location_id),
  FOREIGN KEY (dropoff_location_id) REFERENCES "LOCATION"(location_id),
  CHECK (latest_pickup IS NULL OR latest_pickup >= earliest_pickup)
);

CREATE TABLE "RIDE_MATCH" (
  match_id     INTEGER PRIMARY KEY,
  request_id   INTEGER NOT NULL,
  offer_id     INTEGER NOT NULL,
  seats_booked INTEGER NOT NULL CHECK (seats_booked >= 1),
  price_total  NUMERIC(10,2) NOT NULL CHECK (price_total >= 0),
  state        TEXT NOT NULL CHECK (state IN ('pending','confirmed','completed','cancelled','no_show')),
  matched_at   TEXT NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (request_id) REFERENCES "RIDE_REQUEST"(request_id),
  FOREIGN KEY (offer_id)   REFERENCES "RIDE_OFFER"(offer_id),
  UNIQUE (request_id, offer_id)
);

CREATE TABLE "RATING" (
  rating_id    INTEGER PRIMARY KEY,
  match_id     INTEGER NOT NULL,
  from_user_id INTEGER NOT NULL,
  to_user_id   INTEGER NOT NULL,
  stars        INTEGER NOT NULL CHECK (stars BETWEEN 1 AND 5),
  comment      TEXT,
  created_at   TEXT NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (match_id)     REFERENCES "RIDE_MATCH"(match_id),
  FOREIGN KEY (from_user_id) REFERENCES "USER"(user_id),
  FOREIGN KEY (to_user_id)   REFERENCES "USER"(user_id),
  CHECK (from_user_id <> to_user_id),
  UNIQUE (match_id, from_user_id, to_user_id)
);

CREATE TABLE "MESSAGE_THREAD" (
  thread_id     INTEGER PRIMARY KEY,
  user1_id      INTEGER NOT NULL,
  user2_id      INTEGER NOT NULL,
  ride_match_id INTEGER,
  created_at    TEXT NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (user1_id)      REFERENCES "USER"(user_id),
  FOREIGN KEY (user2_id)      REFERENCES "USER"(user_id),
  FOREIGN KEY (ride_match_id) REFERENCES "RIDE_MATCH"(match_id),
  CHECK (user1_id <> user2_id)
);

CREATE TABLE "MESSAGE" (
  message_id INTEGER PRIMARY KEY,
  thread_id  INTEGER NOT NULL,
  sender_id  INTEGER NOT NULL,
  body       TEXT    NOT NULL,
  sent_at    TEXT    NOT NULL DEFAULT (datetime('now')),
  FOREIGN KEY (thread_id) REFERENCES "MESSAGE_THREAD"(thread_id),
  FOREIGN KEY (sender_id) REFERENCES "USER"(user_id)
);
"""

# --- 1. HARDCODED DATA GENERATION ---

# Hardcoded Preexisting Users
users_existing = [
    (1, "abdul_bookwala", "abdul.bookwala1@example.edu", "8569861234", "driver", 4.72, "2025-10-02T00:00:00"),
    (2, "quincy_lu", "quincy.lu2@example.edu", "8569862334", "driver", 4.67, "2025-10-09T00:00:00"),
    (3, "ame_shabuse", "ame.shabuse3@example.edu", "8569861334", "rider", 4.55, "2025-10-02T00:00:00"),
    (4, "ame_lu", "ame.lu4@example.edu", "8569831234", "both", 4.54, "2025-10-10T00:00:00"),
    (5, "kennan_shajid", "kennan.shajid5@example.edu", "8569861234", "both", 4.70, "2025-10-01T00:00:00"),
    (1001, "driver.drexel", "driver.drexel@drexel.edu", "8560001001", "driver", 4.80, "2025-09-01 09:05:00"),
    (1002, "rider.drexel", "rider.drexel@drexel.edu", "8560001002", "rider", 3.50, "2025-09-02 10:00:00"),
    (1003, "both.guy", "both.guy@drexel.edu", "8560001003", "both", 4.20, "2025-09-03 11:00:00"),
    (1004, "chill.rider", "chill.rider@drexel.edu", "8560001004", "rider", 5.00, "2025-09-04 12:00:00"),
    (1005, "late.canceller", "late.canceller@drexel.edu", "8560001005", "rider", 1.90, "2025-09-05 13:00:00"),
]

# Hardcoded Preexisting Vehicles
vehicles_existing = [
    (1,2,"Tesla","Model Y","Blue","NJ-7909",7,2025,"n/a"),
    (2,5,"Tesla","Model 3","Gray","NJ-6737",5,2024,"n/a"),
    (3,6,"Toyota","Camry","Silver","NJ-8767",4,2015,"n/a"), 
    (4,8,"Chevrolet","Malibu","Red","NJ-8301",5,2021,"n/a"),
    (5,9,"Toyota","Camry","Blue","NJ-6823",4,2024,"n/a"),
    (6,14,"Honda","Civic","White","NJ-7519",5,2022,"n/a"),
    (100, 1, "Tesla", "Model 3", "Midnight Silver", "PA-ABDUL3", 5, 2024, "Always has lo-fi playing."),
    (101, 1, "Subaru", "Outback", "Forest Green", "PA-ABDULSUB", 5, 2023, "Perfect for Costco and grocery runs."),
    (102, 1001, "Honda", "Civic", "Blue", "PA-DRV222", 4, 2018, "Has a Drexel dragon sticker on the back."),
    (103, 1004, "Toyota", "RAV4", "White", "PA-RAV404", 5, 2022, "Trunk is full of CS textbooks."),
]

# Hardcoded Preexisting Locations
locations_existing = [
    (1,"Drexel Main Building","3141 Chestnut St"),
    (2,"Korman Center","3220-26 Woodland Walk"),
    (3,"University Crossings","3175 JFK Blvd"),
    (4,"30th Street Station","2955 Market St"),
    (5,"Queen Lane Campus","2900 Queen Ln"),
    (6,"Vidas Athletic Complex","43rd & Powelton"),
    (7,"Cira Green","129 S 30th St"),
    (8,"Wawa 34th Market","3400 Market St"),
    (9, "PISB - Papadakis Integrated Sciences Building", "3245 Chestnut St"),
    (10, "Drexel Recreation Center", "3301 Market St"),
    (11, "Liberty Place", "1625 Chestnut St"),
]

# Hardcoded Preexisting Message Threads
threads_existing = [(1, 1, 2, None, "2025-11-30 19:25:00")]

# Hardcoded Preexisting Messages
messages_existing = [
    (1, 1, 2, "Hey, are we still on for 5:30 PM?", "2025-11-30 03:03:42"),
    (2, 1, 1, "Yes, I will be there in 10 minutes.", "2025-11-30 03:03:42"),
    (3, 1, 2, "Hey Quincy, are we still on for 5:30 PM?", "2025-11-30 19:29:48"),
    (4, 1, 1, "Yes absolutely! Leaving now.", "2025-11-30 19:29:48"),
    (5, 1, 2, "Perfect, see you soon!", "2025-11-30 19:29:48"),
    (6, 1, 1, "Hey Abdul, do you still need a ride tomorrow?", "2025-11-30 19:29:51"),
    (7, 1, 2, "Yes please! Around 9 AM if possible.", "2025-11-30 19:29:51"),
    (8, 1, 1, "I ll be there.", "2025-11-30 19:29:51"),
    (9, 1, 2, "Got you!", "2025-11-30 19:29:51"),
    (10, 1, 1, "Thanks for the ride earlier!", "2025-11-30 19:29:55"),
    (11, 1, 2, "Anytime man!", "2025-11-30 19:29:55"),
    (12, 1, 1, "Left you a 5-star rating too :)", "2025-11-30 19:29:55"),
    (13, 1, 2, "Hey bro, heading out soon?", "2025-11-30 19:32:38"),
    (14, 1, 1, "Yeah give me 5 minutes.", "2025-11-30 19:32:38"),
    (15, 1, 2, "Bet, take your time.", "2025-11-30 19:32:38"),
    (16, 1, 1, "Traffic is crazy today.", "2025-11-30 19:32:38"),
    (17, 1, 2, "Fr bro, Drexel roads are wild.", "2025-11-30 19:32:38"),
    (18, 1, 1, "Want to grab food after?", "2025-11-30 19:32:38"),
    (19, 1, 2, "m down for halal cart.", "2025-11-30 19:32:38"),
    (20, 1, 1, "Say less.", "2025-11-30 19:32:38"),
    (21, 1, 2, "Almost downstairs.", "2025-11-30 19:32:38"),
    (22, 1, 1, "I see you.", "2025-11-30 19:32:38"),
    (23, 1, 2, "Don t forget the aux today.", "2025-11-30 19:32:38"),
    (24, 1, 1, "Haha alright.", "2025-11-30 19:32:38"),
    (25, 1, 2, "Let s go.", "2025-11-30 19:32:38"),
    (26, 1, 1, "On my way now.", "2025-11-30 19:32:38"),
    (27, 1, 2, "You driving today?", "2025-11-30 19:32:38"),
    (28, 1, 1, "Yeah I got it.", "2025-11-30 19:32:38"),
    (29, 1, 2, "Fire.", "2025-11-30 19:32:38"),
    (30, 1, 1, "Good morning! Still need a ride?", "2025-11-30 19:32:38"),
    (31, 1, 2, "Yes please! Appreciate you.", "2025-11-30 19:32:38"),
    (32, 1, 1, "Leaving in 10.", "2025-11-30 19:32:38"),
    (33, 1, 2, "ll meet you outside.", "2025-11-30 19:32:38"),
    (34, 1, 1, "Perfect.", "2025-11-30 19:32:38"),
    (35, 1, 2, "Did you do the homework for CS 281?", "2025-11-30 19:32:38"),
    (36, 1, 1, "Barely, that class is wild.", "2025-11-30 19:32:38"),
    (37, 1, 2, "Facts bro.", "2025-11-30 19:32:38"),
    (38, 1, 1, "ll quiz you in the car lol.", "2025-11-30 19:32:38"),
    (39, 1, 2, "Bet.", "2025-11-30 19:32:38"),
    (40, 1, 1, "m by the curb.", "2025-11-30 19:32:38"),
    (41, 1, 2, "Coming now.", "2025-11-30 19:32:38"),
    (42, 1, 1, "You want coffee?", "2025-11-30 19:32:38"),
    (43, 1, 2, "Nah I m good, thanks.", "2025-11-30 19:32:38"),
    (44, 1, 1, "Alright see you.", "2025-11-30 19:32:38"),
    (45, 1, 2, "Almost there.", "2025-11-30 19:32:38"),
    (46, 1, 1, "Thanks again for covering me last ride bro.", "2025-11-30 19:32:38"),
    (47, 1, 2, "No problem at all.", "2025-11-30 19:32:38"),
    (48, 1, 1, "You good for later today?", "2025-11-30 19:32:38"),
    (49, 1, 2, "Yep, same time as usual.", "2025-11-30 19:32:38"),
    (50, 1, 1, "Bet.", "2025-11-30 19:32:38"),
    (51, 1, 2, "You wanna stop at Wawa after?", "2025-11-30 19:32:38"),
    (52, 1, 1, "Always bro.", "2025-11-30 19:32:38"),
    (53, 1, 2, "Haha say less.", "2025-11-30 19:32:38"),
    (54, 1, 1, "You see the game last night?", "2025-11-30 19:32:38"),
    (55, 1, 2, "Bro Embiid is insane.", "2025-11-30 19:32:38"),
    (56, 1, 1, "MVP season.", "2025-11-30 19:32:38"),
    (57, 1, 2, "100%.", "2025-11-30 19:32:38"),
    (58, 1, 1, "Heading out now.", "2025-11-30 19:32:38"),
    (59, 1, 2, "m already outside.", "2025-11-30 19:32:38"),
    (60, 1, 1, "Coming.", "2025-11-30 19:32:38"),
]

# Hardcoded Preexisting Ride Offers
ride_offers_existing = [
    (1,5,3,3,8,"2025-10-16T01:45:00",2,4.17,0.72,"closed","2025-10-15T01:45:00"),
    (2,1,6,6,10,"2025-10-15T18:45:00",1,5.31,0.86,"closed","2025-10-14T18:45:00"),
    (3,12,1,2,3,"2025-10-14T13:00:00",2,4.63,1.13,"open","2025-10-13T13:00:00"),
    (4,6,2,3,8,"2025-10-14T17:45:00",3,8.52,0.9,"closed","2025-10-13T17:45:00"),
]

# Hardcoded Preexisting Ride Requests
ride_requests_existing = [
    (1,11,7,10,"2025-10-14T19:05:00","2025-10-14T19:45:00",1,"cancelled","2025-10-13T19:15:00"),
    (2,10,6,8,"2025-10-14T13:15:00","2025-10-14T13:25:00",1,"open","2025-10-13T13:15:00"),
    (3,7,1,9,"2025-10-14T07:20:00","2025-10-14T07:40:00",1,"matched","2025-10-13T07:30:00"),
    (4,13,6,7,"2025-10-13T23:50:00","2025-10-14T00:30:00",2,"matched","2025-10-13T00:00:00"),
    (5,10,7,2,"2025-10-15T21:45:00","2025-10-15T21:55:00",1,"open","2025-10-14T21:45:00"),
    (6,11,3,2,"2025-10-15T03:00:00","2025-10-15T03:10:00",1,"cancelled","2025-10-14T03:00:00"),
]

# Hardcoded Preexisting Ride Matches
matches_existing = [
    (1,3,3,1,11.71,"completed","2025-10-13T13:30:00"),
]

# Hardcoded Preexisting Ratings
ratings_existing = [
    (1,1,2,12,5,"Friendly","2025-10-13T15:30:00"),
]

# --- RANDOM MOCK DATA GENERATION LOGIC ---
EXTRA = 100
start_user_id = 6
start_location_id = 12
start_offer_id = max(o[0] for o in ride_offers_existing) + 1
start_request_id = max(r[0] for r in ride_requests_existing) + 1
start_match_id = max(m[0] for m in matches_existing) + 1
start_rating_id = max(r[0] for r in ratings_existing) + 1

first_names = ["Alex","Jordan","Taylor","Morgan","Cameron","Riley","Casey","Jamie","Avery","Sam","Sydney","Charlie","Drew","Logan","Peyton","Harper","Blake","Quinn","Rowan","Elliot","Maria","Jamal","Priya","Diego","Lina","Marcus"]
last_names = ["Smith","Johnson","Williams","Brown","Jones","Miller","Davis","Garcia","Rodriguez","Wilson","Martinez","Anderson","Taylor","Thomas","Hernandez","Moore","Martin","Jackson","Thompson","White"]
car_makes_models = [("Toyota","Camry"),("Honda","Civic"),("Tesla","Model 3"),("Tesla","Model Y"),("Chevrolet","Malibu"),("Hyundai","Elantra"),("Ford","Focus"),("Nissan","Altima")]
colors = ["Blue","Gray","Silver","Red","Black","White","Green","Gold","Maroon"]
roles = ["rider","driver","both"]

def rand_datetime_oct13_to_nov30():
    base = datetime.datetime(2025,10,13)
    delta = datetime.timedelta(days=random.randint(0,48), hours=random.randint(0,23), minutes=random.randint(0,59))
    return (base + delta).isoformat(timespec="seconds")

# Users
users_generated = []
for i in range(EXTRA):
    uid = start_user_id + i
    fn = random.choice(first_names)
    ln = random.choice(last_names)
    username = f"{fn.lower()}.{ln.lower()}{uid}"
    email = f"{fn.lower()}.{ln.lower()}{uid}@drexel.edu"
    phone = f"215{random.randint(2000000,9999999)}"
    role = random.choices(roles, weights=[0.45,0.40,0.15], k=1)[0]
    rating_avg = round(max(2.5, min(5.0, random.gauss(4.4,0.4))),2)
    created_at = (datetime.datetime(2025,10,1) + datetime.timedelta(days=random.randint(0,60))).isoformat(timespec="seconds")
    users_generated.append((uid, username, email, phone, role, rating_avg, created_at))
users_all = users_existing + users_generated

# Locations
locations_generated = []
for i in range(EXTRA):
    lid = start_location_id + i
    name = f"Philly Point {lid}"
    address = f"{random.randint(100,3999)} Market St"
    locations_generated.append((lid, name, address))
locations_all = locations_existing + locations_generated

# Vehicles
driver_candidates = [u for u in users_all if u[4] in ("driver","both")]
if len(driver_candidates) < 40:
    for j in range(30):
        idx = j % len(users_generated)
        u = users_generated[idx]
        users_generated[idx] = (u[0], u[1], u[2], u[3], "driver", u[5], u[6])
    users_all = users_existing + users_generated
    driver_candidates = [u for u in users_all if u[4] in ("driver","both")]

used_vids = set(v[0] for v in vehicles_existing)
vehicles_generated = []
vid_counter = 7
generated_count = 0
while generated_count < EXTRA:
    if vid_counter not in used_vids:
        owner = random.choice(driver_candidates)[0]
        make, model = random.choice(car_makes_models)
        color = random.choice(colors)
        plate = f"NJ-{1000 + vid_counter:04d}"
        seats = random.choice([4,4,5,5,6])
        year = random.randint(2010,2025)
        fun_fact = random.choice(["n/a","student vehicle","rideshare-ready","garage kept"])
        vehicles_generated.append((vid_counter, owner, make, model, color, plate, seats, year, fun_fact))
        generated_count += 1
    vid_counter += 1
vehicles_all = vehicles_existing + vehicles_generated

# Offers
offers_generated = []
for i in range(EXTRA):
    oid = start_offer_id + i
    driver = random.choice(driver_candidates)[0]
    owned = [v for v in vehicles_all if v[1] == driver]
    vehicle_id = random.choice(owned)[0] if owned else random.choice(vehicles_all)[0]
    origin = random.choice(locations_all)[0]
    dest = random.choice(locations_all)[0]
    if origin == dest:
        dest = (dest % (start_location_id + EXTRA - 1)) + 1
    depart_at = rand_datetime_oct13_to_nov30()
    seats_available = random.choice([1,1,2,2,3,4])
    price_base = round(random.uniform(3.0,9.0),2)
    price_per_mile = round(random.uniform(0.5,1.6),2)
    status = random.choices(["open","closed"], weights=[0.6,0.4])[0]
    created_at = (datetime.datetime.fromisoformat(depart_at) - datetime.timedelta(days=random.randint(0,7))).isoformat(timespec="seconds")
    offers_generated.append((oid, driver, vehicle_id, origin, dest, depart_at, seats_available, price_base, price_per_mile, status, created_at))
offers_all = ride_offers_existing + offers_generated

# Requests
riders = [u for u in users_all if u[4] in ("rider","both")]
requests_generated = []
for i in range(EXTRA):
    rid = start_request_id + i
    rider = random.choice(riders)[0]
    pickup = random.choice(locations_all)[0]
    dropoff = random.choice(locations_all)[0]
    if pickup == dropoff:
        dropoff = (dropoff % (start_location_id + EXTRA - 1)) + 1
    earliest_dt = datetime.datetime(2025,10,13) + datetime.timedelta(days=random.randint(0,48), hours=random.randint(6,20), minutes=random.randint(0,59))
    latest_dt = earliest_dt + datetime.timedelta(minutes=random.randint(10,60))
    seats_needed = random.choice([1,1,2])
    status = random.choices(["open","matched","cancelled"], weights=[0.55,0.35,0.10])[0]
    created_at = (earliest_dt - datetime.timedelta(days=random.randint(0,5))).isoformat(timespec="seconds")
    requests_generated.append((rid, rider, pickup, dropoff, earliest_dt.isoformat(timespec="seconds"), latest_dt.isoformat(timespec="seconds"), seats_needed, status, created_at))
requests_all = ride_requests_existing + requests_generated

# Matches
matches_generated = []
match_id = start_match_id
for req in requests_generated:
    if random.random() < 0.6:
        candidates = [o for o in offers_all if o[6] >= req[6]]
        if not candidates:
            continue
        offer = random.choice(candidates)
        seats_booked = min(offer[6], req[6])
        miles = random.uniform(2.0,12.0)
        price_total = round(offer[7] + offer[8]*miles, 2) if isinstance(offer[7], float) else round(offer[7] + offer[8]*miles,2)
        state = random.choices(["confirmed","completed","cancelled","no_show"], weights=[0.4,0.35,0.15,0.10])[0]
        matched_at = (datetime.datetime.fromisoformat(req[4]) - datetime.timedelta(hours=random.randint(0,4))).isoformat(timespec="seconds")
        matches_generated.append((match_id, req[0], offer[0], seats_booked, price_total, state, matched_at))
        match_id += 1
matches_all = matches_existing + matches_generated

# Ratings
ratings_generated = []
rating_id = start_rating_id
for m in matches_generated:
    if m[5] == "completed" and random.random() < 0.85:
        req = next((r for r in requests_all if r[0] == m[1]), None)
        offer = next((o for o in offers_all if o[0] == m[2]), None)
        if not req or not offer:
            continue
        from_user = req[1]
        to_user = offer[1]
        if from_user == to_user:
            continue
        stars = random.choices([5,4,3,2,1], weights=[0.6,0.25,0.08,0.04,0.03])[0]
        comment = random.choice(["Great ride","Friendly driver","On time","Would ride again","Car was clean","Driver was late","Helpful with bags"])
        created_at = (datetime.datetime.fromisoformat(m[6]) + datetime.timedelta(hours=random.randint(1,48))).isoformat(timespec="seconds")
        ratings_generated.append((rating_id, m[0], from_user, to_user, stars, comment, created_at))
        rating_id += 1

cleaned_ratings = []
seen_pairs = set()
for r in ratings_generated:
    rid, match_id, from_user, to_user, stars, comment, created_at = r
    if from_user == to_user:
        continue
    pair_key = (from_user, to_user)
    if pair_key in seen_pairs:
        continue
    seen_pairs.add(pair_key)
    cleaned_ratings.append(r)
ratings_generated = cleaned_ratings
ratings_all = ratings_existing + ratings_generated


# --- 2. WRITING FILES ---
def sql_quote(val):
    if val is None:
        return "NULL"
    if isinstance(val, str):
        return "'" + val.replace("'", "''") + "'"
    return str(val)

# Write schema.sql
with open(SCHEMA_PATH, 'w', encoding='utf-8') as f:
    f.write(SCHEMA_SQL)
print(f"Created schema file: {SCHEMA_PATH}")

# Write populate.sql
with open(POPULATE_PATH, 'w', encoding='utf-8') as f:
    f.write("-- USERS\n")
    for u in users_all:
        f.write("INSERT INTO USER (user_id,username,email,password_hash,phone_number,role,rating_avg,created_at) VALUES (%s,%s,%s,%s,%s,%s,%s,%s);\n" % (
            sql_quote(u[0]), sql_quote(u[1]), sql_quote(u[2]), "'x'", sql_quote(u[3]), sql_quote(u[4]), sql_quote(u[5]), sql_quote(u[6])
        ))
    f.write("\n-- VEHICLE\n")
    for v in vehicles_all:
        f.write("INSERT INTO VEHICLE (vehicle_id,owner_user_id,make,model,color,plate,seats_total,year,fun_fact) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s);\n" % tuple(sql_quote(x) for x in v))
    f.write("\n-- LOCATION\n")
    for loc in locations_all:
        f.write("INSERT INTO LOCATION (location_id,name,address) VALUES (%s,%s,%s);\n" % (sql_quote(loc[0]), sql_quote(loc[1]), sql_quote(loc[2])))
    f.write("\n-- RIDE_OFFER\n")
    for o in offers_all:
        f.write("INSERT INTO RIDE_OFFER (offer_id,driver_id,vehicle_id,original_location_id,dest_location_id,depart_at,seats_available,price_base,price_per_mile,status,created_at) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);\n" % (
            sql_quote(o[0]), sql_quote(o[1]), sql_quote(o[2]), sql_quote(o[3]), sql_quote(o[4]), sql_quote(o[5]), sql_quote(o[6]), sql_quote(o[7]), sql_quote(o[8]), sql_quote(o[9]), sql_quote(o[10])
        ))
    f.write("\n-- RIDE_REQUEST\n")
    for r in requests_all:
        f.write("INSERT INTO RIDE_REQUEST (request_id,rider_id,pickup_location_id,dropoff_location_id,earliest_pickup,latest_pickup,seats_needed,status,created_at) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s);\n" % (
            sql_quote(r[0]), sql_quote(r[1]), sql_quote(r[2]), sql_quote(r[3]), sql_quote(r[4]), sql_quote(r[5]), sql_quote(r[6]), sql_quote(r[7]), sql_quote(r[8])
        ))
    f.write("\n-- RIDE_MATCH\n")
    for m in matches_all:
        f.write("INSERT INTO RIDE_MATCH (match_id,request_id,offer_id,seats_booked,price_total,state,matched_at) VALUES (%s,%s,%s,%s,%s,%s,%s);\n" % (
            sql_quote(m[0]), sql_quote(m[1]), sql_quote(m[2]), sql_quote(m[3]), sql_quote(m[4]), sql_quote(m[5]), sql_quote(m[6])
        ))
    f.write("\n-- RATING\n")
    for rt in ratings_all:
        f.write("INSERT INTO RATING (rating_id,match_id,from_user_id,to_user_id,stars,comment,created_at) VALUES (%s,%s,%s,%s,%s,%s,%s);\n" % (
            sql_quote(rt[0]), sql_quote(rt[1]), sql_quote(rt[2]), sql_quote(rt[3]), sql_quote(rt[4]), sql_quote(rt[5]), sql_quote(rt[6])
        ))
    f.write("\n-- MESSAGE_THREAD\n")
    for th in threads_existing:
        f.write("INSERT INTO MESSAGE_THREAD (thread_id,user1_id,user2_id,ride_match_id,created_at) VALUES (%s,%s,%s,%s,%s);\n" % (
            sql_quote(th[0]), sql_quote(th[1]), sql_quote(th[2]), sql_quote(th[3]), sql_quote(th[4])
        ))
    f.write("\n-- MESSAGE\n")
    for msg in messages_existing:
        f.write("INSERT INTO MESSAGE (message_id,thread_id,sender_id,body,sent_at) VALUES (%s,%s,%s,%s,%s);\n" % (
            sql_quote(msg[0]), sql_quote(msg[1]), sql_quote(msg[2]), sql_quote(msg[3]), sql_quote(msg[4])
        ))
print(f"Created data file: {POPULATE_PATH}")


# --- 3. BUILD DATABASE ---
if os.path.exists(CURRENT_DB_PATH):
    os.remove(CURRENT_DB_PATH)
    print(f"Removed existing {CURRENT_DB_PATH} to start fresh.")

print(f"Creating new {CURRENT_DB_PATH}...")
conn = sqlite3.connect(CURRENT_DB_PATH)

# Read and execute schema.sql against the database
with open(SCHEMA_PATH, 'r', encoding='utf-8') as f:
    schema_script = f.read()
    conn.executescript(schema_script)
    print("Executed schema.sql successfully.")

# Read and execute populate.sql against the database
with open(POPULATE_PATH, 'r', encoding='utf-8') as f:
    populate_script = f.read()
    conn.executescript(populate_script)
    print("Executed populate.sql successfully.")

conn.commit()
conn.close()
print("Done! Database initialized and populated with mock data.")

# --- 4. COPY TO ASSETS ---
print(f"Copying database to {ASSET_DB_PATH}...")
shutil.copy2(CURRENT_DB_PATH, ASSET_DB_PATH)
print("Database copied successfully.")