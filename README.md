# Find-My-Ride

## Database Setup

1. Install SQLite if you don’t have it (`sqlite3 --version` to check).
2. In the root project folder, run:
```bash
sqlite3 findmyride.db < database/schema.sql
```
3. You can view the tables with:
```bash
sqlite3 findmyride.db
sqlite> .tables
```