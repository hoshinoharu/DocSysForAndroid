package com.rehoshi.simple.form.binder;

import android.view.View;

/**
 * Created by hoshino on 2019/3/3.
 * 泛用的视图查找器
 */

public class GenericViewFinder extends BaseViewFinder<ViewProvider> {

    public GenericViewFinder(ViewProvider finder) {
        super(finder);
    }

    @Override
    protected <T extends View> T find(ViewProvider finder, int id) {
        return finder.findViewById(id);
    }
}
