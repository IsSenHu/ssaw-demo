package com.ssaw.ssawmehelper.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author HuSen
 * @date 2019/3/22 13:29
 */
public class DateFormatUtil {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");

    /**
     * LocalDate 格式化
     * @param localDate localDate
     * @return 格式化后的字符串
     */
    public static String localDateFormat(LocalDate localDate) {
        return LOCAL_DATE_FORMATTER.format(localDate);
    }

    /**
     * LocalDate 按指定格式进行格式化
     * @param localDate localDate
     * @param format format
     * @return 格式化后的字符串
     */
    public static String localDateFormat(LocalDate localDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDate);
    }
}