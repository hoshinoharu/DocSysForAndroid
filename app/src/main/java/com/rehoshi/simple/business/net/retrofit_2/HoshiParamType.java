package com.rehoshi.simple.business.net.retrofit_2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hoshino on 2018/12/14.
 */

public class HoshiParamType implements ParameterizedType {

    private Type raw ;

    private Type[] args ;

    public HoshiParamType(Type raw, Type... args) {
        this.raw = raw;
        this.args = args;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
