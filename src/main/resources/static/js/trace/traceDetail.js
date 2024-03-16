let map;
let marker;
let pathLine;
let pathLines = [];
//let pathCoordinatesGroups;
//let geoMedias;
let geoMediaMarkers = [];
let intervalId;

initMap();

async function tracePlay() {
  //마커는 투명도만 조정하면 된다.
  //path는 다시 그려야된다.
  pathLines.forEach((pathLine) => {
    pathLine.setMap(null);
    pathLine = null;
  });
  pathLine = [];

  geoMediaMarkers.forEach((mediaMarker) => {
    deleteAnimationToMarker(mediaMarker);
    mediaMarker.content.classList.add("opacity-0");
  });

  await animatePaths();
  await adjustMapZoom();
  //await createMarkers();
  geoMediaMarkers.forEach((mediaMarker) => {
    applyAnimationToMarker(mediaMarker);

    //setTimeout(() => applyAnimationToMarker(mediaMarker), 50);
  });
}

async function initMap() {
  const { Map } = await google.maps.importLibrary("maps");
  const jsonFilePath = "/testData/traceData.json"; // Update the path to your JSON file
  const jsonData = await loadJsonFile(jsonFilePath);
//  pathCoordinatesGroups = jsonData.pathCoordinatesGroups;
//  const pathCoordinatesGroupsElement = document.getElementById("pathCoordinatesGroupsData");
//  const pathCoordinatesGroups = JSON.parse(pathCoordinatesGroupsElement.getAttribute('data-path-coordinates-groups'));
  console.log(pathCoordinatesGroups);

//  geoMedias = jsonData.geoMedias;
  let mapId = "originMapId1";

  map = new Map(document.getElementById("map"), {
    center: pathCoordinatesGroups[0][0],
    zoom: 19,
    mapId: mapId,
  });

  google.maps.event.addListenerOnce(map, "idle", async () => {
    await animatePaths();
    await adjustMapZoom();
    await createMarkers();
  });
}

async function animatePaths() {
  for (const path of pathCoordinatesGroups) {
    pathLine = new google.maps.Polyline({
      path: [],
      strokeColor: "#FF0000",
      strokeOpacity: 1.0,
      strokeWeight: 2,
    });
    pathLine.setMap(map);
    await animatePath(path);
    pathLines.push(pathLine);
  }
}

//경로 재생 후 폴리라인의 모든 점을 포함하도록 경계를 확장
function adjustMapZoom() {
  return new Promise((resolve) => {
    const bounds = new google.maps.LatLngBounds();
    pathLines.forEach((polyline) => {
      polyline.getPath().forEach((point) => {
        bounds.extend(point);
      });
    });
    map.fitBounds(bounds);
    google.maps.event.addListenerOnce(map, "bounds_changed", () => {
      resolve(); // bounds 변경이 완료되면 Promise를 해결합니다.
    });
  });
}

// function animatePaths() {
//   // 경로들을 순차적으로 애니메이션 처리하기 위한 비동기 함수를 정의합니다.
//   (async () => {
//     // 각 경로 좌표 그룹을 순회합니다.
//     for (const path of pathCoordinatesGroups) {
//       // 현재 경로에 대한 새로운 폴리라인을 지도에 생성합니다.
//       pathLine = new google.maps.Polyline({
//         path: [], // 경로를 빈 배열로 시작합니다.
//         strokeColor: '#FF0000', // 선의 색상입니다.
//         strokeOpacity: 1.0, // 선의 불투명도입니다.
//         strokeWeight: 2, // 선의 두께입니다.
//       });
//       // 폴리라인을 지도에 추가합니다.
//       pathLine.setMap(map);

//       // 현재 경로의 애니메이션이 완료될 때까지 기다립니다.
//       await animatePath(path);
//       pathLines.push(pathLine);
//     }
//   })(); // 정의한 비동기 함수를 즉시 호출합니다.
//   }

function doDeleteTrace() {
  //todo
  commonsAlert("여기에 삭제 처리넣을 것");
  document.getElementById("confirm_for_delete_trace_modal").close();
}

