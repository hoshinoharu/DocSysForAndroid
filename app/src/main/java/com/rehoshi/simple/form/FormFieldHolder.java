package com.rehoshi.simple.form;

import java.lang.reflect.Field;

/**
 * Created by hoshino on 2019/3/3.
 */

public class FormFieldHolder {
    private FormField annotation;
    private Field formField ;

    public FormFieldHolder(FormField annotation, Field formField) {
        this.annotation = annotation;
        this.formField = formField;
    }

    public FormField getAnnotation() {
        return annotation;
    }

    public void setAnnotation(FormField annotation) {
        this.annotation = annotation;
    }

    public Field getFormField() {
        return formField;
    }

    public void setFormField(Field formField) {
        this.formField = formField;
    }
}
