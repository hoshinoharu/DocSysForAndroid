package com.rehoshi.simple.business.net.retrofit_2;

import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by hoshino on 2018/12/14.
 */

public class HoshiRxJava2Plugins {
    /**
     * 装配适配器
     */
    public interface AssemblyAdapter {
        HoshiObservable get(Observable observable);
    }

    /**
     * 用来过滤Observable
     *
     * @param assemblyAdapter
     */
    public static void assembly(AssemblyAdapter assemblyAdapter) {
        RxJavaPlugins.setOnObservableAssembly(observable -> {
            if (observable instanceof HoshiObservable || observable == null) {
                return observable;
            } else {
                //返回一个代理类 用来扩展自己的方法
                return assemblyAdapter.get(observable);
            }
        });
    }
}
