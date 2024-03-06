$(document).ready(function () {
  appendToScreenFromTraceCardList();
  observeCards(); // Intersection Observer를 사용해 카드 관찰을 시작하는 함수 호출
});

function appendToScreenFromTraceCardList() {
  const traceCardList = getTraceCardList();
  traceCardList.forEach((traceCard) => {
    $(traceCard).css({ opacity: 0 }).addClass("hiddenCard"); /// 각 카드를 투명하게 처리하고 'hiddenCard' 클래스를 추가
    $("#trace_card_container").append(traceCard);
  });
}

function getTraceCardList() {
  const traceCardList = [];
  for (let step = 0; step < 16; step++) {
    let traceCard = createTraceCard();
    traceCardList.push(traceCard);
  }
  return traceCardList;
}

//후에 아작스로 처리할 부분
function getTraceCardList() {
  const traceCardList = [];
  for (step = 0; step < 8; step++) {
    let traceCard = createTraceCard();
    traceCardList.push(traceCard);
  }

  return traceCardList;
}

function createTraceCard() {
  // card 요소 생성
  const card = $("<div>").addClass("card xl:w-1/4 md:w-1/2 w-full p-5 m-0");
  //todo card에 데이터 컬럼 추가.

  // 카드 상단 부분
  const cardTop = $("<div>").addClass("w-full flex justify-between text-md");
  const avatarAndName = $("<div>").addClass("flex items-center");
  const avatar = $("<div>").addClass("avatar");
  const avatarInnerImgContainer = $("<div>").addClass("w-9 rounded-full");
  const avatarInnerImg = $("<img>").attr("src", "/images/avatar/boy.webp");
  avatarInnerImgContainer.append(avatarInnerImg);
  avatar.append(avatarInnerImgContainer);

  const name = $("<a>").attr("href", "#").text("가장긴이름가장긴이름");
  const time = $("<div>").addClass("flex items-center").html("<p>1일전</p>");

  avatarAndName.append(avatar, name);
  cardTop.append(avatarAndName, time);

  // 이미지 부분
  const image = $("<div>")
    .addClass("w-full flex justify-center")
    .html(
      '<figure class="w-10/12"><img src="/images/etc/etc2.webp" class="masked"></figure>'
    );

  // 카드 본문
  const cardBody = $("<div>").addClass("card-body p-2");
  const title = $("<div>")
    .addClass(
      "w-full overflow-ellipsis whitespace-nowrap overflow-hidden font-bold text-lg text-center mt-2 text-primary hover:text-primary_hard cursor-pointer"
    )
    .html('<a href="#">여행을 떠나보았다고는 하지만 나는 이제부터 무엇을</a>');

  // 태그 컨테이너
  const tagContainer = $("<div>")
    .attr("id", "tag_name_container")
    .addClass("flex-nowrap flex gap-2 justify-center mt-2");
  // 예제 태그들 (실제 사용 시에는 동적으로 생성)
  const tagNames = ["테스트", "긴 글씨를 쓰면 어떻게", "오늘 날씨가 유독 덥다"];
  tagNames.forEach((tagName) => {
    const tag = $("<div>").addClass("tooltip tag_name_badge_container");
    const badge = $("<div>")
      .addClass(
        "tag_name_badge text-sm text-neutral rounded-full px-2 bg-primary w-16 overflow-ellipsis whitespace-nowrap overflow-hidden"
      )
      .css("background-color", "rgb(237, 206, 205)")
      .text(tagName);
    const tooltip = $("<span>").addClass("tooltiptext").text(tagName);
    tag.append(badge, tooltip);
    tagContainer.append(tag);
  });

  // 도시 컨테이너 (이 부분도 동적으로 생성 가능)
  const cityContainer = $("<div>")
    .attr("id", "stayed_city_container")
    .addClass("flex-nowrap flex gap-2 justify-center mt-2");
  // 예제 도시들
  const cityNames = ["도쿄", "교토"];
  cityNames.forEach((cityName) => {
    const city = $("<div>").addClass("tooltip stayed_city_badge_container");
    const badge = $("<div>")
      .addClass(
        "stayed_city_badge text-sm text-neutral rounded-full px-2 bg-primary w-16 overflow-ellipsis whitespace-nowrap overflow-hidden"
      )
      .css("background-color", "rgb(181, 148, 140)")
      .text(cityName);
    const tooltip = $("<span>").addClass("tooltiptext").text(cityName);
    city.append(badge, tooltip);
    cityContainer.append(city);
  });

  // 상호작용 컨테이너
  const interactionContainer = $("<div>")
    .attr("id", "interaction_container")
    .addClass(
      "flex-nowrap flex gap-4 justify-center text-neutral text-lg mt-2"
    );
  // 예제 상호작용 아이콘들
  const interactions = [
    { icon: "fa-eye", count: 12 },
    { icon: "fa-heart", count: 50 },
    { icon: "fa-bookmark", count: 40 },
    { icon: "fa-comment", count: 32 },
  ];
  interactions.forEach((interaction) => {
    const item = $("<div>").addClass(
      "flex gap-1 interaction_item items-center"
    );
    const icon = $("<i>").addClass(`fa-solid ${interaction.icon}`);
    const count = $("<p>").text(interaction.count);
    item.append(icon, count);
    interactionContainer.append(item);
  });

  // 요소들을 card 내부에 추가
  cardBody.append(title, tagContainer, cityContainer, interactionContainer);
  card.append(cardTop, image, cardBody);

  return card;
}

