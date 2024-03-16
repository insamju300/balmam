package com.smw.project.balmam.utill;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    
    
    public static String getTimeAgo(Timestamp timestamp) {
        Instant timestampToInstant = timestamp.toInstant();
        Instant now = Instant.now();

        // 시간 차이를 계산합니다.
        long secondsAgo = ChronoUnit.SECONDS.between(timestampToInstant, now);
        long minutesAgo = secondsAgo / 60;
        long hoursAgo = minutesAgo / 60;
        long daysAgo = hoursAgo / 24;

        // LocalDateTime으로 변환하여 더 정확한 날짜 차이(월, 년)를 계산합니다.
        LocalDateTime dateTimeThen = LocalDateTime.ofInstant(timestampToInstant, ZoneId.systemDefault());
        LocalDateTime dateTimeNow = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        Period period = Period.between(dateTimeThen.toLocalDate(), dateTimeNow.toLocalDate());
        int monthsAgo = period.getMonths() + period.getYears() * 12;
        int weeksAgo = (int) (daysAgo / 7);
        int yearsAgo = period.getYears();

        // 조건에 따라 적절한 문자열을 반환합니다.
        if (daysAgo < 1) {
            return hoursAgo + "시간 전";
        } else if (daysAgo < 7) {
            return daysAgo + "일 전";
        } else if (daysAgo < 30) {
            return weeksAgo + "주일 전";
        } else if (monthsAgo < 12) {
            return monthsAgo + "달 전";
        } else {
            return yearsAgo + "년 전";
        }
    }
    
    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        return String.format("%d시 %d분 %d초", hours, minutes, secs);
    }
    

    public static String convertTimestampToDateTimeFormattedString (Timestamp timestamp) {
    	DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return FORMATTER.format(dateTime);
    }
}

