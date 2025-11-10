# Data Sources (Public + Simulated)

## Public datasets (inspiration/benchmarks)
List each dataset + URL + 1-line purpose (pricing ranges, route density, rating distribution, etc.).

## Simulated data (how we generated it)
- **Users**: 200 records via Faker (names, emails `.edu`), roles distributed 40% rider / 40% driver / 20% both. `created_at` uniform across last 12 months. `rating_avg` ~ Normal(4.5, 0.4), clipped [2.5, 5].
- **Vehicles**: 120 records; foreign key to drivers; `seats_total` in [4..7]; `plate` unique.
- **Locations**: Fixed Drexel/UCity POIs + a few generic addresses.
- **Ride Offers**: For each driver, 0–3 recurring routes. `status` in {open, closed}. Prices: `price_base` ~ U[3,10], `price_per_mile` ~ U[0.5,1.2].
- **Ride Requests**: For riders, 0–4 requests; `earliest_pickup` / `latest_pickup` within same day; `status` in {open, matched, cancelled, expired}.
- **Matches**: Join a subset of open requests with compatible offers; `state` in {pending, confirmed, completed, cancelled, no_show}.
- **Ratings**: Only after `completed` matches; `stars` 1–5 (skewed to 4–5). Enforce `from_user_id <> to_user_id`.

> Reproducibility: set random seed (e.g., 42). Keep FK integrity by generating in order: USER → VEHICLE → LOCATION → RIDE_OFFER → RIDE_REQUEST → RIDE_MATCH → RATING.
