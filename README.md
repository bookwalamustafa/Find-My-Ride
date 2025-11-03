# Find-My-Ride

## Database Setup

1. Install SQLite if you don’t have it (`sqlite3 --version` to check). 
    - Using Version: 3.50.4 2025-07-30 19:33:53 4d8adfb30e03f9cf27f800a2c1ba3c48fb4ca1b08b0f5ed59a4d5ecbf45e20a3 (64-bit)
2. In the root project folder, run:
```bash
sqlite3 findmyride.db < database/schema.sql
```
3. You can view the tables with:
```bash
sqlite3 findmyride.db
sqlite> .tables
```