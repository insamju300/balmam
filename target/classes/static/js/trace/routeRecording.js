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
let recodingLimitTime = 3 * timestampInHours; //3시간
let recodingMinTime = 3 * timestampInSeconds; //todo 개발 후 3분 최저 경로녹화 시간으로 바꿀것

let puseTime = null;
let puseTimes = [];

let stayedCities = new Map();
let currentStayedCityName;
let currentStayedCityStartTime;
let currentStayedCityEndTime;

let mediaRecorder;
let recordedChunks = [];
let recordedSize = 0; // 녹화된 데이터의 크기를 추적하는 변수

const cameraView = document.getElementById("camera_view");
const canvas = document.getElementById("canvas");
const context = canvas.getContext("2d"); // 다양한 그리기 메서드와 속성에 접근할 수 있게 해줌

let mediaCount = 0;
const maxMediaCount = 50; 
let isExceededMaxMediaCount = false;
let isVideoRecording = false;

let geoMedias = new Map();
let geoMarkers = new Map();
let tmpId = 0; //임시 테스트용 시퀀스.

let recordTimeoutId;

const maxRecordSize = 5 * 1024 * 1024; // 5MB = 5 * 1024 * 1024 bytes\ 영상 녹화 최대 사이즈
const videoRecordingLimitTime=10000; //영상 녹화 최대 길이 (10초);


//동영상 촬영중엔 모드 변경 불가.

if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
  //브라우저가 mediaDevices API를 지원하고, 그 안에 getUserMedia 메소드가 존재하는지 확인. 안되면 어떻게할까..
  navigator.mediaDevices.getUserMedia({ video: true }).then(function (stream) {
    //메소드를 호출하여 비디오 입력을 요청합니다. { video: true }는 비디오(카메라) 스트림에 접근하고자 함을 나타냅니다. 이 메소드는 프로미스를 반환하며, 성공적으로 미디어 스트림에 접근했을 때 실행될 콜백 함수를 .then으로 연결합니다.
    cameraView.srcObject = stream; //콜백 함수 내부에서, stream 변수를 통해 받은 미디어 스트림을 HTML 문서 내의 비디오 태그(여기서는 cameraView로 가정)의 srcObject 속성에 할당합니다. 이를 통해 비디오 태그에서 카메라 스트림을 재생할 수 있게 됩니다.
    cameraView.play(); //비디오 태그에 스트림이 할당된 후, play 메소드를 호출하여 카메라 스트림의 재생을 시작합니다. 사용자의 카메라로부터 실시간 비디오 피드가 화면에 표시되기 시작합니다.
  });
} else {
  //사진 촬영이 불가능함을 표시
}

initMap();

