// 테스트 데이터 작성
messageInformation = {
  isValidationFail: true,
  message: "이메일을 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요",
  //비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요.
};

console.log(messageInformation.message);

function init() {
  if (
    messageInformation != null &&
    messageInformation.isValidationFail == true
  ) {
    setValidationText(messageInformation.message);
  }
}

init();
