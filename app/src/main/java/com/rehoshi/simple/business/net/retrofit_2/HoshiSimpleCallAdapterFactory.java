package com.rehoshi.simple.business.net.retrofit_2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by hoshino on 2018/12/14.
 */

public class HoshiSimpleCallAdapterFactory extends HoshiRxJava2CallAdapterFactory {

    private Class apiCls;

    public HoshiSimpleCallAdapterFactory(Class apiCls) {
        super(RxJava2CallAdapterFactory.create());
        this.apiCls = apiCls;
    }

    @Override
    protected Type createJsonReturnType(ParameterizedType returnType) {
        return new HoshiParamType(apiCls, returnType.getActualTypeArguments());
    }

    public static HoshiSimpleCallAdapterFactory create(Class apiCls) {
        return new HoshiSimpleCallAdapterFactory(apiCls);
    }
}
