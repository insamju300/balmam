function changeProfileAvatar(ele) {
  let img_src_data = ele.getAttribute("data-profile-img-src");

  const default_img_path = "/images/avatar/";
  const default_img_extention = ".webp";
  let image_path = default_img_path + img_src_data + default_img_extention;

  if (img_src_data == "upload") {
    $(".hidden-upload").click();
    return;
  }
  $("#form_avatar_img").attr("src", image_path);
}

//만약 이미지가 업로드 되었을 경우.
$(document).on("change", ".hidden-upload", function () {
  //테스트용
  let fileSize = this.files[0].size;
  let nameBits = this.files[0].name.split(".");
  let extension = nameBits.pop().toLowerCase();
  let name = nameBits.join(".");

  if (!fileSize) {
    commonsAlert("파일의 사이즈가 0입니다. 빈 파일은 업로드할 수 없습니다.");
    return;
  }

  if (!name) {
    commonsAlert("이름 또는 확장자가 비어있습니다. 파일 명을 확인해 주세요.");
    return;
  }

  let maxSize = 5 * 1024 * 1024;
  let allowedExtensionList = ["jpg", "jpeg", "png", "gif", "webp"];

  if (!allowedExtensionList.includes(extension)) {
    commonsAlert(
      "지원하지 않는 확장자 입니다. 지원되는 확장자는 다음과 같습니다. <br>.jpg .jpeg .png .gif .webp"
    );
    return;
  }

  if (fileSize > maxSize) {
    commonsAlert("파일의 사이즈가 5MB를 넘습니다. 파일의 용량을 줄여주세요.");
    return;
  }

  //todo ajax처리로 파일 등록후 파일 이미지의 db 번호 가져오기.
  let formData = new FormData();
  formData.append("file", this.files[0]);

  $.ajax({
    url: "/files/upload",
    type: "POST",
    data: formData,
    processData: false, // 필수: FormData를 사용할 때는 false로 설정
    contentType: false, // 필수: FormData를 사용할 때는 false로 설정
    success: function (response) {
      // 파일 업로드 성공 시, 처리
      let mediaFileDto=response.data;
      console.log(mediaFileDto);
      $("#form_avatar_img").attr("src", mediaFileDto.url);
      alert(response.message); // 성공 메시지 표시
      // 추가적으로, 응답으로 받은 데이터를 사용한 로직 구현 가능
      
    },
    error: function (xhr, status, error) {
      // 파일 업로드 실패 시, 처리
      alert("파일 업로드에 실패하였습니다.");
    },
  });
});

function emailValidationCheck() {
  let validationMessage = "";

  let email = $("#form_email_input").val();
  if (email === "") {
    validationMessage = "email 입력은 필수입니다.";
  } else if (!validateEmailFormat(email)) {
    validationMessage = "유효하지 않은 emial 형식입니다.";
  } else if (!isEmailDuplicated(email)) {
    validationMessage = "이미 등록된 email 입니다.";
  }
  $("#form_email_validation_message").text(validationMessage);

  if (validationMessage === "") {
    return true;
  }
  return false;
}
function passwordValidationCheck() {
  let validationMessage = "";
  let password = $("#form_password_input").val();
  if (password === "") {
    validationMessage = "비밀번호 입력은 필수입니다.";
  } else if (!validatePasswordFormat(password)) {
    validationMessage =
      "비밀번호는 대문자, 소문자, 숫자, 특수문자(@$!%*?&)를 포함하여 8~12자여야 합니다. ";
  }
  $("#form_password_validation_message").text(validationMessage);

  if (validationMessage === "") {
    return true;
  }
  return false;
}
function confirmPasswordValidationCheck() {
  let validationMessage = "";
  let password = $("#form_password_input").val();
  let confirmPassword = $("#form_confirm_password_input").val();
  if (confirmPassword === "") {
    validationMessage = "비밀번호 확인은 필수입니다.";
  } else if (password != confirmPassword) {
    validationMessage = "비밀번호와 비밀번호 확인의 값이 일치하지 않습니다.";
  }
  $("#form_confirm_password_validation_message").text(validationMessage);

  if (validationMessage === "") {
    return true;
  }
  return false;
}
function nicknameValidationCheck() {
  let validationMessage = "";
  let nickname = $("#form_nickname_input").val();

  if (nickname === "") {
    validationMessage = "닉네임 입력은 필수입니다.";
  } else if (!validateNicknameFormat(nickname)) {
    validationMessage =
      "닉네임은 숫자, 한글(가-힣), 영문, 특수문자(-, _)만 허용되며, 1~10자까지 사용할 수 있습니다.";
  } else if (!isNicknameDuplicated(nickname)) {
    validationMessage = "이미 등록되어있는 닉네임입니다.";
  }
  $("#form_nickname_validation_message").text(validationMessage);

  if (validationMessage === "") {
    return true;
  }
  return false;
}

