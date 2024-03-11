$(document).ready(function () {
    // 수정 버튼 클릭 이벤트
    // 수정 버튼 클릭 이벤트 - 이벤트 위임 사용
    $('main').on('click', '.reply_modify_button', function () {
      var replyItem = $(this).closest('.reply_item').first();
      var replyBody = replyItem.find('.reply_body').first();
      var replyModifyContainer = replyItem.find('.reply_modify_container').first();
      var textarea = replyModifyContainer.find('textarea').first();

      // reply_body 내용을 textarea에 설정
      textarea.val(replyBody.text());

      // reply_modify_container 보이게 하고 reply_body 숨김
      replyModifyContainer.removeClass('hidden');
      replyBody.addClass('hidden');

      $(this).closest('.dropdown-content').hide();
    });

    $('main').on('mouseover', '.dropdown', function () {
      $(this).find('.dropdown-content').show();
    });



    $('main').on('click', '.transmission_reply_modify_button', function () {
      // 전송 버튼 클릭 이벤트
      var replyItem = $(this).closest('.reply_item').first();
      var replyBody = replyItem.find('.reply_body').first();
      var replyModifyContainer = replyItem.find('.reply_modify_container').first();
      var textarea = replyModifyContainer.find('textarea').first();

      // textarea의 내용과 reply_item의 ID를 콘솔에 출력
      console.log(textarea.val(), replyItem.data('id'));

      // reply_body 업데이트 및 UI 상태 변경
      replyBody.text(textarea.val());
      replyModifyContainer.addClass('hidden');
      replyBody.removeClass('hidden');
    });

    // 취소 버튼 클릭 이벤트

    $('.cancle_reply_modify_button').on('click', function () {
      var replyItem = $(this).closest('.reply_item').first();
      var replyBody = replyItem.find('.reply_body').first();
      var replyModifyContainer = replyItem.find('.reply_modify_container').first();

      // reply_modify_container 숨기고 reply_body 다시 표시
      replyModifyContainer.addClass('hidden');
      replyBody.removeClass('hidden');
    });



    // 삭제버튼 이벤트.
    $('main').on('click', '.reply_delete_button', function () {
      const replyItem = $(this).closest('.reply_item');
      const replyId = replyItem.data('id');
      const modal = $('#confirm_for_delete_reply_modal');
      modal.attr('data-delete-target-id', replyId);
      modal[0].showModal();
      $(this).closest('.dropdown-content').hide();
    });

    // Close modal on cancel button click
    $('main').on('click', '#delete_reply_modal_cancle_button', function () {
      document.getElementById("confirm_for_delete_reply_modal").close();

    });

    // 모달에서 댓글 삭제 눌렀을 경우 작동
    $('main').on('click', '#delete_reply_modal_delete_button', function () {
      const modal = document.getElementById("confirm_for_delete_reply_modal");
      const replyId = modal.getAttribute('data-delete-target-id');
      console.log(replyId); // Log the id to the console
      const replyItem = document.querySelector(`.reply_item[data-id="${replyId}"]`);
      if (replyItem) {
        replyItem.parentNode.removeChild(replyItem); // Remove the reply item from DOM
      }
      modal.close(); // Close the modal
    });


    $('main').on('click', '.reply_input_open_btn', function () {
      // reply_input_container를 표시합니다.
      $(this).closest(".reply_item").first().find(".reply_input_container").first().removeClass("hidden");
    });

    // cancle_reply_cancle_button 버튼을 클릭하면 이벤트를 처리합니다.
    $('main').on('click', '.cancle_reply_cancle_button', function () {
      // reply_input_container를 숨깁니다.
      $(this).closest(".reply_input_container").first().addClass("hidden");
      // textarea 안의 내용을 지웁니다.
      $(this).closest(".reply_input_container").first().find("textarea").first().val("");
    });

    $('main').on('click', '.show_nested_reply', function () {
      $(this).closest('.reply_item').first().find('.reply_outer_container').children('.reply_container').first().toggle();
      $(this).find('i').toggleClass('fa-caret-down fa-caret-up');
      
      if($(this).data("is-already-opend")===0){
        $(this).closest('.reply_item').first().find('.reply_outer_container').children('.reply_container').first().append(createReplyItems(1));
        $(this).closest('.reply_item').first().find('.reply_outer_container').children('.reply_container').first().append(createShowMoreNestedReplyBtn());
        $(this).data("is-already-opend", 1);
      }
    });

    
    function createShowMoreNestedReplyBtn(){
      let showMoreNestedRplyBtn = $("<a>").addClass("show_more_nested_reply cursor-pointer").text("답글 더보기");
      return showMoreNestedRplyBtn;
    }

    $('main').on('click', '.show_more_nested_reply', function(){
      $(this).closest('.reply_item').first().find('.reply_outer_container').children('.reply_container').first().append(createReplyItems(1));
      $(this).closest('.reply_item').first().find('.reply_outer_container').children('.reply_container').first().append(createShowMoreNestedReplyBtn());
      $(this).remove();

    });

    $('main').on('click', '.transmission_reply_write_button', function(){
    // Find the parent '.reply_input_container' element
    var $replyContainer = $(this).closest('.reply_input_container');

    // Get the data attributes
    var relType = $replyContainer.data('reltype');
    var relId = $replyContainer.data('relid');
    var depth = $replyContainer.data('depth');

    // Get the value of the textarea
    var body = $replyContainer.find('textarea').val();

    // Log the retrieved values to the console (or handle as needed)
    //console.log("RelType: " + relType + ", RelId: " + relId + ", Depth: " + depth + ", Text: " + textAreaValue);
   

    if(depth===0 && relType=='trace'){
      $('.reply_container[data-depth="0"]').prepend(createReplyItem(body, depth));
    }

    if(depth>=1){
      let replyItem = createReplyItem(body, depth);
      let outer_container = $(this).closest('.reply_outer_container[data-depth="1"]');
      outer_container.find('.reply_pre_container').first().prepend(replyItem);
      //todo 어떻게 보여주지?
    }


    })


    // for (let i = 0; i < 15; i++) {
      $('.reply_container[data-depth="0"]').append(createReplyItems());

    // }

    $('.reply_container').on('scroll', function () {
      // Check if the scroll has reached the bottom of the container
      if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight - 10) {
        // Reached the bottom, call createReplyItem and append it to the container
        $(this).append(createReplyItems());
        // replyItems.forEach((replyItem) =>$(this).append(createReplyItem()) );
        
      }
    });

    $('.drawer-side').on('click', function(e) {
        if ($(e.target).is('.drawer-side')) {
          $('#my-drawer').click();
        }
      });

  });

  
  function createReplyItems(depth=0){
    let replyItems = [];
    for (let i = 0; i < 15; i++) {
      replyItems.push(createReplyItem('body', depth));

    }

    return replyItems;
  }



  function createReplyItem(body='body', depth=0) {
    // Generate a random ID for the new reply item
    var replyId = Math.floor(Math.random() * 10000);

    // Creating the main reply item container
    var replyItem = $("<div>").addClass("reply_item py-5").attr("data-id", replyId);
    let innerHtml = `
                      <!-- Sidebar content here (dynamic content) -->
                      <!-- Avatar -->
                      <div class="flex flex-row items-stretch ">
                        <div class="author_avatar_container">

                          <img class="w-10 h-10 rounded-full mx-4 author_avartar"
                            src="https://source.unsplash.com/random/50x50?face" alt="Avatar">
                        </div>
                        <!-- Comment Content -->
                        <div class="w-full">
                          <div class="flex w-full">
                            <div class="flex justify-start flex-row grow gap-1">
                              <div class="text-sm font-semibold text-primary parent_reply_author"></div>
                              <div class="text-sm font-semibold">User nickName</div>
                              <div class="text-gray-500 text-xs">3 hours ago</div>
                            </div>
                            <div class="dropdown dropdown-hover">
                              <div tabindex="0" role="button" class="w-5 flex justify-center"><i
                                  class="fa-solid fa-ellipsis-vertical"></i></div>

                              <ul tabindex="0"
                                class="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-20 -translate-x-3/4">
                                <li class="hover:bg-black hover:text-white reply_modify_button"><a>수정</a></li>
                                <li class="hover:bg-black hover:text-white reply_delete_button"><a>삭제</a></li>
                              </ul>
                            </div>
                          </div>

                          <p class="text-gray-700 mb-2 reply_body">${body}</p>

                          <div class="reply_modify_container hidden">
                            <textarea
                              class="w-full px-3 py-2 bg-transparent border-0 border-b-2 border-secondary_hard  focus:border-secondary focus:outline-none transition duration-300 placeholder-white items-center"
                              rows="1"></textarea>
                            <div class="flex-row flex gap-2">
                              <button
                                class="btn btn-xs flex justify-center border-0 bg-base_white text-secondary_hard
                        hover:bg-secondary_hard hover:text-bg_main text-md mt-2 transmission_reply_modify_button">전송</button>
                              <button class="btn btn-xs flex justify-center border-0 bg-base_white text-secondary_hard
                        hover:bg-secondary_hard hover:text-bg_main text-md mt-2 cancle_reply_modify_button">취소</button>
                            </div>
                          </div>
                          <div class="flex flex-row gap-2 items-center">
                            <div class="flex flex-row text-md items-center  w-20">
                              <div class="w-8 text-center hover:text-primary cursor-pointer"
                                onclick="likeButtonToggleEvent(this); return false" data-reltype="reply" data-relId="${replyId}"
                                data-state="0">
                                <i class="fa-solid fa-heart"></i>
                              </div>
                              <p>2천만</p>
                            </div>

                            <button class="btn btn-xs border-0 bg-base_white text-secondary_hard
                        hover:bg-secondary_hard hover:text-bg_main text-md mt-2 reply_input_open_btn">답글<button>
                          </div>
                          `;

                          if(depth===0){
                            innerHtml +=  `<a href="#" onclick="false" class="show_nested_reply" data-is-already-opend="0"> <i class="fa-solid fa-caret-down"></i></i>
                             답글 50개</a>`;
                            }


                        innerHtml+= `

                        </div>
                      

                      </div>

                      <!-- Replies -->

                      <div class="reply_outer_container pl-14 w-full h-30" data-reltype="reply" data-relId="${replyId}"
                        data-depth="${depth+1}">
                        <div
                          class="reply_input_container w-full h-20 mt-4 gap-1 hidden"
                          data-reltype="reply" data-relId="${replyId}" data-depth="1">
                          <div class="flex flex-row">
                            <div class="avatar user_avatar">
                              <div class="w-9 rounded-full">
                                <img src="/images/avatar/boy.webp" />
                              </div>
                            </div>
                            <textarea
                              class="w-full px-3 py-2 bg-transparent border-0 border-b-2 border-secondary_hard  focus:border-secondary focus:outline-none transition duration-300 placeholder-white items-center "
                              rows="1" placeholder="댓글 추가..."></textarea>
                          </div>
                          <div class="flex flex-row">
                            <button
                              class="btn btn-xs flex justify-center border-0 bg-base_white text-secondary_hard
                      hover:bg-secondary_hard hover:text-bg_main text-md mt-2 transmission_reply_write_button">전송</button>
                            <button class="btn btn-xs flex justify-center border-0 bg-base_white text-secondary_hard
                      hover:bg-secondary_hard hover:text-bg_main text-md mt-2 cancle_reply_cancle_button">취소</button>
                          </div>
                        </div>`;

                        if(depth===0){

                        innerHtml += `<div class="reply_pre_container w-full flex-grow overflow-auto justify-start">

                        </div>`;
                      }




                      innerHtml += `<div class="reply_container w-full flex-grow overflow-auto justify-start hidden" data-reltype="reply" data-relId="${replyId}"
                        data-depth="${depth+1}">      
                            <div class="reply_outer_container pl-14 w-full" data-reltype="reply" data-relId="${replyId}"
                              data-depth="${depth+2}">
                              <div
                                class="reply_input_container w-full h-20 mt-4 gap-1 hidden"
                                data-reltype="reply" data-relId="${replyId}" data-depth="${depth+2}">
                                <div class="flex flex-row">
                                  <div class="avatar user_avatar">
                                    <div class="w-9 rounded-full">
                                      <img src="/images/avatar/boy.webp" />
                                    </div>
                                  </div>
                                  <textarea
                                    class="w-full px-3 py-2 bg-transparent border-0 border-b-2 border-secondary_hard  focus:border-secondary focus:outline-none transition duration-300 placeholder-white items-center "
                                    rows="1" placeholder="댓글 추가..."></textarea>
                                </div>
                                <div class="flex flex-row">
                                  <button
                                    class="btn btn-xs flex justify-center border-0 bg-base_white text-secondary_hard
                            hover:bg-secondary_hard hover:text-bg_main text-md mt-2 transmission_reply_write_button">전송</button>
                                  <button class="btn btn-xs flex justify-center border-0 bg-base_white text-secondary_hard
                            hover:bg-secondary_hard hover:text-bg_main text-md mt-2 cancle_reply_cancle_button">취소</button>
                                </div>
                              </div>
                            </div> 
                          </div>
                        </div>
                      </div>`;
    

    replyItem.html(innerHtml);



    // Ensure interactive elements work - attach events if necessary
    // This step may require further implementation based on how your original event listeners were attached

    return replyItem;
  }