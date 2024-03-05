const deleteTargetMediaContentItems = new Set();
const deleteMediaIds = new Set();


$(document).on("click", ".media_content_item", function () {
  let img_container = $(this).children(".img_container");
  if (!$(img_container).hasClass("ring")) {
    img_container.addClass("ring");
    img_container.addClass("ring-primary");
    deleteTargetMediaContentItems.add(this);
  } else {
    img_container.removeClass("ring");
    img_container.removeClass("ring-primary");
    deleteTargetMediaContentItems.delete(this);
  }
});


function clearDeleteTargetMediaContentItems(){
    let img_container = $("#form_media_content_manager .media_content_item .img_container");
    img_container.removeClass("ring");
    img_container.removeClass("ring-primary");
    deleteTargetMediaContentItems.clear();
}

function deleteSelectedMediaContentItems(event){
    deleteTargetMediaContentItems.forEach((element) => {
        deleteMediaIds.add(element.getAttribute("data-media-id"));
        element.remove();
    }
    );
    deleteTargetMediaContentItems.clear();
    console.log(deleteMediaIds);
}