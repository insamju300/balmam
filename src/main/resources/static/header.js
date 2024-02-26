function showFullScreenMenu() {
    $('#full_size_slide_menu').addClass("top-0");
    $('#full_size_slide_menu').removeClass("-top-full");

    anime({
        targets: "path",
        strokeDashoffset: [anime.setDashoffset, 0],
        easing: 'easeInOutSine',
        duration: 600,
        delay: function(el, i) { return i * 200 },
        direction: 'nomal',
        loop: false
     });
  }

  function hideFullScreenMenu(e) {
    console.log(e.target.tagName);


    if (e.target.tagName === "DIV") {
      $('#full_size_slide_menu').addClass("-top-full");
      $('#full_size_slide_menu').removeClass("top-0");
    }
  }