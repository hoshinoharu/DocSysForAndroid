package com.rehoshi.simple.form.value;


import com.rehoshi.simple.form.FormField;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by hoshino on 2019/3/3.
 */

public class CollectionValueMapper extends ObjectValueMapper<Collection> {

    private int minLength ;

    public CollectionValueMapper(Object target, Field field, FormField formField, int minLength) {
        super(target, field, formField);
        this.minLength = minLength ;
    }

    @Override
    protected boolean judgeValue(Collection value) {
        return super.judgeValue(value) && value.size() >= minLength;
    }
}
