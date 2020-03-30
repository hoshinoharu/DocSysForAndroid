package com.rehoshi.simple.utils;

import android.content.Intent;

import com.google.gson.Gson;

/**
 * Created by hoshino on 2018/12/5.
 */

public class JsonUtil {

    public static <T> T readFromIntent(Class<T> cls, Intent intent, T defaultObject) {
        T obj;
        obj = readFromIntent(cls, intent) ;
        if(obj == null){
            obj = defaultObject ;
        }
        return obj ;
    }

    public static <T> T readFromIntent(Class<T> cls, Intent intent) {
        String json = intent.getStringExtra(cls.getName());
        return fromJson(cls, json);
    }

    public static Intent writeIntoIntent(Object obj, Intent intent) {
        if (obj != null) {
            String json = toJson(obj);
            intent.putExtra(obj.getClass().getName(), json);
        }
        return intent;
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static <T> T fromJson(Class<T> cls, String json) {
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, cls);
    }
}
