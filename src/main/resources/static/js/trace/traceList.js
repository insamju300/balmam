$(document).ready(function () {
  appendToScreenFromTraceCardList(null, null).then(() => {
    observeCards(); // 데이터 로딩과 카드 추가가 완료된 후 카드 관찰을 시작
  });
});

async function appendToScreenFromTraceCardList(lastItemTraceId, lastItemOrderPoint) {
  const traceCardList = await getTraceCardList(lastItemTraceId, lastItemOrderPoint);
  traceCardList.forEach((traceCard) => {
    $("#trace_card_container").append(traceCard);
    // Anime.js 애니메이션 효과를 여기서 적용
    anime({
      targets: traceCard,
      opacity: [0, 1], // 투명에서 불투명으로
      translateY: [50, 0], // 아래에서 위로 이동
      delay: anime.stagger(100), // 연속적인 애니메이션에 대한 지연 시간 설정
      duration: 1000,
      begin: function() {
        $(traceCard).removeClass("hiddenCard");
      }
    });
  });
}


async function getTraceCardList(lastItemTraceId, lastItemOrderPoint) {
  const limit = 16;
  const url = '/trace/traceList';
  const data = { lastItemTraceId, lastItemOrderPoint, limit };
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }

  const resultData = await response.json();
  return resultData.data.map(trace => createTraceCard(trace));
}
////후에 아작스로 처리할 부분
//function getTraceCardList() {
//  const traceCardList = [];
//  for (step = 0; step < 8; step++) {
//    let traceCard = createTraceCard();
//    traceCardList.push(traceCard);
//  }
//
//  return traceCardList;
//}

function createTraceCard(trace) {
  // card 요소 생성
	const card = $("<div>").addClass("card xl:w-1/4 md:w-1/2 w-full p-5 m-0")
	                       .attr("data-id", trace.id)
	                       .attr("data-order-point", trace.orderPoint);
  
  //todo card에 데이터 컬럼 추가.

  // 카드 상단 부분
  const cardTop = $("<div>").addClass("w-full flex justify-between text-md");
  const avatarAndName = $("<div>").addClass("flex items-center");
  const avatar = $("<div>").addClass("avatar");
  const avatarInnerImgContainer = $("<div>").addClass("w-9 rounded-full");
  const avatarInnerImg = $("<img>").attr("src", trace.writerProfileImageUrl);
  avatarInnerImgContainer.append(avatarInnerImg);
  avatar.append(avatarInnerImgContainer);

  const name = $("<a>").attr("href", "/member/detail?id="+trace.writerId).text(trace.writerNickname);
  const time = $("<div>").addClass("flex items-center").html("<p>"+trace.regDate+"</p>");

  avatarAndName.append(avatar, name);
  cardTop.append(avatarAndName, time);

  // 이미지 부분
  const image = $("<div>")
    .addClass("w-full flex justify-center")
    .html(
      '<figure class="w-9/12"><img src="' + trace.featuredImageUrl + '" class="masked"></figure>'
    );

  // 카드 본문
  const cardBody = $("<div>").addClass("card-body p-2");
  const title = $("<div>")
    .addClass(
      "w-full overflow-ellipsis whitespace-nowrap overflow-hidden font-bold text-lg text-center mt-2 text-primary hover:text-primary_hard cursor-pointer"
    )
    .html('<a href="/trace/traceDetail?id='+trace.id+'">' + trace.title + '</a>');

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
    { icon: "fa-eye", count: trace.hitCount },
    { icon: "fa-heart", count: trace.likeCount },
    { icon: "fa-bookmark", count: trace.bookmarkCount },
    { icon: "fa-comment", count: trace.commentCount },
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
  cardBody.append(title, interactionContainer);
//  cardBody.append(title, tagContainer, cityContainer, interactionContainer);
  card.append(cardTop, image, cardBody);

  return card;
}


function observeCards() {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        anime({
          targets: entry.target,
          opacity: [0, 1], // 투명에서 불투명으로
          scale: [0.5, 1], // 크기 축소에서 원래 크기로
          easing: 'easeInOutQuad',
          duration: 800,
          complete: function(anim) {
            $(entry.target).addClass("animated"); // 애니메이션이 적용됨을 표시
          }
        });
        observer.unobserve(entry.target); // 애니메이션 적용 후 관찰 종료
      }
    });
  }, { threshold: 0.1 });

  $("#trace_card_container .card:not(.animated)").each(function () {
    observer.observe(this);
  });
}

$(window).scroll(async function () {
  var documentHeight = $(document).height();
  var scrollPosition = $(window).height() + $(window).scrollTop();
  if (scrollPosition >= documentHeight - 10) {
    const lastCard = $("#trace_card_container .card").last();
    const lastCardId = lastCard.data("id");
    const lastCardOrderPoint = lastCard.data("order-point");
    await appendToScreenFromTraceCardList(lastCardId, lastCardOrderPoint);
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