function validateEmailFormat(email) {
  var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}$/;
  return regex.test(email);
}

function validatePasswordFormat(password) {
  var regex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,12}$/;
  return regex.test(password);
}

function validateNicknameFormat(nickname) {
  var regex = /^[0-9a-zA-Z가-힣-_]{1,10}$/;
  return regex.test(nickname);
}

function isEmailDuplicated(email) {
  return true; //todo ajax
}
function isNicknameDuplicated(nickname) {
  return true; //todo ajax
}

function modifyFormValidationCheck(ele) {
  let isValidationOk = nicknameValidationCheck() && isValidationOk;

  if (isValidationOk) {
    $(ele).submit();
  }
}

function loginValidationCheck(ele) {
  let email = $("#login_form_email_input").val();
  let password = $("#login_form_password_input").val();

  if (email.trim() === "") {
    setLoginValidationText("이메일을 입력해 주세요.");
    return;
  }
  if (password.trim() === "") {
    setLoginValidationText("비밀번호를 입력해 주세요.");
    return;
  }

  $("#login_form").submit();
}

function setLoginValidationText(msg) {
  $("#login_form_validation_message_container>p").text(msg);
}

function doPasswordRestoration() {
  let email = $("#password_restoration_modal_email_input").val();
  console.log("테스트" + validateEmailFormat(email));
  if (!validateEmailFormat(email)) {
    $("#password_restoration_validation_message_container>p").text(
      "이메일의 형식이 올바르지 않습니다. 다시 확인해주세요."
    );
  } else {
    $("#password_restoration_validation_message_container>p").text("");
    $("#password_restoration_modal_email_input").val("");
    document.getElementById("password_restoration_modal").close();
    //todo 실제로 email 보내는 로직 구현
    sendMailForPasswordRestoration(email);
  }
}

function sendMailForPasswordRestoration(email) {
  //작성자 mail이나 id를 넘겨줄것.
  showEmailAlertForPasswordRestoration(email);
}

function showEmailAlertForPasswordRestoration(email) {
  commonsAlert(
    "비밀번호 변경을 위한 링크를 메일(" +
      email +
      ")로 전송하였습니다. <br> 전송된 링크에서 비밀번호를 변경해 주세요.",
    "메일을 전송하였습니다."
  );
}

function joinFormValidationCheck(ele) {
  let isValidationOk = emailValidationCheck();
  isValidationOk = passwordValidationCheck() && isValidationOk;
  isValidationOk = confirmPasswordValidationCheck() && isValidationOk;
  isValidationOk = nicknameValidationCheck() && isValidationOk;

  if (isValidationOk) {
    $(ele).submit();
  }
}

function passwordRestorationFormValidationCheck(ele) {
  let isValidationOk = passwordValidationCheck() && isValidationOk;
  isValidationOk = confirmPasswordValidationCheck() && isValidationOk;

  if (isValidationOk) {
    $(ele).submit();
  }
}

// todo 회원 가입이 정상적으로 가입되면 다음 메시지를 담아서 historyback or home 화면 보낼것.
// 인증 링크가 이메일로 전송되었습니다. 이메일을 확인하여 계정을 인증해주세요. 만약 이메일을 받지 못하셨다면 스팸 메일함을 확인해보세요.
// header에는 query에 message가 있는 경우 message를 alert로 출력하는 기능을 넣어 줄 것. //추후 개발
