PRAGMA foreign_keys = ON; -- Enforces foreign key constraints

-- USER Table For Users
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

-- VEHICLE Table for Vehicles
CREATE TABLE "VEHICLE" (
  vehicle_id     INTEGER PRIMARY KEY,
  owner_user_id  INTEGER NOT NULL,
  make           TEXT    NOT NULL,
  model          TEXT    NOT NULL,
  color          TEXT,
  plate          TEXT    NOT NULL UNIQUE,
  seats_total    INTEGER NOT NULL CHECK (seats_total BETWEEN 1 AND 8),
  year           INTEGER CHECK (year BETWEEN 1900 AND 2100), -- We did 2100 because why not keeping it future proof
  fun_fact       TEXT, -- Just for fun to add some personality for each vehicle
  FOREIGN KEY (owner_user_id) REFERENCES "USER"(user_id)
);

-- LOCATION Table for Locations
CREATE TABLE "LOCATION" (
  location_id INTEGER PRIMARY KEY,
  name        TEXT NOT NULL,
  address     TEXT NOT NULL
);

-- RIDE_OFFER Table for Ride Offers
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

-- RIDE_REQUEST Table for Ride Requests
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

-- RIDE_MATCH Table for Matches between Requests and Offers
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

-- RATING Table for User Ratings
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
