package com.rehoshi.simple.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hoshino on 2019/3/3.
 * 表单字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {

    int value() default -1;

    String fieldName() default "";

    //最小长度
    int minLength() default 1;

    //最大长度
    int maxLength() default -1;

    String[] dateFormat() default {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    int order() default 0 ;

    boolean trim() default false ;

    /**
     * 数值类型 不能为 0
     * @return
     */
    boolean notZero() default true ;
}