// 카드가 뷰포트에 들어올 때 페이드인 처리를 위해 Intersection Observer API를 사용하는 함수
function observeCards() {
  const observer = new IntersectionObserver(
    (entries, observer) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting && !$(entry.target).hasClass("animated")) {
          // 이미 애니메이션이 적용된 요소는 건너뜁니다.
          $(entry.target).addClass("animated"); // 애니메이션이 적용됨을 표시
          observer.unobserve(entry.target); // 관찰을 종료합니다.

          anime({
            targets: entry.target,
            opacity: [0, 0.8, 1],
            //translateY: [-100, 0 ],
            scale: [0.5, 1.1, 1],
            duration: 1000
          });
        }
      });
    },
    { threshold: 0.1 }
  );

  $(".hiddenCard").each(function () {
    if (!$(this).hasClass("animated")) {
      // 애니메이션이 이미 적용되지 않은 요소만 관찰 대상으로 등록합니다.
      observer.observe(this);
    }
  });
}

// 스크롤 이벤트를 통해 무한 스크롤 기능 구현
$(window).scroll(function () {
  var documentHeight = $(document).height(); // 문서의 전체 높이
  var scrollPosition = $(window).height() + $(window).scrollTop(); // 현재 스크롤 위치
  if (scrollPosition >= documentHeight - 10) {
    // 스크롤이 문서의 끝에 도달했는지 확인
    appendToScreenFromTraceCardList(); // 추가 카드 로드 함수 호출
    observeCards(); // 새로 추가된 카드에 대해 관찰 시작
  }
});

// 무한스크롤 db 고찰
//   방안 1: 복합 키 사용
// 만약 id 이외의 필드(예: date, likes, subscriptions)를 포함하여 정렬하고 있다면, 마지막으로 가져온 데이터의 해당 필드값들을 기억해 두었다가 그 값들을 조합하여 다음 쿼리의 조건으로 사용할 수 있습니다. 예를 들어, '좋아요 수가 많은 순으로 정렬하되, 같은 좋아요 수 내에서는 최신 순으로 정렬한다'라는 조건이 있다면, 마지막으로 가져온 항목의 '좋아요 수'와 '날짜'를 조건으로 사용해야 합니다.

// sql
// Copy code
// SELECT *
// FROM traces
// WHERE (likes < #{lastLikes} OR (likes = #{lastLikes} AND date < #{lastDate}))
// ORDER BY likes DESC, date DESC
// LIMIT #{limit}
