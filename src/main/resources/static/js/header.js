// 전체 화면 메뉴를 표시하는 함수입니다.
function showFullScreenMenu() {
  // 전체 크기 슬라이드 메뉴에 'top-0' 클래스를 추가하여 화면 상단에 위치시킵니다.
  $("#full_size_slide_menu").addClass("top-0");
  // 메뉴가 화면 바깥으로 나가지 않도록 '-top-full' 클래스와 '-translate-y-80'클래스를 제거합니다.
  $("#full_size_slide_menu").removeClass("-top-full");
  $("#full_size_slide_menu").removeClass("-translate-y-80");

  // anime.js를 사용하여 글자가 써지는듯한 효과를 구현합니다.
  anime({
    targets: "path",
    strokeDashoffset: [anime.setDashoffset, 0],
    easing: "easeInOutSine", //애니메이션의 시작과 끝 모두에서 천천히 시작하여 속도를 높이고 끝으로 갈수록 다시 느려지는 부드러운 전환
    duration: 300, // 300밀리초, 즉 0.3초
    delay: function (el, i) {
      return i * 100; // 각 요소의 애니메이션 시작 지연 시간을 설정합니다. 0.1초
    },
    direction: "normal", 
    loop: false,
  });
}

// 전체 화면 메뉴를 숨기는 함수입니다.
function hideFullScreenMenu(e) {
  // 클릭된 요소가 DIV일 경우에만 실행.
  if (e.target.tagName === "DIV") {
    // 메뉴를 화면 상단 바깥으로 이동시키기 위해 '-top-full -translate-y-80' 클래스를 추가합니다.
    $("#full_size_slide_menu").addClass("-top-full");
    $("#full_size_slide_menu").addClass("-translate-y-80");

    // 메뉴가 화면 상단에 고정되지 않도록 'top-0' 클래스를 제거
    $("#full_size_slide_menu").removeClass("top-0");
  }
}

// anime.js를 사용하여 글자가 써지는듯한 효과를 구현
function drowPath(ele) {
  // 주어진 요소의 id를 기반으로 path를 선택합니다.
  let path = "#" + $(ele).attr("id") + " path";
  // anime.js를 사용하여 path 요소의 애니메이션을 설정합니다.
  anime({
    targets: path,
    strokeDashoffset: [anime.setDashoffset, 0],
    easing: "easeInOutSine",
    duration: 500,// 500밀리초, 즉 0.5초
    delay: function (el, i) {
      return i * 200; // 각 요소의 애니메이션 시작 지연 시간을 설정. 0.2초
    },
    direction: "nomal", 
    loop: false,
  });
}

// 공통 경고 모달을 표시하는 함수. alert창 대용으로 사용.
function commonsAlert(message = "", title = "") {
  // 메시지와 제목을 모달에 설정합니다.
  $("#commons_alert_modal p").html(message);
  $("#commons_alert_modal h3").html(title);
  // 모달을 표시합니다.
  document.getElementById("commons_alert_modal").showModal();
}

// 윈도우 크기가 변경될 때마다 실행되는 이벤트 리스너.
// 위치가 고정된 header 뒤쪽으로 main이 말려들어가지 않게 하기 위함.
window.addEventListener("resize", function() {
  // 네비게이션 바의 높이를 계산.
  let headerClientHeight = document.getElementById("navbar").offsetHeight;
  // ghost_header의 높이를 네비게이션 바의 높이와 동일하게 설정.
  $("#ghost_header").height(headerClientHeight);
})

// 윈도우가 로드될 때 실행되는 이벤트입니다.
$(window).on('load', function () {
  // 네비게이션 바의 높이를 계산합니다.
  let headerClientHeight = document.getElementById("navbar").offsetHeight;
  // ghost_header의 높이를 네비게이션 바의 높이와 동일하게 설정합니다.
  $("#ghost_header").height(headerClientHeight);
});