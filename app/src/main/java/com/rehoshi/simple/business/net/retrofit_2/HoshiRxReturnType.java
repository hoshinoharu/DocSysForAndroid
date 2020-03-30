package com.rehoshi.simple.business.net.retrofit_2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * Created by hoshino on 2018/12/14.
 * 返回类型的代理 用来欺骗Rx适配器工厂
 */

public abstract class HoshiRxReturnType implements ParameterizedType {

    /**
     * 这里返回的类型 主要用于Json解析
     *
     * @return
     */
    @Override
    public Type[] getActualTypeArguments() {
        return getJsonTypeArguments();
    }

    @Override
    public Type getRawType() {
        return Observable.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    /**
     * 获取用于Json解析的泛型参数列表
     *
     * @return
     */
    public abstract Type[] getJsonTypeArguments();
}
