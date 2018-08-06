# BlindTransport
Simple application for Android Phones over Kit Kat v4.4. 
The main purpose of this app is to help blind and partially sighted persons to know witch bus arrives in the station. 
Behind the scenes, there is a small Android app with a simple relational SQLite database containing stations, lines, and a many to many relation for buses stopping in a station. 
Each bus is configured with two bluetooth beacons pieces that will advertise the identifier. The app gets the identifier, parses its data and pushes a notification to notification area of android system. 
This way, the number is advertised to the user. 
If alfa testing will considder, we can implement audio files playback saying bus number(s) arrived. 

Bluetooth Bicro Location is a simple BLE v4 radio signal advertised by a beacon, whoom's identifiers are managed by a Google Eddystone platform or by a proprietary one. 

