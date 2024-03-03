let map;
let marker;
let lastUpdateTime;
const intervalGap = 2000;
let pathLine;
let pathCoordinates=[];
let pauseCount=0;
let maxPauseCount=10;
const pathLines=[];
const pathCoordinatesGroups=[];
let watchId;
let intervalId;




async function initMap() {
  // Request needed libraries.
  const { Map } = await google.maps.importLibrary("maps");
  const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
  const balmami = document.createElement("img");
  balmami.src = "/images/character/balmami.png";
  balmami.style.height = "3rem";

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

      creatNewPathLine(userLocation);
      lastUpdateTime = new Date();
    },
    function () {
      handleLocationError(true);
    }
  );

  watchId = navigator.geolocation.watchPosition(
    function (position) {
      //https://developer.mozilla.org/ko/docs/Web/API/Geolocation/watchPosition

      let now = new Date();

      if (
        lastUpdateTime &&
        now.getTime() - lastUpdateTime.getTime() < intervalGap
      ) {
        return;
      }
      lastUpdateTime = now;
      

      const userLocation = getUserLocation(position);

      //사용자의 현재 위치로 마커 업데이트
      marker.position = userLocation;
      
      //선 그리기
      pathCoordinates.push(userLocation);
      pathLine.setPath(pathCoordinates);
      map.panTo(userLocation);


      // 지도의 중앙을 사용자의 현재 위치로 설정
      map.setCenter(userLocation);

      
    },
    function () {
      handleLocationError(true);
    }
  );
}

function handleLocationError(browserHasGeolocation) {
  console.log(
    browserHasGeolocation
      ? "Error: The Geolocation service failed."
      : "Error: Your browser doesn't support geolocation."
  );
}

initMap();

function resumeTraceRecoding(){
  navigator.geolocation.getCurrentPosition(
    function (position) {
      const userLocation = getUserLocation(position);
      creatNewPathLine(userLocation)
      lastUpdateTime = new Date();
    },
    function () {
      handleLocationError(true);
    }
  );

  

  clearInterval(intervalId);

  watchId = navigator.geolocation.watchPosition(
    function (position) {
      //https://developer.mozilla.org/ko/docs/Web/API/Geolocation/watchPosition

      let now = new Date();

      if (
        lastUpdateTime &&
        now.getTime() - lastUpdateTime.getTime() < intervalGap
      ) {
        return;
      }
      lastUpdateTime = now;

      const userLocation = getUserLocation(position);

      //사용자의 현재 위치로 마커 업데이트
      marker.position = userLocation;
      
      //선 그리기
      pathCoordinates.push(userLocation);
      pathLine.setPath(pathCoordinates);
      map.panTo(userLocation);

      // 지도의 중앙을 사용자의 현재 위치로 설정
      map.setCenter(userLocation);
    },
    function () {
      handleLocationError(true);
    }
  );


  pathLine.setMap(map);
    $("#trace_recoding_resume_btn").hide();
    $("#trace_recoding_pause_btn").show();
}

function pauseTraceRecoding(){
  if(pauseCount>=maxPauseCount){
    commonsAlert(`최대 일시정지 횟수(${maxPauseCount}회)를 초과하여 더 일시정지 하실 수 없습니다.`, "최대 일시정지 횟수 추가");
    return;
  }
  pathLines.push(pathLine);
  pathCoordinatesGroups.push(pathCoordinates);

  pathCoordinates=[];



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

function stopTraceRecoding(){
  
}



function creatNewPathLine(userLocation){
  
  pathCoordinates = [userLocation];

  pathLine = new google.maps.Polyline({
    path: pathCoordinates,
    strokeColor: '#E07A5F',
    strokeOpacity: 0.7,
    strokeWeight: 2,
  });
  pathLine.setMap(map);
}


function getUserLocation(position){
  const userLocation = {
    lat: position.coords.latitude,
    lng: position.coords.longitude,
    time: Date.now(),
  };

  return userLocation;
}