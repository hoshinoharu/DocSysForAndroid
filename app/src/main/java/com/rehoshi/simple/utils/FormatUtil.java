package com.rehoshi.simple.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hoshino on 2018/12/5.
 */

public class FormatUtil {
    public static String formatMinutes(long mills) {
        int minutes = (int) (mills / 1000 / 60);
        int sec = (int) (mills / 1000 % 60);
        return String.format(Locale.CHINA, "%02d:%02d", minutes, sec);
    }

    public static String formatMillions(Double millions) {
        if (millions == null) {
            return "0";
        } else {
            return formatString("%.2f", millions / 10000);
        }
    }

    public static String formatObject(Object object) {
        String str = null;
        if (object == null) {
            str = "";
        } else {
            str = object.toString();
        }
        return str;
    }

    public static String formatString(String patten, Object... args) {
        return String.format(Locale.CHINA, patten, args);
    }

    public static String formatFloor(int floor) {
        return FormatUtil.formatString("%d层", floor);
    }

    public static String formatArea(Double area) {
        if(area == null){
            return "" ;
        }
        return FormatUtil.formatString("%.2f㎡", area);
    }

    public static String formatHousePrice(boolean isRent, Double price) {
        if (price != null) {
            if (isRent) {
                return FormatUtil.formatString("%.2f元/每月", price);
            } else {
                return FormatUtil.formatString("%.2f万元", price / 10000);
            }
        } else {
            return "";
        }
    }

    public static String formatRoomCount(int roomCount) {
        return FormatUtil.formatString("%d室", roomCount);
    }

    private static SimpleDateFormat dateFormat;

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        String format = "";
        if (date != null) {
            format = getDateFormat(pattern).format(date);
        }
        return format;
    }

    public static Date parseDate(String text) {
        Date date = null;
        if (text != null) {
            try {
                date = getDateFormat().parse(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static SimpleDateFormat getDateFormat() {
        return getDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    private static SimpleDateFormat getDateFormat(String pattern) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    public static String concat(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : objects) {
            stringBuilder.append(formatObject(object));
        }
        return stringBuilder.toString();
    }

}
