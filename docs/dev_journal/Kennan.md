# 11/16/2025

## Development Strategy

As we team we focused and came up with the strategy with 3 Phases.

We are following Three Phase Strategy:

UI Development -> UI Connection Development -> Frontend to Backend Database Connection

And [ChatGPT](https://chatgpt.com/share/691a8a83-69c0-800e-be1c-aa304a8a901f) verified this is the correct process that modern companies use.

Following paging structure with react where we have folders for our features.

## Importing Icons

You should use your own SVG/XML files if you want to import icons onto the UI for the screens.
    
    1. Get your icons - Download the icons you want as .svg or .xml (Android Vector) files. (Sites like Heroicons or Phosphor Icons are good sources).
    
    2. Place them in the resources folder Go to composeApp/src/commonMain/composeResources/drawable. Paste your files there (e.g., ic_calendar.xml, ic_location.xml). 
    
    3. Update the code to use painterResource

EXAMPLE CALL using painterResource: 
icon = painterResource(Res.drawable.ic_calendar)

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

sqlite3 findmyride.db < database/populate.sql
```