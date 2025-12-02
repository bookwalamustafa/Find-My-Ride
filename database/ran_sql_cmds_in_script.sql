-- =====================================================================
-- SQL used at runtime by the Find-My-Ride application
-- =====================================================================

-- ---------------------------------------------------------------------
-- Source: app/composeApp/src/androidMain/kotlin/com/example/demo/AndroidAuthRepository.kt
-- ---------------------------------------------------------------------

-- Login: check email + password
SELECT user_id, email, username, role
FROM "USER"
WHERE email = ? AND password_hash = ?;

-- Check if email already exists (used in registration & login)
SELECT 1 FROM "USER" WHERE email = ? LIMIT 1;

-- Register new user (default role = 'rider')
INSERT INTO "USER"(email, username, password_hash, role)
VALUES(?, ?, ?, 'rider');

-- (Second use of the same existence check)
SELECT 1 FROM "USER" WHERE email = ? LIMIT 1;



-- ---------------------------------------------------------------------
-- Source: app/composeApp/src/androidMain/kotlin/com/example/demo/AndroidMessagesRepository.kt
-- Was used for debugging errors in script execution
-- ---------------------------------------------------------------------

-- Seed message threads (demo data)
INSERT INTO MESSAGE_THREAD (thread_id, user1_id, user2_id)
VALUES
    (1, 1, 2),
    (2, 1, 3),
    (3, 1, 4);

-- Seed messages - Thread 1: Abdul <-> Quincy
INSERT INTO MESSAGE (thread_id, sender_id, body) VALUES
                                                     (1, 1, 'Hey Quincy, are we still on for 5:30 PM?'),
                                                     (1, 2, 'Yes! I''ll be there in 10 minutes.'),
                                                     (1, 1, 'Perfect, see you soon.');

-- Seed messages - Thread 2: Abdul <-> Ame
INSERT INTO MESSAGE (thread_id, sender_id, body) VALUES
                                                     (2, 3, 'Hey Abdul, do you still need a ride tomorrow?'),
                                                     (2, 1, 'Yeah! Morning around 9 would be amazing.'),
                                                     (2, 3, 'Got you, I''ll swing by then.');

-- Seed messages - Thread 3: Abdul <-> Kennan
INSERT INTO MESSAGE (thread_id, sender_id, body) VALUES
                                                     (3, 1, 'Thanks again for the last ride!'),
                                                     (3, 4, 'No problem, happy to help.'),
                                                     (3, 1, 'I left you a 5-star rating too :)');

-- Load all message threads for the logged-in user (with last message preview)
SELECT
    t.thread_id,
    CASE
        WHEN t.user1_id = ? THEN u2.username
        ELSE u1.username
        END AS contact_name,
    COALESCE(last_msg.body, 'No messages yet')  AS last_message,
    last_msg.sent_at AS last_sent_at
FROM MESSAGE_THREAD t
         JOIN "USER" u1 ON t.user1_id = u1.user_id
         JOIN "USER" u2 ON t.user2_id = u2.user_id
         LEFT JOIN MESSAGE last_msg ON last_msg.message_id = (
    SELECT m.message_id
    FROM MESSAGE m
    WHERE m.thread_id = t.thread_id
    ORDER BY m.sent_at DESC, m.message_id DESC
    LIMIT 1
    )
WHERE t.user1_id = ? OR t.user2_id = ?
ORDER BY
    (last_sent_at IS NULL),
    last_sent_at DESC,
    t.thread_id DESC;

-- Load all messages inside a specific thread
SELECT message_id, sender_id, body, sent_at
FROM MESSAGE
WHERE thread_id = ?
ORDER BY sent_at ASC, message_id ASC;

-- Insert a new chat message
INSERT INTO MESSAGE(thread_id, sender_id, body)
VALUES (?, ?, ?);

-- Get sent_at for a specific message (used after insert)
SELECT sent_at
FROM MESSAGE
WHERE message_id = ?;



-- ---------------------------------------------------------------------
-- Source: app/composeApp/src/androidMain/kotlin/com/example/demo/AndroidProfileRepository.kt
-- ---------------------------------------------------------------------

-- Load main profile info for the logged-in user
SELECT username, email, phone_number, rating_avg
FROM "USER"
WHERE user_id = ?
    LIMIT 1;

-- Load all vehicles owned by logged-in user
SELECT vehicle_id, make, model, color, plate, seats_total, year, fun_fact
FROM "VEHICLE"
WHERE owner_user_id = ?
ORDER BY vehicle_id;

-- Update profile fields (username, email, phone number)
UPDATE "USER"
SET username = ?, email = ?, phone_number = ?
WHERE user_id = ?;

-- Get all vehicle IDs for current user (used to compute inserts/updates/deletes)
SELECT vehicle_id
FROM "VEHICLE"
WHERE owner_user_id = ?;

-- Update an existing vehicle
UPDATE "VEHICLE"
SET make = ?, model = ?, color = ?, plate = ?,
    seats_total = ?, year = ?, fun_fact = ?
WHERE vehicle_id = ?;

-- Insert a new vehicle
INSERT INTO "VEHICLE"(
    vehicle_id, owner_user_id, make, model, color,
    plate, seats_total, year, fun_fact
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);

-- Delete a vehicle
DELETE FROM "VEHICLE" WHERE vehicle_id = ?;



-- ---------------------------------------------------------------------
-- Source: app/composeApp/src/androidMain/kotlin/com/example/demo/AndroidRideRepository.kt
-- ---------------------------------------------------------------------

-- Load open ride offers, joined with location names for origin/destination
SELECT
    o.offer_id,
    o.driver_id,
    o.vehicle_id,
    lo_from.name AS from_name,
    lo_to.name   AS to_name,
    o.depart_at,
    o.seats_available,
    o.price_base
FROM RIDE_OFFER o
         JOIN LOCATION lo_from ON o.original_location_id = lo_from.location_id
         JOIN LOCATION lo_to   ON o.dest_location_id     = lo_to.location_id
WHERE o.status = 'open'
ORDER BY o.depart_at ASC;



-- ---------------------------------------------------------------------
-- Source: app/composeApp/src/androidMain/kotlin/com/example/demo/RideShareDbHelper.kt
-- (Legacy/demo local table not tied to main project schema but just included here for completeness)
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rides(
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    pickup TEXT NOT NULL,
                                    dropoff TEXT NOT NULL,
                                    ride_time TEXT NOT NULL
);
