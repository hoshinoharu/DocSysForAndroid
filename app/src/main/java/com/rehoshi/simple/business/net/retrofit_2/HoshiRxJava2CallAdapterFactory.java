package com.rehoshi.simple.business.net.retrofit_2;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by hoshino on 2018/12/14.
 * 原生RxJava工厂的代理
 */

public abstract class HoshiRxJava2CallAdapterFactory extends CallAdapter.Factory {

    private RxJava2CallAdapterFactory realFactory;

    public HoshiRxJava2CallAdapterFactory(RxJava2CallAdapterFactory realFactory) {
        this.realFactory = realFactory;
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter adapter = null;
        Class<?> rawType = getRawType(returnType);

        if (HoshiObservable.class.isAssignableFrom(rawType)) {

            if (returnType instanceof ParameterizedType) {

                Type jsonReturnType = new HoshiRxReturnType() {
                    @Override
                    public Type[] getJsonTypeArguments() {
                        return new Type[]{createJsonReturnType((ParameterizedType) returnType)};
                    }
                };
                //欺骗原来的工厂 使其创建出Observable 对象
                adapter = this.realFactory.get(jsonReturnType, annotations, retrofit);
            }
        }

        //调用了原来的方法处理
        if (adapter == null) {
            adapter = realFactory.get(returnType, annotations, retrofit);
        }
        return adapter;
    }

    /**
     * @param returnType
     */
    protected abstract Type createJsonReturnType(ParameterizedType returnType);
}
