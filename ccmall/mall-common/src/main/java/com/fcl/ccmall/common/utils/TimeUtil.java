package com.fcl.ccmall.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static LocalDateTime timeMillisToLocalDateTime(long timeMillis) {
        return LocalDateTime.ofEpochSecond(timeMillis/1000, 0, ZoneOffset.ofHours(8));
    }

    /**
     * 比较与当前日期的大小
     * @param paramDate
     * @return
     */
    public static boolean afterDate(String paramDate) {
        LocalDate localDate = LocalDate.parse(paramDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (LocalDate.now().isAfter(localDate)) {
            return true;
        }
        return false;
    }
}
