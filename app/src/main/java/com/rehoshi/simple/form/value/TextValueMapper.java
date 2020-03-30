package com.rehoshi.simple.form.value;

import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.rehoshi.simple.form.FormField;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hoshino on 2019/3/3.
 */

public class TextValueMapper extends ValueMapper<TextView> {

    private SimpleDateFormat dateFormat;

    public TextValueMapper(TextView view, Object target, Field field, FormField formField) {
        super(view, target, field, formField);
    }

    @Override
    protected MapInfo mapValue(TextView view, Class fieldClass, Field field, Object target, FormField formField) {
        MapInfo mapInfo = new MapInfo();
        mapInfo.fieldNameTag = formField.fieldName();
        if (view.getVisibility() == View.VISIBLE) {//可见才去映射

            String string = view.getText().toString();


            if (formField.trim()) {
                if (string.startsWith(" ") || string.endsWith(" ")) {
                    mapInfo.result = MapInfo.Result.FORMAT_ERROR;
                    mapInfo.setMsg(String.format(Locale.CHINA, "%s前后不能输入空格", mapInfo.fieldNameTag));
                    return mapInfo;
                }
            }

            int minLength = formField.minLength();
            if (string.length() < minLength) {
                if (string.length() < 1) {
                    //表示 不能输入为空
                    mapInfo.result = MapInfo.Result.EMPTY_INPUT;
                } else {
                    mapInfo.result = MapInfo.Result.FORMAT_ERROR;
                    //比较长度限制 如果 最小和最大一样 改变一下描述
                    if (minLength == formField.maxLength()) {
                        mapInfo.setMsg(String.format(Locale.CHINA, "%s长度在%d位", mapInfo.fieldNameTag, minLength));
                    } else {
                        mapInfo.setMsg(String.format(Locale.CHINA, "%s长度在%d位到%d位之间", mapInfo.fieldNameTag, minLength, formField.maxLength()));
                    }
                }
            } else {
                if (String.class.isAssignableFrom(fieldClass)) {
                    boolean flag = false;
                    if (view.getInputType() == InputType.TYPE_CLASS_PHONE) {//手机输入
                        String phoneRegex = "1[3456789][0-9]{9}";
                        if (string.matches(phoneRegex)) {
                            flag = true;
                        }
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        try {
                            field.set(target, string);
                            mapInfo.result = MapInfo.Result.SUCCESS;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (Integer.class.isAssignableFrom(fieldClass) || int.class.isAssignableFrom(fieldClass)) {

                    Integer integer = Integer.valueOf(string);
                    if (formField.notZero() && 0 == integer) {
                        mapInfo.result = MapInfo.Result.FORMAT_ERROR;
                        mapInfo.setMsg(String.format(Locale.CHINA, "%s不能为0", mapInfo.fieldNameTag));
                    } else {
                        try {
                            field.set(target, integer);
                            mapInfo.result = MapInfo.Result.SUCCESS;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (Double.class.isAssignableFrom(fieldClass) || double.class.isAssignableFrom(fieldClass)) {
                    if (string.length() > 12) {
                        mapInfo.result = MapInfo.Result.FORMAT_ERROR;
                        mapInfo.setMsg(String.format(Locale.CHINA, "%s长度不能超过%d位", mapInfo.fieldNameTag, 12));
                    } else {
                        Double d = Double.valueOf(string);
                        if (formField.notZero() && 0 == d) {
                            mapInfo.result = MapInfo.Result.FORMAT_ERROR;
                            mapInfo.setMsg(String.format(Locale.CHINA, "%s不能为0", mapInfo.fieldNameTag));
                        } else {
                            try {
                                field.set(target, d);
                                mapInfo.result = MapInfo.Result.SUCCESS;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (Date.class.isAssignableFrom(fieldClass)) {
                    if (dateFormat == null) {
                        dateFormat = new SimpleDateFormat("", Locale.CHINESE);
                    }
                    String[] dateFormatString =
                            formField.dateFormat();
                    for (String s : dateFormatString) {
                        dateFormat.applyPattern(s);
                        try {
                            Date parse = dateFormat.parse(string);
                            try {
                                field.set(target, parse);
                                mapInfo.result = MapInfo.Result.SUCCESS;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            mapInfo.result = MapInfo.Result.FORMAT_ERROR;
                        }
                        if (mapInfo.success()) {
                            break;
                        }
                    }
                }
            }
        } else {//不可见直接映射成功
            mapInfo.result = MapInfo.Result.SUCCESS;
        }
        return mapInfo;
    }

    @Override
    protected MapInfo fillValue(TextView view, Object value, FormField formField) {
        if (value != null) {
            view.setText(value.toString());
        }
        MapInfo mapInfo = new MapInfo();
        mapInfo.result = MapInfo.Result.SUCCESS;
        return mapInfo;
    }
}
