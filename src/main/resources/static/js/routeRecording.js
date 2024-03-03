let map;
let marker;
let lastUpdateTime;
const intervalGap = 2000;
let pathLine;
let pathCoordinates = [];
const pathLines = [];
const pathCoordinatesGroups = [];
let watchId;
let intervalId;

async function initMap() {
  // Load necessary libraries
  const { Map, AdvancedMarkerElement } = await google.maps.importLibrary("maps", "marker");
  
  map = new Map(document.getElementById("map"), {
    zoom: 18,
    center: { lat: -34.397, lng: 150.644 },
    mapId: "4504f8b37365c3d0",
  });

  if (!navigator.geolocation) {
    handleLocationError(false);
    return;
  }

  navigator.geolocation.getCurrentPosition(initPosition, () => handleLocationError(true));
}

function initPosition(position) {
  const userLocation = getPositionObject(position);
  createMarker(userLocation);
  creatNewPathLine(userLocation);
  lastUpdateTime = Date.now();
  startLocationWatch();
}

function startLocationWatch() {
  watchId = navigator.geolocation.watchPosition(updatePosition, () => handleLocationError(true), {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
  });
}

function updatePosition(position) {
  const now = Date.now();
  if (lastUpdateTime && now - lastUpdateTime < intervalGap) return;
  
  const userLocation = getPositionObject(position);
  updateMarkerAndCenter(userLocation);
  lastUpdateTime = now;
}

function createMarker(location) {
  const balmami = document.createElement("img");
  balmami.src = "/images/character/balmami.png";
  balmami.style.height = "3rem";
  
  marker = new AdvancedMarkerElement({
    map: map,
    position: location,
    content: balmami,
  });
}

function updateMarkerAndCenter(location) {
  marker.setPosition(location);
  pathCoordinates.push(location);
  pathLine.setPath(pathCoordinates);
  map.setCenter(location);
}

function creatNewPathLine(location) {
  pathCoordinates = [location];
  pathLine = new google.maps.Polyline({
    path: pathCoordinates,
    strokeColor: '#E07A5F',
    strokeOpacity: 0.7,
    strokeWeight: 2,
  });
  pathLine.setMap(map);
}

function pauseTraceRecording() {
  if(pauseCount >= maxPauseCount) {
    alert(`Maximum pause count of ${maxPauseCount} exceeded.`);
    return;
  }
  clearInterval(intervalId);
  navigator.geolocation.clearWatch(watchId);
  pathLines.push(pathLine);
  pathCoordinatesGroups.push(pathCoordinates);
  pathCoordinates = [];
  pauseCount++;
}

function resumeTraceRecording() {
  clearInterval(intervalId); // Ensure no interval is running
  intervalId = setInterval(fetchCurrentPosition, 1000);
}

function fetchCurrentPosition() {
  navigator.geolocation.getCurrentPosition(position => {
    updateMarkerAndCenter(getPositionObject(position));
  }, () => handleLocationError(true));
}

function handleLocationError(hasGeolocation) {
  console.log(hasGeolocation ? "Error: The Geolocation service failed." : "Error: Your browser doesn't support geolocation.");
}

function getPositionObject(position) {
  return new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
}

// Initialize the map
initMap();