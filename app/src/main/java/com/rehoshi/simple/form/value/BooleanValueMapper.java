package com.rehoshi.simple.form.value;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rehoshi.simple.form.FormField;

import java.lang.reflect.Field;

/**
 * Created by hoshino on 2019/3/3.
 */

public class BooleanValueMapper extends ValueMapper<View> {

    public BooleanValueMapper(View view, Object target, Field field, FormField formField) {
        super(view, target, field, formField);
    }

    @Override
    protected MapInfo mapValue(View view, Class fieldClass, Field field, Object target, FormField formField) {
        MapInfo mapInfo = new MapInfo();
        if (view.getVisibility() == View.VISIBLE) {
            boolean res = false;
            if (view instanceof CheckBox) {
                res = ((CheckBox) view).isChecked();
            } else if (view instanceof TextView) {
                String string = ((TextView) view).getText().toString();
                if (string.length() <= 0 || string.equals("0")) {
                    res = false;
                } else {
                    res = true;
                }
            }
            try {
                field.set(target, res);
                mapInfo.result = MapInfo.Result.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mapInfo.result = MapInfo.Result.SUCCESS;
        }
        return mapInfo;
    }
}