async function initMap() {
  // Request needed libraries.
  recodingStartTime = Date.now();
  const { Map } = await google.maps.importLibrary("maps");
  const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
  const { Geocoder } = await google.maps.importLibrary("geocoding");
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
            console.log(addressComponents);
            const cityComponent = addressComponents.find(
              (component) =>
                component.types.includes("administrative_area_level_1") &&
                component.types.includes("political")
            );
//		      const cityComponent = addressComponents.find(
//		        (component) =>
//		          component.types.includes("locality") && // 'locality'를 사용해 도시를 찾습니다.
//		          component.types.includes("political")
//		      );
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
	//todo 에러처리 
//  console.log(
//    browserHasGeolocation
//      ? "Error: The Geolocation service failed."
//      : "Error: Your browser doesn't support geolocation."
//  );
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
  if(isVideoRecording){
    stopVideo();
  }

  document.getElementById('confirm_for_stop_trace_modal').close()

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
  console.log(pathCoordinatesGroups); //todo 길이가 0일경우 어떻게 할지 생각할 것.
//  console.log("일시정지 시간");
//  console.log(puseTimes);
  console.log("전체 일시정지 시간");
  console.log(getTotalPuseTime());
  console.log("머문 도시 리스트");
  console.log(stayedCities);
  console.log("지오 미디어 리스트");
//  console.log(geoMedias);
  
  
  const stayedCitiesForTransfer = [];

  stayedCities.forEach((value, key) => {
    stayedCitiesForTransfer.push({
      name: key,
      stayedTime: value
    });
  });
  
  
  const geoMediasForTransfer = [];

  for (let [coordinateString, mediaFiles] of geoMedias) {
    // 각 Map 항목을 원하는 형태의 객체로 변환하여 리스트에 추가
    let  coordinate = JSON.parse(coordinateString);
    geoMediasForTransfer.push({
      coordinate,
      mediaFiles
    });
  }
  console.log(geoMediasForTransfer);
  
    // Code to send the POST request without waiting for a response
  const routeRecordingDTO = {
    recordingStartTime: recodingStartTime,
    recordingEndTime: recodingEndTime,
    totalPauseTime: getTotalPuseTime(),
    pathCoordinatesGroups: pathCoordinatesGroups,
    stayedCities: stayedCitiesForTransfer,
    geoMedias: geoMediasForTransfer
  };

	fetch('/trace/routeRecording', {
	    method: 'POST',
	    headers: {
	      'Content-Type': 'application/json',
	    },
	    body: JSON.stringify(routeRecordingDTO),
	})
	.then(response => response.json()) // 응답을 JSON으로 파싱
	.then(result => {
	    if (result.success) {
	        window.location.href = result.data; // 서버로부터 받은 리다이렉트 URL로 페이지 이동
	    } else {
	        // 에러 처리 또는 사용자에게 메시지 표시
	        console.error('Failed:', result.message);
	    }
	})
	.catch((error) => {
	    console.error('Error:', error);
	});

//  //녹화정보 json형식으로 다운로드. 프론트단 작업을 위한 부분이며 추후 삭제할 것. start
//  const dataToDownload = {
//    totalRecordingTime: recodingEndTime - recodingStartTime,
//    pathCoordinatesGroups: pathCoordinatesGroups, // Assuming this is already an array or object
//    pauseTimes: puseTimes,
//    totalPauseTime: getTotalPuseTime(), // Make sure this function returns the total pause time
//    stayedCities: Object.fromEntries(stayedCities),
//    geoMedias:  Object.fromEntries(geoMedias) // Assuming this is already an array or object
//  };
//
//  // Convert the data to a JSON string
//  const jsonString = JSON.stringify(dataToDownload, null, 2);
//
//  // Create a Blob from the JSON string
//  const blob = new Blob([jsonString], {type: "application/json"});
//
//  // Generate a URL for the Blob
//  const url = URL.createObjectURL(blob);
//
//  // Create a temporary anchor element
//  const a = document.createElement("a");
//  a.href = url;
//  a.download = "traceData.json"; // File name for the downloaded data
//
//  // Append the anchor to the document, trigger the download, and remove the anchor
//  document.body.appendChild(a);
//  a.click();
//  document.body.removeChild(a);
//
//  // Optional: Revoke the blob URL to free up resources
//  URL.revokeObjectURL(url);
  //녹화정보 json형식으로 다운로드. 프론트단 작업을 위한 부분이며 추후 삭제할 것. end

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

  geocoder.geocode({ location: userLocation }, (results, status) => {
    if (status === google.maps.GeocoderStatus.OK) {
      if (results[0]) {
        // 결과에서 도시 이름을 찾습니다.
        const addressComponents = results[0].address_components;
         console.log(addressComponents);
        const cityComponent = addressComponents.find(
          (component) =>
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

async function takePhoto() {
  if (isExceededMaxMediaCount) {
    commonsAlert("최대 촬영 횟수에 달하여 더이상 사진 촬영 및 녹화가 불가능합니다. 2");
    return;
  }

  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");

  let tmpPosition = JSON.stringify({ lat: marker.position.lat, lng: marker.position.lng });
  if (geoMarkers.has(tmpPosition)) {
    let existingMarker = geoMarkers.get(tmpPosition);
    existingMarker.setMap(null);
    existingMarker = null;
  }

  canvas.width = cameraView.offsetWidth * 2;
  canvas.height = cameraView.offsetHeight * 2;

  context.drawImage(cameraView, 0, 0, canvas.width, canvas.height);

  // 캔버스에 그려진 이미지를 Blob으로 변환
  canvas.toBlob(async function (blob) {
    // 임시 파일명 생성 (예: tmp.png)
    const tmpFileName = 'tmp.png';

    // FormData 생성
    let formData = new FormData();
    // 파일 객체에 이름 설정
    formData.append("file", blob, tmpFileName);

    try {
      // AJAX를 사용하여 이미지를 업로드
      const response = await $.ajax({
        url: "/files/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
      });

      // 파일 업로드 성공 시, 처리
      console.log(response);
      let mediaFileDto = response.data;
      
      const img = document.createElement("img");
      img.src = mediaFileDto.url;
      img.style.width = "100%";
      img.style.height = "100%"; 
      

	  const pin = new PinElement({
	    glyph: img,
	    background: "#E07A5F",
	    borderColor: "#B3614C",
	  });
	  
	  let mediaMarker = new AdvancedMarkerElement({
	    map,
	    position: marker.position,
	    content: pin.element,
	  });
	  
	  if(geoMedias.has(tmpPosition)){
	    let geoMediaList = geoMedias.get(tmpPosition);
	    geoMediaList.push(mediaFileDto);
	  }else{
	    geoMedias.set(tmpPosition, [mediaFileDto]);
	  }
	   geoMarkers.set(tmpPosition, mediaMarker);
	   
      
	  createSlideImageForMediaMarker(geoMedias.get(tmpPosition), mediaMarker);
	  mediaCount++;
	  mediaCountCheck();
      //todo 에러처리
    } catch (error) {
      // 파일 업로드 실패 시, 에러 처리
      console.error("파일 업로드에 실패하였습니다.", error);
      alert("파일 업로드에 실패하였습니다.");
    }
  }, 'image/png');
}
//async function takePhoto() {
//
//  
//  if(isExceededMaxMediaCount){
//    commonsAlert("최대 촬영 횟수에 달하여 더이상 사진 촬영 및 녹화가 불가능합니다. 2");
//    return;
//  }
//
//  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary(
//    "marker"
//  );
//
//  let tmpPosition = JSON.stringify({lat: marker.position.lat, lng: marker.position.lng});
//  if(geoMarkers.has(tmpPosition)){
//    let existingMarker = geoMarkers.get(tmpPosition);
//    existingMarker.setMap(null);
//    existingMarker = null;
//  }
//
//  canvas.width = cameraView.offsetWidth * 2;
//  canvas.height = cameraView.offsetHeight * 2;
//
//  context.drawImage(cameraView, 0, 0, canvas.width, canvas.height);
//
//  //사진 저장
//  const imageDataUrl = canvas.toDataURL("image/png", 1.0);
//  // localStorage.setItem('capturedImage', imageDataUrl);
//
//  // Display the captured image
//  const img = document.createElement("img");
//  img.src = imageDataUrl;
//  img.style.width = "100%";
//  img.style.height = "100%";
//
//  const pin = new PinElement({
//    glyph: img,
//    background: "#E07A5F",
//    borderColor: "#B3614C",
//  });
//
//  let mediaMarker = new AdvancedMarkerElement({
//    map,
//    position: marker.position,
//    content: pin.element,
//  });
//
//  
//
//  if(geoMedias.has(tmpPosition)){
//    let geoMediaList = geoMedias.get(tmpPosition);
//    geoMediaList.push({time:Date.now(), mediaId:tmpId++, mediaType: "photo", url: imageDataUrl});
//  }else{
//    geoMedias.set(tmpPosition, [{time:Date.now(), mediaId:tmpId++, mediaType: "photo", url: imageDataUrl}]);
//  }
//
//  geoMarkers.set(tmpPosition, mediaMarker);
//  
//
//  createSlideImageForMediaMarker(geoMedias.get(tmpPosition), mediaMarker);
//  mediaCount++;
//  mediaCountCheck();
//}

function videoToggle() {
  $("#take_video_button").show();
  $("#take_photo_button").hide();
  $("#photo_toggle_button").show();
  $("#video_toggle_button").hide();
}

function photoToggle() {
  if(isVideoRecording){
    commonsAlert("아직 비디오 녹화가 진행중입니다.");
    return;
  }
  $("#take_photo_button").show();
  $("#take_video_button").hide();
  $("#video_toggle_button").show();
  $("#photo_toggle_button").hide();
}

function takeVideo() {
  if(isExceededMaxMediaCount){
    commonsAlert("최대 촬영 횟수에 달하여 더이상 사진 촬영 및 녹화가 불가능합니다. 2");
    return;
  }

  

  if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    
    isVideoRecording = true;

    navigator.mediaDevices
      .getUserMedia({ video: true })
      .then((stream) => {
        mediaRecorder = new MediaRecorder(stream);
        recordedChunks = []; // 녹화된 청크를 저장할 배열
        recordedSize = 0; // 누적된 데이터 크기 초기화

        mediaRecorder.ondataavailable = function (event) {
          if (event.data.size > 0 && recordedSize <= maxRecordSize) {
            recordedChunks.push(event.data);
            recordedSize += event.data.size; // 누적 데이터 크기 업데이트

            if (recordedSize > maxRecordSize) {
              // 최대 용량 초과 시
              commonsAlert(
                "녹화 가능한 최대 용량을 초과하여 녹화를 중지합니다."
              ); //todo: 최대 사이즈 명기
              stopVideo(); // 녹화 중지
            }
          }
        };

        mediaRecorder.start(); // 녹화 시작

        if (recordTimeoutId) {
          clearTimeout(recordTimeoutId);
        }
        

        // 10초 후 자동 녹화 중지
        recordTimeoutId = setTimeout(() => {
          if (mediaRecorder.state !== "inactive") {
            commonsAlert("녹화 가능한 최대 시간을 초과하여 녹화를 중지합니다."); //todo: 최대 녹화시간 명기
            stopVideo();
          }
        }, videoRecordingLimitTime); // 10초 = 10000ms

        // UI 변경
        $("#take_video_button").hide();
        $("#stop_video_button").show();
      })
      .catch((err) => {
        console.error("Failed to start video capture:", err);
      });

      
  } else {
    alert("Your browser does not support video capture.");
  }
}

async function stopVideo() {
  let tmpPosition = JSON.stringify({ lat: marker.position.lat, lng: marker.position.lng });
  if (geoMarkers.has(tmpPosition)) {
    let existingMarker = geoMarkers.get(tmpPosition);
    existingMarker.setMap(null);
    existingMarker = null;
  }

  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");

  isVideoRecording = false;

  mediaRecorder.stop(); // 녹화를 중지합니다.
  mediaRecorder.onstop = async function () {
    const blob = new Blob(recordedChunks, { type: "video/mp4" }); // 녹화된 청크들을 합쳐 하나의 Blob 객체를 생성합니다.
    recordedChunks = []; // 다음 녹화를 위해 청크 배열을 초기화합니다.

    // FormData 생성
    let formData = new FormData();
    // Blob 객체에 임시 파일명 설정 (tmp.mp4)
    formData.append("file", blob, "tmp.mp4");

    try {
      // AJAX를 사용하여 비디오를 업로드
      const response = await $.ajax({
        url: "/files/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
      });

      // 파일 업로드 성공 시, 처리
      let mediaFileDto = response.data;
      console.log(mediaFileDto);
      // todo: 업로드된 비디오 파일에 대한 처리 추가
      let img = document.createElement("img");
      img.src = mediaFileDto.thumbnailUrl;
      
       img.style.width = "100%";
       img.style.height = "100%"
       
        const pin = new PinElement({
          glyph: img,
          background: "#E07A5F",
          borderColor: "#B3614C",
        });
        
        let mediaMarker = new AdvancedMarkerElement({
          map,
          position: marker.position,
          content: pin.element,
        });
        
        if(geoMedias.has(tmpPosition)){
          let geoMediaList = geoMedias.get(tmpPosition);
          geoMediaList.push(mediaFileDto);
          
        }else{
          geoMedias.set(tmpPosition, [mediaFileDto]);
        }
        geoMarkers.set(tmpPosition, mediaMarker);
        createSlideImageForMediaMarker(geoMedias.get(tmpPosition), mediaMarker);
        

      // UI 업데이트: "녹화 중지" 버튼을 숨기고 "비디오 촬영 시작" 버튼을 다시 표시합니다.
      $("#stop_video_button").hide();
      $("#take_video_button").show();
    } catch (error) {
      // 파일 업로드 실패 시, 에러 처리
      console.error("비디오 업로드에 실패하였습니다.", error);
      alert("비디오 업로드에 실패하였습니다.");
    }
  };
}

//async function stopVideo() {
//  let tmpPosition = JSON.stringify({lat: marker.position.lat, lng: marker.position.lng});
//  if(geoMarkers.has(tmpPosition)){
//    let existingMarker = geoMarkers.get(tmpPosition);
//    existingMarker.setMap(null);
//    existingMarker = null;
//  }
//  
//
//  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary(
//    "marker"
//  );
//
//  isVideoRecording = false;
//
//  mediaRecorder.stop(); // 녹화를 중지합니다.
//  mediaRecorder.onstop = async function () {
//    const blob = new Blob(recordedChunks, { type: "video/mp4" }); // 녹화된 청크들을 합쳐 하나의 Blob 객체를 생성합니다.
//    recordedChunks = []; // 다음 녹화를 위해 청크 배열을 초기화합니다.
//    const videoURL = URL.createObjectURL(blob); // Blob 객체로부터 URL을 생성합니다. todo -> 이부분 확인해서 ajax처리.
//
//    // 비디오 태그를 생성하고, 녹화된 비디오를 소스로 설정합니다.
//    let video = document.createElement("video");
//    
//    video.src = videoURL;
//    video.load(); // 비디오를 로드합니다.
//    video.addEventListener("loadeddata", function () {
//      video.currentTime = 0; // 비디오의 첫 프레임에 접근하기 위해 시간을 설정합니다.
//      video.addEventListener("seeked", function () {
//        // 비디오의 첫 프레임을 캔버스에 그립니다.
//        let canvas = document.createElement("canvas");
//        canvas.width = video.videoWidth; // 캔버스의 너비를 비디오의 너비로 설정합니다.
//        canvas.height = video.videoHeight; // 캔버스의 높이를 비디오의 높이로 설정합니다.
//        let ctx = canvas.getContext("2d");
//        ctx.drawImage(video, 0, 0, canvas.width, canvas.height); // 비디오의 현재 프레임을 캔버스에 그립니다.
//    
//        // 캔버스의 내용을 기반으로 이미지를 생성하고, 이를 페이지에 표시합니다.
//        let thumbnailURL = canvas.toDataURL("image/png"); // 캔버스를 이미지 데이터 URL로 변환합니다.
//        let img = document.createElement("img"); // 이미지 엘리먼트를 생성합니다.
//        img.src = thumbnailURL; // 생성된 이미지 데이터 URL을 이미지 소스로 설정합니다.
//        // document.getElementById('recordedVideos').appendChild(img); // 생성된 이미지를 동영상 목록 컨테이너에 추가합니다.
//
//        img.style.width = "100%";
//        img.style.height = "100%";
//    
//        const pin = new PinElement({
//          glyph: img,
//          background: "#E07A5F",
//          borderColor: "#B3614C",
//        });
//
//        let mediaMarker = new AdvancedMarkerElement({
//          map,
//          position: marker.position,
//          content: pin.element,
//        });
//
//
//        if(geoMedias.has(tmpPosition)){
//          let geoMediaList = geoMedias.get(tmpPosition);
//          let id1 = tmpId++;
//          let id2 = tmpId++;
//          geoMediaList.push({time:Date.now(), mediaId:id1, mediaType: "video", url:videoURL});
//          geoMediaList.push({time:Date.now(), mediaId:id2, mediaType: "thumbnail", parentMedia: id1, url:thumbnailURL});
//        }else{
//          let id1 = tmpId++;
//          let id2 = tmpId++;
//          geoMedias.set(tmpPosition, [{time:Date.now(), mediaId:id1, mediaType: "video",  url:videoURL}, {time:Date.now(), mediaId:id2, mediaType: "thumbnail", parentMedia: id1}]);
//        }
//
//        geoMarkers.set(tmpPosition, mediaMarker);
//
//        createSlideImageForMediaMarker(geoMedias.get(tmpPosition), mediaMarker);
//
//
//    
//      });
//    });
//
//   
//
//
//    mediaCount++;
//    mediaCountCheck();
//
//    // videoElement.style.width = '100%'; // 비디오 너비를 100%로 설정합니다.
//    // videosContainer.appendChild(videoElement); // 생성된 비디오 요소를 동영상 목록 컨테이너에 추가합니다.
//
//    // UI 업데이트: "녹화 중지" 버튼을 숨기고 "비디오 촬영 시작" 버튼을 다시 표시합니다.
//    $("#stop_video_button").hide();
//    $("#take_video_button").show();
//  };
//}

//function createThumbnail(video) {
//  video.currentTime = 0; // 비디오의 마지막 프레임에 접근하기 위해 시간을 설정합니다.
//  video.addEventListener("seeked", function () {
//    // 비디오의 첫 프레임을 캔버스에 그립니다.
//    let canvas = document.createElement("canvas");
//    canvas.width = video.videoWidth; // 캔버스의 너비를 비디오의 너비로 설정합니다.
//    canvas.height = video.videoHeight; // 캔버스의 높이를 비디오의 높이로 설정합니다.
//    let ctx = canvas.getContext("2d");
//    ctx.drawImage(video, 0, 0, canvas.width, canvas.height); // 비디오의 현재 프레임을 캔버스에 그립니다.
//
//    // 캔버스의 내용을 기반으로 이미지를 생성하고, 이를 페이지에 표시합니다.
//    let thumbnailURL = canvas.toDataURL("image/png"); // 캔버스를 이미지 데이터 URL로 변환합니다.
//    let img = document.createElement("img"); // 이미지 엘리먼트를 생성합니다.
//    img.src = thumbnailURL; // 생성된 이미지 데이터 URL을 이미지 소스로 설정합니다.
//    console.log(img);
//    return img;
//    // document.getElementById('recordedVideos').appendChild(img); // 생성된 이미지를 동영상 목록 컨테이너에 추가합니다.
//  });
//}


function mediaCountCheck(){
  if(mediaCount>=maxMediaCount){
    isExceededMaxMediaCount = true;
  }
  
}

function createSlideImageForMediaMarker(geoMediaList, mediaMarker){
	    console.log(geoMediaList);
	
        let slideIdCount=1;
        let carousel=document.createElement('div');

        carousel.classList.add("carousel", "w-full");
        
        geoMediaList.forEach( function(geoMedia){
			  
              if(geoMedia.type==='video'){
              const carouselItem = document.createElement("div");
              carouselItem.classList.add("carousel-item", "relative", "w-full");
              carouselItem.id="slide"+(slideIdCount++);
              

               const videoElement = document.createElement("video");
               videoElement.src = geoMedia.url;
               videoElement.controls = true;
               videoElement.classList.add("w-full", "h-auto");
               carouselItem.appendChild(videoElement);
               if(geoMediaList.length > 1){
                const carouselBtns = document.createElement("div");
                carouselBtns.classList.add("absolute" ,"flex", "justify-between" , "transform", "-translate-y-1/2", "left-5", "right-5" ,"top-1/2");
                let beforSlideId = slideIdCount-2;
                if(beforSlideId<1){
                  beforSlideId = geoMediaList.length;
                }
                let afterSlideId = slideIdCount;
                if(afterSlideId>geoMediaList.length){
                  afterSlideId = 1;
                }

                let beforBtn = document.createElement("a");
                beforBtn.href="#slide"+beforSlideId;
                beforBtn.classList.add("btn", "btn-circle");
                beforBtn.innerText = "❮";


                let afterBtn = document.createElement("a");
                afterBtn.href="#slide"+afterSlideId;
                afterBtn.classList.add("btn", "btn-circle");
                afterBtn.innerHTML="❯";

                carouselBtns.appendChild(beforBtn);
                carouselBtns.appendChild(afterBtn);

                carouselItem.appendChild(carouselBtns)
                

               }
               carousel.appendChild(carouselItem);


              }else if(geoMedia.type==='image'){
                

                const carouselItem = document.createElement("div");
                carouselItem.classList.add("carousel-item", "relative", "w-full");
                carouselItem.id="slide"+(slideIdCount++);

                const img = document.createElement("img");
                img.src = geoMedia.url;
                img.classList.add("w-full", "h-auto");
                carouselItem.appendChild(img);

                if(geoMediaList.length > 1){
                  const carouselBtns = document.createElement("div");
                  carouselBtns.classList.add("absolute" ,"flex", "justify-between" , "transform", "-translate-y-1/2", "left-5", "right-5" ,"top-1/2");
                  let beforSlideId = slideIdCount-2;
                  if(beforSlideId<1){
                    beforSlideId = geoMediaList.length;
                  }
                  let afterSlideId = slideIdCount;
                  if(afterSlideId>geoMediaList.length){
                    afterSlideId = 1;
                  }
  
                  let beforBtn = document.createElement("a");
                  beforBtn.href="#slide"+beforSlideId;
                  beforBtn.classList.add("btn", "btn-circle");
                  beforBtn.innerText = "❮";
  
  
                  let afterBtn = document.createElement("a");
                  afterBtn.href="#slide"+afterSlideId;
                  afterBtn.classList.add("btn", "btn-circle");
                  afterBtn.innerHTML="❯";
  
                  carouselBtns.appendChild(beforBtn);
                  carouselBtns.appendChild(afterBtn);
  
                  carouselItem.appendChild(carouselBtns)
                  
  
                 }
                 carousel.appendChild(carouselItem);

              }
        });


        mediaMarker.addListener("click", ({ domEvent, latLng }) => {
          const { target } = domEvent;
    
          commonsAlert(carousel.outerHTML);
        });
}

function stopTraceRecodingValidation(){
  if(Date.now() - recodingStartTime - getTotalPuseTime() < recodingMinTime){
    commonsAlert(
      `일시정지를 제외하고 최소(${Math.floor(
        recodingMinTime / timestampInMinutes
      )}분)까지 녹화해 주세요.`
    );
    return false;
  }

  document.getElementById("confirm_for_stop_trace_modal").showModal();
}