async function loadJsonFile(filePath) {
  try {
    const response = await fetch(filePath);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error loading JSON:", error);
  }
}

// 주어진 경로를 애니메이션하는 함수를 정의합니다.
function animatePath(path) {
  // 애니메이션이 완료되면 해결되는 프로미스를 반환합니다.
  return new Promise((resolve) => {
    const step = path.length / 50; // 경로 길이를 기반으로 애니메이션 단계를 계산합니다.
    let index = 0; // 애니메이션 진행 상황을 추적하기 위해 인덱스를 초기화합니다.

    // 다음 세그먼트를 그리기 위한 함수를 정의합니다.
    function drawNextSegment() {
      // 더 그릴 세그먼트가 있는지 확인합니다.
      if (index < path.length) {
        // 그려질 현재 경로 세그먼트를 계산합니다.
        const currentSegment = path.slice(0, Math.floor(index + 1));
        // 폴리라인 경로를 현재 세그먼트로 업데이트합니다.
        pathLine.setPath(currentSegment);
        // 현재 세그먼트의 마지막 위치를 가져옵니다.
        const currentPos = currentSegment[currentSegment.length - 1];
        // 지도 중심을 최신 위치로 설정합니다.
        map.setCenter(currentPos);
        // 상세한 뷰를 위해 확대합니다.
        map.setZoom(19);
        // 다음 애니메이션 프레임을 위해 인덱스를 증가시킵니다.
        index += step;
        // 다음 세그먼트를 그리기 위해 다음 애니메이션 프레임을 요청합니다.
        requestAnimationFrame(drawNextSegment);
      } else {
        // 모든 세그먼트가 그려지면 폴리라인 경로를 완성된 경로로 설정합니다.
        pathLine.setPath(path);
        // 경로의 마지막 위치를 가져옵니다.
        const lastPos = path[path.length - 1];
        // 지도 중심을 마지막 위치로 설정합니다.
        map.setCenter(lastPos);

        // 애니메이션이 완료되었음을 나타내기 위해 프로미스를 해결합니다.
        resolve();
      }
    }

    // 경로 세그먼트 그리기를 시작합니다.
    drawNextSegment();
  });
}
// const intersectionObserver = new IntersectionObserver((entries) => {
//   for (const entry of entries) {
//     if (entry.isIntersecting) {
//       entry.target.classList.add('drop');
//       intersectionObserver.unobserve(entry.target);
//     }
//   }
// });
//마커 생성
// Google Maps 라이브러리에서 필요한 마커 관련 요소를 비동기적으로 불러옵니다.
async function createMarkers() {
  // Google Maps 마커 라이브러리에서 필요한 요소들을 가져옵니다.
  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary(
    "marker"
  );

  // geoMedias 객체의 키(위치 정보)를 순회합니다.
    geoMedias.forEach((geoMedia) => {
    // 위치 정보 문자열을 객체로 파싱합니다.
    const { lat, lng } = geoMedia.coordinate;
    // 현재 위치에 해당하는 미디어 목록을 가져옵니다.
    const medias = geoMedia.mediaFiles;




    // 이미지 요소를 생성합니다.
    let img = document.createElement("img");
    // 이미지의 소스를 담을 변수를 null로 초기화합니다.
    let src = null;
    // src가 null인지 확인하는 로그를 출력합니다.
    console.log(src === null);

    // 현재 위치의 미디어 목록을 순회합니다.
    let firstMedia = medias[0];
    if(firstMedia.type == "video"){
		src = firstMedia.thumbnailUrl;
	}
	if(firstMedia.type == "image"){
		src = firstMedia.url;
	}
//    medias.forEach((media) => {
//      // 미디어 타입이 비디오가 아니고, src가 아직 null일 때
//      if (media.type != "video" && src === null) {
//        // src에 미디어 URL을 할당합니다.
//        src = media.url;
//      }
//    });

    // 이미지의 소스를 설정합니다.
    img.src = src;

    // 이미지의 스타일을 설정합니다.
    img.style.width = "100%";
    img.style.height = "100%";

    // PinElement를 생성합니다.
    const pin = new PinElement({
      glyph: img, // 이미지를 글리프로 사용합니다.
      background: "#E07A5F", // 배경색을 설정합니다.
      borderColor: "#B3614C", // 테두리 색을 설정합니다.
    });

    // AdvancedMarkerElement로 마커를 생성합니다.
    let mediaMarker = new AdvancedMarkerElement({
      map, // 지도 객체
      position: {
        // 마커의 위치
        lat: lat,
        lng: lng,
      },
      content: pin.element, // 마커의 내용
    });

    // 마커의 내용(DOM 요소)을 가져옵니다.
    const content = mediaMarker.content;

    applyAnimationToMarker(mediaMarker);

    createSlideImageForMediaMarker(medias, mediaMarker);

    geoMediaMarkers.push(mediaMarker);
  });
}

