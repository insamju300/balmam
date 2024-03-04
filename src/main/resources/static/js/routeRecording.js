let map;
let marker;
let geocoder;
let lastUpdateTime;
const intervalGap = 1000;
let pathLine;
let pathCoordinates = [];
let pauseCount = 0;
let maxPauseCount = 10;
const pathLines = [];
const pathCoordinatesGroups = [];
let watchId;
let intervalId;
let intervalIdForTimeCheck;

let timestampInHours = 1000 * 60 * 60;
let timestampInMinutes = 1000 * 60;
let timestampInSeconds = 1000;

let recodingStartTime;
let recodingEndTime;
let recodingLimitTime = 3 * timestampInMinutes;

let puseTime = null;
let puseTimes = [];

let stayedCities = new Map();
let currentStayedCityName;
let currentStayedCityStartTime;
let currentStayedCityEndTime;

const video = document.getElementById('video');
// Get access to the camera
if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
  navigator.mediaDevices.getUserMedia({ video: true }).then(function(stream) {
      video.srcObject = stream;
      video.play();
  });
}else{
  //사진 촬영이 불가능함을 표시
}

initMap();

async function initMap() {
  // Request needed libraries.
  recodingStartTime = Date.now();
  const { Map } = await google.maps.importLibrary("maps");
  const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
  const {Geocoder} = await google.maps.importLibrary("geocoding")
  const balmami = document.createElement("img");
  balmami.src = "/images/character/balmami.png";
  balmami.classList.add("h-16");
  balmami.classList.add("drop-shadow-lg");

  if (!navigator.geolocation) {
    handleLocationError(false, map.getCenter());
  }

  //맵 생성시 현제 위치를 가운데로 marker과 함께 생성
  navigator.geolocation.getCurrentPosition(
    function (position) {
      const userLocation = getUserLocation(position);
      map = new Map(document.getElementById("map"), {
        center: userLocation,
        zoom: 18,
        mapId: "4504f8b37365c3d0",
      });

      marker = new AdvancedMarkerElement({
        map,
        position: userLocation,
        content: balmami,
      });

      geocoder = new google.maps.Geocoder();

      geocoder.geocode({ location: userLocation }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          if (results[0]) {
            // 결과에서 도시 이름을 찾습니다.
            const addressComponents = results[0].address_components;
            const cityComponent = addressComponents.find((component) =>
            component.types.includes("administrative_area_level_1") &&
            component.types.includes("political")
            );
            const cityName = cityComponent ? cityComponent.long_name : null;

            if (cityName) {
              currentStayedCityName = cityComponent.long_name;
              currentStayedCityStartTime = Date.now();
              console.log(currentStayedCityName);
            }
          } else {
            console.log("Geocoder 실패: " + status);
          }
        }
      });

      creatNewPathLine(userLocation);
      lastUpdateTime = Date.now();
    },
    function () {
      handleLocationError(true);
    }
  );

  watchId = navigator.geolocation.watchPosition(
    function (position) {
      successWatch(position);
    },
    function () {
      handleLocationError(true);
    }
  );

  intervalIdForTimeCheck = setInterval(() => {
    if (
      Date.now() - recodingStartTime - getTotalPuseTime() >
      recodingLimitTime
    ) {
      commonsAlert(
        `최대 실녹화 시간(${Math.floor(
          recodingLimitTime / timestampInMinutes
        )}분)까지 녹화되어 다음 단계로 넘어갑니다.`,
        "녹화 종료 알림"
      );
      stopTraceRecoding();
      return;
    }
  }, 1000);
}

function handleLocationError(browserHasGeolocation) {
  console.log(
    browserHasGeolocation
      ? "Error: The Geolocation service failed."
      : "Error: Your browser doesn't support geolocation."
  );
}



function resumeTraceRecoding() {
  puseTime.endTime = Date.now();
  puseTimes.push(puseTime);
  puseTime = null;

  navigator.geolocation.getCurrentPosition(
    function (position) {
      const userLocation = getUserLocation(position);
      creatNewPathLine(userLocation);
      lastUpdateTime = Date.now();
    },
    function () {
      handleLocationError(true);
    }
  );

  clearInterval(intervalId);

  watchId = navigator.geolocation.watchPosition(
    function (position) {
      successWatch(position);
    },
    function () {
      handleLocationError(true);
    }
  );

  pathLine.setMap(map);
  $("#trace_recoding_resume_btn").hide();
  $("#trace_recoding_pause_btn").show();
}

function pauseTraceRecoding() {
  if (pauseCount >= maxPauseCount) {
    commonsAlert(
      `최대 일시정지 횟수(${maxPauseCount}회)를 초과하여 더 일시정지 하실 수 없습니다.`,
      "최대 일시정지 횟수 추가"
    );
    return;
  }
  pathLines.push(pathLine);
  puseTime = new Object();
  puseTime.startTime = Date.now();

  pathCoordinates = [];

  navigator.geolocation.clearWatch(watchId);

  intervalId = setInterval(() => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userLocation = getUserLocation(position);
        // 위치 정보가 성공적으로 가져와졌을 때의 처리
        // //사용자의 현재 위치로 마커 업데이트
        marker.position = userLocation;

        // 지도의 중앙을 사용자의 현재 위치로 설정
        map.setCenter(userLocation);
      },
      (error) => {
        // 위치 정보를 가져오는데 실패했을 때의 처리
        console.error("Error Getting Location: ", error);
      }
    );
  }, 1000); // 1초마다 getCurrentPosition을 호출

  $("#trace_recoding_pause_btn").hide();
  $("#trace_recoding_resume_btn").show();
  pauseCount++;
}

