const maxTagNameCount = 3; //태그 최대 갯수
let tagNames=new Map();

$("#form_tag_name_input").on('keydown',function (event) {

    if (event.which === 13) {
        // Enter 키가 눌렸을 때 실행할 작업.
        if(tagNames.size >= maxTagNameCount){
            $("#form_tag_name_validation_message").text(`최대 태그 등록수(${maxTagNameCount})에 달하여 더이상 태그를 등록할 수 없습니다.`);
            // commonsAlert(`최대 태그 등록수(${maxTagNameCount})에 달하여 더이상 태그를 등록할 수 없습니다.`);
            $("#form_tag_name_input").val("");
            return;
        }

        let tagName= $("#form_tag_name_input").val().trim();
        
        console.log(tagName);
        if(!validateTagNameFormat(tagName)){
            $("#form_tag_name_validation_message").text(`태그는 앞뒤 여백 제외 1자에서 20자의 한영숫자띄어쓰기 조합으로만 생성할 수 있습니다."`);
            $("#form_tag_name_input").val("");
            return;
        }

        if(tagNames.has(tagName)){
            $("#form_tag_name_validation_message").text(`이미 등록하신 태그입니다.`);
            $("#form_tag_name_input").val("");
            return;
        }

        //todo 태그가 db에 있는지 확인하고 있으면 색깔값 가져올것. 없으면 새로 생성.
        let badgeBgColor= generatePastelColorHex();

        let tagNameBadgeContainer = document.createElement("div");
        tagNameBadgeContainer.classList.add("tooltip", "indicator", "tag_name_badge_container");
        
        let indicatorItem = document.createElement("span");
        indicatorItem.classList.add("indicator-item", "badge", "p-1",  "bg-neutral", "text-base_white", "text-xs", "cursor-pointer");
        let deleteIcon = document.createElement("i");
        deleteIcon.classList.add("fa-solid", "fa-x");
        indicatorItem.appendChild(deleteIcon);

        indicatorItem.addEventListener("click", function(event){
            let deleteTargetTagName = $(this).siblings(".tag_name_badge").text().trim();
            console.log(deleteTargetTagName);
            tagNames.delete(deleteTargetTagName);
            $(this).closest('.tag_name_badge_container').remove();
        });
        
        let tagNameBadge = document.createElement("div");
        tagNameBadge.classList.add("tag_name_badge", "text-sm", "text-neutral", "rounded-full", "px-2", "bg-primary"
        , "w-20", "overflow-ellipsis", "whitespace-nowrap", "overflow-hidden");
        tagNameBadge.style.backgroundColor = badgeBgColor;
        tagNameBadge.innerText = tagName;

        let tooltip = document.createElement("span");
        tooltip.classList.add("tooltiptext");
        tooltip.innerText = tagName;

        tagNameBadgeContainer.appendChild(indicatorItem);
        tagNameBadgeContainer.appendChild(tagNameBadge);
        tagNameBadgeContainer.appendChild(tooltip);

        let tagNameContainer = document.getElementById("tag_name_container");

        tagNameContainer.appendChild(tagNameBadgeContainer);
        
        tagNames.set(tagName, badgeBgColor);
        console.log(tagNames);
        $("#form_tag_name_validation_message").text("");
        $("#form_tag_name_input").val("");
    }
});


function validateTagNameFormat(tagName) {
    var regex = /^[0-9a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ\s]{1,20}$/;
    return regex.test(tagName);
  }


