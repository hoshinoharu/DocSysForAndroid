package com.rehoshi.simple.utils;

import android.widget.TextView;

/**
 * Created by hoshino on 2018/12/10.
 */

public class StringUtil {

    public static boolean isNullOrEmpty(TextView textView) {
        return isNullOrEmpty(getText(textView));
    }

    public static String getText(TextView textView) {
        return textView == null ? "" : textView.getText().toString();
    }

    public static Double getDouble(TextView textView) {
        return textView == null ? null : Double.valueOf(textView.getText().toString());
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals("");
    }
}
