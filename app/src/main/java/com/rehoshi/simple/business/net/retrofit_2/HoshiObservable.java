package com.rehoshi.simple.business.net.retrofit_2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hoshino on 2018/12/14.
 * 观察发布者代理
 */

public class HoshiObservable<T> extends Observable<T> {

    private Observable<T> upstream;

    public HoshiObservable(Observable<T> upstream) {
        this.upstream = upstream;
    }


    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        this.upstream.subscribe(observer);
    }

    protected Observable<T> normalThreadStrategy() {
        return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
