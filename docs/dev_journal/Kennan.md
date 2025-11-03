# 11/2/2025

- Initialize the SQLite Database
- [How to Setup SQLite Database](https://chatgpt.com/share/6907f926-9e04-800e-8063-528f38672896)

Setting Up Database:
```bash

sqlite3 findmyride.db < database/schema.sql
sqlite3 findmyride.db
sqlite> .tables
```

Injecting Database Information (TO-DO):
```bash

sqlite3 findmyride.db < database/teamDeliverable2_DDL.sql
sqlite3 findmyride.db < database/populate.sql
```