function applyAnimationToMarker(mediaMarker) {
  // 'animated-marker' 클래스를 마커의 content에 추가합니다.
  // 이 클래스는 CSS에서 미리 정의된 애니메이션 효과를 적용합니다.
  mediaMarker.content.classList.add("animated-marker");
}

function deleteAnimationToMarker(mediaMarker) {
  // 'animated-marker' 클래스를 마커의 content에 추가합니다.
  // 이 클래스는 CSS에서 미리 정의된 애니메이션 효과를 적용합니다.
  mediaMarker.content.classList.remove("animated-marker");
}

function createSlideImageForMediaMarker(geoMediaList, mediaMarker) {
	console.log(geoMediaList);
  let slideIdCount = 1;
  let carousel = document.createElement("div");

  carousel.classList.add("carousel", "w-full");

//  const geoMediaListWithoutThumbnail = geoMediaList.filter(
//    (geoMedia) => geoMedia.mediaType != "thumbnail"
//  );

  geoMediaList.forEach(function (geoMedia) {
    if (geoMedia.type === "video") {
      const carouselItem = document.createElement("div");
      carouselItem.classList.add("carousel-item", "relative", "w-full");
      carouselItem.id = "slide" + slideIdCount++;

      const videoElement = document.createElement("video");
      videoElement.src = geoMedia.url;
      videoElement.controls = true;
      videoElement.classList.add("w-full", "h-auto");
      carouselItem.appendChild(videoElement);
      if (geoMediaList.length > 1) {
        const carouselBtns = document.createElement("div");
        carouselBtns.classList.add(
          "absolute",
          "flex",
          "justify-between",
          "transform",
          "-translate-y-1/2",
          "left-5",
          "right-5",
          "top-1/2"
        );
        let beforSlideId = slideIdCount - 2;
        if (beforSlideId < 1) {
          beforSlideId = geoMediaList.length;
        }
        let afterSlideId = slideIdCount;
        if (afterSlideId > geoMediaList.length) {
          afterSlideId = 1;
        }

        let beforBtn = document.createElement("a");
        beforBtn.href = "#slide" + beforSlideId;
        beforBtn.classList.add("btn", "btn-circle");
        beforBtn.innerText = "❮";

        let afterBtn = document.createElement("a");
        afterBtn.href = "#slide" + afterSlideId;
        afterBtn.classList.add("btn", "btn-circle");
        afterBtn.innerHTML = "❯";

        carouselBtns.appendChild(beforBtn);
        carouselBtns.appendChild(afterBtn);

        carouselItem.appendChild(carouselBtns);
      }
      carousel.appendChild(carouselItem);
    } else if (geoMedia.type === "image") {
      const carouselItem = document.createElement("div");
      carouselItem.classList.add("carousel-item", "relative", "w-full");
      carouselItem.id = "slide" + slideIdCount++;

      const img = document.createElement("img");
      img.src = geoMedia.url;
      img.classList.add("w-full", "h-auto");
      carouselItem.appendChild(img);

      if (geoMediaList.length > 1) {
        const carouselBtns = document.createElement("div");
        carouselBtns.classList.add(
          "absolute",
          "flex",
          "justify-between",
          "transform",
          "-translate-y-1/2",
          "left-5",
          "right-5",
          "top-1/2"
        );
        let beforSlideId = slideIdCount - 2;
        if (beforSlideId < 1) {
          beforSlideId = geoMediaList.length;
        }
        let afterSlideId = slideIdCount;
        if (afterSlideId > geoMediaList.length) {
          afterSlideId = 1;
        }

        let beforBtn = document.createElement("a");
        beforBtn.href = "#slide" + beforSlideId;
        beforBtn.classList.add("btn", "btn-circle");
        beforBtn.innerText = "❮";

        let afterBtn = document.createElement("a");
        afterBtn.href = "#slide" + afterSlideId;
        afterBtn.classList.add("btn", "btn-circle");
        afterBtn.innerHTML = "❯";

        carouselBtns.appendChild(beforBtn);
        carouselBtns.appendChild(afterBtn);

        carouselItem.appendChild(carouselBtns);
      }
      carousel.appendChild(carouselItem);
    }
  });

  mediaMarker.addListener("click", ({ domEvent, latLng }) => {
    const { target } = domEvent;

    commonsAlert(carousel.outerHTML);
  });
}

