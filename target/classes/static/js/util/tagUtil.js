const maxTagNameCount = 3; // 태그 최대 갯수
let tagNames = new Map();


$("#form_tag_name_input").on('keydown', function(event) {
    if (event.which === 13) {
        if (tagNames.size >= maxTagNameCount) {
            $("#form_tag_name_validation_message").text(`최대 태그 등록수(${maxTagNameCount})에 달하여 더이상 태그를 등록할 수 없습니다.`);
            $("#form_tag_name_input").val("");
            return;
        }

        let tagName = $(this).val().trim();
        console.log(tagName);
        if (!validateTagNameFormat(tagName)) {
            $("#form_tag_name_validation_message").text(`태그는 앞뒤 여백 제외 1자에서 20자의 한영숫자띄어쓰기 조합으로만 생성할 수 있습니다.`);
            $(this).val("");
            return;
        }

        if (tagNames.has(tagName)) {
            $("#form_tag_name_validation_message").text(`이미 등록하신 태그입니다.`);
            $(this).val("");
            return;
        }
        
        
        
		$.ajax({
		    url: "/tag/findOrInsertTag?tagName=" + tagName,
		    type: "GET",
		    processData: false,
		    contentType: false
		})
		.then(function(response) {
		    // Success handler
		    let tag = response.data;
		    // todo: response에서 에러 코드 왔을 때 처리 추가할 것.
		   printTag(tag);

		    
		  })
		  .catch(function(xhr, status, error) {
		      // Error handler
		      // todo: handle errors
		      alert("태그 처리에 실패하였습니다.");
		  });
		
		

        $("#form_tag_name_validation_message").text("");
        $("#form_tag_name_input").val("");
    }
});


function getTagsList(){
	return Array.from(tagNames.values());
}


function validateTagNameFormat(tagName) {
    var regex = /^[0-9a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ\s]{1,20}$/;
    return regex.test(tagName);
}

function printTag(tag){
		let badgeBgColor = tag.color;
		let tagName = tag.name;
	    
       let tagNameBadgeContainer = $('<div></div>').addClass("tooltip indicator tag_name_badge_container cursor-pointer");
       
       tagNameBadgeContainer.on("click", function(event) {
           let deleteTargetTagName = $(this).find(".tag_name_badge").text().trim();
            tagNames.delete(deleteTargetTagName);
            $(this).closest('.tag_name_badge_container').remove();
            console.log(deleteTargetTagName);
            console.log(tagNames);

       });
//       let theIndicatorItem = $('<span></span>').addClass("indicator-item badge p-1 bg-neutral text-base_white text-xs cursor-pointer");
//       let theDeleteIcon = $('<i></i>').addClass("fa-solid fa-x");
//       theIndicatorItem.append(theDeleteIcon);
//
//       theIndicatorItem.on("click", function(event) {
//           let deleteTargetTagName = $(this).siblings(".tag_name_badge").text().trim();
//            console.log(deleteTargetTagName);
//            tagNames.delete(deleteTargetTagName);
//            $(this).closest('.tag_name_badge_container').remove();
//       });

       let tagNameBadge = $('<div></div>').addClass("tag_name_badge text-sm text-neutral rounded-full px-2 bg-primary w-20 overflow-ellipsis whitespace-nowrap overflow-hidden").css("backgroundColor", badgeBgColor).text(tagName);

       let tooltip = $('<span></span>').addClass("tooltiptext").text(tagName);

//       tagNameBadgeContainer.append(theIndicatorItem).append(tagNameBadge).append(tooltip);
       
       tagNameBadgeContainer.append(tagNameBadge).append(tooltip);

       $("#tag_name_container").append(tagNameBadgeContainer);

       tagNames.set(tagName, tag); 
       console.log(tagNames);
}
