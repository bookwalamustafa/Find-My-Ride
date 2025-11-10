#!/usr/bin/env python3
"""
generate_seed.py
Generates sql/seed_full.sql with realistic synthetic data for Find-My-Ride.
"""

import os, random, datetime
from pathlib import Path

random.seed(42)

OUT_DIR = Path(".")
OUT_DIR.mkdir(parents=True, exist_ok=True)
SQL_PATH = OUT_DIR / "populate.sql"

# --- keep existing sample rows  ---
users_existing = [
    # (user_id, full_name_or_username, email, phone, role, rating_avg, created_at)
    (1, "abdul_bookwala", "abdul.bookwala1@example.edu", "8569861234", "driver", 4.72, "2025-10-02T00:00:00"),
    (2, "quincy_lu", "quincy.lu2@example.edu", "8569862334", "driver", 4.67, "2025-10-09T00:00:00"),
    (3, "ame_shabuse", "ame.shabuse3@example.edu", "8569861334", "rider", 4.55, "2025-10-02T00:00:00"),
    (4, "ame_lu", "ame.lu4@example.edu", "8569831234", "both", 4.54, "2025-10-10T00:00:00"),
    (5, "kennan_shajid", "kennan.shajid5@example.edu", "8569861234", "both", 4.70, "2025-10-01T00:00:00"),
]

vehicles_existing = [
    # (vehicle_id, owner_user_id, make, model, color, plate, seats_total, year, fun_fact)
    (1,2,"Tesla","Model Y","Blue","NJ-7909",7,2025,"n/a"),
    (2,5,"Tesla","Model 3","Gray","NJ-6737",5,2024,"n/a"),
    (3,6,"Toyota","Camry","Silver","NJ-8767",4,2015,"n/a"),
    (4,8,"Chevrolet","Malibu","Red","NJ-8301",5,2021,"n/a"),
    (5,9,"Toyota","Camry","Blue","NJ-6823",4,2024,"n/a"),
    (6,14,"Honda","Civic","White","NJ-7519",5,2022,"n/a"),
]

locations_existing = [
    (1,"Drexel Main Building","3141 Chestnut St"),
    (2,"Korman Center","3220-26 Woodland Walk"),
    (3,"University Crossings","3175 JFK Blvd"),
    (4,"30th Street Station","2955 Market St"),
    (5,"Queen Lane Campus","2900 Queen Ln"),
    (6,"Vidas Athletic Complex","43rd & Powelton"),
    (7,"Cira Green","129 S 30th St"),
    (8,"Wawa 34th Market","3400 Market St"),
]

ride_offers_existing = [
    # (offer_id,driver_id,vehicle_id,origin_location_id,dest_location_id,depart_at,seats_available,price_base,price_per_mile,status,created_at)
    (1,5,3,3,8,"2025-10-16T01:45:00",2,4.17,0.72,"closed","2025-10-15T01:45:00"),
    (2,1,6,6,10,"2025-10-15T18:45:00",1,5.31,0.86,"closed","2025-10-14T18:45:00"),
    (3,12,1,2,3,"2025-10-14T13:00:00",2,4.63,1.13,"open","2025-10-13T13:00:00"),
    (4,6,2,3,8,"2025-10-14T17:45:00",3,8.52,0.9,"closed","2025-10-13T17:45:00"),
]

ride_requests_existing = [
    # (request_id, rider_id, pickup_location_id, dropoff_location_id, earliest_pickup, latest_pickup, seats_needed, status, created_at)
    (1,11,7,10,"2025-10-14T19:05:00","2025-10-14T19:45:00",1,"cancelled","2025-10-13T19:15:00"),
    (2,10,6,8,"2025-10-14T13:15:00","2025-10-14T13:25:00",1,"open","2025-10-13T13:15:00"),
    (3,7,1,9,"2025-10-14T07:20:00","2025-10-14T07:40:00",1,"matched","2025-10-13T07:30:00"),
    (4,13,6,7,"2025-10-13T23:50:00","2025-10-14T00:30:00",2,"matched","2025-10-13T00:00:00"),
    (5,10,7,2,"2025-10-15T21:45:00","2025-10-15T21:55:00",1,"open","2025-10-14T21:45:00"),
    (6,11,3,2,"2025-10-15T03:00:00","2025-10-15T03:10:00",1,"cancelled","2025-10-14T03:00:00"),
]

