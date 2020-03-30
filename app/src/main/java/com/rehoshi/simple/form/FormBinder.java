package com.rehoshi.simple.form;

import android.app.Activity;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import com.rehoshi.simple.form.binder.ActivityViewFinder;
import com.rehoshi.simple.form.binder.ContentViewFinder;
import com.rehoshi.simple.form.binder.GenericViewFinder;
import com.rehoshi.simple.form.binder.ViewFinder;
import com.rehoshi.simple.form.binder.ViewProvider;
import com.rehoshi.simple.form.value.BooleanValueMapper;
import com.rehoshi.simple.form.value.CollectionValueMapper;
import com.rehoshi.simple.form.value.MapInfo;
import com.rehoshi.simple.form.value.ObjectValueMapper;
import com.rehoshi.simple.form.value.TextValueMapper;
import com.rehoshi.simple.form.value.ValueMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoshino on 2019/3/3.
 */

public class FormBinder {

    private ViewFinder viewFinder;

    //需要绑定表单属性的目标
    private Object bindTarget;

    private Map<Integer, ValueMapper> valueCache = new HashMap<>();

    private ValueMapper.ValueConverter valueConverter;

    public FormBinder(Activity activity) {
        this(activity, activity);
    }


    public FormBinder(Activity activity, Object bindTarget) {
        this.viewFinder = new ActivityViewFinder(activity);
        this.bindTarget = bindTarget;
    }

    public FormBinder(View view, Object bindTarget) {
        this.viewFinder = new ContentViewFinder(view);
        this.bindTarget = bindTarget;
    }

    public FormBinder(ViewProvider viewProvider, Object bindTarget) {
        this.viewFinder = new GenericViewFinder(viewProvider);
        this.bindTarget = bindTarget;
    }

    /**
     * 调整格式
     */
    public void adjustViewInput() {
        List<FormFieldHolder> formFieldHolders = bindFields();
        if (formFieldHolders != null) {
            for (FormFieldHolder formFieldHolder :
                    formFieldHolders) {
                FormField formField = formFieldHolder.getAnnotation();
                Class fieldClass = formFieldHolder.getFormField().getType();
                if (formField != null) {
                    int inputLength = formField.maxLength();
                    if (inputLength <= 0) {
                        if (String.class.isAssignableFrom(fieldClass)) {
                            //文本默认 11
                            inputLength = 11;
                        } else if (Integer.class.isAssignableFrom(fieldClass) || int.class.isAssignableFrom(fieldClass)) {
                            //整形数字默认10
                            inputLength = 9;
                        } else if (Double.class.isAssignableFrom(fieldClass) || double.class.isAssignableFrom(fieldClass)) {
                            //浮点型默认9
                            inputLength = 16;
                        } else if (Date.class.isAssignableFrom(fieldClass)) {
                            inputLength = 19;
                        } else {
                            inputLength = 10;
                        }
                    }
                    int viewId = formField.value();
                    View viewById = viewFinder.findViewById(viewId);
                    //设置输入长度
                    if (viewById instanceof TextView) {
                        TextView textView = (TextView) viewById;
                        textView.setFilters(new InputFilter[]{
                                new InputFilter.LengthFilter(inputLength)
                        });
                    }
                }
            }
        }
    }

    /**
     * 自动填充表单
     * @return
     */
    public MapInfo fill(){
        MapInfo mapInfo = null;
        List<FormFieldHolder> formFieldHolders = bindFields();
        if (formFieldHolders != null) {
            for (FormFieldHolder formFieldHolder :
                    formFieldHolders) {
                Field field = formFieldHolder.getFormField();
                FormField formField = formFieldHolder.getAnnotation();
                //绑定的视图ID
                int viewId = formField.value();
                ValueMapper valueMapper = getMapper(viewId, field, formField);
                if (valueMapper != null) {
                    mapInfo = valueMapper.fill();
                }

                if (mapInfo != null && !mapInfo.success()) {
                    break;
                }
            }
        }
        return mapInfo;
    }

    public MapInfo bind() {
        MapInfo mapInfo = null;
        List<FormFieldHolder> formFieldHolders = bindFields();
        if (formFieldHolders != null) {
            for (FormFieldHolder formFieldHolder :
                    formFieldHolders) {
                Field field = formFieldHolder.getFormField();
                FormField formField = formFieldHolder.getAnnotation();
                //绑定的视图ID
                int viewId = formField.value();
                ValueMapper valueMapper = getMapper(viewId, field, formField);
                if (valueMapper != null) {
                    mapInfo = valueMapper.map();
                }

                if (mapInfo != null && !mapInfo.success()) {
                    break;
                }
            }
        }
        return mapInfo;
    }

    private ValueMapper getMapper(int viewId, Field field, FormField formField){
        ValueMapper valueMapper = null ;
        //如果绑定了视图
        if (viewId > 0) {
            //如果没有缓存
            if (valueCache.containsKey(viewId)) {
                valueMapper = valueCache.get(viewId);
            } else {
                View viewById = viewFinder.findViewById(viewId);
                if(viewById == null){
                    throw new RuntimeException("视图ID映射错误") ;
                }
                if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
                    valueMapper = new BooleanValueMapper(viewById, bindTarget, field, formField);
                } else if (viewById instanceof TextView) {
                    valueMapper = new TextValueMapper((TextView) viewById, bindTarget, field, formField);
                }
            valueMapper.setValueConverter(this.valueConverter);
                valueCache.put(viewId, valueMapper);
            }
        } else if (Collection.class.isAssignableFrom(field.getType())) {
            valueMapper = new CollectionValueMapper(bindTarget, field, formField, formField.minLength());
        } else {
            valueMapper = new ObjectValueMapper(bindTarget, field, formField);
        }
        return valueMapper ;
    }


    private List<FormFieldHolder> formFieldHolders;

    private List<FormFieldHolder> bindFields() {
        if (formFieldHolders == null) {
            formFieldHolders = new ArrayList<>();
            Field[] declaredFields = bindTarget.getClass().getDeclaredFields();
            for (Field field :
                    declaredFields) {
                FormField annotation = field.getAnnotation(FormField.class);
                if (annotation != null) {
                    formFieldHolders.add(new FormFieldHolder(annotation, field));
                }
            }
            Collections.sort(formFieldHolders, (formFieldHolder, t1) -> Integer.compare(formFieldHolder.getAnnotation().order(), t1.getAnnotation().order()));
        }
        return formFieldHolders;
    }

    public void setValueConverter(ValueMapper.ValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }
}

