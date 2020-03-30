package com.rehoshi.simple.form.value;


import com.rehoshi.simple.form.FormField;

import java.lang.reflect.Field;

/**
 * Created by hoshino on 2019/3/3.
 */

public class ObjectValueMapper<T> extends ValueMapper<Object> {

    public ObjectValueMapper(Object target, Field field, FormField formField) {
        super(null, target, field, formField);
    }

    @Override
    protected MapInfo mapValue(Object view, Class fieldClass, Field field, Object target, FormField formField) {
        //如果没有绑定视图 就只检查对象是否为空
        MapInfo mapInfo = new MapInfo();
        try {
            mapInfo.fieldNameTag = formField.fieldName();
            T value = (T) field.get(target);
            if (!judgeValue(value)) {
                mapInfo.result = MapInfo.Result.EMPTY_INPUT;
            } else {
                mapInfo.result = MapInfo.Result.SUCCESS;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mapInfo;
    }

    protected boolean judgeValue(T value){
        return value != null ;
    }
}
