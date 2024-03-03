let map;
let marker;
async function initMap() {
    // Request needed libraries.
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
    const balmami = document.createElement('img');
    balmami.src = '/images/character/balmami.png';
    balmami.style.height="3rem";

    if(!navigator.geolocation){
      handleLocationError(false, map.getCenter());
    }

    //맵 생성시 현제 위치를 가운데로 marker과 함께 생성
    navigator.geolocation.getCurrentPosition(function(position){
      const { latitude, longitude } = position.coords;
      map = new Map(document.getElementById("map"), {
        center: { lat: latitude, lng: longitude },
        zoom: 18,
        mapId: "4504f8b37365c3d0",
      });

      marker = new AdvancedMarkerElement({
        map,
        position: { lat: latitude, lng: longitude },
        content: balmami,
      });
    }, function() {
      handleLocationError(true);
  });


    navigator.geolocation.watchPosition(function(position) {//https://developer.mozilla.org/ko/docs/Web/API/Geolocation/watchPosition
      console.log(position);
      const userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
      };

      //사용자의 현재 위치로 마커 업데이트
      marker.position=userLocation;
      console.log(marker.position)

      // 지도의 중앙을 사용자의 현재 위치로 설정
      map.setCenter(userLocation);
  }, function() {
      handleLocationError(true);
  });
}

function handleLocationError(browserHasGeolocation) {
  console.log(browserHasGeolocation ?
      'Error: The Geolocation service failed.' :
      'Error: Your browser doesn\'t support geolocation.');
}

  
initMap();


  