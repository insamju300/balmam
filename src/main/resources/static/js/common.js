tailwind.config = {
  theme: {
    extend: {
      colors: {
        primary_hard: "#B3614C",
        primary: "#E07A5F",
        primary_soft: "#FF9272",
        secondary_hard: "#B0918A",
        secondary: "#DDB6AD",
        secondary_soft: "#FFDACF",
        accent_hard: "#998066",
        accent: "#C0A080",
        accent_soft: "#E6C099",
        neutral_hard: "#000000",
        neutral: "#333333",
        neutral_soft: "#666666",
        bg_main_hard: "#CCAF89",
        bg_main: "#FFDBAC",
        bg_main_soft: "#FFFFCE",
        base_white_hard: "#CAC4B5",
        base_white: "#FDF6E3",
        base_white_soft: "#FFFFFF",
      },
    },
  },
};


function generatePastelColorHex() {
  // 랜덤하게 128에서 255 사이의 숫자를 생성하는 함수
  function getRandomPastelValue() {
      return Math.floor(Math.random() * (255 - 128 + 1) + 128);
  }

  // RGB 값을 생성
  const r = getRandomPastelValue();
  const g = getRandomPastelValue();
  const b = getRandomPastelValue();

  // RGB 값을 HEX 코드로 변환. 각각의 컴포넌트를 16진수로 변환하고, 결과가 한 자리수라면 앞에 '0'을 붙입니다.
  const hexColor = "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);

  return hexColor;
}
