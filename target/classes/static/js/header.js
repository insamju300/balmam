function showFullScreenMenu() {
  $("#full_size_slide_menu").addClass("top-0");
  $("#full_size_slide_menu").removeClass("-top-full");
  $("#full_size_slide_menu").removeClass("-translate-y-80");

  anime({
    targets: "path",
    strokeDashoffset: [anime.setDashoffset, 0],
    easing: "easeInOutSine",
    duration: 300,
    delay: function (el, i) {
      return i * 100;
    },
    direction: "nomal",
    loop: false,
  });
}

function hideFullScreenMenu(e) {
  if (e.target.tagName === "DIV") {
    $("#full_size_slide_menu").addClass("-top-full");
    $("#full_size_slide_menu").addClass("-translate-y-80");

    $("#full_size_slide_menu").removeClass("top-0");
  }
}

function drowPath(ele) {
  let path = "#" + $(ele).attr("id") + " path";
  anime({
    targets: path,
    strokeDashoffset: [anime.setDashoffset, 0],
    easing: "easeInOutSine",
    duration: 500,
    delay: function (el, i) {
      return i * 200;
    },
    direction: "nomal",
    loop: false,
  });
}

function commonsAlert(message = "", title = "") {
  $("#commons_alert_modal p").html(message);
  $("#commons_alert_modal h3").html(title);
  document.getElementById("commons_alert_modal").showModal();
}

window.addEventListener("resize", function() {
  let headerClientHeight = document.getElementById("navbar").offsetHeight;
  $("#ghost_header").height(headerClientHeight);
})

$(window).on('load',function () {
  let headerClientHeight = document.getElementById("navbar").offsetHeight;
  $("#ghost_header").height(headerClientHeight);
 
});