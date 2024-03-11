const deleteTargetMediaContentItems = new Set();
const deleteMediaIds = new Set();
let featuredImageId = null;

$(document).on("click", ".media_content_item", function () {
  let img_container = $(this).children(".img_container");
  if (!img_container.hasClass("ring")) {
    img_container.addClass("ring ring-primary");
    deleteTargetMediaContentItems.add(this);
  } else {
    img_container.removeClass("ring ring-primary");
    deleteTargetMediaContentItems.delete(this);
  }
});

function clearDeleteTargetMediaContentItems() {
  $("#form_media_content_manager .media_content_item .img_container").removeClass("ring ring-primary");
  deleteTargetMediaContentItems.clear();
}

function deleteSelectedMediaContentItems(event) {
  deleteTargetMediaContentItems.forEach((element) => {
    let jqElement = $(element); // Convert to jQuery object
    deleteMediaIds.add(jqElement.data("media-id")); // Using jQuery data() to get 'data-media-id'
    jqElement.remove(); // jQuery remove
  });
  deleteTargetMediaContentItems.clear();

  let isDeletedFeaturedImage = false;
  deleteMediaIds.forEach((mediaId) => {
    $('#form_candidate_featured_image_manager > .candidate_featured_image').each(function() {
      if ($(this).data('media-id') === mediaId) {
        $(this).remove();
        if(mediaId===featuredImageId){
          isDeletedFeaturedImage=true;

        }
      }
    });
  });

  if(isDeletedFeaturedImage){
    selectTempFeaturedImage();

  }


  console.log(Array.from(deleteMediaIds)); // Convert Set to Array for better logging
}

$(document).ready(function () {
  selectTempFeaturedImage()
});

$(document).on("click", ".candidate_featured_image", function () {
  $(".candidate_featured_image>.indicator-item").remove();

  let indicatorItem =getFeaturedImageIndicatorItem(); 
  $(this).append(indicatorItem);
  featuredImageId=$(this).data('media-id');
  console.log(featuredImageId);
});


function getFeaturedImageIndicatorItem(){
  return  $('<span></span>')
  .addClass("indicator-item indicator-center badge bg-primary")
  .text("대표");
}

function selectTempFeaturedImage(){
  let firstCandidateFeaturedImage = $('#form_candidate_featured_image_manager > .candidate_featured_image').first();
  featuredImageId = firstCandidateFeaturedImage.data('media-id'); // jQuery data() method to get 'data-media-id'

  let indicatorItem =getFeaturedImageIndicatorItem();

  firstCandidateFeaturedImage.append(indicatorItem);
}

function titleValidationCheck() {
  let title = $("#form_title_input").val().trim(); 
  if (title.length === 0) {
    $("#form_title_validation_message").text("제목은 필수 입력입니다.");
    return false;
  }

  if (title.length > 100) {
    $("#form_title_validation_message").text("제목은 앞뒤 여백 제외 0에서 100자까지 입력 할 수 있습니다.");
    return false;
  }

  $("#form_title_validation_message").text("");
  return true;
}

function traceFormValidationCheck(){
  if(!titleValidationCheck()){
    $("#form_title_input").focus();
    return;
  }
  
  //todo ajax post 전송 처리. 또는 form 정리해서 form으로 전송. 
  alert("정상 처리됨.")
}