matches_existing = [
    # (match_id,request_id,offer_id,seats_booked,price_total,state,matched_at)
    (1,3,3,1,11.71,"completed","2025-10-13T13:30:00"),
    (2,4,7,1,7.95,"cancelled","2025-10-13T06:00:00"),
    (3,8,16,2,14.77,"confirmed","2025-10-15T02:00:00"),
    (4,9,16,1,15.16,"no_show","2025-10-13T08:00:00"),
    (5,15,2,1,11.01,"completed","2025-10-13T16:45:00"),
    (6,21,3,1,10.23,"no_show","2025-10-13T23:45:00"),
]

ratings_existing = [
    # (rating_id,match_id,from_user_id,to_user_id,stars,comment,created_at)
    (1,1,2,12,5,"Friendly","2025-10-13T15:30:00"),
    (3,8,9,4,5,"Great ride","2025-10-13T04:45:00"),
]

# --- generation parameters ---
EXTRA = 100  # number of rows to add per table
start_user_id = max(u[0] for u in users_existing) + 1  # 6
start_vehicle_id = max((v[0] for v in vehicles_existing), default=0) + 1
start_location_id = max(l[0] for l in locations_existing) + 1
start_offer_id = max(o[0] for o in ride_offers_existing) + 1
start_request_id = max(r[0] for r in ride_requests_existing) + 1
start_match_id = max(m[0] for m in matches_existing) + 1
start_rating_id = max(r[0] for r in ratings_existing) + 1

first_names = ["Alex","Jordan","Taylor","Morgan","Cameron","Riley","Casey","Jamie","Avery","Sam","Sydney","Charlie","Drew","Logan","Peyton","Harper","Blake","Quinn","Rowan","Elliot","Maria","Jamal","Priya","Diego","Lina","Marcus"]
last_names = ["Smith","Johnson","Williams","Brown","Jones","Miller","Davis","Garcia","Rodriguez","Wilson","Martinez","Anderson","Taylor","Thomas","Hernandez","Moore","Martin","Jackson","Thompson","White"]
car_makes_models = [("Toyota","Camry"),("Honda","Civic"),("Tesla","Model 3"),("Tesla","Model Y"),("Chevrolet","Malibu"),("Hyundai","Elantra"),("Ford","Focus"),("Nissan","Altima")]
colors = ["Blue","Gray","Silver","Red","Black","White","Green","Gold","Maroon"]
roles = ["rider","driver","both"]

# helper to produce ISO timestamps in Oct 2025 window
def rand_datetime_oct13_to_nov30():
    base = datetime.datetime(2025,10,13)
    delta = datetime.timedelta(days=random.randint(0,48), hours=random.randint(0,23), minutes=random.randint(0,59))
    return (base + delta).isoformat(timespec="seconds")

# --- generate users ---
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
    users_generated.append((uid, email, username, "x", phone, role, rating_avg, created_at))

users_all = users_existing + users_generated

# --- generate locations ---
locations_generated = []
for i in range(EXTRA):
    lid = start_location_id + i
    name = f"Philly Point {lid}"
    address = f"{random.randint(100,3999)} Market St"
    locations_generated.append((lid, name, address))
locations_all = locations_existing + locations_generated

