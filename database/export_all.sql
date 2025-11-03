.headers on
.mode csv

.output exports/USER.csv
SELECT * FROM "USER" ORDER BY user_id;

.output exports/VEHICLE.csv
SELECT * FROM "VEHICLE" ORDER BY vehicle_id;

.output exports/LOCATION.csv
SELECT * FROM "LOCATION" ORDER BY location_id;

.output exports/RIDE_OFFER.csv
SELECT * FROM "RIDE_OFFER" ORDER BY offer_id;

.output exports/RIDE_REQUEST.csv
SELECT * FROM "RIDE_REQUEST" ORDER BY request_id;

.output exports/RIDE_MATCH.csv
SELECT * FROM "RIDE_MATCH" ORDER BY match_id;

.output exports/RATING.csv
SELECT * FROM "RATING" ORDER BY rating_id;

.output stdout