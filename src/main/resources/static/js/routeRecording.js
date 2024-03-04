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

const cameraView = document.getElementById('camera_view');
const canvas = document.getElementById('canvas');
const context =  canvas.getContext('2d'); // 다양한 그리기 메서드와 속성에 접근할 수 있게 해줌

if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) { //브라우저가 mediaDevices API를 지원하고, 그 안에 getUserMedia 메소드가 존재하는지 확인. 안되면 어떻게할까..
  navigator.mediaDevices.getUserMedia({ video: true }).then(function(stream) { //메소드를 호출하여 비디오 입력을 요청합니다. { video: true }는 비디오(카메라) 스트림에 접근하고자 함을 나타냅니다. 이 메소드는 프로미스를 반환하며, 성공적으로 미디어 스트림에 접근했을 때 실행될 콜백 함수를 .then으로 연결합니다.
    cameraView.srcObject = stream; //콜백 함수 내부에서, stream 변수를 통해 받은 미디어 스트림을 HTML 문서 내의 비디오 태그(여기서는 cameraView로 가정)의 srcObject 속성에 할당합니다. 이를 통해 비디오 태그에서 카메라 스트림을 재생할 수 있게 됩니다.
    cameraView.play(); //비디오 태그에 스트림이 할당된 후, play 메소드를 호출하여 카메라 스트림의 재생을 시작합니다. 사용자의 카메라로부터 실시간 비디오 피드가 화면에 표시되기 시작합니다.
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
  const {Geocoder} = await google.maps.importLibrary("geocoding");
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



async function takePhoto(){
  
  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");
  // 사진 찍어서 마크찍기
  // console.log(cameraView.offsetWidth + ", " + cameraView.offsetHeight);
  
  // let videoWidth = cameraView.videoWidth;
  // let videoHeight = cameraView.videoHeight;
  // console.log("canvas : " + canvas.width + "," +  canvas.height)
  // console.log("cameraView : " +cameraView.width + "," +  cameraView.height)
  // console.log("canvasOffset : " + canvas.offsetWidth + "," +  canvas.offsetHeight)
  // console.log("cameraViewOffset : " +cameraView.offsetWidth + "," +  cameraView.offsetHeight)
  
  canvas.width = cameraView.offsetWidth;
  canvas.height = cameraView.offsetHeight;


  context.drawImage(cameraView, 0, 0, canvas.width, canvas.height);

  //사진 저장
  const imageDataUrl = canvas.toDataURL('image/png');
  // localStorage.setItem('capturedImage', imageDataUrl);



  // Display the captured image
  const img = document.createElement('img');
  img.src = imageDataUrl;
  img.style.width = "100%"; 
  img.style.height = "100%"; 

  const pin = new PinElement({
    glyph: img,
    background: '#E07A5F',
    borderColor: '#B3614C',
  });

  console.log("여기는 옴")

  let mediaMarker = new AdvancedMarkerElement({
    map,
    position: marker.position,
    content: pin.element,
  });

  const alertImg = document.createElement('img');
  alertImg.src = imageDataUrl;

  mediaMarker.addListener("click", ({ domEvent, latLng }) => {
    const { target } = domEvent;
    

    commonsAlert(alertImg.outerHTML);
  });

}


function videoToggle(){
  $("#take_video_button").show();
  $("#take_photo_button").hide();
  $("#photo_toggle_button").show();
  $("#video_toggle_button").hide();
}

function photoToggle(){
  $("#take_photo_button").show();
  $("#take_video_button").hide();
  $("#video_toggle_button").show();
  $("#photo_toggle_button").hide();
}