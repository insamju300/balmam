tailwind.config = {
  theme: {
    extend: {
      colors: {
        primary_hard: "#B3614C",
        primary: "#E07A5F",
        primary_soft: "#FF9272",
        secondary_hard: "#B0918A",
        secondary: "#DDB6AD",
        secondary_soft: "#FFDACF",
        accent_hard: "#998066",
        accent: "#C0A080",
        accent_soft: "#E6C099",
        neutral_hard: "#000000",
        neutral: "#333333",
        neutral_soft: "#666666",
        bg_main_hard: "#CCAF89",
        bg_main: "#FFDBAC",
        bg_main_soft: "#FFFFCE",
        base_white_hard: "#CAC4B5",
        base_white: "#FDF6E3",
        base_white_soft: "#FFFFFF",
      },
    },
  },
};

function generatePastelColorHex() {
  // 랜덤하게 128에서 255 사이의 숫자를 생성하는 함수
  function getRandomPastelValue() {
    return Math.floor(Math.random() * (255 - 128 + 1) + 128);
  }

  // RGB 값을 생성
  const r = getRandomPastelValue();
  const g = getRandomPastelValue();
  const b = getRandomPastelValue();

  // RGB 값을 HEX 코드로 변환. 각각의 컴포넌트를 16진수로 변환하고, 결과가 한 자리수라면 앞에 '0'을 붙입니다.
  const hexColor =
    "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);

  return hexColor;
}

//좋아요 버튼 이벤트
function likeButtonToggleEvent(ele) {
  // Get data attributes
  var relType = $(ele).data("reltype");
  var relId = $(ele).data("relid");
  var state = $(ele).data("state");

  // Log data to console
  console.log("relType:", relType);
  console.log("relId:", relId);
  console.log("state:", state);

  // Get the sibling p element
  var siblingP = $(ele).next("p");

  // Check the state
  if (state === 0) {
    // Toggle classes based on state 0
    var faSolid = $(ele).find("i.fa-solid");
    faSolid.addClass("text-primary");
    faSolid.removeClass("text-neutral");

    // Update data-state attribute
    $(ele).data("state", 1);
  } else if (state === 1) {
    // Toggle classes based on state 1
    var faSolid = $(ele).find("i.fa-solid");
    faSolid.addClass("text-neutral");
    faSolid.removeClass("text-primary");

    // Update data-state attribute
    $(ele).data("state", 0);
  }

  // Update sibling p element with random number between 1 and 100
  var randomNumber = Math.floor(Math.random() * 100) + 1;
  siblingP.text(randomNumber);
}

//북마크 버튼 이벤트
function bookmarkButtonToggleEvent(ele) {
  // Get data attributes
  var relType = $(ele).data("reltype");
  var relId = $(ele).data("relid");
  var state = $(ele).data("state");

  // Log data to console
  console.log("relType:", relType);
  console.log("relId:", relId);
  console.log("state:", state);

  // Get the sibling p element
  var siblingP = $(ele).next("p");

  // Check the state
  if (state === 0) {
    // Toggle classes based on state 0
    var faSolid = $(ele).find("i.fa-solid");
    faSolid.addClass("text-secondary");
    faSolid.removeClass("text-neutral");

    // Update data-state attribute
    $(ele).data("state", 1);
  } else if (state === 1) {
    // Toggle classes based on state 1
    var faSolid = $(ele).find("i.fa-solid");
    faSolid.addClass("text-neutral");
    faSolid.removeClass("text-secondary");

    // Update data-state attribute
    $(ele).data("state", 0);
  }

  // Update sibling p element with random number between 1 and 100
  var randomNumber = Math.floor(Math.random() * 100) + 1;
  siblingP.text(randomNumber);
}

function subscriptButtonToggleEvent(ele) {
  var memberId = $(ele).data("memberId");
  var state = $(ele).data("state");

  if (state === 0) {
    // Toggle classes based on state 0
    $(ele)
      .text("구독 취소")
      .addClass(
        "bg-secondary_hard text-bg_main hover:bg-base_white hover:text-secondary_hard"
      )
      .removeClass(
        "bg-base_white text-secondary_hard hover:bg-secondary_hard hover:text-bg_main"
      );

    // Update data-state attribute
    $(ele).data("state", 1);
  } else if (state === 1) {
    // Toggle classes based on state 1
    $(ele)
      .text("구독")
      .removeClass(
        "bg-secondary_hard text-bg_main hover:bg-base_white hover:text-secondary_hard"
      )
      .addClass(
        "bg-base_white text-secondary_hard hover:bg-secondary_hard hover:text-bg_main"
      );

    // Update data-state attribute
    $(ele).data("state", 0);
  }
}
