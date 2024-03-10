let map;
let marker;
let pathLine;
let pathLines = [];
let pathCoordinatesGroups;
let geoMedias;

initMap();

async function initMap() {
  const { Map } = await google.maps.importLibrary("maps");
  const jsonFilePath = "/testData/traceData.json"; // Update the path to your JSON file
  const jsonData = await loadJsonFile(jsonFilePath);
  pathCoordinatesGroups = jsonData.pathCoordinatesGroups;
  geoMedias = jsonData.geoMedias;
  let mapId = "originMapId1";

  map = new Map(document.getElementById("map"), {
    center: pathCoordinatesGroups[0][0],
    zoom: 20,
    mapId: mapId,
  });

  google.maps.event.addListenerOnce(map, "idle", async () => {
    await animatePaths();
    adjustMapZoom();
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
  const bounds = new google.maps.LatLngBounds();
  pathLines.forEach((polyline) => {
    polyline.getPath().forEach((point) => {
      bounds.extend(point);
    });
  });
  map.fitBounds(bounds);
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
    const step = path.length / 25; // 경로 길이를 기반으로 애니메이션 단계를 계산합니다.
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
        map.setZoom(20);
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
async function createMarkers() {
  const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary(
    "marker"
  );

  Object.keys(geoMedias).forEach((location) => {
    const { lat, lng } = JSON.parse(location);
    const medias = geoMedias[location];

    let img = document.createElement("img");
    let src = null;
    console.log(src === null);

    medias.forEach((media) => {
      if (media.mediaType != "video" && src === null) {
        src = media.url;
      }
    });

    img.src = src;

    img.style.width = "100%";
    img.style.height = "100%";

    const pin = new PinElement({
      glyph: img,
      background: "#E07A5F",
      borderColor: "#B3614C",
    });

    let mediaMarker = new AdvancedMarkerElement({
      map,
      position: {
        lat: lat,
        lng: lng,
      },
      content: pin.element,
    });


    // const element = mediaMarker.content;
    // console.log(element)
    // element.style.opacity = '0';
    // element.addEventListener('animationend', (event) => {
    //   element.classList.remove('drop');
    //   element.style.opacity = '1';
    // });
    //intersectionObserver.observe(element);



    // medias.forEach(media => {
    //   const marker = new Marker({
    //     position: { lat, lng },
    //     map: map,
    //   });

    //   const infoWindow = new InfoWindow({
    //     content: generateContentForMedia(media),
    //   });

    //   marker.addListener('click', () => {
    //     infoWindow.open(map, marker);
    //   });
  });
}

function createSlideImageForMediaMarker(geoMediaList, mediaMarker) {
  let slideIdCount = 1;
  let carousel = document.createElement("div");

  carousel.classList.add("carousel", "w-full");

  const geoMediaListWithoutThumbnail = geoMediaList.filter(
    (geoMedia) => geoMedia.mediaType != "thumbnail"
  );

  geoMediaListWithoutThumbnail.forEach(function (geoMedia) {
    if (geoMedia.mediaType === "video") {
      const carouselItem = document.createElement("div");
      carouselItem.classList.add("carousel-item", "relative", "w-full");
      carouselItem.id = "slide" + slideIdCount++;

      const videoElement = document.createElement("video");
      videoElement.src = geoMedia.url;
      videoElement.controls = true;
      videoElement.classList.add("w-full", "h-auto");
      carouselItem.appendChild(videoElement);
      if (geoMediaListWithoutThumbnail.length > 1) {
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
          beforSlideId = geoMediaListWithoutThumbnail.length;
        }
        let afterSlideId = slideIdCount;
        if (afterSlideId > geoMediaListWithoutThumbnail.length) {
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
    } else if (geoMedia.mediaType === "photo") {
      const carouselItem = document.createElement("div");
      carouselItem.classList.add("carousel-item", "relative", "w-full");
      carouselItem.id = "slide" + slideIdCount++;

      const img = document.createElement("img");
      img.src = geoMedia.url;
      img.classList.add("w-full", "h-auto");
      carouselItem.appendChild(img);

      if (geoMediaListWithoutThumbnail.length > 1) {
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
          beforSlideId = geoMediaListWithoutThumbnail.length;
        }
        let afterSlideId = slideIdCount;
        if (afterSlideId > geoMediaListWithoutThumbnail.length) {
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
