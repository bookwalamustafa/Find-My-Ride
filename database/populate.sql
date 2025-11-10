INSERT INTO "USER"(user_id,email,username,password_hash,phone_number,role,rating_avg,created_at) VALUES
(1,'abdul@example.edu','abdul','x','856-111-1111','driver',4.7,'2025-10-02T00:00:00'),
(2,'quincy@example.edu','quincy','x','856-222-2222','rider',4.6,'2025-10-09T00:00:00');

INSERT INTO "VEHICLE"(vehicle_id,owner_user_id,make,model,color,plate,seats_total,year,fun_fact) VALUES
(1,1,'Toyota','Camry','Silver','NJ-8767',4,2015,'Loves Turnpike');

INSERT INTO "LOCATION"(location_id,name,address) VALUES
(1,'Drexel Main Building','3141 Chestnut St'),
(2,'30th Street Station','2955 Market St');

INSERT INTO "RIDE_OFFER"(offer_id,driver_id,vehicle_id,original_location_id,dest_location_id,depart_at,seats_available,price_base,price_per_mile,status,created_at) VALUES
(1,1,1,1,2,'2025-10-16T08:00:00',2,4.50,0.80,'open','2025-10-15T12:00:00');

INSERT INTO "RIDE_REQUEST"(request_id,rider_id,pickup_location_id,dropoff_location_id,earliest_pickup,latest_pickup,seats_needed,status,created_at) VALUES
(1,2,1,2,'2025-10-16T07:45:00','2025-10-16T08:15:00',1,'open','2025-10-15T19:00:00');

INSERT INTO "RIDE_MATCH"(match_id,request_id,offer_id,seats_booked,price_total,state,matched_at) VALUES
(1,1,1,1,8.10,'confirmed','2025-10-15T20:00:00');

INSERT INTO "RATING"(rating_id,match_id,from_user_id,to_user_id,stars,comment,created_at) VALUES
(1,1,2,1,5,'Great ride','2025-10-16T10:00:00');