# --- generate vehicles (assign to drivers/both) ---
driver_candidates = [u for u in users_all if u[5] in ("driver","both")]
# ensure enough drivers by converting some generated users if needed
if len(driver_candidates) < 40:
    for j in range(30):
        idx = j % len(users_generated)
        u = users_generated[idx]
        users_generated[idx] = (u[0], u[1], u[2], u[3], u[4], "driver", u[6], u[7])
    users_all = users_existing + users_generated
    driver_candidates = [u for u in users_all if u[5] in ("driver","both")]

vehicles_generated = []
for i in range(EXTRA):
    vid = start_vehicle_id + i
    owner = random.choice(driver_candidates)[0]
    make, model = random.choice(car_makes_models)
    color = random.choice(colors)
    plate = f"NJ-{1000 + vid:04d}"
    seats = random.choice([4,4,5,5,6])
    year = random.randint(2010,2025)
    fun_fact = random.choice(["n/a","student vehicle","rideshare-ready","garage kept"])
    vehicles_generated.append((vid, owner, make, model, color, plate, seats, year, fun_fact))
vehicles_all = vehicles_existing + vehicles_generated

# --- generate ride_offers ---
offers_generated = []
for i in range(EXTRA):
    oid = start_offer_id + i
    driver = random.choice(driver_candidates)[0]
    # pick a vehicle by this owner or random
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

# --- generate ride_requests ---
riders = [u for u in users_all if u[5] in ("rider","both")]
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

# --- generate matches by trying to align requests to offers ---
matches_generated = []
match_id = start_match_id
# simple matching: randomly attempt to pair some requests to offers
for req in requests_generated:
    if random.random() < 0.6:  # 60% of generated requests get a match
        # pick a candidate offer where seats >= seats_needed
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

# --- generate ratings for completed matches ---
ratings_generated = []
rating_id = start_rating_id
for m in matches_generated:
    if m[5] == "completed" and random.random() < 0.85:
        # find from_user (rider) and to_user (driver)
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

# --- Clean up ratings to ensure no self-ratings or duplicates ---
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

# --- write SQL file ---
def sql_quote(val):
    if val is None:
        return "NULL"
    if isinstance(val, str):
        return "'" + val.replace("'", "''") + "'"
    return str(val)

with open(SQL_PATH, "w", encoding="utf-8") as f:
    f.write("-- USERS\n")
    for u in users_all:
        if len(u) == 7:
            # older sample format (id, fullname, email, phone, role, rating, created_at)
            uid, uname, email, phone, role, rating, created = u if len(u) == 7 else (u[0], u[2], u[1], u[3], u[4], u[5], u[6])
            # if the sample stored full name, ensure username is simple
            # but our existing list is normalized so treat generically:
            f.write("INSERT INTO USER (user_id,email,username,password_hash,phone_number,role,rating_avg,created_at) VALUES (%s,%s,%s,%s,%s,%s,%s,%s);\n" % (
                sql_quote(uid), sql_quote(email), sql_quote(uname), sql_quote("x"), sql_quote(phone), sql_quote(role), sql_quote(rating), sql_quote(created)
            ))
        else:
            # generated entries: (uid, email, username, pw, phone, role, rating, created_at)
            uid, email, uname, pw, phone, role, rating, created = u
            f.write("INSERT INTO USER (user_id,email,username,password_hash,phone_number,role,rating_avg,created_at) VALUES (%s,%s,%s,%s,%s,%s,%s,%s);\n" % (
                sql_quote(uid), sql_quote(email), sql_quote(uname), sql_quote(pw), sql_quote(phone), sql_quote(role), sql_quote(rating), sql_quote(created)
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

print("Wrote SQL to:", SQL_PATH)
print("Row counts (approx): USERS=%d VEHICLE=%d LOCATION=%d RIDE_OFFER=%d RIDE_REQUEST=%d RIDE_MATCH=%d RATING=%d" % (
    len(users_all), len(vehicles_all), len(locations_all), len(offers_all), len(requests_all), len(matches_all), len(ratings_all)
))