function stopTraceRecoding() {
  clearInterval(intervalId);
  clearInterval(intervalIdForTimeCheck);
  navigator.geolocation.clearWatch(watchId);

  if (puseTime && !puseTime.endTime) {
    puseTime.endTime = Date.now();
    puseTimes.push(puseTime);
  }

  if (currentStayedCityName && !currentStayedCityEndTime) {
    currentStayedCityEndTime = Date.now();
    let stayedCity = {
      name: currentStayedCityName,
      stayedTime: currentStayedCityEndTime - currentStayedCityStartTime,
    };
    if (stayedCities.has(currentStayedCityName)) {
      let existingStayedTime = stayedCities.get(currentStayedCityName);
      stayedCities.set(
        currentStayedCityName,
        currentStayedCityEndTime -
          currentStayedCityStartTime +
          existingStayedTime
      );
    } else {
      stayedCities.set(
        currentStayedCityName,
        currentStayedCityEndTime - currentStayedCityStartTime
      );
    }
  }

  recodingEndTime = Date.now();
  let totalRecodingTime = recodingEndTime - recodingStartTime;
  let hours = Math.floor(totalRecodingTime / timestampInHours);
  totalRecodingTime %= timestampInHours;
  let minutes = Math.floor(totalRecodingTime / timestampInMinutes);
  totalRecodingTime %= timestampInMinutes;
  let seconds = Math.floor(totalRecodingTime / timestampInSeconds);
  console.log("전체 레코딩 시간");
  console.log(hours + ":" + minutes + ":" + seconds);
  console.log("녹화된 경로");
  console.log(pathCoordinatesGroups);
  console.log("일시정지 시간");
  console.log(puseTimes);
  console.log("전체 일시정지 시간");
  console.log(getTotalPuseTime());
  console.log("머문 도시 리스트");
  console.log(stayedCities);
}

function creatNewPathLine(userLocation) {
  const tmpPathCoordinates = [userLocation];
  pathCoordinatesGroups.push(tmpPathCoordinates);
  pathCoordinates = tmpPathCoordinates;

  pathLine = new google.maps.Polyline({
    path: pathCoordinates,
    strokeColor: "#E07A5F",
    strokeOpacity: 0.7,
    strokeWeight: 2,
  });
  pathLine.setMap(map);
}

function getUserLocation(position) {
  const userLocation = {
    lat: position.coords.latitude,
    lng: position.coords.longitude,
    time: Date.now(),
  };

  return userLocation;
}

function successWatch(position) {
  //const {PlacesService} = await google.maps.importLibrary("places");
  //const service = new google.maps.places.PlacesService(map);
  //https://developer.mozilla.org/ko/docs/Web/API/Geolocation/watchPosition

  let now = Date.now();

  if (lastUpdateTime && now - lastUpdateTime < intervalGap) {
    return;
  }
  lastUpdateTime = now;

  const userLocation = getUserLocation(position);
  //로직 바꿀것.
  geocoder.geocode({ location: userLocation }, (results, status) => {
    if (status === google.maps.GeocoderStatus.OK) {
      if (results[0]) {
        // 결과에서 도시 이름을 찾습니다.
        const addressComponents = results[0].address_components;
        const cityComponent = addressComponents.find((component) =>
          component.types.includes("administrative_area_level_1") &&
          component.types.includes("political")
        );
        const cityName = cityComponent ? cityComponent.long_name : null;
        console.log(addressComponents);

        if (cityName && cityName !== currentStayedCityName) {
          if (!currentStayedCityName) {
            currentStayedCityName = cityComponent.long_name;
            currentStayedCityStartTime = Date.now();
          } else {
            currentStayedCityEndTime = Date.now();
            let stayedCity = {
              name: currentStayedCityName,
              stayedTime: currentStayedCityEndTime - currentStayedCityStartTime,
            };
            if (stayedCities.has(currentStayedCityName)) {
              let existingStayedTime = stayedCities.get(currentStayedCityName);
              stayedCities.set(
                currentStayedCityName,
                currentStayedCityEndTime -
                  currentStayedCityStartTime +
                  existingStayedTime
              );
            } else {
              stayedCities.set(
                currentStayedCityName,
                currentStayedCityEndTime - currentStayedCityStartTime
              );
            }
            currentStayedCityName = cityName;
            currentStayedCityStartTime = Date.now();
            currentStayedCityEndTime = null;
          }
        }
      } else {
        console.log("Geocoder 실패: " + status);
      }
    }
  });

  console.log(currentStayedCityName);

  //사용자의 현재 위치로 마커 업데이트
  marker.position = userLocation;

  //선 그리기
  pathCoordinates.push(userLocation);
  pathLine.setPath(pathCoordinates);
  map.panTo(userLocation);

  // 지도의 중앙을 사용자의 현재 위치로 설정
  map.setCenter(userLocation);
}

function getTotalPuseTime() {
  const initialValue = 0;
  const totalPuseTime = puseTimes.reduce(
    (accumulator, puseTime) =>
      accumulator + (puseTime.endTime - puseTime.startTime),
    initialValue
  );

  return totalPuseTime;
}



