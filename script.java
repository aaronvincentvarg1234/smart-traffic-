// ================= MAP =================

var map = L.map('map').setView([22.9734,78.6569],5);

L.tileLayer(
'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
{
    attribution:'© OpenStreetMap'
}).addTo(map);

var marker;

// ================= PLACES =================

let places = {

"bangalore":[12.9716,77.5946],
"mumbai":[19.0760,72.8777],
"delhi":[28.7041,77.1025],
"kochi":[9.9312,76.2673],
"goa":[15.2993,74.1240],
"munnar":[10.0889,77.0595],
"wayanad":[11.6854,76.1320],
"coorg":[12.3375,75.8069],
"tirupati":[13.6288,79.4192],
"leh":[34.1526,77.5770],
"manali":[32.2432,77.1892],

"bangalore airport":[13.1986,77.7066],
"kochi airport":[10.1520,76.3929],
"delhi airport":[28.5562,77.1000]

};

// ================= SEARCH =================

function searchPlace(){

    let place =
    document.getElementById("placeInput")
    .value.toLowerCase();

    if(places[place]){

        let coords = places[place];

        map.setView(coords,11);

        if(marker){
            map.removeLayer(marker);
        }

        marker = L.marker(coords)
        .addTo(map)
        .bindPopup(place.toUpperCase())
        .openPopup();

        getWeather(place);

        simulateTraffic(place);

    }

    else{

        alert(
        "Location not added yet."
        );

    }

}

// ================= WEATHER =================

async function getWeather(city){

    const apiKey =
    "0c2ea8dc4abf34adfddb5c57fc666493";

    const url =
`https://api.openweathermap.org/data/2.5/weather?q=${city},IN&appid=${apiKey}&units=metric`;

    try{

        let response = await fetch(url);

        let data = await response.json();

        let temp = data.main.temp;

        let weather = data.weather[0].main;

        let humidity = data.main.humidity;

        let wind = data.wind.speed;

        document.getElementById("weatherResult")
        .innerHTML = `

        🌡 Temperature: ${temp} °C <br>

        ☁ Weather: ${weather} <br>

        💧 Humidity: ${humidity}% <br>

        🌬 Wind Speed: ${wind} km/h

        `;

        // ALERTS

        if(weather.includes("Rain")){

            alert(
            "🌧 Rain Alert! Roads may be slippery."
            );

        }

        if(weather.includes("Fog")){

            alert(
            "🌫 Fog Warning!"
            );

        }

        if(weather.includes("Thunderstorm")){

            alert(
            "⛈ Thunderstorm Warning!"
            );

        }

    }

    catch(error){

        document.getElementById("weatherResult")
        .innerHTML =
        "Weather data unavailable.";

    }

}

// ================= TRAFFIC =================

function simulateTraffic(place){

    let levels = [
    "🟢 LOW TRAFFIC",
    "🟡 MODERATE TRAFFIC",
    "🔴 HIGH TRAFFIC"
    ];

    let random =
    levels[Math.floor(Math.random()*levels.length)];

    document.getElementById("status")
    .innerHTML =
    place.toUpperCase() +
    " : " + random;

    if(Notification.permission === "granted"){

        setTimeout(()=>{

            new Notification(
            "🚦 Traffic Update",
            {
                body:
                "Traffic updated for " + place
            });

        },5000);

    }

}

// ================= CURRENT LOCATION =================

function getCurrentLocation(){

    if(navigator.geolocation){

        navigator.geolocation.getCurrentPosition((position)=>{

            let lat = position.coords.latitude;
            let lng = position.coords.longitude;

            map.setView([lat,lng],13);

            if(marker){
                map.removeLayer(marker);
            }

            marker = L.marker([lat,lng])
            .addTo(map)
            .bindPopup("YOUR LOCATION")
            .openPopup();

        });

    }

}

// ================= NOTIFICATIONS =================

function enableNotifications(){

    if(Notification.permission !== "granted"){

        Notification.requestPermission();

    }

    alert(
    "🔔 Notifications Enabled"
    );

}