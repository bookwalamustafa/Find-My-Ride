# 11/20/2025

Experienced a bunch of issues with trying to build and run the app.

Solution: Deleted the .gradle folder in the app subdirectory and forced it to be rebuilt.

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

#### 11/21/2025 Update: ANDROID DOES NOT SUPPORT SVG FILES

Here's how to convert them to XML before use:

    1. In the Project view (left panel), right-click on your commonMain/composeResources/drawable folder.

    2. Select New > Vector Asset.

    3. In the "Asset Type" section, choose Local file (SVG, PSD).

    4. Click the folder icon next to Path and select your original SVG file.

    5. Click Next and then Finish.

This will create a new .xml file in that folder. Delete the old .svg file so there is no confusion.

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