package com.smw.project.balmam.utill;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Random;

public class Ut {

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof String) {
			return ((String) obj).trim().length() == 0;
		}

		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}

		return false;
	}
	
    public static String generatePastelColorHex() {
        // 랜덤하게 128에서 255 사이의 숫자를 생성하는 함수
        int r = getRandomPastelValue();
        int g = getRandomPastelValue();
        int b = getRandomPastelValue();

        // RGB 값을 HEX 코드로 변환. 각각의 컴포넌트를 16진수로 변환하고, 결과가 한 자리수라면 앞에 '0'을 붙입니다.
        String hexColor = String.format("#%02x%02x%02x", r, g, b);

        return hexColor;
    }

    private static int getRandomPastelValue() {
        Random rand = new Random();
        return rand.nextInt((255 - 128) + 1) + 128;
    }
}

