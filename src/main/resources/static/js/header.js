function showFullScreenMenu() {
  $("#full_size_slide_menu").addClass("top-0").removeClass("-top-full -translate-y-80");

  anime({
    targets: "path",
    strokeDashoffset: [anime.setDashoffset, 0],
    easing: "easeInOutSine",
    duration: 300,
    delay: function (el, i) {
      return i * 100;
    },
    direction: "normal",
    loop: false,
  });
}

function hideFullScreenMenu(e) {
  if ($(e.target).prop("tagName") === "DIV") {
    $("#full_size_slide_menu").addClass("-top-full -translate-y-80").removeClass("top-0");
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
    direction: "normal",
    loop: false,
  });
}

function commonsAlert(message = "", title = "") {
  $("#commons_alert_modal p").html(message);
  $("#commons_alert_modal h3").html(title);
  $("#commons_alert_modal").showModal();
}

$(window).on("resize", function() {
  let headerClientHeight = $("#navbar").outerHeight();
  $("#ghost_header").height(headerClientHeight);
});

$(window).on('load', function () {
  let headerClientHeight = $("#navbar").outerHeight();
  $("#ghost_header").height(headerClientHeight);
});