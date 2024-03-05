$("#form_tag_input").on('keydown',function (event) {
    if (event.which === 13) {
        // Enter 키가 눌렸을 때 실행할 작업을 여기에 추가합니다.
        console.log('Enter key pressed!');
    }
});