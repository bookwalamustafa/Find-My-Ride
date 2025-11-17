# Application Plan

## Primary use cases
1) Rider posts a request with time window; system matches to existing driver offers.
2) Driver posts a recurring offer (route + depart time); seats decrement on booking.
3) Both parties confirm; post-trip ratings update user reputation.

## Screens / flows
- **Auth**: Login / Signup (driver flow asks vehicle details)
- **Home (role-aware)**: “Find a Ride” (rider) / “Offer a Ride” (driver)
- **Map + Form**:
  - Rider: choose pickup/dropoff + earliest/latest + seats_needed → submit request
  - Driver: choose origin/destination + depart_at + seats_available + pricing → submit offer
- **Matches**: pending/confirmed/completed per user, with actions (confirm / cancel / rate)
- **Profile**: user info, vehicle(s), rating_avg, history

## Data + queries per screen
- See `docs/planned_queries.md` for query list and ownership.
