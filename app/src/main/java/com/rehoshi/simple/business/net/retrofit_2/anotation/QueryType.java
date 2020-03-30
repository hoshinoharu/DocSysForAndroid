package com.rehoshi.simple.business.net.retrofit_2.anotation;

/**
 * Created by hoshino on 2019/1/8.
 */

public @interface QueryType {
    ParameterType value() default ParameterType.JSON;
}