$(document).ready(function () {
  // Edit button click event
  // Edit button click event - use event delegation
  $("main").on("click", "#all_mideas_carousels_modal .avatar", function () {
    $("#all_mideas_carousels_modal .avatar").removeClass("ring ring-primary");
    $(this).addClass("ring ring-primary");
    setImageViewer($(this).find("img"));

    // Replace the src attribute of img within .image-viewer with the src of the clicked img
//    var clickedImgSrc = $(this).find("img").attr("src");
//
//    $("#all_mideas_carousels_modal .image-viewer img").attr(
//      "src",
//      clickedImgSrc
//    );
  });
});

function setImageViewer(imgEle){
	var dataType = imgEle.attr('data-type');

	if(dataType==="image"){
		var imgSrc = imgEle.attr('src');
        var imageView = $('<img>').attr('src', imgSrc);
        $('.image-viewer').empty().append(imageView);
	}else if(dataType==="video"){
		var videoSrc = imgEle.attr('data-video-src');
        var videoElement = $('<video>').attr({
            'src': videoSrc,
            'autoplay': 'autoplay',
            'controls': 'controls'
        });
        $('.image-viewer').empty().append(videoElement);
	}
}

//모든 미디어를 담고있는 모달창 열기
function showAllMideasCarouselsModal() {
  var modal = document.querySelector("#all_mideas_carousels_modal");
  var firstImg = $(".media-files > .avatar:first-child > div > img");
  if(!firstImg.length){
	  return;
  }
  
  setImageViewer(firstImg);
  

  modal.showModal();
  
  
  
  

  // Scroll to the top - 수정된 부분
  document.querySelector(
    "#all_mideas_carousels_modal .modal-box"
  ).scrollTop = 0;
}



//유저 현재 위치 보여주기
async function showUserCurrentLocation() {
  const { AdvancedMarkerElement} = await google.maps.importLibrary(
    "marker"
  );

  const balmami = document.createElement("img");
  balmami.src = "/images/character/balmami.png";
  balmami.classList.add("h-10");
  balmami.classList.add("drop-shadow-lg");

  navigator.geolocation.getCurrentPosition(

    function (position) {
      const userLocation = getUserLocation(position);

      if(!marker){
      marker = new AdvancedMarkerElement({
        map,
        position: userLocation,
        content: balmami,
      });
    }else{
      marker.position = userLocation;
    }

      map.setCenter(userLocation);
    }
  );

  intervalId = setInterval(() => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userLocation = getUserLocation(position);
        // 위치 정보가 성공적으로 가져와졌을 때의 처리
        // //사용자의 현재 위치로 마커 업데이트
        marker.position = userLocation;
      },
      (error) => {
        // 위치 정보를 가져오는데 실패했을 때의 처리
        console.error("Error Getting Location: ", error);
      }
    );
  }, 1000); 


  
}

function getUserLocation(position) {
  const userLocation = {
    lat: position.coords.latitude,
    lng: position.coords.longitude,
    time: Date.now(),
  };

  return userLocation;
}