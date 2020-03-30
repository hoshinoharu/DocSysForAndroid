package com.rehoshi.simple.form.binder;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by hoshino on 2019/3/3.
 * 基础的视图寻找器
 */

public abstract class BaseViewFinder<F> implements ViewFinder {

    private WeakReference<F> findWeakReference;

    public BaseViewFinder(F finder) {
        this.findWeakReference = new WeakReference<>(finder);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T view = null;
        F f = findWeakReference.get();
        if (f != null) {
            view = find(f, id);
        }
        return view;
    }

    protected abstract <T extends View> T find(F finder, int id);
}
