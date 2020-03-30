package com.rehoshi.simple.business.vo;

import android.content.Context;

import com.rehoshi.simple.utils.JsonUtil;
import com.rehoshi.simple.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoshino on 2018/12/28.
 */

public class SPAble {

    private static Map<Class<? extends SPAble>, SPAble> spCache = new HashMap<>();

    public void writeToSP(Context context) {
        String json = JsonUtil.toJson(this);
        context.getSharedPreferences(SPAble.class.getName(), Context.MODE_PRIVATE)
                .edit()
                .putString(this.getClass().getName(), json)
                .apply();
    }

    /**
     * 更改存储的SPAble
     *
     * @param data 需要修改的对象
     */
    public static <T extends SPAble> void change(T data, Context context) {
        // 修改内存中的对象
        spCache.put(data.getClass(), data);
        //持久化对象
        data.writeToSP(context);
    }


    public static <T extends SPAble> T get(Class<T> cls, Context context) {
        T data = null;
        if (spCache.containsKey(cls)) {
            data = (T) spCache.get(cls);
        } else {
            String json = null;
            json = context.getSharedPreferences(SPAble.class.getName(), Context.MODE_PRIVATE)
                    .getString(cls.getName(), "");
            if (!StringUtil.isNullOrEmpty(json)) {
                data = JsonUtil.fromJson(cls, json);
            }
            if (data == null) {
                try {
                    data = cls.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            spCache.put(cls, data);
        }
        return data;
    }
}
