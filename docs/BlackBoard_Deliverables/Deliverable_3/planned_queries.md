# Planned Queries

## Q01_OpenOffersBetweenLocations - Samii Shabuse
Purpose: Rider search page – list open offers between two locations in a time window.
Inputs: origin_id, dest_id, start_ts, end_ts, min_seats
Outputs: offer_id, driver (username), vehicle make/model, depart_at, seats_available, price_base, price_per_mile — one row per matching open offer, ordered by depart_at.
SQL:
```sql
SELECT o.offer_id, u.username AS driver, v.make, v.model,
       o.depart_at, o.seats_available, o.price_base, o.price_per_mile
FROM RIDE_OFFER o
JOIN "USER" u ON u.user_id = o.driver_id
JOIN "VEHICLE" v ON v.vehicle_id = o.vehicle_id
WHERE o.status = 'open'
  AND o.original_location_id = ?1
  AND o.dest_location_id = ?2
  AND o.depart_at BETWEEN ?3 AND ?4
  AND o.seats_available >= ?5
ORDER BY o.depart_at;
```

## Q02_CreateRideRequest - Samii Shabuse
Purpose: Submit a new rider request.
Inputs: rider_id, pickup_id, dropoff_id, earliest, latest, seats_needed
Outputs: Returns the newly-created request_id (single row) via RETURNING; side effect: new RIDE_REQUEST row inserted with status 'open'.
SQL:
```sql
INSERT INTO RIDE_REQUEST(rider_id,pickup_location_id,dropoff_location_id,
                         earliest_pickup,latest_pickup,seats_needed,status)
VALUES (?1,?2,?3,?4,?5,?6,'open')
RETURNING request_id;
```

## Q03_CreateRideOffer - Kennan Lu
Purpose: Driver posts an offer.
Inputs: driver_id, vehicle_id, origin_id, dest_id, depart_at, seats_avail, price_base, ppm
Outputs: Returns the newly-created offer_id (single row) via RETURNING; side effect: new RIDE_OFFER row inserted with status 'open'.
SQL:
```sql
INSERT INTO RIDE_OFFER(driver_id,vehicle_id,original_location_id,dest_location_id,
                       depart_at,seats_available,price_base,price_per_mile,status)
VALUES (?1,?2,?3,?4,?5,?6,?7,?8,'open')
RETURNING offer_id;
```

## Q04_MatchRequestToOffer - Kennan Lu
Purpose: Create a match when capacity/time fits; mark request ‘matched’.
Inputs: request_id, offer_id, seats_booked, price_total
Outputs: No SELECT-returned rows; the transaction produces three side effects: a new RIDE_MATCH row (state 'pending') is inserted, RIDE_REQUEST.status is set to 'matched', and RIDE_OFFER.seats_available is decremented (and may change status to 'full'). The client can observe success by affected row counts or by querying the inserted RIDE_MATCH.
SQL:
```sql
BEGIN;
INSERT INTO RIDE_MATCH(request_id,offer_id,seats_booked,price_total,state)
VALUES (?1,?2,?3,?4,'pending');

UPDATE RIDE_REQUEST
SET status = 'matched'
WHERE request_id = ?1;

UPDATE RIDE_OFFER
SET seats_available = seats_available - ?3,
    status = CASE WHEN seats_available - ?3 = 0 THEN 'full' ELSE status END
WHERE offer_id = ?2;

COMMIT;
```

## Q05_UserUpcomingMatches - Samii Shabuse
Purpose: Show upcoming trips for a user (rider or driver).
Inputs: user_id
Outputs: Rows with columns: match_id, state, matched_at, depart_at, original_location_id, dest_location_id, rider_id, driver_id — one row per match involving the user, ordered by depart_at DESC.
SQL:
```sql
SELECT m.match_id, m.state, m.matched_at,
       ro.depart_at, ro.original_location_id, ro.dest_location_id,
       rr.rider_id, ro.driver_id
FROM RIDE_MATCH m
JOIN RIDE_OFFER ro ON ro.offer_id = m.offer_id
JOIN RIDE_REQUEST rr ON rr.request_id = m.request_id
WHERE rr.rider_id = ?1 OR ro.driver_id = ?1
ORDER BY ro.depart_at DESC;
```

## Q06_Completematch - Mustafa Bookwala
Purpose: Mark a match completed after trip (enables rating).
Inputs: match_id
Outputs: No result set; side effect: the specified RIDE_MATCH row's state is updated to 'completed'. The command returns an update count (typically 1 when successful).
SQL:
```sql
UPDATE RIDE_MATCH SET state='completed' WHERE match_id=?1;
```

## Q07_CreateRating - Mustafa Bookwala
Purpose: Add a rating after completion; prevents self-rating by constraint.
Inputs: match_id, from_user_id, to_user_id, stars, comment
Outputs: No SELECT-returned rows by default; side effect: a new RATING row is inserted. If you need the new rating_id, add a RETURNING clause. The operation will fail if constraints (e.g., self-rating) are violated.
SQL:
```sql
INSERT INTO RATING(match_id,from_user_id,to_user_id,stars,comment)
VALUES (?1,?2,?3,?4,?5);
```

## Q08_UpdateRatingAverages - Samii Shabuse 
Purpose: Recompute USER.rating_avg (could be run nightly or after insert).
SQL:
```sql
UPDATE "USER" u
SET rating_avg = COALESCE((
  SELECT AVG(r.stars*1.0) FROM RATING r WHERE r.to_user_id = u.user_id
), u.rating_avg);
```

Inputs: none (operates across users)
Outputs: No result set; side effect: updates USER.rating_avg for users with ratings. The statement returns the number of rows updated. After running, each affected user's rating_avg reflects AVG(r.stars).

## Q09_DriverDashboardStats - Kennan Lu
Purpose: Driver dashboard totals.
Inputs: driver_id
Outputs: Single row with columns: revenue_completed (sum of price_total for completed matches), seats_booked_total (sum of seats_booked), no_shows (count of matches with state='no_show'). If the driver has no matches, sums may be NULL; consider COALESCE(...,0) if you prefer zeros.
SQL:
```sql
SELECT
  SUM(CASE WHEN m.state='completed' THEN m.price_total ELSE 0 END) AS revenue_completed,
  SUM(m.seats_booked) AS seats_booked_total,
  SUM(CASE WHEN m.state='no_show' THEN 1 ELSE 0 END) AS no_shows
FROM RIDE_MATCH m
JOIN RIDE_OFFER o ON o.offer_id = m.offer_id
WHERE o.driver_id = ?1;
```

## Q10_AdminHealthCheck - Mustafa Bookwala
Purpose: Quick integrity checks for grading/demo.
Inputs: none
Outputs: Rows with two columns: metric (text) and val (count). Three rows returned for 'offers_open', 'requests_open', and 'matches_pending', each showing the current count.
SQL:
```sql
SELECT 'offers_open' AS metric, COUNT(*) val FROM RIDE_OFFER WHERE status='open'
UNION ALL
SELECT 'requests_open', COUNT(*) FROM RIDE_REQUEST WHERE status='open'
UNION ALL
SELECT 'matches_pending', COUNT(*) FROM RIDE_MATCH WHERE state='pending';
```

