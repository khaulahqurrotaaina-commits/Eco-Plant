# EcoPlant Android App + PHP API

EcoPlant Android and the web frontend should use the same PHP API base URL: http://10.210.34.155:3000/api/. Configure the web app to call the same /api endpoints.

## Backend PHP

Run PHP on the server laptop from the backend php folder:

```powershell
& 'C:\xampp\php\php.exe' -S 0.0.0.0:3000 -t public public/router.php
```

Check http://10.210.34.155:3000/api/health. Keep the server laptop and the Android device/web laptop on a network that can reach each other, and allow port 3000 through the server laptop firewall.

## Database

Until the group shares the database schema and connection settings, this scaffold stores temporary data in backend php/storage/data.json. The Android app and web app must go through this same API; they should not connect directly to the database. Replace the temporary JSON storage with the shared database after its schema is available.

Do not commit local.properties, API tokens, passwords, runtime data, Gradle caches, or build outputs.