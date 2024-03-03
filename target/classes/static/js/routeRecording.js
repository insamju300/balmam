let map;
let marker;
async function initMap() {
    // Request needed libraries.
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
    map = new Map(document.getElementById("map"), {
      center: { lat: 37.4239163, lng: -122.0947209 },
      zoom: 14,
      mapId: "4504f8b37365c3d0",
    });
    marker = new AdvancedMarkerElement({
      map,
      position: { lat: 37.4239163, lng: -122.0947209 },
    });


    if(!navigator.geolocation){
      handleLocationError(false, map.getCenter());
    }

    navigator.geolocation.getCurrentPosition(function(position){
      alert("되냐")
    }, function() {
      handleLocationError(true, map.getCenter());
  });


    navigator.geolocation.watchPosition(function(position) {//https://developer.mozilla.org/ko/docs/Web/API/Geolocation/watchPosition
      console.log(position);
      const userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
      };

      // 사용자의 현재 위치에 마커 생성 또는 업데이트
      if (marker) {
          // 마커가 이미 존재하면 위치만 업데이트
          marker.postion=userLocation;
      } else {
          // 새로운 마커 생성
          const newmarker = new AdvancedMarkerElement({
            map,
            position:userLocation,
          });
      }

      // 지도의 중앙을 사용자의 현재 위치로 설정
      map.setCenter(userLocation);
  }, function() {
      handleLocationError(true, map.getCenter());
  });
}

function handleLocationError(browserHasGeolocation, pos) {
  console.log(browserHasGeolocation ?
      'Error: The Geolocation service failed.' :
      'Error: Your browser doesn\'t support geolocation.');
}

  
initMap();


  