package com.rehoshi.simple.form.value;


import com.rehoshi.simple.form.FormField;

import java.lang.reflect.Field;

/**
 * Created by hoshino on 2019/3/3.
 * 将视图对应的值 映射 到字段上
 */

public abstract class ValueMapper<V> {

    public interface ValueConverter {
        Object convertValue(Object value, FormField formField);
    }

    //显示值的视图
    private V view;

    //需要绑定目标属性
    private Object target;

    //表单字段
    private Field field;

    //字段类型
    private Class fieldClass;

    private FormField formField;

    private ValueConverter valueConverter;


    public ValueMapper(V view, Object target, Field field, FormField formField) {
        this.view = view;
        this.target = target;
        this.field = field;
        this.fieldClass = field.getType();
        this.field.setAccessible(true);
        this.formField = formField;
    }

    /**
     * 开始映射值
     */
    public MapInfo map() {
        MapInfo mapInfo = mapValue(view, fieldClass, field, target, formField);
        if (mapInfo != null) {
            mapInfo.fieldNameTag = formField.fieldName();
        }
        return mapInfo;
    }

    protected abstract MapInfo mapValue(V view, Class fieldClass, Field field, Object target, FormField formField);

    public MapInfo fill() {
        Object value = null;
        try {
            value = getValueConverter().convertValue(field.get(target), formField);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        MapInfo mapInfo = fillValue(view, value, formField);
        if (mapInfo != null) {
            mapInfo.fieldNameTag = formField.fieldName();
        }
        return mapInfo;
    }

    protected MapInfo fillValue(V view, Object value, FormField formField) {
        return new MapInfo();
    }

    public ValueConverter getValueConverter() {
        if (valueConverter == null) {
            valueConverter = (value, formField1) -> value;
        }
        return valueConverter;
    }

    public void setValueConverter(ValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }
}
